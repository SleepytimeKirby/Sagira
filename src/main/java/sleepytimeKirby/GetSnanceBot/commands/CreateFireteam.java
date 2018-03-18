package sleepytimeKirby.GetSnanceBot.commands;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Category;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.GuildController;
import net.dv8tion.jda.core.requests.restaction.ChannelAction;
import sleepytimeKirby.GetSnanceBot.DatabaseClasses.Fireteams;
import sleepytimeKirby.GetSnanceBot.Utils.DatabaseManager;
@Deprecated  // We are moving from fireteams to a more generic LFG. Use GroupFinder Class
public class CreateFireteam extends Command{

	DatabaseManager dm = DatabaseManager.getInstance();
	//Member Role ID for private 
			String roleID = "375431228523806721";
			//Guest Role ID for guest enabled public
			String guestRoleID = "375739065959710720";
			String honoraryMembersID = "376057114688356352";
			// Join Permisions for Teams
			Collection<Permission> privateChannel = new ArrayList<Permission>();
			//Naming List here (We use the NATO Phonetic Alphabet for our temp channel names)
			List<String> names = Arrays.asList("Alpha","Beta","Charlie","Delta","Foxtrot","Golf","Hotel","India","Juliett","Kilo","Lima",
					"Mike","Novemember","Oscar","Papa","Quebec","Romeo","Sierra","Tango","Uniform","Victor","Whiskey","Xray","Yankee","Zulu");
			List<String> channelNames = new ArrayList<String>();
	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		//testing for shit args. In theroy since its two strings, default is members only, if they mess up it will still work as long as at least 2 extra args are included regardless of text.
		if(args.length < 3){
			sendMessage(e,new MessageBuilder().append("Wrong number of arguments, please try again").build());
			return;
		}
		if(e.getMember().getRoles().contains(e.getGuild().getRoleById(guestRoleID))||e.getMember().getRoles().contains(e.getGuild().getRoleById(honoraryMembersID))){
			sendMessage(e,new MessageBuilder().append("Sorry, must be a member or higher to create a temporary channel.").build());
			return;
		}
		String type = args[2];
		String game = args[1];
		try{
			privateChannel.add(Permission.VOICE_CONNECT);
			List<Fireteams> fireTeams = dm.getFireteams();
			for(Fireteams fireteam:fireTeams) {
				channelNames.add(fireteam.getChannelName());
			}
			for(String name:names){
				if(!channelNames.contains("Fireteam " + name)){
					boolean guests = false;
					boolean privateAccess = false;
					GuildController gc = new GuildController(e.getGuild()); 
					Category cat = e.getGuild().getCategoriesByName("FIRETEAMS", true).get(0);
					ChannelAction ca = cat.createVoiceChannel("Fireteam " + name + " (" + game + ")");
					if(type.equalsIgnoreCase("private")){
					ca.addPermissionOverride(gc.getGuild().getRoleById(roleID),null,privateChannel);
					privateAccess = true;
					}
					if(type.equalsIgnoreCase("public")){
						ca.addPermissionOverride(gc.getGuild().getRoleById(guestRoleID),privateChannel,null);
						ca.addPermissionOverride(gc.getGuild().getRoleById(honoraryMembersID),privateChannel,null);
						guests = true;
					}
					ca.complete();
					Random rn = new Random();
					Long randomKey = rn.nextLong() + 1;
					rn = null;
					cat = e.getGuild().getCategoriesByName("FIRETEAMS", true).get(0);
					VoiceChannel vc = getVoiceChannel(cat.getVoiceChannels(),("Fireteam " + name + " (" + game + ")"));
					dm.addChannel(vc.getIdLong(),e.getGuild().getIdLong(), "Fireteam " + name, privateAccess, guests, randomKey);
					gc.moveVoiceMember(e.getMember(),vc).complete();
					if(type.equalsIgnoreCase("public")){
					sendMessage(e,new MessageBuilder().append("Fireteam created. Channel will be deleted if empty").build());
					}
					else {
						sendPrivateMessage(e,new MessageBuilder().append("Fireteam created. Random Key is " + randomKey +". You may give this to other uses so they may join using the .joinFireteam <key> command by whispering GetSnanceBot. Channel will be deleted if empty").build());
					}
					break;
				}
			}
			
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
	}
	private VoiceChannel getVoiceChannel(List<VoiceChannel> voiceChannels,String name) {
		for (VoiceChannel voice:voiceChannels) {
			if(voice.getName().equals(name)) {
				return voice;
			}
		}
		return null;
	}
	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList(".createFireteam",".cf","createft");
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Use to create a fireteam voice channel.";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Create Fireteam";
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return "Usage: .createFireteam <game> <public/members/private>.\n "
				+ "game: what game the group is for. \n"
				+ "public: anyone can join \n"
				+ "members: only members can freely join. Guests must be added in or join using a verification code. \n"
				+ "private: all users must be either added manually using .addFireteam or join using a verificatoin code.";
	}
	

}
