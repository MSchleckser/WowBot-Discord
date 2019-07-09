package wow.bot.systems.user.authentication.enums;

public enum Role {
	USER(0), ADMIN(10);

	private final int privilegeLevel;

	Role(int privilegeLevel) {
		this.privilegeLevel = privilegeLevel;
	}

	public int getPrivilegeLevel() {
		return privilegeLevel;
	}
}
