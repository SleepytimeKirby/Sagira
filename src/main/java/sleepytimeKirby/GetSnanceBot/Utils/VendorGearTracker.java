package sleepytimeKirby.GetSnanceBot.Utils;

import java.awt.Color;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.client.SocketIOException;
import io.socket.emitter.Emitter;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import okhttp3.OkHttpClient;

public class VendorGearTracker {
//Because we want more functionality then the one offered at destiny-vendor-gear-tracker.com
// However their actual site is nice and is what we are going be using to get our data through niffty sockets.io
//I got no idea what i'm doing.
	
	Socket socket;
	DatabaseManager dm = DatabaseManager.getInstance();
	JDA api;
	public void initalize(JDA api) {
		try {
			this.api = api;
			 SSLContext sc = SSLContext.getInstance("TLS");
	         sc.init(null, trustAllCerts, new SecureRandom());
	         OkHttpClient okHttpClient = new OkHttpClient.Builder()
	        		 .hostnameVerifier(new RelaxedHostNameVerifier())
	        		 .sslSocketFactory(sc.getSocketFactory(),new X509TrustManager() {
	        		        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	        		            return new java.security.cert.X509Certificate[] {};
	        		        }

	        		        public void checkClientTrusted(X509Certificate[] chain,
	        		                                       String authType) throws CertificateException {
	        		        }

	        		        public void checkServerTrusted(X509Certificate[] chain,
	        		                                       String authType) throws CertificateException {
	        		        }
	        		    })
	        		 .build();
	        IO.Options opts = new IO.Options();
	        opts.secure = true;
	        opts.reconnection = true;
	        opts.callFactory = okHttpClient;
	        opts.webSocketFactory = okHttpClient;
			socket = IO.socket("https://destiny-vendor-gear-tracker.com:4000",opts);
			socket.on("test-channel:VendorUpdated", new Emitter.Listener() {
				  @Override
				  public void call(Object... args) {
				    JSONObject obj = (JSONObject)args[0];
				    //Maybe this will work? i got no idea
				    
				   /* Information
				    * 
				    * Important keys
				    * 
				    * plus:
				    * minus:
				    * 
				    * Those are for when people vote
				    * 
				    * reset:true
				    * 
				    * Sent every 30 mins when vendors reset
				    * 
				    * vendor - JSONobject
				    * 
				    * 	id:
				    * 	name:
				    * 	image:
				    * 	updated_at:
				    * 	zone:
				    *  According to the javascript there is a data.notify somewhere, however as destiny 2 is currently in maintance
				    *  No one is updating shit. In theory both data.notify and data.reset wont be needed, as plus and minus get sent every time.
				    *  We can just see what they are and do what we do.
				    *
				    *
				    */
				    Boolean notify = false;
				    int plus = obj.getInt("plus");
				    int minus = obj.getInt("minus");
				    
				    JSONObject vendorData = obj.getJSONObject("vendor");
				    String name = vendorData.getString("name");
				    String updatedAt = vendorData.getString("updated_at");
				    String imageUrl = vendorData.getString("image");
				    String zone = vendorData.getString("zone");
				    if(obj.has("notify")) { // for some reason json doesn't have the notify after resets. since i dont control the json input, we just set notify to false, and only update if it has the field
				     notify = obj.getBoolean("notify");
				    }
				    vendorUpdate(name,updatedAt,imageUrl,zone,plus,minus,notify);
				  }
				}).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
	                @Override
	                public void call(Object... args) {
	                	System.out.println("eventDisconnect");
	                }
	            }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
	                @Override
	                public void call(Object... args) {
	                    System.out.println("eventConnectError");
	                    for(Object o : args){
	                    	System.out.println("object: " + o.toString());
	                        if(o instanceof SocketIOException)
	                            ((SocketIOException) o).printStackTrace();
	                    }
	                }
	            }).on(Socket.EVENT_CONNECT, new Emitter.Listener() {
	                @Override
	                public void call(Object... args) {
	                	System.out.println("eventConnect");
	                }
	            }).on(Socket.EVENT_ERROR, new Emitter.Listener() {
	                @Override
	                public void call(Object... args) {
	                	System.out.println("eventError");
	                }
	            });
	            socket.connect();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			//We should never hit this, its hard coded value. but just incase something happens.
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Super Super Fun Time ahead.
	//Each vendor gets its own message. WOO!!!!
	//what we will do, in order to make this most dynamicist as possible is call on each fire, check if exists, if does update, else make.
	//Should be able to handle new vendors with ease, as long as the site updates.
	//Edit 12-14-17 : Changes to isMax vs is300 since nolonger is 300 max light. Also with new expansion, bot manage to handle the addition of 
	//new vendors fine.
	public Message vendorUpdate(String vendorName, String updatedAt, String imageUrl, String zone, int plus, int minus,Boolean notify) {
		Settings s = Settings.getInstance();
		String guildId = s.get("GUILD_ID");
		String updateId = s.get("UPDATE_CHAN_ID");
		long messageId = dm.getVendor(vendorName);
		Message message;
		Color color;
		String isMax;
		int netVote = plus - minus;
		if(netVote == 0) {
			color = new Color(0,169,255); // Pretty Blue for informative
			isMax = "No";
		} else if (netVote > 0) {
			color = new Color(97,255,0); // Nice Green for Yes
			isMax = "Yes";
		} else {
			color = new Color(209,30,10); // Intimidating Red for no
			isMax = "No";
		}
		EmbedBuilder embed = new EmbedBuilder();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX");
		embed.setColor(color);
		embed.setAuthor(vendorName, "https://destiny-vendor-gear-tracker.com", "https://destiny-vendor-gear-tracker.com" + imageUrl); // cause we nice we link back to the og site.
		embed.setTitle("Zone");
		embed.setDescription(zone);
		embed.addField("330(Yes)",Integer.toString(plus),true);
		embed.addField("330(No)",Integer.toString(minus),true);
		embed.addField("Most Likely 330",isMax,true);
		embed.setFooter("Data from destiny-vendor-gear-tracker.com", "https://destiny-vendor-gear-tracker.com" + imageUrl);
		embed.setTimestamp(LocalDateTime.parse(updatedAt+"Z",formatter));
		if(messageId == -1) {
			//No vendor exists, lets make a new one
			message = api.getGuildById(guildId).getTextChannelById(updateId).sendMessage(embed.build()).complete();
			dm.insertVendor(vendorName,message.getIdLong());
		} else {
			message = api.getGuildById(guildId).getTextChannelById(updateId).editMessageById(messageId, embed.build()).complete();
			if(message.isPinned()) {
				if(netVote <= 0) {
					message.unpin().complete(); // Remove any pins for messages not 300
				}
			}
		}
		if(netVote > 0) {
			message.pin().complete(); // Add a pin if possilbe 300
		}
		if(notify) {
			//Grab those who signed up for notifications, then send them a pm.
			ArrayList<Long> userToNotify = dm.getUserNoti();
			for(Long userId:userToNotify) {
				api.getUserById(userId).openPrivateChannel().complete().sendMessage(message).complete();
			}
		}
		return message;
	}
	
	  private TrustManager[] trustAllCerts = new TrustManager[] { (TrustManager) new X509TrustManager() {
	        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	            return new java.security.cert.X509Certificate[] {};
	        }

	        public void checkClientTrusted(X509Certificate[] chain,
	                                       String authType) throws CertificateException {
	        }

	        public void checkServerTrusted(X509Certificate[] chain,
	                                       String authType) throws CertificateException {
	        }
	    } };

	    public static class RelaxedHostNameVerifier implements HostnameVerifier {
	        public boolean verify(String hostname, SSLSession session) {
	            return true;
	        }
	    }
}
