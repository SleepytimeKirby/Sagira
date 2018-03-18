package sleepytimeKirby.GetSnanceBot.DatabaseClasses;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Guests {

	Long userId;
	Long guildId;
	LocalDateTime joinDate;
	int accessDuration;
	
	public Guests(){
		
	}
	public Guests(Long userId,Long guildId, Timestamp joinDate,int accessDuration){
		this.userId = userId;
		this.guildId = guildId;
		this.joinDate = joinDate.toLocalDateTime();
		this.accessDuration = accessDuration;
	}
	public Long getGuildId() {
		return guildId;
	}
	public void setGuildId(Long guildId) {
		this.guildId = guildId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public LocalDateTime getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(Timestamp joinDate) {
		this.joinDate = joinDate.toLocalDateTime();
	}
	public int getAccessDuration() {
		return accessDuration;
	}
	public void setAccessDuration(int accessDuration) {
		this.accessDuration = accessDuration;
	}
}
