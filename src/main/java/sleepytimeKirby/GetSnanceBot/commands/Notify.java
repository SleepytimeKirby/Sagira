package sleepytimeKirby.GetSnanceBot.commands;

import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import sleepytimeKirby.GetSnanceBot.Utils.DatabaseManager;

public class Notify extends Command{
	DatabaseManager dm = DatabaseManager.getInstance();
	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		// TODO Auto-generated method stub
		String opt = args[1];
		switch(opt) {
		case("add"):
			dm.insertUserNoti(e.getAuthor().getIdLong());
			sendMessage(e,"Added to notification list.");
			break;
		case("remove"):
			dm.delUserNoti(e.getAuthor().getIdLong());
			sendMessage(e,"Removed from notification list.");
			break;
		default:
			sendMessage(e,"Wrong option, please try again");
		}
	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList(".notify");
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Adds or removes notifications for 300 engrams";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Notify";
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return ".notify [add|remove]";
	}

}
