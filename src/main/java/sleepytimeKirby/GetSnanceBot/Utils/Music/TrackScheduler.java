package sleepytimeKirby.GetSnanceBot.Utils.Music;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import net.dv8tion.jda.core.entities.TextChannel;
import sleepytimeKirby.GetSnanceBot.Utils.CentralMessenger;

	/*
	 * This class schedules tracks for the audio player. It contains the queue of tracks.
	 */
	public class TrackScheduler extends AudioEventAdapter{
	  private final AudioPlayer player;
	  private final BlockingDeque<AudioTrack> queue;
	  private final TextChannel output;
	  private static CentralMessenger cm = CentralMessenger.getInstance();
	  private List<String> voteSkip = new ArrayList<String>();

	
	  public TrackScheduler(AudioPlayer player,TextChannel output) {
	    this.player = player;
	    this.queue = new LinkedBlockingDeque<>();
	    this.output = output;
	    
	  }
	  public void voteSkip(int currentListeners,String id) {
		  if(!voteSkip.contains(id)) {
			  voteSkip.add(id);
		  }
		  if(voteSkip.size() > (currentListeners/2)) {
			  skip();
		  } else {
			  Integer needed = (currentListeners/2)-voteSkip.size();
			  cm.sendMessage(output, "Vote Skip: " + voteSkip.size() + " have voted to skip. Need "
			  + needed + " more to skip.");
		  }
	  }
	  public void addToQueue(AudioTrack audioTrack) {
	    queue.addLast(audioTrack);
	    startNextTrack(true);
	  }

	  public List<AudioTrack> drainQueue() {
	    List<AudioTrack> drainedQueue = new ArrayList<>();
	    queue.drainTo(drainedQueue);
	    return drainedQueue;
	  }

	  public void playNow(AudioTrack audioTrack, boolean clearQueue) {
	    if (clearQueue) {
	      queue.clear();
	    }

	    queue.addFirst(audioTrack);
	    startNextTrack(false);
	  }
	  public void clear() {
		  queue.clear();
		  startNextTrack(false);
	  }
	  public void skip() {
	    startNextTrack(false);
	    cm.sendMessage(output,"Skipped to next track.");

	  }
	  public void shuffle() {
		  List<AudioTrack> drained = drainQueue();
		  Collections.shuffle(drained);
		  queue.clear();
		  queue.addAll(drained);
	  }
	  private void startNextTrack(boolean noInterrupt) {
		    AudioTrack next = queue.pollFirst();

		    if (next != null) {
		      if (!player.startTrack(next, noInterrupt)) {
		        queue.addFirst(next);
		      } else {
		    	  cm.sendMessage(output,"Now Playing: " + next.getInfo().title);
		      }
		    } else {
		      player.stopTrack();
		      cm.sendMessage(output,"Queue finished.");
		    }
		  }

	  @Override
	  public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
	    // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
	    if (endReason.mayStartNext) {	    	
	         cm.sendMessage(output,String.format("Track %s finished.", track.getInfo().title));
	         voteSkip.clear();
	         startNextTrack(true);	         
	    }
	  }
	}
