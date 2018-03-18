package sleepytimeKirby.GetSnanceBot.DatabaseClasses;

import java.util.Date;

public class ModActionList {

	int modID;
	Date entryDate;
	String user;
	long userId;
	String action;
	public ModActionList(){
	
	}
	public ModActionList(int modID, Date entryDate, String user, long userId, String action) {
		this.modID = modID;
		this.entryDate = entryDate;
		this.user = user;
		this.userId = userId;
		this.action = action;
	}
	public int getModID() {
		return modID;
	}
	public void setModID(int modID) {
		this.modID = modID;
	}
	public Date getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
}
