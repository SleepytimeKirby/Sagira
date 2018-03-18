package sleepytimeKirby.GetSnanceBot.Utils;


import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import sleepytimeKirby.GetSnanceBot.Utils.Builders.EmbedBuildingClass;
import sleepytimeKirby.GetSnanceBot.Utils.Builders.EmbedFields;

public class CentralMessenger {
//One class for non command handlers to output messages (Such as notification, music, ect.) Also will provide api access to classes that may need it and have no other way(such as scheduler)
	
	private static CentralMessenger cm = null;
	private static MessageBuilder mb = new MessageBuilder();
	private static EmbedBuilder eb = new EmbedBuilder();
	private JDA api;
	
	private CentralMessenger() {};
	public static CentralMessenger getInstance(){
		if(cm == null) {
			cm = new CentralMessenger();
		}
		return cm;
	}
	public void init(JDA api) {
		this.api = api;
	}
	public JDA getApi() {
		return api;
	}
	public void clearEmbedBuilder() {
		eb.clearFields()
        .setTitle(null)
        .setDescription(null)
        .setTimestamp(null)
        .setColor(null)
        .setThumbnail(null)
        .setAuthor(null, null, null)
        .setFooter(null, null)
        .setImage(null);
	}
	public void createEmbed(EmbedBuildingClass ebc) {
		if(ebc.getAuthorUrl() != null) {
			eb.setAuthor(ebc.getAuthor(), ebc.getAuthorUrl());
		} else {
			eb.setAuthor(ebc.getAuthor());
		}
		eb.setTitle(ebc.getTitle());
		eb.setDescription(ebc.getDescription());
		eb.setColor(ebc.getColor());
		for(EmbedFields field:ebc.getFields()) {
			eb.addField(field.getName(), field.getValue(), field.isInline());
		}
		eb.setFooter(ebc.getFooterText(), ebc.getFooterUrl());
		eb.setTimestamp(ebc.getTimestamp());
	}
	public void clearMessageBuilder() {
		mb.clear();
	}
	public Message sendMessage(TextChannel channel, Message message) {
		return channel.sendMessage(message).complete();
	}
	public Message sendMessage(TextChannel channel, String message)
    {	
        return channel.sendMessage(message).complete();
    }
	public Message sendMessage(TextChannel channel, MessageEmbed message)
    {	
        return channel.sendMessage(message).complete();
    }
	public Message editMessage(Message messageToEdit,Message editedMessage) {
		return messageToEdit.editMessage(editedMessage).complete();
	}
	public Message editMessage(Message messageToEdit,String editedMessage) {
		return messageToEdit.editMessage(editedMessage).complete();
	}
	public Message editMessage(Message messageToEdit,MessageEmbed editedMessage) {
		return messageToEdit.editMessage(editedMessage).complete();
	}
	public Message sendPrivateMessage(User target,Message message) {
		return target.openPrivateChannel().complete().sendMessage(message).complete();
	}
	public Message sendPrivateMessage(User target,String message) {
		return target.openPrivateChannel().complete().sendMessage(message).complete();
	}
	public Message sendPrivateMessage(User target,MessageEmbed message) {
		return target.openPrivateChannel().complete().sendMessage(message).complete();
	}
}
