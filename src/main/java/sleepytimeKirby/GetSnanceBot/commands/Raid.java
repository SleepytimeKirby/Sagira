package sleepytimeKirby.GetSnanceBot.commands;

import java.util.List;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/*
 * Raid List 
 *  
 *  Example Messages
 *  Raid 1 - Lets Pwn Noobs. Start Time: Wed 2:33pm est. Attendees: tits(c), boobs(c). 4 slots available. use .raid join 1 to sign up.
 * Raid 1 - Lets Pwn Noobs. Start Time Tue 2:30pm est. Attendees tits(c), boobs (p), 5(4) Slots available. 1 Pending.
 * Raid 1 - Lets Pwn Noobs, Start Time, TBD. Pending Attendees: tits(Tuesday 2-4 est) Boobs (wensday 4-5 est) Current common time (None)
 * use .raid join 1 <avaiblity>
 *
 * Use this format for availabiltiy
 * Tue 2pm 4:30pm EST,Wed 4pm 9pm EST
 * 
 * 
 * Commands * is optional
 * .raid create <name> <desc> <start time* <end time*> <recurring>>    (If a start time is given, you can also include recurring in order to make it a weekly raid. Raids with this flag will be simply emptied on weekly reset vs deleted. You may also include a scheduale end time, however it can be left open ended if you are continuing till completion/exhaustion) 
 * Note must use double quotes if spaces are in name or desc.
 * .raid join <name/id> <time available*> (If no start time is set, time available must be included)
 * .raid leave <name/id> (Cancels your signup for the raid)
 * .raid delete <name>  (Only creator/admins/mods can delete raids.
 * 
 * 
 * Admin/Mod only command
 * .raid override @mention <raid command> - Allows an admin or mod to manually run a raid command for a specific user.
 * 
 * Note for schedulaing , create a new task with a delay of (future time - 900000) - current time in Milliseconds. This will create a notification 15 mins prior to raid start to those confirmed and pending. At this time it will also create a raid voice channel
 * in the form of Raid - Raid Name.
 * This channel will be set on the same deletion scehdual as our temp channels. Currently it will inherite the same permissions as our normal channels, as in anyone guest or higher can connect. 
 * If the clan gets bigger and there become problems with this, we will end up using private channels, similar to fireteams, with the addition of a temp permsion for those who signed up to connect.
 * 
 * Database:
 * 
 * raid_main
 * 
 * RAID_ID varchar(36) unique primary key (we will be using java UUID function to generate these)
 * RAID_NAME varchar(24) (limit of 24 chars for raid name)
 * RAID_DESC
 * RAID_CREATOR bigint(20) (Discord ID of creator)
 * RAID_RECURRING tinyint(1)  (mysql version of boolean)
 * 
 * 
 * Database for User Times:
 * 
 * raid_users
 * 
 * RAID_ID varchar(36)
 * USER_ID bigint(20)
 * USER_DAY varchar(3) // 3 letter code for day (SUN,MON,TUE,WED,THU,FRI,SAT)
 * USER_START_TIME (TIME)
 * USER_END_TIME (TIME)
 * 
 * Database for Raid times:
 * 
 * raid_times
 * 
 * RAID_ID
 * RAID_DAY
 * RAID_START_TIME
 * RAID_END_TIME
 * 
 * 
 * We 
 *  Scheduler Methods
 *  
 *  raid_reset - every Tuesday
 * 	annoucment - 15 mins before raid starts
 *  
 *  For channel deletion, we will just store our channel creation in the fireteam database. This will automatically get pulled during the normal cleanup of temp channels.
 *  
 *  DatabaseManager Methods
 *  
 *  addRaid(RaidTemplate raid)
 *  joinRaid(int raidId,RaidUserTemplate user) // for use when no start time is givin)
 *  joinRaid(int raidId, long userId)
 *  leaveRaid(int raidId, long userId)
 *  deleteRaid(int raidId)
 *  
 * 
 */
public class Raid extends Command{

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return null;
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
