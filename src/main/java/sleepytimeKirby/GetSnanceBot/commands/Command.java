package sleepytimeKirby.GetSnanceBot.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public abstract class Command extends ListenerAdapter{

		private static final String BOT_LOGGIN_CHANNEL_ID = "378285744340598785";
		private static final Pattern p = Pattern.compile("([^\"]\\S*|\".+?\")\\s*"); // Used to split our inputs vs String.split, since we want to allow for spaces in some args. Args with spaces must be wrapped in double quotes.
		public abstract void onCommand(MessageReceivedEvent e, String[] args);
		public abstract List<String> getAliases();
	    public abstract String getDescription();
	    public abstract String getName();
	    public abstract String getUsage();
	    
		@Override
		public void onMessageReceived(MessageReceivedEvent e){
			if(e.getAuthor().isBot()){
				return;
			}
			if(isCommand(e.getMessage())){
				onCommand(e, commandArgs(e.getMessage()));
			}
		}
		 	protected boolean isCommand(Message message)
		    {
		        return getAliases().contains(commandArgs(message)[0]);
		    }

		    protected String[] commandArgs(Message message)
		    {
		        return commandArgs(message.getContent());
		    }

		    protected String[] commandArgs(String string)
		    {
		    	ArrayList<String> args = new ArrayList<String>();
		    	Matcher m = p.matcher(string);
		    	while(m.find()) {
		    		args.add(m.group(1).replace("\"",""));//replace wont fail if can't find, and we dont want the quotes in the actual arguments.
		    	}
		        return  args.toArray(new String[args.size()]); // Makes it so i dont have to change previous code vs just returning a ArrayList.
		    }
		    protected Message sendMessage(MessageReceivedEvent e, Message message)
		    {
		        if(e.isFromType(ChannelType.PRIVATE))
		            return e.getPrivateChannel().sendMessage(message).complete();
		        else
		            return e.getTextChannel().sendMessage(message).complete();
		    }
		    protected Message sendPrivateMessage(MessageReceivedEvent e, Message message)
		    {
		        if(e.isFromType(ChannelType.PRIVATE))
		            return e.getPrivateChannel().sendMessage(message).complete();
		        else
		        	return e.getAuthor().openPrivateChannel().complete().sendMessage(message).complete();
		    }
		    protected Message sendPrivateMessage(MessageReceivedEvent e, String message)
		    {
		    	
	            return e.getAuthor().openPrivateChannel().complete().sendMessage(message).complete();
		    }
		    protected Message sendMessage(MessageReceivedEvent e, String message)
		    {
		        return sendMessage(e, new MessageBuilder().append(message).build());
		    }
		    protected Message sendModMessage(MessageReceivedEvent e, String message) {
		    	return sendModMessage(e, new MessageBuilder().append(message).build());
		    }
		    protected Message sendModMessage(MessageReceivedEvent e, Message message) {
		    	return e.getGuild().getTextChannelById(BOT_LOGGIN_CHANNEL_ID).sendMessage(message).complete();
		    }
}
