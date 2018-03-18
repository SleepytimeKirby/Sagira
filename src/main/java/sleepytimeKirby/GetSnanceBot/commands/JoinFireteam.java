package sleepytimeKirby.GetSnanceBot.commands;

import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import sleepytimeKirby.GetSnanceBot.DatabaseClasses.Fireteams;
import sleepytimeKirby.GetSnanceBot.Utils.DatabaseManager;
@Deprecated // We are moving from fireteams to a more generic LFG. Use GroupFinder Class
public class JoinFireteam extends Command{

	DatabaseManager dm = DatabaseManager.getInstance();
	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		if(args.length < 2){
			sendMessage(e,new MessageBuilder().append("No key detected, please try again").build());
			return;
		}
		Long userId = e.getAuthor().getIdLong();
		String key = args[1];
		Fireteams fireteam = dm.getFireteam(Long.valueOf(key));
		if(fireteam.getChannelId() == 0 && fireteam.getGuildId() == 0){
			sendMessage(e,new MessageBuilder().append("Invalid key, please try again").build());
			return;
		}
		Guild guild = e.getJDA().getGuildById(fireteam.getGuildId());
		guild.getController().moveVoiceMember(guild.getMemberById(userId),guild.getVoiceChannelById(fireteam.getChannelId())).complete();
		
		
		
	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList(".joinFireteam",".joinft");
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Join a fireteam using a private key";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Join Fireteam";
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return "Usage: joinFiretam <key>"
				+ "key: Private Key associated with fireteam, creator recieves it.";
	}

}
