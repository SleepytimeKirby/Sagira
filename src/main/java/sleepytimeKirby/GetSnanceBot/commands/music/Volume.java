package sleepytimeKirby.GetSnanceBot.commands.music;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import sleepytimeKirby.GetSnanceBot.Utils.Music.Music;
import sleepytimeKirby.GetSnanceBot.commands.ModCommand;

public class Volume extends ModCommand{
	Music m = Music.getInstance();

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		String target = args[1];
		if(NumberUtils.isCreatable(target)) {
			m.volume(Integer.valueOf(target));
			sendMessage(e,"Volume changed to " + args[1]);
		} else {
			sendMessage(e,"Not a valid number");
		}
		
	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList("!volume");
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
