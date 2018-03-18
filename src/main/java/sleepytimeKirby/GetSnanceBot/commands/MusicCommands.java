package sleepytimeKirby.GetSnanceBot.commands;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import sleepytimeKirby.GetSnanceBot.Utils.Settings;
import sleepytimeKirby.GetSnanceBot.Utils.Music.Music;
@Deprecated
public class MusicCommands extends Command{
/*
 *  .music add
 *  .music skip
 *  .music clear
 * 
 */
	
	Settings s = Settings.getInstance();
	private List<String> overrideRoles = Arrays.asList(s.get("ADMIN_ID"),s.get("MOD_ID"),s.get("DJ_ID"));
	Music m = Music.getInstance();
	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		boolean override = false;
		for(Role role:e.getMember().getRoles()) {
			if(overrideRoles.contains(role.getId())) {
				override = true;
			}
		}
		switch(args[1]) {
		case("add"):
			m.add(args[2], override);
			break;
		case("now"):
			m.now(args[2], override);
			break;
		case("clear"):
			if(override) {
				m.clear();
			}
			break;
		case("volume"):
			if(override) {
				String target = args[2];
				if(NumberUtils.isCreatable(target)) {
					m.volume(Integer.valueOf(target));
				} else {
					sendMessage(e,"Not a valid number");
				}
			} else {
				sendMessage(e,"Sorry, must be either admin, mod, or dj to use this command");
			}
			break;
		case("skip"):
			if(override) {
				m.skip();
			//}  else if(){
				//To-Do: add voteskip
				
			} else {
				sendMessage(e,"Sorry, must be either admin, mod, or dj to use this command");
			}
			break;
		default:
			sendMessage(e,"Unrecognized arguement " + args[1] +". Please try again.");
		}
	}
	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList(".music");
				
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
