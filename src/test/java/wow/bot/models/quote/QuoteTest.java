package wow.bot.models.quote;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class QuoteTest {

	private Quote quote;

	@Test
	public void testGetUser(){
		quote = new Quote("user", "text", LocalDateTime.now());
		assertThat(quote.getUser(), is("user"));
	}

}
