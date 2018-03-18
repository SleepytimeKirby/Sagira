package sleepytimeKirby.GetSnanceBot.commands.music;

import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import sleepytimeKirby.GetSnanceBot.Utils.Music.Music;
import sleepytimeKirby.GetSnanceBot.commands.ModCommand;

public class Skip extends ModCommand{
	Music m = Music.getInstance();
	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		m.skip();
	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList("!skip");
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
