package sleepytimeKirby.GetSnanceBot.Utils.Builders;

import java.awt.Color;
import java.time.LocalDateTime;
import java.util.List;

public class EmbedBuildingClass {

	private Color color;
	private String author;
	private String authorUrl; //Optional
	private String title;
	private String description;
	private List<EmbedFields> fields; //Each entrie in the first list represents a row of fields (inline = true). Each entry in the next list represents the fields in the row, and each hashmap is title - text for that field
	private String footerText;
	private String footerUrl;
	private LocalDateTime timestamp;
	public EmbedBuildingClass() {
		
	}
	public EmbedBuildingClass(Color color, String author, String title, String description,
			List<EmbedFields> fields, String footerText, String footerUrl, LocalDateTime timestamp) {
		this.color = color;
		this.author = author;
		this.title = title;
		this.description = description;
		this.fields = fields;
		this.footerText = footerText;
		this.footerUrl = footerUrl;
		this.timestamp = timestamp;
	}
	public EmbedBuildingClass(Color color, String author, String authorUrl, String title, String description,
			List<EmbedFields> fields, String footerText, String footerUrl, LocalDateTime timestamp) {
		this.color = color;
		this.author = author;
		this.authorUrl = authorUrl;
		this.title = title;
		this.description = description;
		this.fields = fields;
		this.footerText = footerText;
		this.footerUrl = footerUrl;
		this.timestamp = timestamp;
	}
	public Color getColor() {
		return color;
	}
	public String getAuthor() {
		return author;
	}
	public String getAuthorUrl() {
		return authorUrl;
	}
	public String getTitle() {
		return title;
	}
	public String getDescription() {
		return description;
	}
	public List<EmbedFields> getFields() {
		return fields;
	}
	public String getFooterText() {
		return footerText;
	}
	public String getFooterUrl() {
		return footerUrl;
	}
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public void setAuthorUrl(String authorUrl) {
		this.authorUrl = authorUrl;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setFields(List<EmbedFields> fields) {
		this.fields = fields;
	}
	public void setFooterText(String footerText) {
		this.footerText = footerText;
	}
	public void setFooterUrl(String footerUrl) {
		this.footerUrl = footerUrl;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	
	
}
