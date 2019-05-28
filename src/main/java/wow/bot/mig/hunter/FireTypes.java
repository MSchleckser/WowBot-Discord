package wow.bot.mig.hunter;

public enum FireTypes {
	FOX(50, "Splash! Bogey going down. (Mig killed after %.0f seconds, %s has %s kills)", "Missile defeated try again.",
			10, "Standy for lock. (%.0f seconds left on cooldown)"),
	GUNS(20, "He's smoking! Bandit in the water. (Mig killed after %.0f seconds, %s has %s kills)", "That was close, go back around for another run!.",
			3, "Pull harder! You aren't on him yet! (%.0f seconds left on cooldown)");

	private float hitChance;
	private String hitMessage;
	private String missMessage;
	private float coolDown;
	private String coolDownMessage;

	FireTypes(float hitChance, String hitMessage, String missMessage, float coolDown, String coolDownMessage) {
		this.hitChance = hitChance;
		this.hitMessage = hitMessage;
		this.missMessage = missMessage;
		this.coolDown = coolDown;
		this.coolDownMessage = coolDownMessage;
	}

	public float getHitChance() {
		return hitChance;
	}

	public String getHitMessage() {
		return hitMessage;
	}

	public String getMissMessage() {
		return missMessage;
	}

	public float getCoolDown() {
		return coolDown;
	}

	public String getCoolDownMessage() {
		return coolDownMessage;
	}
}
