package sleepytimeKirby.GetSnanceBot.Utils;

import java.awt.Color;
import java.time.LocalDateTime;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.MessageEmbed;

public class GroupRequestEmbed {

	public MessageEmbed postGroupRequest(String requestor,String group, String notes,JDA api,Color color) {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(color);
		embed.setAuthor(requestor);
		embed.setTitle(group);
		embed.setDescription("Group Request");
		embed.addField("Additional Information",notes,false);
		embed.setFooter("Automated Group Request","https://loremflickr.com/320/240/dog");//Random image for footer, is required.
		embed.setTimestamp(LocalDateTime.now());
		return embed.build();
		
	}
}

