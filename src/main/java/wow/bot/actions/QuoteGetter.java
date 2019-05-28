package wow.bot.actions;

import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import wow.bot.models.quote.Quote;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class QuoteGetter {

    private static String QUOTE_RETIREVAL_FAILED = "Unable to retrieve any quotes for %s. Sorry :(";
    private static String QUOTE_FORMAT = "%s said \"%s\" on %s at %s CST";
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM dd");
    private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm");

    public static String getQuote(MessageReceivedEvent message){
        User user = retrieveUser(message.getMessage());
        Quote quote = retrieveQuoteForUser(user);

        String formattedQuote = String.format(QUOTE_RETIREVAL_FAILED, user.getAsMention());
        if(quote != null) {
            formattedQuote = String.format(QUOTE_FORMAT,
                    quote.getUser(),
                    quote.getText(),
                    dateFormatter.format(quote.getTimeStamp()),
                    timeFormatter.format(quote.getTimeStamp()));
        }

        return formattedQuote;
    }

    public static String getRandomQuote(MessageReceivedEvent message) {
        User user = retrieveUser(message.getMessage());
        Quote quote = retrieveRandomQuoteForUser(user);

        String formattedQuote = String.format(QUOTE_RETIREVAL_FAILED, user.getAsMention());
        if(quote != null) {
            formattedQuote = String.format(QUOTE_FORMAT,
                    quote.getUser(),
                    quote.getText(),
                    dateFormatter.format(quote.getTimeStamp()),
                    timeFormatter.format(quote.getTimeStamp()));
        }

        return formattedQuote;
    }

    private static Quote retrieveRandomQuoteForUser(User user) {
        Quote quote = null;

        try {
            String jsonReturn = Unirest.get("http://localhost:8080/quote/random")
                    .queryString("user", user.getAsMention())
                    .asJson().getBody().toString();

            System.out.println(jsonReturn);
            quote = convertJsonToQuote(jsonReturn);
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return quote;
    }

    private static Quote retrieveQuoteForUser(User user) {
        Quote quote = null;

        try {
            String jsonReturn = Unirest.get("http://localhost:8080/quote")
                    .queryString("user", user.getAsMention())
                    .asJson().getBody().toString();

            quote = convertJsonToQuote(jsonReturn);
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return quote;
    }

    private static User retrieveUser(Message message){
        return message.getMentionedMembers().get(0).getUser();
    }

    private static Quote convertJsonToQuote(String json){
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
            e.printStackTrace();
        }

        return quote;
    }
}
