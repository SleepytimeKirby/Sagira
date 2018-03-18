package sleepytimeKirby.GetSnanceBot.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import sleepytimeKirby.GetSnanceBot.Messages;
import sleepytimeKirby.GetSnanceBot.DatabaseClasses.*;
import sleepytimeKirby.GetSnanceBot.User.UserHeat;

import com.mysql.cj.jdbc.MysqlDataSource;

public class DatabaseManager {
	MysqlDataSource ds = new MysqlDataSource();  
    static Connection conn = null;
    Statement s;
    PreparedStatement psInsert;
    PreparedStatement channelInsert;
    PreparedStatement guestInsert;
    PreparedStatement verifyInsert;
    PreparedStatement modListInsert;
    PreparedStatement schedulerInsert;
    PreparedStatement channelSelect;
    PreparedStatement guestSelect;
    PreparedStatement verifySelect;
    PreparedStatement modListSelect;
    PreparedStatement channelSelectAll;
    PreparedStatement channelDelete;
    PreparedStatement guestDelete;
    PreparedStatement addWhitelist;
    PreparedStatement removeWhitelist;
    PreparedStatement getWhitelist;
    PreparedStatement getUserHeat;
    PreparedStatement getAllUserHeat;
    PreparedStatement saveUserHeat;
    PreparedStatement getSettings;
    PreparedStatement insertGroup;
    PreparedStatement insertRaid;
    PreparedStatement updateRaid;
    PreparedStatement updateLeader;
    PreparedStatement getGroupPlayers;
    PreparedStatement getGroupInfo;
    PreparedStatement insertVendor;
    PreparedStatement selectVendor;
    PreparedStatement insertUserNotify;
    PreparedStatement selectUserNotify;
    PreparedStatement delUserNotify;
    PreparedStatement isGroupLeader;
    PreparedStatement delGroup;
    PreparedStatement delPlayerFromGroup;
    PreparedStatement insertPlayerGroup;
    PreparedStatement updatePendingPlayer;
    PreparedStatement selectAllVendors;
    PreparedStatement insertSetting;
    private static DatabaseManager dm = null;
    
    private DatabaseManager(){};
    public static DatabaseManager getInstance(){
    	if(dm == null) {
    		dm = new DatabaseManager();
         }
         return dm;
    }
    
    public void inializeConnection(){
		 try {
	            // Create (if needed) and connect to the database.
	            // The driver is loaded automatically.
			 	ds.setUser(Messages.getString("DatabaseManager.DatabaseUser")); //$NON-NLS-1$
			 	ds.setPassword(Messages.getString("DatabaseManager.DatabasePassword")); //$NON-NLS-1$
			 	ds.setServerName(Messages.getString("DatabaseManager.DatabaseServer")); //$NON-NLS-1$
			 	ds.setDatabaseName(Messages.getString("DatabaseManager.DatabaseName")); //$NON-NLS-1$
	            conn = ds.getConnection(); 
	            System.out.println("Connected to database " + ds.getDatabaseName());
	            
	            //   ## INITIAL SQL SECTION ## 
	            //   Create a statement to issue simple commands.  
	            s = conn.createStatement();
	             // Call utility method to check if table exists.
	             //      Create the table if needed
	       /*      createTableIfNeeded(createString1);
	             createTableIfNeeded(createString2);
	             createTableIfNeeded(createString3);
	             createTableIfNeeded(createString4); */
	            
	             channelInsert = conn.prepareStatement("insert into temp_channel(CHANNEL_ID,GUILD_ID,CHANNEL_NAME,CHANNEL_PRIVATE,CHANNEL_GUESTS_ALLOWED,CHANNEL_PRIVATE_KEY) "
	             		+ "values (?,?,?,?,?,?)");
	             guestInsert = conn.prepareStatement("insert into guests(USER_ID,GUILD_ID,ACCESS_LENGTH) "
		             		+ "values (?,?,?)");
	             verifyInsert = conn.prepareStatement("insert into verification_list(VERIFY_ID,VERIFICATION_CODE,BATTLE_NET_ID,BUNGIE_ID) "
		             		+ "values (?,?,?,?)");
	             modListInsert = conn.prepareStatement("insert into mod_list(MOD_USER,MOD_USER_ID,MOD_ACTION) "
		             		+ "values (?,?,?)");
	             schedulerInsert = conn.prepareStatement("INSERT INTO scheduler(TASK_NAME,TASK_TYPE,DURATION,REPEAT) VALUES(?,?,?,?)");
	             channelSelect = conn.prepareStatement("select CHANNEL_ID,GUILD_ID,CHANNEL_NAME from temp_channel WHERE CHANNEL_PRIVATE_KEY = ?");
	             channelSelectAll = conn.prepareStatement("select CHANNEL_ID,GUILD_ID,CHANNEL_NAME from temp_channel");
	             guestSelect = conn.prepareStatement("SELECT USER_ID,GUILD_ID,JOIN_DATE,ACCESS_LENGTH from guests");
	             verifySelect = conn.prepareStatement("SELECT VERIFICATION_CODE,BATTLE_NET_ID,BUNGIE_ID FROM verification_list WHERE VERIFY_ID = ?");
	             modListSelect = conn.prepareStatement("SELECT * from mod_list WHERE ENTRY_DATE > ?");	            
	             channelDelete = conn.prepareStatement("DELETE from temp_channel WHERE CHANNEL_ID = ? AND GUILD_ID = ?");
	             guestDelete = conn.prepareStatement("DELETE from guests WHERE USER_ID = ?");
	             addWhitelist = conn.prepareStatement("Insert IGNORE INTO whitelist(LINK_URL) values(?)");
	             removeWhitelist = conn.prepareStatement("DELETE from whitelist WHERE LINK_URL = ?");
	             getWhitelist = conn.prepareStatement("SELECT LINK_URL from whitelist");
	             getUserHeat = conn.prepareStatement("SELECT * FROM userheat WHERE USER_ID = ?");
	             saveUserHeat = conn.prepareStatement("INSERT INTO userheat(USER_ID,CURRENT_HEAT,WARN_COUNT) values (?,?,?) ON DUPLICATE KEY UPDATE"
	             		+ "CURRENT_HEAT = ?,WARN_COUNT = ?");
	             getAllUserHeat = conn.prepareStatement("SELECT * FROM userheat");
	             getSettings = conn.prepareStatement("SELECT * FROM settings");
	             insertSetting = conn.prepareStatement("INSERT INTO settings VALUES (?,?)");
	             insertGroup = conn.prepareStatement("INSERT INTO group_finder(GROUP_UUID,GROUP_TYPE,GROUP_GAME,GROUP_SLOTS,GROUP_ACTIVITY,GROUP_NOTES,GROUP_KEY,GROUP_LEADER,GROUP_CHANNEL_ID,GROUP_ROLE_ID,GROUP_POST_ID)"
	             		+ "values(?,?,?,?,?,?,?,?,?,?,?)");
	             updateLeader = conn.prepareStatement("UPDATE group_finder SET GROUP_LEADER = ? WHERE GROUP_UUID = ?");
	             getGroupPlayers = conn.prepareStatement("Select PLAYER_ID,PENDING from group_finder_players where GROUP_UUID = ?");
	             delPlayerFromGroup = conn.prepareStatement("DELETE FROM group_finder_players where PLAYER_ID = ? AND GROUP_UUID = ?");
	             insertPlayerGroup = conn.prepareStatement("INSERT INTO group_finder_players(GROUP_UUID,PLAYER_ID,PENDING) values(?,?,?)");
	             updatePendingPlayer = conn.prepareStatement("UPDATE group_finder_player SET PENDING = ? WHERE GROUP_UUID = ? AND PLAYER_ID = ?");
	             getGroupInfo = conn.prepareStatement("Select GROUP_SLOTS,GROUP_KEY,GROUP_LEADER,GROUP_CHANNEL_ID,GROUP_ROLE_ID,GROUP_POST_ID FROM group_finder where GROUP_UUID = ?");
	             insertVendor = conn.prepareStatement("INSERT INTO vendors(VENDOR_NAME,MESSAGE_ID) values (?,?)");
	             selectVendor = conn.prepareStatement("SELECT MESSAGE_ID FROM vendors WHERE VENDOR_NAME = ?");
	             selectAllVendors = conn.prepareStatement("Select MESSAGE_ID FROM vendors");
	             insertUserNotify = conn.prepareStatement("INSERT IGNORE INTO user_notify(USER_ID) values(?)");
	             selectUserNotify = conn.prepareStatement("SELECT * from user_notify");
	             delUserNotify = conn.prepareStatement("DELETE FROM user_notify WHERE USER_ID = ?");
	             isGroupLeader = conn.prepareStatement("SELECT GROUP_UUID from group_finder where GROUP_LEADER = ?");
	             delGroup = conn.prepareStatement("DELETE FROM group_finder WHERE GROUP_UUID = ?");
		 } catch(SQLException e) {
		 		
		 		e.printStackTrace();
	}
    }
    public void insertSettings(HashMap<String,String> settings) {
    	settings.forEach((key, value) -> {
    		try {
    			insertSetting.setString(1, key);
    			insertSetting.setString(2, value);
    			insertSetting.executeUpdate();
    		}catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	});
    }
    public void updateLeader(String groupUUID,Long playerId) {
    	try {
    		updateLeader.setString(2, groupUUID);
    		updateLeader.setLong(1, playerId);
    		updateLeader.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public ArrayList<Long> selectAllVendorMessage(){
    	ArrayList<Long> vendors = new ArrayList<Long>();
    	try {
    		ResultSet rs = selectAllVendors.executeQuery();
    		while(rs.next()) {
    			vendors.add(rs.getLong(1));
    		}
    	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return vendors;
    }
    public void updatePlayerApproval(String groupUUID,Long playerId,int pending) {
    	try {
    		insertPlayerGroup.setString(2, groupUUID);
    		insertPlayerGroup.setLong(3, playerId);
    		insertPlayerGroup.setInt(1, pending);//0=notpending/approved, 1 = pending, -1 = denied
    		insertPlayerGroup.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void insertPlayerinGroup(String groupUUID,Long playerId,int pending) {
    	try {
    		insertPlayerGroup.setString(1, groupUUID);
    		insertPlayerGroup.setLong(2, playerId);
    		insertPlayerGroup.setInt(3, pending);//0=notpending/approved, 1 = pending, -1 = denied
    		insertPlayerGroup.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void deletePlayerFromGroup(Long playerId,String groupUUID) {
    	try {
    		delPlayerFromGroup.setLong(1, playerId);
    		delPlayerFromGroup.setString(2, groupUUID);
    		delPlayerFromGroup.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void deleteGroup(String groupUUID) {
    	try {
			delGroup.setString(1, groupUUID);
			delGroup.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public HashMap<Long,Integer>  getGroupPlayers(String groupUUID) {
    	HashMap<Long,Integer>  players = new HashMap<Long,Integer>();
    	try {
    		getGroupPlayers.setString(1, groupUUID);
    		ResultSet rs = getGroupPlayers.executeQuery();
    		while(rs.next()) {
    			players.put(rs.getLong(1),rs.getInt(2));
    		}
    	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return players;
    }
    public String isUserGroupLeader(Long userId) {
    	//Return UUID of group they are a leader of if they are a leader.
    	try {
    		isGroupLeader.setLong(1, userId);
    		ResultSet rs = isGroupLeader.executeQuery();
    		if(rs.next()) {
    			return rs.getString(1);
    		}
    		return "0";
    	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return "-1";
    }
    public void insertUserNoti(Long userId) {
    	try {
    		insertUserNotify.setLong(1, userId);
    		insertUserNotify.executeUpdate();
	    	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void delUserNoti(Long userId) {
    	try {
    		delUserNotify.setLong(1, userId);
    		delUserNotify.executeUpdate();
	    	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public ArrayList<Long> getUserNoti() {
    	ArrayList<Long> out = new ArrayList<Long>();
    	try {  		
    		ResultSet rs = selectUserNotify.executeQuery();
	    	while(rs.next()) {
	    		out.add(rs.getLong(1));
	    	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return out;
    }
    public long getVendor(String vendor) {
    	try {
    		selectVendor.setString(1, vendor);
    		ResultSet rs = selectVendor.executeQuery();
    		if(rs.next()) {
    		return rs.getLong(1);
    		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return -1;
    }
    public void insertVendor(String vendorName, long messageId) {
    	try {
    		insertVendor.setString(1, vendorName);
    		insertVendor.setLong(2,messageId);
    		insertVendor.executeUpdate();
	    	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public Group getGroupDetails(String uuid) {
    	Group group = new Group();
    	try {
    		getGroupPlayers.setString(1, uuid);
    		ResultSet rs1 = getGroupPlayers.executeQuery();
    		getGroupInfo.setString(1, uuid);
    		ResultSet rs2 = getGroupInfo.executeQuery();
    		int players = 0;
    		while(rs1.next()){
    			if(rs1.getInt(2) == 0) {
    				players++; // Only care for those who are approved
    			}
    		}
    		if(rs2.next()) {
    		group.setType(rs2.getInt(7));
    		group.setSlots(rs2.getInt(1));
    		group.setKey(rs2.getLong(2));
    		group.setLeaderId(rs2.getLong(3));
	    	group.setChannelId(rs2.getLong(4));
	    	group.setRoleId(rs2.getLong(5));
	    	group.setSlotsTaken(players);
    		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return group;
    }
    public void insertGroup(String uuid,int type,String game,int slots,String activity,String notes,long key,long leaderId,long channelId,long roldId,long postId) {
    	try {
			insertGroup.setString(1, uuid);
			insertGroup.setInt(2, type);
			insertGroup.setString(3, game);
			insertGroup.setInt(4, slots);
			insertGroup.setString( 5, activity);
			insertGroup.setString(6, notes);
			insertGroup.setLong(7, key);
			insertGroup.setLong(8, leaderId);
			insertGroup.setLong(9, channelId);
			insertGroup.setLong(10,roldId);
			insertGroup.setLong(11, postId);
			insertGroup.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    public HashMap<String,String> getSettings(){
    	HashMap<String,String> results = new HashMap<String,String>();
    	try {
    		ResultSet rs = getSettings.executeQuery();
    		while(rs.next()){
    			results.put(rs.getString(1),rs.getString(2));
    		}
	    	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return results;
    }
    public void addWhitelistUrl(String url){
    	try {
    		addWhitelist.setString(1, url);
    		addWhitelist.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @Deprecated
    public HashMap<Long, UserHeat> getUserHeats(){
    	HashMap<Long, UserHeat> results = new HashMap<Long, UserHeat>();
    	try {
    		ResultSet rs = getAllUserHeat.executeQuery();
    		while(rs.next()){
    			UserHeat user = new UserHeat(rs.getLong(1),rs.getDouble(2),rs.getInt(3));
    			results.put(rs.getLong(1), user);
    		}
	    	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return results;
    }
    public UserHeat getUserHeat(Long userId){
    	try{
    		getUserHeat.setLong(1, userId);
    		ResultSet rs = getUserHeat.executeQuery();
    		rs.next();
			UserHeat user = new UserHeat(rs.getLong(1),rs.getDouble(2),rs.getInt(3));
			return user;
    	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
    public void removeWhitelistUrl(String url){
    	try {
    		removeWhitelist.setString(1, url);
	    	removeWhitelist.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public ArrayList<String> getWhitelistUrl(){
    	ArrayList<String> results = new ArrayList<String>();
    	try {
			ResultSet rs = getWhitelist.executeQuery();
			while(rs.next()){
				results.add(rs.getString(1));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return results;
    }
    public void channelDelete(Long channelId,Long guildId){
    	try {
			channelDelete.setLong(1, channelId);
	    	channelDelete.setLong(2, guildId);
	    	channelDelete.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void deleteGuest(Long guestId){
    	try {
    		guestDelete.setLong(1, guestId);
    		guestDelete.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public VerificationCode getVerify(Long userId){
    	try {
    		verifySelect.setLong(1, userId);
    		ResultSet rs = verifySelect.executeQuery();
    		rs.next();
    		VerificationCode verify = new VerificationCode(rs.getLong(1),rs.getLong(2),rs.getLong(3));
    		rs.close();
    		return verify;
    	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return new VerificationCode(-1,-1,-1);
    }
    public ArrayList<Guests> getGuest(){
    	ArrayList<Guests> guestList= new ArrayList<Guests>();
    	try {
			ResultSet rs = guestSelect.executeQuery();
			while(rs.next()){
				Guests guest = new Guests(rs.getLong(1),rs.getLong(2),rs.getTimestamp(3),rs.getInt(4));
				guestList.add(guest);
			}
			rs.close();
			return guestList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		return guestList;
    	
    }
    public ArrayList<Fireteams> getFireteams(){
    	ArrayList<Fireteams> fireteamList = new ArrayList<Fireteams>();
    	try{
    		ResultSet rs = channelSelectAll.executeQuery();
    		while(rs.next()){
    			Fireteams fireteam = new Fireteams(rs.getLong(1),rs.getLong(2),rs.getString(3));
    			fireteamList.add(fireteam);
    		}
    		rs.close();
    		return fireteamList;
    	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fireteamList;
    	
    }
    public Fireteams getFireteam(long accessCode){
    	try {
			channelSelect.setLong(1, accessCode);
			ResultSet rs = channelSelect.executeQuery();
	    	rs.next();
	    	Fireteams fireteam = new Fireteams(rs.getLong(1),rs.getLong(2),rs.getString(3));
	    	rs.close();
	    	return fireteam;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return new Fireteams(0,0,"Error");
    }
    public void addChannel(Long id,Long guildId, String name,boolean privateChannel, boolean guests, Long accessCode){
 	   try {
		channelInsert.setLong(1, id);
		channelInsert.setLong(2, guildId);
		channelInsert.setString(3, name);
		channelInsert.setBoolean(4, privateChannel);
		channelInsert.setBoolean(5, guests);
		channelInsert.setLong(6, accessCode);
		channelInsert.executeUpdate();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }
    public void addGuest(Long id,Long guildId,int length){
    	  try {
    		  guestInsert.setLong(1, id);
    		  guestInsert.setLong(2,guildId);
    		  guestInsert.setInt(3, length);
    		  guestInsert.executeUpdate();
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    }}
    public void addVerify(Long id,Long verificationCode, Long battleNetID, Long bungieId){
    	  try {
    		  verifyInsert.setLong(1, id);
    		  verifyInsert.setLong(2, verificationCode);
    		  verifyInsert.setLong(3, battleNetID);
    		  verifyInsert.setLong(4, bungieId);
    		  verifyInsert.executeUpdate();
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    }}
    public void addModList(String user, Long id, String action){
	   try {
		   modListInsert.setString(1, user);
		   modListInsert.setLong(2, id);
		   modListInsert.setString(3, action);
		   modListInsert.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
   }}
    public Connection getConn(){
	   return conn;
   }
    public void shutdown(){
    	//Just kill me now please
        try {
        	channelInsert.close();
			guestInsert.close();
			 verifyInsert.close();
		        modListInsert.close();
		        schedulerInsert.close();
		        channelSelect.close();
			    guestSelect.close();
			    verifySelect.close();
			    modListSelect.close();
			    addWhitelist.close();
			    removeWhitelist.close();
		        channelSelectAll.close();
		        channelDelete.close();
		        getWhitelist.close();
		        guestDelete.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
       
	}
/*	private void createTableIfNeeded(String creation) throws SQLException {
	      try {
	         Statement s = conn.createStatement();
	         s.execute(creation);
	      }  catch (SQLException sqle) {
	         String theError = (sqle).getSQLState();
	         if (theError.equals("X0Y32"))   // Table exists
	         {  return;
	    
	          } else { 
	             System.out.println("createTable: Unhandled SQLException" );
	             throw sqle; 
	          }
	      }
	    
	   } */
}
