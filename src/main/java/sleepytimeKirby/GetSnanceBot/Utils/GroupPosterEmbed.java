package sleepytimeKirby.GetSnanceBot.Utils;

import java.awt.Color;
import java.time.LocalDateTime;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;

public class GroupPosterEmbed {

	public Message postGroupFinder(String creator,String game,String activity, String slotsTotal, String slotsFilled, String notes, String type, String uuid,JDA api,Color color) {
		Settings s = Settings.getInstance();
		String guildId = s.get("GUILD_ID");
		String lfgChannelId = s.get("GROUP_POST_ID");
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(color);
		embed.setAuthor(creator);
		embed.setTitle(game);
		embed.setDescription(activity);
		embed.addField("Slots", slotsFilled + "/" + slotsTotal, true);
		embed.addField("Type",type,true);
		embed.addField("Additional Information",notes,false);
		embed.setFooter(uuid,"https://loremflickr.com/320/240/dog");//Random image for footer, is required.
		embed.setTimestamp(LocalDateTime.now());
		return api.getGuildById(guildId).getTextChannelById(lfgChannelId).sendMessage(embed.build()).complete();
		
	}
}
