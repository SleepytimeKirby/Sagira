package sleepytimeKirby.GetSnanceBot.Utils;

import java.awt.Color;
import java.time.LocalDateTime;
import java.util.List;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;

/*
 * 
 *  This will be currently used just for posting raids on the raid text channel. Eventually it will also be used for reminders when implemented.
 *  
 *  Will need to be called on each raid command, in order to update the post.
 * 
 */
public class RaidEmbed {
	public Message sendRaidInfo(String raidCreator, String raidName,String game,String time, String slots,String recurring,List<String> attendants,String uuid ,JDA api) {
		Settings s = Settings.getInstance();
		String guildId = s.get("GUILD_ID");
		String raidChannelId = s.get("RAID_CHAN_ID");
		Color color = new Color(0,160,157);
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(color);
		embed.setAuthor(raidCreator);
		embed.setTitle(raidName);
		embed.setDescription(game);
		embed.addField("Slots",slots,true);
		embed.addField("Time", time, true);
		embed.addField("Recurring", recurring, true);
		String attendantField = "";
		for(String attendant:attendants) {
			attendantField = attendantField + attendant + "\n";
		}
		embed.addField("Attendees",attendantField,false);
		embed.setFooter(uuid,"https://loremflickr.com/320/240/dog");//Random image for footer, is required.
		embed.setTimestamp(LocalDateTime.now());
		return api.getGuildById(guildId).getTextChannelById(raidChannelId).sendMessage(embed.build()).complete();
		
	}
}
