package sleepytimeKirby.GetSnanceBot.Utils.Builders;

public class EmbedFields {

	private String name;
	private String value;
	private boolean inline;
	public EmbedFields(String name, String value, boolean inline) {
		this.name = name;
		this.value = value;
		this.inline = inline;
	}
	public String getName() {
		return name;
	}
	public String getValue() {
		return value;
	}
	public boolean isInline() {
		return inline;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public void setInline(boolean inline) {
		this.inline = inline;
	}
	
	
}
