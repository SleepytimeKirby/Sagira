package sleepytimeKirby.GetSnanceBot.Utils;

import java.awt.Color;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import sleepytimeKirby.GetSnanceBot.DatabaseClasses.Fireteams;
import sleepytimeKirby.GetSnanceBot.DatabaseClasses.Group;
import sleepytimeKirby.GetSnanceBot.DatabaseClasses.Guests;
import sleepytimeKirby.GetSnanceBot.Utils.Builders.EmbedBuildingClass;
import sleepytimeKirby.GetSnanceBot.Utils.Builders.EmbedFields;

public class Scheduler {
	private final ScheduledExecutorService scheduler =
			Executors.newScheduledThreadPool(100);
	private static Scheduler s = null;
	private static Settings setting = Settings.getInstance();
	private static String guestRoleID =  setting.get("GUEST_ID");
	private static String honoraryMembersID = setting.get("PERM_GUEST_ID");;
	private JDA api = null;
	private static DatabaseManager dm = DatabaseManager.getInstance();
	private static CentralMessenger cm = CentralMessenger.getInstance();
	private Scheduler(){};
	@SuppressWarnings("rawtypes")
	private HashMap<String,ScheduledFuture> taskList = new HashMap<String,ScheduledFuture>();
	public static Scheduler getInstance(){
		if(s == null) {
			s = new Scheduler();
		}
		return s;
	}
	public void initalize(){
		this.api = cm.getApi();
		//final Runnable clearFireteamTask = new Runnable(){public void run(){clearFireteams();}};
		final Runnable cleanUpRemovedGuestsTask = new Runnable(){public void run(){cleanUpRemovedGuests();}};
		final Runnable raidClean = new Runnable(){public void run(){raidCleanup();}};
		final Runnable cleanupPins = new Runnable() {public void run() {cleanupPins();}};
		//scheduler.scheduleAtFixedRate(clearFireteamTask, 1, 3, TimeUnit.MINUTES);
		scheduler.scheduleAtFixedRate(cleanUpRemovedGuestsTask, 1, 10, TimeUnit.MINUTES); //This is incase we remove a guest instead of having it expire, just to keep the database clean.
		scheduler.scheduleAtFixedRate(raidClean,LocalDateTime.now().with(TemporalAdjusters.next((DayOfWeek.TUESDAY))).toInstant(OffsetDateTime.now().getOffset()).toEpochMilli()-LocalDateTime.now().toInstant(OffsetDateTime.now().getOffset()).toEpochMilli(), 604800000, TimeUnit.MILLISECONDS);
		scheduler.scheduleAtFixedRate(cleanupPins, 5, 360, TimeUnit.MINUTES); //Delete the pin spma every 6 hours
		ArrayList<Guests> guestList = dm.getGuest();
		for(Guests guest:guestList){
			final long userId = guest.getUserId();
			final long guildId = guest.getGuildId();
			long duration = guest.getAccessDuration()-ChronoUnit.HOURS.between(guest.getJoinDate(), LocalDateTime.now());
			if(guest.getAccessDuration() != -1){
				if(duration > 0){
					final Runnable guestTask = new Runnable(){public void run(){removeGuest(userId,guildId);}};
					scheduler.schedule(guestTask, duration, TimeUnit.HOURS);
				} else {
					removeGuest(userId,guildId);
				}
			}
		}

	}

	public void createGroup(String groupUUID,Long guild_id) {
		final Runnable groupCheck = new Runnable() {public void run() {clearGroup(groupUUID,guild_id,false);}};
		taskList.put(groupUUID, scheduler.scheduleAtFixedRate(groupCheck, 10, 2, TimeUnit.MINUTES));
	}
	public void removeGroup(String groupUUID,Long guild_id) {
		clearGroup(groupUUID,guild_id,true);
	}
	public void createTask(final Long user_id,final Long guildId, int duration){
		final Runnable guestTask = new Runnable(){public void run(){removeGuest(user_id,guildId);}};
		scheduler.schedule(guestTask, duration, TimeUnit.HOURS);
	}
	public void createTempBan(final Long user_id,final Long guildId, int duration) {
		final Runnable unBanTask = new Runnable(){public void run(){unban(user_id,guildId);}};
		scheduler.schedule(unBanTask, duration, TimeUnit.MINUTES);
	}
	private void removeGuest(Long user_id,Long guild_id){
		Guild guild = api.getGuildById(guild_id);
		Member member = guild.getMemberById(user_id);
		guild.getController().removeRolesFromMember(member, member.getRoles()).complete();
		//new ModLog().sendModMessage("GetSnanceBot", "Ministry of Peace", member.getEffectiveName(), user_id.toString(), "Guest Removal","User guest status has timed out.", UUID.randomUUID().toString(), api, Color.YELLOW);
		EmbedBuildingClass ebc = new EmbedBuildingClass();
		List<EmbedFields> ef = new ArrayList<EmbedFields>();
		ef.add(new EmbedFields("User", member.getEffectiveName(),true));
		ef.add(new EmbedFields("User ID",user_id.toString(),true));
		ebc.setAuthor("GetSnanceBot (Ministry of Peace)");
		ebc.setTitle("Guest Removal");
		ebc.setColor(Color.YELLOW);
		ebc.setDescription("User guest status has timed out.");
		ebc.setFields(ef);
		ebc.setFooterText(UUID.randomUUID().toString());
		ebc.setFooterUrl("https://loremflickr.com/320/240/dog");
		ebc.setTimestamp(LocalDateTime.now());
		dm.deleteGuest(user_id);
	}
	private void unban(Long user_id,Long guild_id) {
		Guild guild = api.getGuildById(guild_id);
		Member member = guild.getMemberById(user_id);
		guild.getController().unban(member.getUser());
		//new ModLog().sendModMessage("GetSnanceBot", "80085", member.getEffectiveName(), user_id.toString(), "Unbanned(Temp Ban)","User was unbanned after a temp ban", UUID.randomUUID().toString(), api, Color.GREEN);
		EmbedBuildingClass ebc = new EmbedBuildingClass();
		List<EmbedFields> ef = new ArrayList<EmbedFields>();
		ef.add(new EmbedFields("User", member.getEffectiveName(),true));
		ef.add(new EmbedFields("User ID",user_id.toString(),true));
		ebc.setAuthor("GetSnanceBot (Ministry of Love)");
		ebc.setTitle("Unbanned(Temp Ban)");
		ebc.setColor(Color.GREEN);
		ebc.setDescription("User was unbanned after a temp ban");
		ebc.setFields(ef);
		ebc.setFooterText(UUID.randomUUID().toString());
		ebc.setFooterUrl("https://loremflickr.com/320/240/dog");
		ebc.setTimestamp(LocalDateTime.now());
		dm.addModList("Scheduled Action via Bot",(long) 80085, "User " + member.getEffectiveName() +" was unbanned");

	}
	private void raidCleanup() {
		
	}
	@SuppressWarnings("unused")
	@Deprecated
	//Switching from fireteams to group finder.
	private void clearFireteams(){
		ArrayList<Fireteams> fireteams = dm.getFireteams();
		for(Fireteams fireteam:fireteams){
			Guild guild = api.getGuildById(fireteam.getGuildId());
			if(guild.getVoiceChannelById(fireteam.getChannelId()) == null) {
				//Looks like database has an entry that no longer exists on discord, just delete database entry.
				dm.channelDelete(fireteam.getChannelId(), fireteam.getGuildId());
			} else if(guild.getVoiceChannelById(fireteam.getChannelId()).getMembers().isEmpty()){
				guild.getVoiceChannelById(fireteam.getChannelId()).delete().complete();
				dm.channelDelete(fireteam.getChannelId(), fireteam.getGuildId());
			}
		}
	}
	private void cleanupPins() {
		String guildId = setting.get("GUILD_ID");
		String updateId = setting.get("UPDATE_CHAN_ID");
		ArrayList<Long> vendors = dm.selectAllVendorMessage();
		for(Message message:api.getGuildById(guildId).getTextChannelById(updateId).getIterableHistory()) {
			if(!vendors.contains(message.getIdLong())) {
				message.delete().complete();
			}
		}
	}
	private void clearGroup(String groupUUID,Long guild_id,boolean force){
		Guild guild = api.getGuildById(guild_id);
		Group groupDetails = dm.getGroupDetails(groupUUID);
		//If voice channel is empty, or if the force settings is set to true.
		if(guild.getVoiceChannelById(groupDetails.getChannelId()).getMembers().isEmpty() || force) {
		//First delete post
		guild.getTextChannelById(setting.get("GROUP_CAT_ID")).deleteMessageById(groupDetails.getPostId());
		//Delete Channel
		guild.getVoiceChannelById(groupDetails.getChannelId()).delete().complete();
		//Delete Role
		guild.getRoleById(groupDetails.getRoleId()).delete().complete();
		//Delete Database Entry
		dm.deleteGroup(groupUUID);
		//Delete entired in group player entires related to that group
		for(Long playerId:dm.getGroupPlayers(groupUUID).keySet()) {
			dm.deletePlayerFromGroup(playerId,groupUUID);
		}
		//Stop Task
		taskList.get(groupUUID).cancel(false);
		}
	}
	private void cleanUpRemovedGuests(){
		
		ArrayList<Guests> guestList = dm.getGuest();
		for(Guests guest:guestList){
			Guild guild = api.getGuildById(guest.getGuildId());
			List<Role> roles = guild.getMemberById(guest.getUserId()).getRoles();
			boolean isGuest = false;
			for(Role role:roles){
				if(role.getId().equals(guestRoleID)||role.getId().equals(honoraryMembersID)){
					isGuest = true;
					break;
				}
			}
			if(!isGuest){
				dm.deleteGuest(guest.getUserId());
			}
		} 

	}

}
