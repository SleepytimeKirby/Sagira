package sleepytimeKirby.GetSnanceBot.commands;

import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import sleepytimeKirby.GetSnanceBot.Utils.DatabaseManager;
import sleepytimeKirby.GetSnanceBot.Utils.Settings;

public class WhitelistLink extends Command{
	DatabaseManager dm = DatabaseManager.getInstance();
	Settings sett = Settings.getInstance();
	String modID = sett.get("MOD_ID");
	String adminID = sett.get("ADMIN_ID");
	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		if(e.getMember().getRoles().contains(e.getGuild().getRoleById(modID))||e.getMember().getRoles().contains(e.getGuild().getRoleById(adminID))){
		// TODO Auto-generated method stub
		if(args.length < 3){
			sendMessage(e,new MessageBuilder().append("Wrong number of arguments, please try again").build());
			return;
		}
		if(args[1].equalsIgnoreCase("add")){
			dm.addWhitelistUrl(args[2]);
			sendMessage(e,"Url " + args[2] + " added!");
		}else if (args[1].equalsIgnoreCase("del")){
			dm.removeWhitelistUrl(args[2]);
		}else {
			sendMessage(e,new MessageBuilder().append("Must use add or del, please try again").build());
			return;
		}
		}else {
			sendMessage(e,"Only mods or admins can whitelist urls. Please contact one with a request to whitelist a url.");
		}
	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList(".whitelist",".wl");
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Whitelist urls";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "whitelist";
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return "Usage: .whitelist <add/del> <url>";
	}

}
