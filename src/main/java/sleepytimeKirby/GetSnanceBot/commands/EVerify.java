package sleepytimeKirby.GetSnanceBot.commands;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import sleepytimeKirby.GetSnanceBot.Messages;
import sleepytimeKirby.GetSnanceBot.DatabaseClasses.VerificationCode;
import sleepytimeKirby.GetSnanceBot.Utils.DatabaseManager;
import sleepytimeKirby.GetSnanceBot.Utils.Settings;

public class EVerify extends Command{

	DatabaseManager dm = DatabaseManager.getInstance();
	String memberID = Settings.getInstance().get("MEMBER_ID");
	String apiKey = Messages.getString("EVerify.APIKey"); //$NON-NLS-1$
	String bungieGuildId = Messages.getString("EVerify.BungieGuildId"); //$NON-NLS-1$
	HttpClient httpClient = HttpClientBuilder.create().build();
	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		HttpURLConnection connection = null;
		/*
		//On Verify with profile, access battle.net api. Pull list of members. Search for profile name. Retrive profile name and id
		//generate random number, and give number to user. User puts number in profile about.
		//On next verify, check database for the info, query api for user data, check if numbers match.
		//https://www.bungie.net/platform/GroupV2/<clanID>/Members/?currentPage=1
		//https://www.bungie.net/platform/User/GetBungieNetUserById/<memberID>
		//random number should be in the about section.
		//Yes the / slash is required at the end and before the ?
		//Otherwise you will get a redirect error.
		//Also yes, https://www.bungie.net is required, again redirection that bugs out. 
		//Also api key gets lost during the redirection so even without it bugging out, it would still not work correctly. That's not my fault, thats an open bug on the api.
		*/
		if(args.length > 1){ // checking for the inclusion of tag (1 would be just the .verify)
			try {

				HttpGet httpget = new HttpGet("https://www.bungie.net/platform/GroupV2/" + bungieGuildId + "/Members/?currentPage=1"); // luckly each page holds 100 entries, and destiny 2 guilds can only have 100 members. Not sure why its required, but it is
				httpget.addHeader("X-API-Key", apiKey);
				HttpResponse response = httpClient.execute(httpget);
				HttpEntity entity = response.getEntity();
				String jsonResponse = EntityUtils.toString(entity,"UTF-8");
				JSONTokener tokener = new JSONTokener(jsonResponse);
				JSONObject root = new JSONObject(tokener);
				JSONArray members = root.getJSONObject("Response").getJSONArray("results");
				for(int i = 0; i < members.length(); i++){
					JSONObject member = members.getJSONObject(i);
					JSONObject info = member.getJSONObject("bungieNetUserInfo");
					String name = info.getString("displayName");
					Long battleNetId = member.getJSONObject("destinyUserInfo").getLong("membershipId");//This id corrisponds to the battle.net account, we need it for certain calls to the api such as get profile.
					Long bungieId = info.getLong("membershipId");
					if(name.equals(args[1])){
						Random rn = new Random();
						Long verificationCode = rn.nextLong();
						dm.addVerify(e.getAuthor().getIdLong(), verificationCode, battleNetId,bungieId);
						sendMessage(e,new MessageBuilder().append("Verification now pending. "
								+ "Please go to https://www.bungie.net/en/Profile/Settings and change your Motto "
								+ "to " + verificationCode + " . "
								+ "Next go to https://www.bungie.net/en/Profile/Settings?category=Accounts "
								+ "and make sure that display on public profile is check. "
								+ "Last wait for the changes to take affect"
								+ "(ussualy 30 seconds, but can be as much as 10 minutes) "
								+ "then type in .verify without any extra modifiers.").build());
						return;	
					}
				}
				sendMessage(e,new MessageBuilder().append("Could not find user. Please contact an admin or mod for aditional help.").build());

			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}catch (Exception e2){
				e2.printStackTrace();
			} finally {
				if (connection != null) {
					connection.disconnect();
				}
			}
		} else {
			try {

				VerificationCode vc = dm.getVerify(e.getAuthor().getIdLong());
				HttpGet httpget = new HttpGet("https://www.bungie.net/platform/User/GetBungieNetUserById/" + vc.getBattleId() + "/"); 
				httpget.addHeader("X-API-Key", apiKey);
				HttpResponse response = httpClient.execute(httpget);
				HttpEntity entity = response.getEntity();
				String jsonResponse = EntityUtils.toString(entity,"UTF-8");
				JSONTokener tokener = new JSONTokener(jsonResponse);
				JSONObject root = new JSONObject(tokener);
				Long userCode = root.getJSONObject("Response").getLong("about");
				if(userCode == vc.getCode()){
					e.getGuild().getController().addRolesToMember(e.getMember(),e.getGuild().getRoleById(memberID)).complete();
					sendMessage(e,new MessageBuilder().append("Verification Successful. Welcome Aboard! :D").build());

				}else {
					sendMessage(e,new MessageBuilder().append("Code mismatch or not found. Please try again or contact an admin or mod for aditional help.").build());
				}

			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}finally {
				if (connection != null) {
					connection.disconnect();
				}
			}
		}

	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList(".verify");
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Verify that you're a member of the guild";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Verify";
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return "Usage: .verify <battle.net profile name>"
		+ "First time use must include your battle.net profile name. \n"
		+ "Once you finish the instructions, you can just use .verify without any extras \n"
		+ "to complete verification.";
	}

}
