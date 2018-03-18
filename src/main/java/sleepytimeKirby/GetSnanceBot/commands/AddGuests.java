package sleepytimeKirby.GetSnanceBot.commands;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.math.NumberUtils;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import sleepytimeKirby.GetSnanceBot.Utils.DatabaseManager;
import sleepytimeKirby.GetSnanceBot.Utils.ModLog;
import sleepytimeKirby.GetSnanceBot.Utils.Scheduler;
import sleepytimeKirby.GetSnanceBot.Utils.Settings;

public class AddGuests extends Command{
	Settings sett = Settings.getInstance();
	String modID = sett.get("MOD_ID");
	String adminID = sett.get("ADMIN_ID");
	String memberID = sett.get("MEMBER_ID");
	String guestRoleID = sett.get("GUEST_ID");
	String honoraryMembersID =sett.get("PERM_GUEST_ID");
	DatabaseManager dm = DatabaseManager.getInstance();
	Scheduler sched = Scheduler.getInstance();
	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		if(args[1].equals("add")) {
		//gotta check if the next two args are actually correct to avoid errors
		if(!NumberUtils.isCreatable(args[args.length-1])){ //get last argument
			sendMessage(e,new MessageBuilder().append("Must be number in last argument, please try again").build());
			return;
		}
		//
		if(e.getMessage().getMentionedUsers().isEmpty()){
			sendMessage(e,new MessageBuilder().append("Must mention a user. format is @username. Discord will autocomplete the mention."
					+ ", please try again").build());
			return;
		}
		
		List<Role> userRole = e.getMember().getRoles();
		boolean mod = false;
		boolean member = false;
		for (Role role:userRole){
			if(role.getId().equals(modID)||role.getId().equals(adminID)){
				mod = true;
			} else if (role.getId().equals(memberID)){
				member = true;
			}
		}
		

		Integer length = Integer.valueOf(args[1]);
		if ((!mod && length == -1)||(!mod && length > 48) ){
			sendMessage(e,new MessageBuilder().append("Sorry only mods or above can add permanent members or times greater then 48 hours").build());
			return;
		}
		if(mod && length == -1){
			User user = e.getMessage().getMentionedUsers().get(0);
			Member userMemberObject = e.getGuild().getMemberById(user.getId());
			e.getGuild().getController().addRolesToMember(userMemberObject, e.getGuild().getRoleById(honoraryMembersID)).complete();
			dm.addGuest(user.getIdLong(),e.getGuild().getIdLong(), length);
			dm.addModList(e.getAuthor().getName(), e.getAuthor().getIdLong(), "User added " + user.getName() + " as an honorary member.");
			//sendModMessage(e,new MessageBuilder().append(e.getAuthor().getName() + "(" + e.getAuthor().getId() + ") Added " + user.getName() + " as an honorary member.").build());
			new ModLog().sendModMessage(e.getAuthor().getName(), e.getAuthor().getId(),user.getName(),user.getId(),
					"Guest Added",e.getAuthor().getName() + " added below user as an Honorary Member", UUID.randomUUID().toString(), e.getJDA(), new Color(168,116,237));
			sendMessage(e,new MessageBuilder().append("Honorary Member Role Added!").build());
		} else if(member && length < 48){
				User user = e.getMessage().getMentionedUsers().get(0);
				Member userMemberObject = e.getGuild().getMemberById(user.getId());
				e.getGuild().getController().addRolesToMember(userMemberObject, e.getGuild().getRoleById(guestRoleID)).complete();
				dm.addGuest(user.getIdLong(),e.getGuild().getIdLong(), length);
				dm.addModList(e.getAuthor().getName(), e.getAuthor().getIdLong(), "User added " + user.getName() + " as a guest.");
				new ModLog().sendModMessage(e.getAuthor().getName(), e.getAuthor().getId(),user.getName(),user.getId(),
						"Guest Added",e.getAuthor().getName() + " added below user as a guest for " + length + "hours", UUID.randomUUID().toString(), e.getJDA(), Color.ORANGE);
				//sendModMessage(e,new MessageBuilder().append(e.getAuthor().getName() + "(" + e.getAuthor().getId() + ") Added " + user.getName() + " as a guest for " + length + " hours.").build());
				sched.createTask(user.getIdLong(),e.getGuild().getIdLong(), length);
				sendMessage(e,new MessageBuilder().append("Guest Role Added!").build());
				
		}else{
			sendMessage(e,new MessageBuilder().append("Sorry you cannot add a guest. Must be a member or above").build());
		}
		}
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(".addGuest",".addG");
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Add a discord user as a guest so they get temporary access to voice and text channels";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Add Guest";
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return "Usage: .addGuest <Duration> <user>"
				+ "User: Must be an @mention user. I.E @SleepytimeKirby. \n"
				+ "Duration: Length of time in hours.(Limit 48 for members) Mod team and Admins can use -1 to denote a permanent guest";
	}

}
