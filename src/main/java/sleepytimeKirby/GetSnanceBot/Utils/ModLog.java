package sleepytimeKirby.GetSnanceBot.Utils;

import java.awt.Color;
import java.time.LocalDateTime;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;

public class ModLog {

	@Deprecated
	 public Message sendModMessage(String author, String authorId,String user,String userId,String action,String description,String uuid,JDA api,Color color) {
		Settings s = Settings.getInstance();
		String guildId = s.get("GUILD_ID");
		String modChannelId = s.get("MOD_LOG_ID");
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(color);
		embed.setAuthor(author+" (" + authorId + ")");
		embed.setTitle(action);
		embed.setDescription(description);
		embed.addField("User", user, true);
		embed.addField("User Id",userId,true);
		embed.setFooter(uuid,"https://loremflickr.com/320/240/dog");//Random image for footer, is required.
		embed.setTimestamp(LocalDateTime.now());
		return api.getGuildById(guildId).getTextChannelById(modChannelId).sendMessage(embed.build()).complete();
		
	}
}
