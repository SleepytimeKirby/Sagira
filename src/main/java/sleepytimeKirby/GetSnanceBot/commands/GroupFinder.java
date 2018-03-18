package sleepytimeKirby.GetSnanceBot.commands;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.math.NumberUtils;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import sleepytimeKirby.GetSnanceBot.DatabaseClasses.Group;
import sleepytimeKirby.GetSnanceBot.Utils.DatabaseManager;
import sleepytimeKirby.GetSnanceBot.Utils.GroupPosterEmbed;
import sleepytimeKirby.GetSnanceBot.Utils.GroupRequestEmbed;
import sleepytimeKirby.GetSnanceBot.Utils.Scheduler;
import sleepytimeKirby.GetSnanceBot.Utils.Settings;

/*
 * 
 * GroupFinder 
 * 
 * Replacing JoinFireteam and CreateFireteam Classes
 * 
 * 
 * Aliases: .gf .groupFinder
 * 
 * 
 * Usage for Creating a group
 * 
 * .gf lfm <number of slots> <public/private/approve> <Game> <Activity> <Additional Information>
 * 
 * Usage for Joining a group
 * 
 * .gf join <group id> <invite key*> <Additional Information*> key is only required on invite groups, Additional Information is used for private groups
 * 
 * Usage for Looking for Group - planned feature, for now with the small number of players, creating a group is more likely.
 * 
 * .gf lfg <game> <activity> <Additional Information>
 * 
 * lfm postings will stay up for a day or until empty, lfg postings stay up for an hour or until offline.
 * 
 * You can manually remove a posting or leave a group by using
 * 
 * .gf leave <new_leader>
 * 
 * You can kick a player (only leader) from a group using 
 * 
 * .gf kick <player_name> // remove permissions and move to general channel.
 * 
 * You can transfer Leadership by using
 * 
 * .gf promote <name>
 * 
 * 
 * 
 * Database:
 * 
 * group_finder
 * 
 * GROUP_UUID varchar(36)
 * GROUP_TYPE int(2) // 1 is private 2 is public
 * GROUP_GAME varchar (32)
 * GROUP_SLOTS int(10)
 * GROUP_ACTIVITY varchar (32)
 * GROUP_NOTES varchar (256)
 * GROUP_KEY bigint(20) //can be null
 * GROUP_LEADER bigint(20) 
 * GROUP_CHANNEL_ID bigint(20)
 * GROUP_ROLE_ID bigint(20)
 * 
 * 
 * group_finder_players
 * 
 * GROUP_UUID
 * PLAYER_ID
 * 
 * Needed Database Methods
 * Insert group
 * insert player
 * get group (with subselect for players, and also count amount Select (PLAYER_ID from group_find_players WHERE GROUP_UUID = )
 * update leader
 * remove player
 * 
 * on join: - get group from database, count number of players, compare to slots, if room, add player, update database, edit posting with new player and slot update.
 * IF PRIVATE - send message to leader, they use .gf <name of player> <accept/deny>
 */
public class GroupFinder extends Command{

	DatabaseManager dm = DatabaseManager.getInstance();
	Settings s = Settings.getInstance();
	Scheduler sch = Scheduler.getInstance();
	Collection<Permission> voiceConnect = new ArrayList<Permission>();
	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		voiceConnect.add(Permission.VOICE_CONNECT);
		/*
		 * Args:
		 * 
		 * 0 = commands (.gf)
		 * 1 = lfm, join, kick, leave, promote
		 * 
		 * rest see above
		 * 
		 */
		String leader;
		Group groupInfo;
		VoiceChannel groupVoice;
		String groupUUID;
		HashMap<Long,Integer> players;
		List<Member> memberList;
		String target;
		switch(args[1]) {
		case("lfm"):
			leader = dm.isUserGroupLeader(e.getMember().getUser().getIdLong());
			if(!leader.equals("no")) {
				//Either SQL failed or they already leader.
				if(leader.equals("-1")) {
					sendMessage(e,"Sorry, error with creation, contact admin.");
					break;
				}
				sendMessage(e,"Sorry, You cannot create a group if you already lead one.");
				break;
			}
			int slots = Integer.valueOf(args[2]);
			String slotsString = args[2];
			int type;
			String typeString = args[3];
			Random rn = new Random();
			Long randomKey = rn.nextLong() + 1;
			switch(typeString) {
			case("public"):
				type = 2;
				break;
			case("private"):
				type = 3;
				break;
			default: //default is must accept
				type = 1;
			}
			String game = args[4];
			String activity = args[5];
			String notes = args[6];
			String uuid = UUID.randomUUID().toString();
			String message = "Group created. Group ID is " + uuid +"/n";
			Role tempRole = e.getGuild().getController().createCopyOfRole(e.getGuild().getRoleById(s.get("TEMP_ROLE_BASE_ID"))).setName(uuid).complete();
			Channel tempChannel = e.getGuild().getCategoryById(s.get("GROUP_CAT_ID")).createVoiceChannel(e.getAuthor().getName()+"'s Group Channel").addPermissionOverride(tempRole, voiceConnect,null).complete();
			Message groupPost = new GroupPosterEmbed().postGroupFinder(e.getAuthor().getName(), game, activity, slotsString, "1", notes, typeString, uuid, e.getJDA(), new Color(244,66,122));
			if(type == 3) {
				message = message + "Your invite key is " + randomKey.toString();
			}
			e.getGuild().getController().addRolesToMember(e.getMember(), tempRole);
			sch.createGroup(uuid, e.getGuild().getIdLong());
			dm.insertGroup(uuid, type, game, slots, activity, notes, randomKey, e.getMember().getUser().getIdLong(), tempChannel.getIdLong(), tempRole.getIdLong(), groupPost.getIdLong());
			dm.insertPlayerinGroup(uuid, e.getMember().getUser().getIdLong(),0);
			sendPrivateMessage(e,message);
			break;
		case("join"):
			groupUUID = args[2];
			String extraArg = "";
			if(args.length > 3) {
				extraArg = args[3];
			}
			groupInfo = dm.getGroupDetails(groupUUID);
			if(groupInfo.getType() == 3){
				//Group is private, check keys and let in if ok
				//First make sure itrs a private channel
				if(!e.isFromType(ChannelType.PRIVATE)) {
					//Someone fucked up, delete post and send message
					e.getMessage().delete().complete();
					sendPrivateMessage(e,"Please try again via Private Message with me. Your post has been deleted for security");
				} else {
					if(NumberUtils.isCreatable(extraArg)) {						
						if(Long.valueOf(extraArg) == groupInfo.getKey()){
							e.getGuild().getController().addRolesToMember(e.getMember(), e.getGuild().getRoleById(groupInfo.getRoleId()));
							dm.insertPlayerinGroup(groupUUID, e.getMember().getUser().getIdLong(),0);
							sendPrivateMessage(e,"Group Joined!");
						} else {
							sendPrivateMessage(e,"Wrong Key, please try again.");
						}
				} else {
					sendPrivateMessage(e,"Last Argument is not a valid key. Key must be a number. May be negative.");
				}
			}} else if (groupInfo.getType() == 1) {
				//Group is accept/deny, send notes to leader if havn't applied
				players = dm.getGroupPlayers(groupUUID);
				if(players.containsKey(e.getAuthor().getIdLong())) {
					sendPrivateMessage(e,"You have either already applied to this group and have been denied, have an application pending, or are already in the group.");
					break;
				}
				dm.insertPlayerinGroup(groupUUID, e.getAuthor().getIdLong(), 1);
				MessageEmbed embed = new  GroupRequestEmbed().postGroupRequest(e.getAuthor().getName() + " (" + e.getAuthor().getId() + ")", e.getGuild().getVoiceChannelById(groupInfo.getChannelId()).getName(), extraArg, e.getJDA(), Color.GREEN);
				e.getGuild().getMemberById(groupInfo.getLeaderId()).getUser().openPrivateChannel().complete().sendMessage(embed).complete();
			}else {
				//public group, just let join.
				e.getGuild().getController().addRolesToMember(e.getMember(), e.getGuild().getRoleById(groupInfo.getRoleId()));
				dm.insertPlayerinGroup(groupUUID, e.getMember().getUser().getIdLong(),0);
			}
			
		case("kick"):
			leader = dm.isUserGroupLeader(e.getMember().getUser().getIdLong());
			String userKick = args[2];
			if(leader.equals("no")||leader.equals("-1")) {
				sendMessage(e,"Only group leaders may kick players.");
				break;
			}
			groupInfo = dm.getGroupDetails(leader);
			memberList = e.getGuild().getVoiceChannelById(groupInfo.getChannelId()).getMembers();
			for(Member member:memberList) {
				if (member.getEffectiveName().equalsIgnoreCase(userKick)) {
					e.getGuild().getController().removeRolesFromMember(member, e.getGuild().getRoleById(groupInfo.getRoleId()));
					groupVoice = e.getGuild().getVoiceChannelById(s.get("GROUP_VOICE_CHAN"));
					e.getGuild().getController().moveVoiceMember(member, groupVoice);
					dm.deletePlayerFromGroup(member.getUser().getIdLong(), leader);
				}
			}
			break;
		case("leave"):
			groupUUID = args[2];
			 groupInfo = dm.getGroupDetails(groupUUID);
			 if(groupInfo.getRoleId() == 0) {
				sendMessage(e,"Invalid group id!");
				break;
			}
			leader = dm.isUserGroupLeader(e.getMember().getUser().getIdLong());
			if(leader.equals("no")||leader.equals("-1")) {
				e.getGuild().getController().removeRolesFromMember(e.getMember(), e.getGuild().getRoleById(groupInfo.getRoleId()));
				groupVoice = e.getGuild().getVoiceChannelById(s.get("GROUP_VOICE_CHAN"));
				e.getGuild().getController().moveVoiceMember(e.getMember(), groupVoice);
				dm.deletePlayerFromGroup(e.getMember().getUser().getIdLong(), groupUUID);
			} else {
				sch.removeGroup(groupUUID,e.getGuild().getIdLong());
				//Group removal handles the deletion of database entries
			}
			break;
		case("promote"):
			leader = dm.isUserGroupLeader(e.getMember().getUser().getIdLong());
			String userPromote = args[2];
			if(leader.equals("no")||leader.equals("-1")) {
				sendMessage(e,"Only group leaders may promote players.");
				break;
			}
			groupInfo = dm.getGroupDetails(leader);
			memberList = e.getGuild().getVoiceChannelById(groupInfo.getChannelId()).getMembers();
			for(Member member:memberList) {
				if (member.getEffectiveName().equalsIgnoreCase(userPromote)) {
					dm.updateLeader(leader,member.getUser().getIdLong());
				}
			}
			break;
		case("approve"):
			leader = dm.isUserGroupLeader(e.getMember().getUser().getIdLong());
			if(leader.equals("no")||leader.equals("-1")) {
				sendMessage(e,"Only group leaders may approve players.");
				break;
			}
			target = args[2];
			if(NumberUtils.isCreatable(target)) {
				groupInfo = dm.getGroupDetails(leader);
				e.getGuild().getController().addRolesToMember(e.getGuild().getMemberById(target), e.getGuild().getRoleById(groupInfo.getRoleId()));
				dm.updatePlayerApproval(leader, Long.valueOf(target), 0);
				e.getGuild().getMemberById(target).getUser().openPrivateChannel().complete().sendMessage("You were Approved to group:" + leader + "./n You may now join their voice channel.").complete();
			} else {
				sendMessage(e,"Last argument must be the USER ID of the user(found in parentheses in the application)");
			}
			break;
		case("deny"):
			leader = dm.isUserGroupLeader(e.getMember().getUser().getIdLong());
			if(leader.equals("no")||leader.equals("-1")) {
				sendMessage(e,"Only group leaders may deny players.");
				break;
			}
			target = args[2];
			if(NumberUtils.isCreatable(target)) {
				dm.updatePlayerApproval(leader, Long.valueOf(target), -1);
				e.getGuild().getMemberById(target).getUser().openPrivateChannel().complete().sendMessage("You were Denied to group:" + leader + "./n We are sorry and hope you will find another group").complete();
			} else {
				sendMessage(e,"Last argument must be the USER ID of the user(found in parentheses in the application)");
			}
			break;
		default:
			sendMessage(e,"Unrecognized arguement " + args[1] +". Please try again.");
			
		}
		
	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList(".groupFinder",".gf");
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
