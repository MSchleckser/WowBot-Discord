package wow.bot.actions;

import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wow.bot.models.quote.Quote;

public class QuoteGrabber {

    private final Logger logger = LoggerFactory.getLogger(QuoteGrabber.class);
    private final String RESPONSE_ERROR = "Unable to grab %s's getQuote. Sorry :(";

    public String getQuote(MessageReceivedEvent messageReceivedEvent){
        Quote quote = convertEventToQuote(messageReceivedEvent);
        return storeQuote(quote);
    }

    private Quote convertEventToQuote(MessageReceivedEvent event){
        User user = event.getMessage().getMentionedMembers().get(0).getUser();
        Message message = getUsersLastMessage(event.getChannel(), user);
        return new Quote(user.getAsMention(), message.getContentDisplay(), message.getCreationTime().toLocalDateTime());
    }

    private Message getUsersLastMessage(MessageChannel channel, User user) {
        return channel.getHistory().retrievePast(100).complete()
                .stream().filter(m -> m.getAuthor().equals(user))
                .findFirst().get();
    }

    private String storeQuote(Quote quote){
        String response = null;
        try {
            response = Unirest.post("http://localhost:8080/quote")
                    .queryString("user", quote.getUser())
                    .queryString("quote", quote.getText())
                    .queryString("timestamp", quote.getTimeStamp())
                    .asString().getBody();
        } catch (UnirestException e) {
            logger.error("Exception when attempting to contact server.", e);
        }

        return response == null || response.length() > 200 ? String.format(RESPONSE_ERROR, quote.getUser()) : response;
    }

}
