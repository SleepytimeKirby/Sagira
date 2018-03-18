package sleepytimeKirby.GetSnanceBot.commands;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Category;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.GuildController;
import net.dv8tion.jda.core.managers.GuildManager;
import net.dv8tion.jda.core.managers.RoleManager;
import sleepytimeKirby.GetSnanceBot.Utils.DatabaseManager;
import sleepytimeKirby.GetSnanceBot.Utils.Builders.PermissionBuilder;
/*
 * Note - REMOVE THIS LISTENER FROM Bot.java AFTER USE. No checks for server owner, just to see if the user has the admin permsion
 * 
 * This is meant to be used on a fresh server to do all setup automatically. Please make sure the bot has admin
 * permissions. This is not meant to be used on an established server, no guarnetee it will work. If you have an
 * established server already, either run the settings initalizer (still in progress) or manually enter the setting into your database. 
 */
public class InitializeBot extends Command{
	DatabaseManager dm = DatabaseManager.getInstance();
	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		// TODO Auto-generated method stub
		//Arguments must include the bot discord id.
		Guild g = e.getGuild();
		if(!g.getMemberById(args[1]).getPermissions().contains(Permission.ADMINISTRATOR)){
			sendMessage(e,"Bot must have admin permissions to setup server. Please add then run again.");
			return;
		}
		if(!e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)){
			sendMessage(e,"Only user with admin permission can use this command.");
			return;
		}
		//Create Roles
		//Currently we have Admin, Mod, Bot, DJ, Member, Honorary Member, Guests, Bot, Temp Channel Base.
		//Admin and mod self explantory. DJ has the same discord permissions as a member, but is used in the bot to give them access to certain music commands. Honoarary memebers and guests have the same permssions, only difference is honorary members do not get automatically purged after a specified time.
		//Temp channel base is the base permsion we copy when create temporary channel. We use it to allow anyone that is part of a group to connect to that group's voice channel.
		//Bot permissions are higher then mods, as the bot needs the ability to manage roles and permissions, along with the creation of channels. We don't grant admin to the bot, while we could there isn't a true reason to. 
		GuildController gc = g.getController();
		PermissionBuilder adminPB = new PermissionBuilder();
		adminPB.setPermissions(true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true);
		Role admin = gc.createRole().setName("Admin").setPermissions(adminPB.getAllowed()).setMentionable(true).setColor(Color.RED).complete();
		PermissionBuilder modPB = new PermissionBuilder();
		modPB.setPermissions(true, true, true, false, false, false, true, false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, false, false, false);
		Role mod = gc.createRole().setName("Moderator").setPermissions(modPB.getAllowed()).setMentionable(true).setColor(new Color(19,131,168)).complete();
		PermissionBuilder botPB = new PermissionBuilder();
		botPB.setPermissions(true, true, true, false, true, false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, true);
		Role bot = gc.createRole().setName("Beep Boop I'm a Bot").setPermissions(botPB.getAllowed()).setMentionable(false).setColor(new Color(10,209,196)).complete();
		PermissionBuilder memberPB = new PermissionBuilder();
		memberPB.setPermissions(true, false, false, false, false, false, true, false, true, true, true, true, false, true, true, true, false, true, true, true, false, false, false, true, true, false, false, false, false, false);
		Role member = gc.createRole().setName("Member").setPermissions(memberPB.getAllowed()).setMentionable(false).setColor(new Color(7,179,21)).complete();
		Role dj = gc.createRole().setName("DJ").setPermissions(memberPB.getAllowed()).setMentionable(true).setColor(new Color(81,179,7)).complete();
		PermissionBuilder guestPB = new PermissionBuilder();
		memberPB.setPermissions(false, false, false, false, false, false, true, false, true, true, true, false, false, true, false, true, false, false, true, true, false, false, false, true, true, false, false, false, false, false);
		Role guest = gc.createRole().setName("Guest").setPermissions(guestPB.getAllowed()).setMentionable(false).setColor(new Color(227,107,9)).complete();
		Role honoraryMember = gc.createRole().setName("Honorary Member").setPermissions(guestPB.getAllowed()).setMentionable(false).setColor(new Color(186,17,183)).complete();
		PermissionBuilder tempPB = new PermissionBuilder();
		tempPB.setPermissions(false, false, false, false, false, false, false, false, true, true, false, false, false, false, false, true, false, false, false, true, false, false, false, true, false, false, false, false, false, false);
		Role tempRoleBase = gc.createRole().setName("Temp Role Base - DO NOT DELETE").setPermissions(guestPB.getAllowed()).setMentionable(false).setColor(new Color(186,17,183)).complete();
		PermissionBuilder noPerms = new PermissionBuilder();
		noPerms.setPermissions(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
		PermissionBuilder everyonePB = new PermissionBuilder();
		everyonePB.setPermissions(false, false, false, false, false, false, false, false, true, true, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
		RoleManager everyone = new RoleManager(g.getPublicRole());
		everyone.setPermissions(everyonePB.getAllowed()).complete();
		//Create Catagorys
		gc.createCategory("HIDDEN CHANNELS").addPermissionOverride(g.getPublicRole(), null, noPerms.getDenied()).addPermissionOverride(mod, modPB.getAllowed(), modPB.getDenied()).addPermissionOverride(admin, adminPB.getAllowed(),null).complete();
		gc.createCategory("MUSIC").complete();
		gc.createCategory("TEXT CHANNELS").complete();
		gc.createCategory("GROUPS").complete();
		gc.createCategory("VOICE CHANNELS").complete();
		//Unfortunatly the channel action returns a channel and not a category object so we do this instead
		HashMap<String,Category> categoryList = new HashMap<String,Category>();
		for(Category c:g.getCategories()) {
			categoryList.put(c.getName(), c);
		}
		//Create Hidden Channels
		Channel adminTextChannel = gc.createTextChannel("admin-chat").addPermissionOverride(mod, null, noPerms.getDenied()).setParent(categoryList.get("HIDDEN CHANNELS")).complete();
		Channel modTextChannel = gc.createTextChannel("mod-chat").setParent(categoryList.get("HIDDEN CHANNELS")).complete();
		Channel botTextChannel = gc.createTextChannel("bot-logging").addPermissionOverride(bot, botPB.getAllowed(), null).setParent(categoryList.get("HIDDEN CHANNELS")).complete();
		//Create Music Channel
		Channel musicTextChannel = gc.createTextChannel("music").setParent(categoryList.get("MUSIC")).complete();
		Channel musicVoiceChannel = gc.createVoiceChannel("Muisc").setParent(categoryList.get("MUSIC")).complete();
		//Create TextChannels
		Channel welcomeText = gc.createTextChannel("welcome").setParent(categoryList.get("TEXT CHANNELS")).complete();
		Channel annoucementsText = gc.createTextChannel("annoucements").setParent(categoryList.get("TEXT CHANNELS")).complete();
		Channel destinyInfoText = gc.createTextChannel("destiny-info").setParent(categoryList.get("TEXT CHANNELS")).complete();
		Channel botCommandsText = gc.createTextChannel("bot-commands").setParent(categoryList.get("TEXT CHANNELS")).complete();
		Channel generalText = gc.createTextChannel("general").setParent(categoryList.get("TEXT CHANNELS")).complete();
		Channel trashText = gc.createTextChannel("trash").setParent(categoryList.get("TEXT CHANNELS")).complete();
		Channel theorycraftinText = gc.createTextChannel("theorycrafting").setParent(categoryList.get("TEXT CHANNELS")).complete();
		//Create Group Channels
		Channel groupGenText = gc.createTextChannel("group-general").setParent(categoryList.get("GROUPS")).complete();
		Channel groupPostText = gc.createTextChannel("group-postings").setParent(categoryList.get("GROUPS")).complete();
		Channel groupGenVoice = gc.createVoiceChannel("Group General").setParent(categoryList.get("GROUPS")).complete();
		Channel raidPostsText = gc.createTextChannel("raid-postings").setParent(categoryList.get("GROUPS")).complete();
		//Create Voice Channels
		Channel genVoice  = gc.createVoiceChannel("General").setParent(categoryList.get("VOICE CHANNELS")).complete();
		Channel pveVoice  = gc.createVoiceChannel("PvE").setParent(categoryList.get("VOICE CHANNELS")).complete();
		Channel raidVoice  = gc.createVoiceChannel("Raid").setParent(categoryList.get("VOICE CHANNELS")).complete();
		Channel nightfallVoice  = gc.createVoiceChannel("Nightfall").setParent(categoryList.get("VOICE CHANNELS")).complete();
		Channel strikeVoice  = gc.createVoiceChannel("Strike").setParent(categoryList.get("VOICE CHANNELS")).complete();
		Channel pvpVoice  = gc.createVoiceChannel("PvP").setParent(categoryList.get("VOICE CHANNELS")).complete();
		Channel trialsVoice  = gc.createVoiceChannel("Trials").setParent(categoryList.get("VOICE CHANNELS")).complete();
		Channel afk  = gc.createVoiceChannel("AFK").setParent(categoryList.get("VOICE CHANNELS")).complete();
		//Setup Database - Need new method for inital setup in manager
		HashMap<String,String> settings = new HashMap<String,String>();
		settings.put("ADMIN_ID", admin.getId());
		settings.put("MOD_ID", mod.getId());
		settings.put("DJ_ID", dj.getId());
		settings.put("MEMBER_ID", member.getId());
		settings.put("PERM_GUEST_ID", honoraryMember.getId());
		settings.put("GUEST_ID", guest.getId());
		settings.put("TEMP_ROLE_BASE_ID", tempRoleBase.getId());
		settings.put("GROUP_CAT_ID", categoryList.get("GROUPS").getId());
		settings.put("GROUP_POST_ID", groupPostText.getId());
		settings.put("GROUP_VOICE_CHAN", groupGenVoice.getId());
		settings.put("MOD_LOG_ID", botTextChannel.getId());
		settings.put("MUSIC_TEXT_ID", musicTextChannel.getId());
		settings.put("MUSIC_VOICE_ID",musicVoiceChannel.getId());
		settings.put("UPDATE_CHAN_ID", destinyInfoText.getId());
		settings.put("GUILD_ID", g.getId());
		dm.insertSettings(settings);
	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList("!init");
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
