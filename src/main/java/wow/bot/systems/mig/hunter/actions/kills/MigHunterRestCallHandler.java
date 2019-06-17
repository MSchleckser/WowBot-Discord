package wow.bot.systems.mig.hunter.actions.kills;

import kong.unirest.Unirest;

public class MigHunterRestCallHandler {

	public int getNumberofKills(String user){
		return Integer.parseInt(Unirest.get("http://localhost:8080/kills/count")
				.queryString("user", user)
				.asString().getBody());
	}
}
