package sleepytimeKirby.GetSnanceBot.commands.music;

import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import sleepytimeKirby.GetSnanceBot.Utils.Settings;
import sleepytimeKirby.GetSnanceBot.Utils.Music.Music;
import sleepytimeKirby.GetSnanceBot.commands.Command;

public class Play extends Command{
	Settings s = Settings.getInstance();
	Music m = Music.getInstance();
	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		m.add(args[1], isDJ(e));		
	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList("!play");
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

	private boolean isDJ(MessageReceivedEvent e) {
    	List<Role> roles = e.getMember().getRoles();
    	for(Role role:roles) {
    		if(role.getId().equals(s.get("ADMIN_ID"))||role.getId().equals(s.get("MOD_ID"))||role.getId().equals(s.get("DJ_ID"))) {
    			return true;
    		}
    	}
    	return false;
    }
}
