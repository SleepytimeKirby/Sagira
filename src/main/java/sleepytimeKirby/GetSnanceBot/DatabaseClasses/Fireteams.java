package sleepytimeKirby.GetSnanceBot.DatabaseClasses;

public class Fireteams {

	long channelId;
	long guildId;
	String channelName;
	public Fireteams() {
	}
	public Fireteams(long channelId, long guildId) {
		this.channelId = channelId;
		this.guildId = guildId;
	}
	public Fireteams(long channelId, long guildId, String channelName) {
		this.channelId = channelId;
		this.guildId = guildId;
		this.channelName = channelName;
	}
	public long getChannelId() {
		return channelId;
	}
	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}
	public long getGuildId() {
		return guildId;
	}
	public void setGuildId(long guildId) {
		this.guildId = guildId;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
}
