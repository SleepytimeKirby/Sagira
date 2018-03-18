package sleepytimeKirby.GetSnanceBot.User;

import java.util.HashMap;

public class UserController {
	private static UserController uc = null;
	private HashMap<Long,UserHeat> userHeats;
	private UserController(){};
	
	public static UserController getInstance(){
		if(uc == null) {
			uc = new UserController();
		}
		return uc;
	}

	public HashMap<Long,UserHeat> getUserHeats() {
		return userHeats;
	}
	
	public void setUserHeats(HashMap<Long,UserHeat> userHeats) {
		this.userHeats = userHeats;
	}
	public boolean userHeatExist(long userId){
		return userHeats.containsKey(userId);
	}
	public UserHeat getUserHeat(long userId){
		return userHeats.get(userId);
		
	}
	public void setUserHeat(long userId, UserHeat userHeat){
		userHeats.put(userId, userHeat);
	}

}
