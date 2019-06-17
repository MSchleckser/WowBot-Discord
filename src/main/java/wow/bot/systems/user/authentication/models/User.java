package wow.bot.systems.user.authentication.models;

public class User {
	private String userMention;

	private String userRole;

	public String getUserMention() {
		return userMention;
	}

	public void setUserMention(String userMention) {
		this.userMention = userMention;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
}
