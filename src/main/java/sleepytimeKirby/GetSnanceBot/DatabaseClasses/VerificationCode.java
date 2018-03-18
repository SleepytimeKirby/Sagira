package sleepytimeKirby.GetSnanceBot.DatabaseClasses;

public class VerificationCode {

	long code;
	long battleId;
	long bungieId;
	public long getBungieId() {
		return bungieId;
	}
	public void setBungieId(long bungieId) {
		this.bungieId = bungieId;
	}
	public long getCode() {
		return code;
	}
	public void setCode(long code) {
		this.code = code;
	}
	public long getBattleId() {
		return battleId;
	}
	public void setBattleId(long battleId) {
		this.battleId = battleId;
	}
	public VerificationCode(long code, long battleId,long bungieId) {
		this.code = code;
		this.battleId = battleId;
		this.bungieId = bungieId;
	}
	public VerificationCode() {
	}
	
}
