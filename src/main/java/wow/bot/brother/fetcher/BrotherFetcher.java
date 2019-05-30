package wow.bot.brother.fetcher;

import kong.unirest.Unirest;

public class BrotherFetcher {

	public String fetchBrother(){
		try {
			return Unirest.get("http://localhost:8080/brother")
					.asString().getBody();
		} catch (Exception e) {
			return "Your brotherhood is lacking";
		}
	}

}
