package wow.bot.actions;

import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wow.bot.models.quote.Quote;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class QuoteGetter {

    private final Logger logger = LoggerFactory.getLogger(QuoteGetter.class);
    private static final String QUOTE_RETRIEVAL_FAILED = "Unable to retrieve any quotes for %s. Sorry :(";
    private static final String QUOTE_FORMAT = "%s said \"%s\" on %s at %s CST";
    private static final String REST_ERROR = "Error encountered during REST service call.";
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM dd");
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm");

    public  String getQuote(MessageReceivedEvent message){
        User user = retrieveUser(message.getMessage());
        Quote quote = retrieveQuoteForUser(user);

        String formattedQuote = String.format(QUOTE_RETRIEVAL_FAILED, user.getAsMention());
        if(quote != null) {
            formattedQuote = String.format(QUOTE_FORMAT,
                    quote.getUser(),
                    quote.getText(),
                    dateFormatter.format(quote.getTimeStamp()),
                    timeFormatter.format(quote.getTimeStamp()));
        }

        return formattedQuote;
    }

    public  String getRandomQuote(MessageReceivedEvent message) {
        User user = retrieveUser(message.getMessage());
        Quote quote = retrieveRandomQuoteForUser(user);

        String formattedQuote = String.format(QUOTE_RETRIEVAL_FAILED, user.getAsMention());
        if(quote != null) {
            formattedQuote = String.format(QUOTE_FORMAT,
                    quote.getUser(),
                    quote.getText(),
                    dateFormatter.format(quote.getTimeStamp()),
                    timeFormatter.format(quote.getTimeStamp()));
        }

        return formattedQuote;
    }

    private Quote retrieveRandomQuoteForUser(User user) {
        Quote quote = null;

        try {
            String jsonReturn = Unirest.get("http://localhost:8080/quote/random")
                    .queryString("user", user.getAsMention())
                    .asJson().getBody().toString();

            quote = convertJsonToQuote(jsonReturn);
        } catch (UnirestException e) {
            logger.error(REST_ERROR, e);
        }

        return quote;
    }

    private Quote retrieveQuoteForUser(User user) {
        Quote quote = null;

        try {
            String jsonReturn = Unirest.get("http://localhost:8080/quote")
                    .queryString("user", user.getAsMention())
                    .asJson().getBody().toString();

            quote = convertJsonToQuote(jsonReturn);
        } catch (UnirestException e) {
            logger.error(REST_ERROR, e);
        }

        return quote;
    }

    private User retrieveUser(Message message){
        return message.getMentionedMembers().get(0).getUser();
    }

    private Quote convertJsonToQuote(String json){
        Quote quote = null;

        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(json);
            String text = (String)jsonObject.get("quote");

            if(text == null)
                return null;

            String user = (String)jsonObject.get("user");
            LocalDateTime timestamp = LocalDateTime.parse((String) jsonObject.get("timestamp"));

            quote = new Quote(user, text, timestamp);
        } catch (ParseException e) {
            logger.error(REST_ERROR, e);
        }

        return quote;
    }
}
