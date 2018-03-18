package sleepytimeKirby.GetSnanceBot.Utils.Music;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;
import sleepytimeKirby.GetSnanceBot.Utils.CentralMessenger;
import sleepytimeKirby.GetSnanceBot.Utils.Settings;

public class Music {
	private static Music m = null;
	private static Settings settings = Settings.getInstance();
	private static CentralMessenger cm = CentralMessenger.getInstance();
	private static VoiceChannel vc;
	private Music() {};
	private static AudioPlayerManager playerManager;
	private static AudioPlayer musicPlayer;
	private static TrackScheduler scheduler;
	private static TextChannel tc;
	private static Guild guild;
	private static final int PLAYLIST_LIMIT = 10;
	private static final int PLAYLIST_OVERRIDE_LIMIT = 25;
	public static Music getInstance() {
		if(m == null) {
			m = new Music();
		}
		return m;
	}
	public void init() {
		JDA api = cm.getApi();
		guild = api.getGuildById(settings.get("GUILD_ID"));
		vc = guild.getVoiceChannelById(settings.get("MUSIC_VOICE_ID"));
		playerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(playerManager);
		musicPlayer = playerManager.createPlayer();
		tc = guild.getTextChannelById(settings.get("MUSIC_TEXT_ID"));
		scheduler = new TrackScheduler(musicPlayer,tc);
		musicPlayer.addListener(scheduler);
		guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(musicPlayer));
	
	}
	private static void connectToVoice( AudioManager audioManager) {
		if(!audioManager.isConnected() && !audioManager.isAttemptingToConnect()) {
			audioManager.openAudioConnection(vc);
		}
	}
	//commands
	public void now(String url, boolean override) {
		addTrack(url,true,override);
	}
	 public void add(String url, boolean override) {
		 addTrack(url,false,override);
	 }
	 public void clear() {
		scheduler.clear(); 
	 }
	 public void volume(int volume) {
		 musicPlayer.setVolume(volume);
	 }
	 public void skip() {
		    scheduler.skip();
	}
	public void voteSkip(String id) {
		scheduler.voteSkip(vc.getMembers().size()-1,id);// Minus bot from channel members
	}
	public void shuffle() {
		scheduler.shuffle();
	}
	public void addTrack(final String trackURL, final boolean now,final boolean override) {
		
		playerManager.loadItemOrdered(this, trackURL, new AudioLoadResultHandler() {
		      @Override
		      public void trackLoaded(AudioTrack track) {
		    	  connectToVoice(guild.getAudioManager());
		    	  cm.sendMessage(tc,"Adding to queue " + track.getInfo().title + " (length " + track.getDuration() + ")");
		    	  if (now) {
		    		  scheduler.playNow(track, false);
		    	  } else {
		    		  scheduler.addToQueue(track);
		    	  }
		      }

		      @Override
		      public void playlistLoaded(AudioPlaylist playlist) {
		    	connectToVoice(guild.getAudioManager());
		    	List<AudioTrack> tracks = playlist.getTracks();
		    	cm.sendMessage(tc,"Loaded playlist: " + playlist.getName() + " (" + tracks.size() + ")");
		        AudioTrack track = playlist.getSelectedTrack();
		        if (track == null) {
		        	track = playlist.getTracks().get(0);
		        }
		        if (now) {
		    		  scheduler.playNow(track, false);
		    	  } else {
		    		  scheduler.addToQueue(track);
		    	  }
		        int limit;
		        if(override) {
		        	limit = PLAYLIST_OVERRIDE_LIMIT;
		        } else {
		        	limit = PLAYLIST_LIMIT;
		        }
		        for (int i = 0; i < Math.min(limit, playlist.getTracks().size()); i++) {
		            if (tracks.get(i) != track) {
		              scheduler.addToQueue(tracks.get(i));
		            }
		          }
		      }

		      @Override
		      public void noMatches() {
		    	  cm.sendMessage(tc,"Nothing found by " + trackURL);
		      }

		      @Override
		      public void loadFailed(FriendlyException exception) {
		    	  cm.sendMessage(tc,"Could not play: " + exception.getMessage());
		      }
		    });
		  }

		 
}
