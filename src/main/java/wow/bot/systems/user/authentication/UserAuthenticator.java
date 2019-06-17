package wow.bot.systems.user.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wow.bot.systems.user.authentication.models.User;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class UserAuthenticator {

	private Logger logger = LoggerFactory.getLogger(UserAuthenticator.class);
	private String getUserRolePath = "http://localhost:8080/user/find/role/mention/";
	private String setUserRolePath = "http://localhost:8080/user/insert";
	private ObjectMapper mapper = new ObjectMapper();

	public String getUserRole(String userMention){
		try {
			userMention = URLEncoder.encode(userMention, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			logger.error("Unexpected Error while encoding userMention.", e);
		}

		return Unirest.get(getUserRolePath+userMention).asString().getBody();
	}

	public boolean setUserRole(String userMention, String role){
		role = role.toUpperCase();

		User user = new User();
		user.setUserMention(userMention);
		user.setUserRole(role);


		try {
			String object = mapper.writeValueAsString(user);
			String response = Unirest.post(setUserRolePath)
					.header("Content-Type", "application/json")
					.body(object).asString().getBody();
			return response.equals("Success");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return false;
	}

}
