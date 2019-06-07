package wow.bot.models.quote;

import java.time.LocalDateTime;

public final class Quote {

    private final String user;
    private final String text;
    private final LocalDateTime timeStamp;

    public Quote(String user, String text, LocalDateTime timeStamp) {
        this.user = user;
        this.text = text;
        this.timeStamp = timeStamp;
    }

    public String getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }
}
