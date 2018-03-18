package sleepytimeKirby.GetSnanceBot.User;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

//
public class UserHeat {

	private long userId;
	private double currentHeat;
	private int warnCount;
	private LocalDateTime startHeat; // No need to store in database, will write heat if not existant, check to see if 10 secs has past since start, if so set to new time and set posts to 0
	private HashMap<String,Integer> heatMap = new HashMap<String,Integer>();
	private String lastMessage; 
	
	public void setZero() {
		heatMap.put("text",0);
		heatMap.put("image",0);
		heatMap.put("whitelist",0);
		heatMap.put("badlink",0);
		heatMap.put("mention",0);
		heatMap.put("repeat",0);
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public double getCurrentHeat() {
		return currentHeat;
	}
	public void setCurrentHeat(double d) {
		this.currentHeat = d;
	}
	public int getWarnCount() {
		return warnCount;
	}
	public void setWarnCount(int warnCount) {
		this.warnCount = warnCount;
	}
	
	public UserHeat() {
	}
	public UserHeat(long userId, double d, int warnCount) {
		this.userId = userId;
		this.currentHeat = d;
		this.warnCount = warnCount;
	}
	public LocalDateTime getStartHeat() {
		return startHeat;
	}
	public void setStartHeat(LocalDateTime localDateTime) {
		this.startHeat = localDateTime;
	}
	public Map<String,Integer> getHeatMap() {
		return heatMap;
	}
	public void addToHeatMap(String type, Integer num){
		heatMap.replace(type, num);
	}
	public int getFromHeatMap(String type){
		return heatMap.get(type);
	}
	public String getLastMessage() {
		return lastMessage;
	}
	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}
}
