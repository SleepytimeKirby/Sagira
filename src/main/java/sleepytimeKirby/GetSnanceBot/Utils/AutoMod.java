package sleepytimeKirby.GetSnanceBot.Utils;

import java.time.LocalDateTime;
import org.apache.commons.text.similarity.LevenshteinDistance;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import sleepytimeKirby.GetSnanceBot.User.UserController;
import sleepytimeKirby.GetSnanceBot.User.UserHeat;
/*Handles all our automated modding
 * Whitelist of links, keep our servers porn and virus free please
 * Can allow someone to post a link thats not whitelisted by using the .permitLink <user> command. They get 30 secs to post links.
 * Mods and Admins are not affected.
 * Note: Regex for link detection. Using greedy detection (as long as tld is not numeric we will grab it, not caring if the link is valid at all) This means that mistakes like end.I 
 * will be detected, However things like 44.3 and other numbers should be fine. Since we are basing links on a whitelist, we dont care if we get invalid urls.
 * \(?\b(https?:\/\/)?(www)?[-A-Za-z0-9+&@#/%?=~_().|!:,;]*\.+[-A-Za-z][-A-Za-z0-9+&@#/%=~_()|]+
 * Includes a warn system, which integrates into our anti=spam system. Posting and getting warnings will incread your heat, which will disapate over time. Automated heat gen disaptes quicker then
 * triggering a manual warn/softban/kick. Heat levels for links go up exponentially. 75 heat triggers a warning message(doesn't generate heat) and at 100 heat you are put in timeout.
 * Timeout consits of a special role and a special channel. If you continue to act up and get to 150 heat (all heat gen is increased in the timeout channel and role) you will be banned for 1 hour
 * after that heat will be reset to 75 and your access restored. Instead of going ot timeout at 100, you will get banned for a day at 110. After a day same thing, except you will be permabanned.
 * If you get banned once, the special requirements will stay for a week.
 * 
 * Things that generate heat and by how much
 * Over 10 second Period.
 * 
 * Posting images:0.8x^3.5
 * Posting Text: x^1.5
 * Posting Links:.25x^15
 * Posting Non-whitelist Links: 2x^2
 * Mentions:2x^2
 * Repeat Message:3^1.755
 *
 * Natural Heat Lost: -30 per 10
 * 
 * What if someone generate bannable heat before warn?
 * Currently they will be banned for 5 mins, and warned via PM. Warnings are a privlage, not a right. We dont have to give you one.
 * After the 5 mins are up your heat will be set to 50 and you will be placed on the second tier (24 hour ban notice).
 * The way its currently set up, most spammers will not hit the warning. They will immediately get banned/timeout.
 * 
 * Per 10 secs
 * Images: Ban at 5, warn at 4
 * Text: Timeout at 13, ban at 14
 * Whitelist: Ban at 6
 * BadLinks: Ban at 4 (Note this links also get deleted immediatly)
 * Mentions: Ban at 3 (3 messages with mentions, not 3 mentions in a message)
 * Repeat Message(Message is similar to last one sent by at least 80%): Ban at 5
 * 
 * Note: Heat is generated on an event basses, but dissipates by a time base. Also there is never negative heat.
 * 
 * 
 * 
 * NOTE EDIT: Currently changing timeout, instead of seperate channel, timeouts are 10 min bans. Bans are hours long bans.
 */
public class AutoMod extends ListenerAdapter{

	DatabaseManager dm = DatabaseManager.getInstance();
	Pattern linkDetection = Pattern.compile("\\(?\\b(https?:\\/\\/)?(www)?[-A-Za-z0-9+&@#/%?=~_().|!:,;]*\\.+[-A-Za-z][-A-Za-z0-9+&@#/%=~_()|]+");
	UserController uc = UserController.getInstance();
	Scheduler s = Scheduler.getInstance();
	Settings sett = Settings.getInstance();
	String modID = sett.get("MOD_ID");
	String adminID = sett.get("ADMIN_ID");
	private static final int WARN_THRESHOLD = 75;
	private static final int TIMEOUT_THRESHOLD = 100;
	private static final int TIMEOUT_DURATION = 10;
	private static final int BAN_THRESHOLD = 150;
	private static final int BAN_DURATION = 60;
	private static final int PERMABAN_WARN_THRESHOLD = 3; // Amount of warnings before permaban. Warnings are any type of action other then a perma ban. I.E both a warn, timeout, and hour ban all add 1
	@Override
	public void onMessageReceived(MessageReceivedEvent e){
		Long userId = e.getAuthor().getIdLong();
		//JDA api = e.getJDA();
		if(e.getAuthor().isBot()){
			return;
		}
		if(!e.isFromType(ChannelType.PRIVATE)){ //Private Message for now are immuyne to heat
			if((e.getAuthor().isBot()||e.getMember().getRoles().contains(e.getGuild().getRoleById(modID))||e.getMember().getRoles().contains(e.getGuild().getRoleById(adminID)))){ // make sure its not admin or mod or bot
				return;
			}
			UserHeat userHeat;
			//Check if Heat Exist
			if(!uc.userHeatExist(userId)){ // create new heat object
				UserHeat newUser = new UserHeat();
				newUser.setUserId(userId);
				newUser.setCurrentHeat(0);
				newUser.setWarnCount(0);
				newUser.setZero();
				uc.setUserHeat(userId, newUser);
				newUser = null;
			}
			userHeat = uc.getUserHeat(userId); // get userHeat
			if(userHeat.getStartHeat()==null) {
				userHeat.setStartHeat(LocalDateTime.now());
			}
			if(ChronoUnit.SECONDS.between(userHeat.getStartHeat(), LocalDateTime.now()) >= 10){ // Check if 10 seconds have past since start of heat cycle.
				//If so, we create a new cycle. I.E Remove 30 heat per 10, set counts to 0
				userHeat.setCurrentHeat(Math.max(Math.floor(ChronoUnit.SECONDS.between(userHeat.getStartHeat(), LocalDateTime.now())/10)*-30,0));//get number of 10 second intervals between start and now, subtract 30 heat per one. If its negative, set heat to 0
				userHeat.setStartHeat(LocalDateTime.now());
				userHeat.setZero();
			}
			double calculatedHeat = 0;
			Matcher match = linkDetection.matcher(e.getMessage().getContent());
			//First check if message is a repeat
			if(userHeat.getLastMessage() != null) {
			if(!userHeat.getLastMessage().isEmpty()) {
				LevenshteinDistance ld = new LevenshteinDistance();
				int distance = ld.apply(userHeat.getLastMessage(), e.getMessage().getContent());
				int maxSize = Math.max(userHeat.getLastMessage().length(), e.getMessage().getContent().length());
				int percent = 100 - ((distance/maxSize)*100); // smaller distance = higher similarity
				if(percent >=80) { // if 80% of the message is similar
					userHeat.addToHeatMap("repeat", userHeat.getFromHeatMap("repeat") + 1);
					calculatedHeat = Math.pow(3*(userHeat.getFromHeatMap("repeat")), 1.755);
					userHeat.setCurrentHeat(userHeat.getCurrentHeat() + calculatedHeat);
					heatCheck(userHeat,e.getMember(),e.getAuthor().getIdLong(),e);
				}
			}
			}
			boolean linkFound = false;
			while(match.find()){
				linkFound = true;
				if(!checkURL(match.group(0))){
					e.getMessage().delete().complete(); // If url doesn't match, delete it and issue a warning, base severity 1
					userHeat.addToHeatMap("badlink", userHeat.getFromHeatMap("badlink") + 1);
					calculatedHeat = Math.pow(2*(userHeat.getFromHeatMap("badlink")), 2);
					userHeat.setCurrentHeat(userHeat.getCurrentHeat() + calculatedHeat);
					heatCheck(userHeat,e.getMember(),e.getAuthor().getIdLong(),e);
					return; // done with automod for this message, delete was issued
				}
			}
			if(linkFound) {
				userHeat.addToHeatMap("whitelist", userHeat.getFromHeatMap("whitelist") + 1);
				calculatedHeat = Math.pow(0.25*(userHeat.getFromHeatMap("whitelist")), 15);
				userHeat.setCurrentHeat(userHeat.getCurrentHeat() + calculatedHeat);
				heatCheck(userHeat,e.getMember(),e.getAuthor().getIdLong(),e);
			}
			if(!e.getMessage().getEmbeds().isEmpty()) {
				// We got somethign attached, either file or image.
				userHeat.addToHeatMap("image", userHeat.getFromHeatMap("image") + 1);
				calculatedHeat = Math.pow(0.8*(userHeat.getFromHeatMap("image")), 3.5);
				userHeat.setCurrentHeat(userHeat.getCurrentHeat() + calculatedHeat);
				heatCheck(userHeat,e.getMember(),e.getAuthor().getIdLong(),e);
			}
			if(!(e.getMessage().getMentionedUsers().isEmpty()&&e.getMessage().getMentionedChannels().isEmpty()&&e.getMessage().getMentionedRoles().isEmpty())) {//if NOT(empty empty empty)
				userHeat.addToHeatMap("mention", userHeat.getFromHeatMap("mention") + 1);
				calculatedHeat = Math.pow(2*(userHeat.getFromHeatMap("mention")), 2);
				userHeat.setCurrentHeat(userHeat.getCurrentHeat() + calculatedHeat);
				heatCheck(userHeat,e.getMember(),e.getAuthor().getIdLong(),e);
			}
			//finally basic message heat
				userHeat.addToHeatMap("text", userHeat.getFromHeatMap("text") + 1);
				calculatedHeat = Math.pow(1*(userHeat.getFromHeatMap("text")),1.5);
				userHeat.setCurrentHeat(userHeat.getCurrentHeat() + calculatedHeat);
				heatCheck(userHeat,e.getMember(),e.getAuthor().getIdLong(),e);
		}
	}
	public void heatCheck(UserHeat userHeatToCheck, Member member,Long id,MessageReceivedEvent e) {
	
	if(userHeatToCheck.getWarnCount() >= PERMABAN_WARN_THRESHOLD) {
		dm.addModList("God Emperor AutoMod",Long.valueOf("46336737672886663"), "User " + member.getEffectiveName() +" wore AutoMod's patience to thin. They are now PermaBanned!");
		member.getGuild().getController().ban(member,1,"Automod PermaBan for spamming");
	}
	if(userHeatToCheck.getCurrentHeat() > BAN_THRESHOLD){
		s.createTempBan(id, member.getGuild().getIdLong(), BAN_DURATION);
		member.getGuild().getController().ban(member,1,"Automod Temp Ban ("+ BAN_DURATION +" Minutes) for spamming");
		dm.addModList("Scheduled Action via Bot",(long) 404, "User " + member.getEffectiveName() +" was banned by AutoMod for 60 Minutes.");
		userHeatToCheck.setWarnCount(userHeatToCheck.getWarnCount()+1);
	}else if (userHeatToCheck.getCurrentHeat() >= TIMEOUT_THRESHOLD) {
		member.getGuild().getController().ban(member,1,"Automod Temp Ban ("+ TIMEOUT_DURATION +" Minutes) for spamming");
		s.createTempBan(id, member.getGuild().getIdLong(), TIMEOUT_DURATION);
		dm.addModList("Scheduled Action via Bot",(long) 404, "User " + member.getEffectiveName() +" was banned by AutoMod for 10 Minutes.");
		userHeatToCheck.setWarnCount(userHeatToCheck.getWarnCount()+1);
	}else if (userHeatToCheck.getCurrentHeat() >= WARN_THRESHOLD) {
		e.getAuthor().openPrivateChannel().complete();
		PrivateChannel chan = e.getPrivateChannel();
		chan.sendMessage("You are sending too many messages to quickly. Please calm the fuck down. Thank you.");
		dm.addModList("Scheduled Action via Bot",(long) 404, "User " + member.getEffectiveName() +" was Warned by AutoMod");
		userHeatToCheck.setWarnCount(userHeatToCheck.getWarnCount()+1);
	}
	
	}
	
	public boolean checkURL(String URL){ // returns is link is allowed or not.
		//Using \(?\b(https?:\/\/)?(www)?[-A-Za-z0-9+&@#/%?=~_().|!:,;]*(urlhere)+[-A-Za-z0-9+&@#/%=~_()|]+
		//if no wildcard for subdomains, remove the [-A-Za-z0-9+&@#/%?=~_().|!:,;]*
		ArrayList<String> whitelist = dm.getWhitelistUrl();
		boolean allowed = false;
		for(String entry:whitelist){
			if(Pattern.matches("\\(?\\b(https?:\\/\\/)?(www)?[-A-Za-z0-9+&@#/%?=~_().|!:,;]*"+entry+"+[-A-Za-z0-9+&@#/%=~_()|]+", URL)){
				allowed=true;
			}
		}
		return allowed;
	}


}
