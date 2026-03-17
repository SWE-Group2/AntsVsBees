package core;

/**
 * A class representing a basic Ant
 * 
 * @author YOUR NAME HERE
 */
public abstract class Ant extends Insect
{
	protected int foodCost; //the amount of food needed to make this ant
	
	/**
	 * Creates a new Ant, with a food cost of 0.
	 * @param armor The armor of the ant.
	 */
	public Ant(int armor)
	{
		super(armor, null);
		this.foodCost = 0;
	}

	/**
	 * Returns the ant's food cost
	 * @return the ant's good cost
	 */
	public int getFoodCost()
	{
		return foodCost;
	}
	
	/**
	 * Removes the ant from its current place
	 */
	public void leavePlace()
	{
		this.place.removeInsect(this);
	}	
}
