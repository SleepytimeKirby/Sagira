package sleepytimeKirby.GetSnanceBot.DatabaseClasses;

public class Group {
private String uuid;
private int slots;
private long key;
private long leaderId;
private long channelId;
private long roleId;
private long postId;
private int slotsTaken;
private int type;

public Group(String uuid,int type, int slots, long key, long leaderId, long channelId, long roleId, long postId,
		int slotsTaken) {
	this.uuid = uuid;
	this.type = type;
	this.slots = slots;
	this.key = key;
	this.leaderId = leaderId;
	this.channelId = channelId;
	this.roleId = roleId;
	this.postId = postId;
	this.slotsTaken = slotsTaken;
}
public Group() {
	// TODO Auto-generated constructor stub
}
public String getUuid() {
	return uuid;
}
public void setUuid(String uuid) {
	this.uuid = uuid;
}
public int getSlots() {
	return slots;
}
public void setSlots(int slots) {
	this.slots = slots;
}
public long getKey() {
	return key;
}
public void setKey(long key) {
	this.key = key;
}
public long getLeaderId() {
	return leaderId;
}
public void setLeaderId(long leaderId) {
	this.leaderId = leaderId;
}
public long getChannelId() {
	return channelId;
}
public void setChannelId(long channelId) {
	this.channelId = channelId;
}
public long getRoleId() {
	return roleId;
}
public void setRoleId(long roleId) {
	this.roleId = roleId;
}
public long getPostId() {
	return postId;
}
public void setPostId(long postId) {
	this.postId = postId;
}
public int getSlotsTaken() {
	return slotsTaken;
}
public void setSlotsTaken(int slotsTaken) {
	this.slotsTaken = slotsTaken;
}
public void setType(int type) {
	this.type = type;	
}
public int getType() {
	return type;
}
}
