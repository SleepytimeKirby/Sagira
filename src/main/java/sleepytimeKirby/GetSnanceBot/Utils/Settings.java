package sleepytimeKirby.GetSnanceBot.Utils;

import java.util.HashMap;

/*
 * 
 * Settings file. Currently storing in database in a Settings table, since I don't want to deal with File.io bullshit.
 * Database layout
 * 
 * SETTING_NAME varchar(32) unique
 * SETTING_VALUE varchar(128)
 *  
 * 
 *  Current settings:
 *  Discord API key
 *  Bungie API key
 *  Guild ID
 *  Moderation Logging Channel ID
 *  Raid Channel ID
 *  Role IDs
 *  Group Default Channel ID
 *  
 * 
 */


public class Settings {
	//Initalize singleton
	private static Settings settings = null;
	private HashMap<String,String> settingList;
	private Settings(){};
    public static Settings getInstance(){
    	if(settings == null) {
    		settings = new Settings();
         }
         return settings;
    }

    
    public void init() {
    	DatabaseManager dm = DatabaseManager.getInstance();
    	settingList = dm.getSettings();
    }
    
    public String get(String setting) {
    	String result = settingList.get(setting);
    	if (result == null) {
    		System.out.println("Whoops, setting:" + setting + " wasn't found!");
    	}
    	return result;
    }
	
}
