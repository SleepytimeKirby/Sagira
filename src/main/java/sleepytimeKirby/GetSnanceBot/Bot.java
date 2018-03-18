package sleepytimeKirby.GetSnanceBot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import sleepytimeKirby.GetSnanceBot.User.UserController;
import sleepytimeKirby.GetSnanceBot.Utils.AutoMod;
import sleepytimeKirby.GetSnanceBot.Utils.CentralMessenger;
import sleepytimeKirby.GetSnanceBot.Utils.DatabaseManager;
import sleepytimeKirby.GetSnanceBot.Utils.Scheduler;
import sleepytimeKirby.GetSnanceBot.Utils.Settings;
import sleepytimeKirby.GetSnanceBot.Utils.VendorGearTracker;
import sleepytimeKirby.GetSnanceBot.Utils.Music.Music;
import sleepytimeKirby.GetSnanceBot.commands.*;
import sleepytimeKirby.GetSnanceBot.commands.music.*;

/**
 * Hello world!
 *
 */
public class Bot 
{
	private static JDA api;
    public static void main( String[] args )
    {
        try{
        	DatabaseManager dm = DatabaseManager.getInstance();
        	dm.inializeConnection();
        	UserController uc = UserController.getInstance();
        	//uc.setUserHeats(dm.getUserHeats()); // Initalize Userheats
        	Settings s = Settings.getInstance();
        	s.init();
        	
        	JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT).setToken(Messages.getString("Bot.APIKey")); //$NON-NLS-1$
        	//jdaBuilder.addEventListener(new InitializeBot());
        	jdaBuilder.addEventListener(new AddGuests());
        	jdaBuilder.addEventListener(new EVerify());
        	//jdaBuilder.addEventListener(new CreateFireteam());
        	//jdaBuilder.addEventListener(new JoinFireteam());
        	//Warning, uncommenting the below line will give this bot powers. Unsure if ai can be trusted. Procede with Caution.
        	jdaBuilder.addEventListener(new AutoMod());
        	jdaBuilder.addEventListener(new Notify());
        	jdaBuilder.addEventListener(new WhitelistLink());
        	jdaBuilder.addEventListener(new GroupFinder());
        	//jdaBuilder.addEventListener(new MusicCommands());
        	
        	//Adding Music Commands
        	jdaBuilder.addEventListener(new Clear());
        	jdaBuilder.addEventListener(new Play());
        	jdaBuilder.addEventListener(new PlayNow());
        	jdaBuilder.addEventListener(new Shuffle());
        	jdaBuilder.addEventListener(new Skip());
        	jdaBuilder.addEventListener(new Volume());
        	jdaBuilder.addEventListener(new VoteSkip());
        	api = jdaBuilder.buildBlocking();
        	CentralMessenger cm = CentralMessenger.getInstance();
        	cm.init(api);
        	Scheduler schedule = Scheduler.getInstance();
        	schedule.initalize();
        	VendorGearTracker vgt = new VendorGearTracker();
        	vgt.initalize(api);
        	Music m = Music.getInstance();
        	m.init();
        }catch(Exception x) {
        	x.printStackTrace();
        }
    }
    
    private void initateServer() {
    	//Create Roles
    	//Create Catagorys
    	//Create Text Channels
    	//Create Voice Channels
    	//Modify Permissions
    	//Setup Database - Need new method for inital setup in manager
    	
    }
}
