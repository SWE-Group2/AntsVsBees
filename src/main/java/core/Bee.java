package core;

/**
 * Represents a Bee
 * 
 * @author YOUR NAME HERE
 */
public class Bee extends Insect {
	private static final int DAMAGE = 1;
	private int slowedTurns = 0; // number of turns the bee is slowed for
	private int stunnedTurns = 0; // number of turns the bee is stunned for

	/**
	 * Creates a new bee with the given armor
	 * 
	 * @param armor The bee's armor
	 */
	public Bee(int armor) {
		super(armor);
		this.watersafe = true;
	}

	/**
	 * Deals damage to the given ant
	 * 
	 * @param ant The ant to sting
	 */
	public void sting(Ant ant) {
		ant.reduceArmor(DAMAGE);
	}

	/**
	 * Moves to the given place
	 * 
	 * @param place The place to move to
	 */
	public void moveTo(Place place) {
		this.place.removeInsect(this);
		place.addInsect(this);
	}

	public void leavePlace() {
		this.place.removeInsect(this);
	}

	/**
	 * Returns true if the bee cannot advance (because an ant is in the way)
	 * 
	 * @return if the bee can advance
	 */
	public boolean isBlocked() {
		return this.place.getAnt() != null;
	}

	/**
	 * A bee's action is to sting the Ant that blocks its exit if it is blocked,
	 * otherwise it moves to the exit of its current place.
	 */
	public void action(AntColony colony) {
		if (this.isStunned()) {
			System.out.println(this + " is stunned and cannot act this turn.");
			this.reduceEffects();
			return;
		}
		if (this.isSlowed()) {
			System.out.println(this + " is slowed and acts only every other turn.");
			if (this.slowedTurns % 2 == 0) {
				this.reduceEffects();
				return; // skip this turn
			}
		}
		if (this.isBlocked())
			sting(this.place.getAnt());
		else if (this.armor > 0)
			this.moveTo(this.place.getExit());

		this.reduceEffects();
	}

	/**
	 * Slows the bee for a certain number of turns
	 * 
	 * @param turns The number of turns to slow the bee for
	 */
	public void slow(int turns) {
		this.slowedTurns = turns;
	}

	/**
	 * Stuns the bee for a certain number of turns
	 * 
	 * @param turns The number of turns to stun the bee for
	 */
	public void stun(int turns) {
		this.stunnedTurns = turns;

	}

	/**
	 * Returns if the bee is currently stunned
	 * 
	 * @return if the bee is currently stunned
	 */
	public boolean isStunned() {
		return this.stunnedTurns > 0;
	}

	/**
	 * Returns if the bee is currently slowed
	 * 
	 * @return if the bee is currently slowed
	 */
	public boolean isSlowed() {
		return this.slowedTurns > 0;
	}

	/**
	 * Reduces the number of turns the bee is stunned and slowed for by 1
	 */
	public void reduceEffects() {
		if (this.slowedTurns > 0)
			this.slowedTurns -= 1;
		if (this.stunnedTurns > 0)
			this.stunnedTurns -= 1;
	}

}
