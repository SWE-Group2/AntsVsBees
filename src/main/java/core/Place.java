package core;

import java.util.ArrayList;

/**
 * Represents a location in the game
 * @author Joel
 * @version Fall 2014
 */
public class Place
{
    private String name;
    private Place exit;
    private Place entrance;
    private ArrayList<Bee> bees;
    private Ant ant;

    /**
     * Creates a new place with the given name and exit
     * @param name The place's name
     * @param exit The place's exit
     */
    public Place(String name, Place exit)
    {
        this.name = name;
        this.exit = exit;
        this.entrance = null;
        this.bees = new ArrayList<Bee>();
        this.ant = null;
    }

    /**
     * Creates a new place with the given name
     * @param name The place's name
     */
    public Place(String name)
    {
        this(name, null);
    }

    /**
     * Returns the place's ant.
     * If a BodyguardAnt is here, returns the BodyguardAnt (it takes hits first).
     * @return the place's ant
     */
    public Ant getAnt()
    {
        return ant;
    }

    /**
     * Checks whether an insect can be added to this place.
     * Normal places accept all insects.
     * WaterPlace overrides this to only allow watersafe insects.
     * @param insect The insect to check
     * @return true if the insect can be added
     */
    public boolean canAddInsect(Insect insect)
    {
        // normal places accept all insects
        return true;
    }

    /**
     * Returns an array of the place's bees
     * @return an array of the place's bees
     */
    public Bee[] getBees()
    {
        return bees.toArray(new Bee[0]);
    }

    /**
     * Returns a nearby bee, up to the maxDistance ahead.
     * @param minDistance The minimum distance away
     * @param maxDistance The maximum distance away
     * @return A random nearby bee.
     */
    public Bee getClosestBee(int minDistance, int maxDistance)
    {
        Place p = this;
        for(int dist = 0; p != null && dist <= maxDistance; dist++)
        {
            if(dist >= minDistance && p.bees.size() > 0)
                return p.bees.get((int)(Math.random() * p.bees.size()));
            p = p.entrance;
        }
        return null;
    }

    /**
     * Returns the name of the place
     * @return the name of the place
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the exit of the place
     * @return the exit of the place
     */
    public Place getExit()
    {
        return exit;
    }

    /**
     * Sets the entrance to the place
     * @param entrance The entrance to the place
     */
    public void setEntrance(Place entrance)
    {
        this.entrance = entrance;
    }

    /**
     * Returns the entrance to the place
     * @return the entrance to the place
     */
    public Place getEntrance()
    {
        return this.entrance;
    }

    /**
     * Adds an ant to the place.
     * Special case: if a BodyguardAnt is being added and there is already an ant here,
     * the BodyguardAnt wraps around the existing ant and protects it.
     * Also if there is already a BodyguardAnt here, a new ant can go inside it.
     * @param ant The ant to add to the place.
     */
    public void addInsect(Ant ant)
    {
        if (this.ant == null)
        {
            // no ant here yet - just add normally
            this.ant = ant;
            ant.setPlace(this);
        }
        else if (ant instanceof ants.BodyguardAnt)
        {
            // adding a BodyguardAnt on top of an existing ant
            // BodyguardAnt wraps around and protects the existing ant
            ants.BodyguardAnt bodyguard = (ants.BodyguardAnt) ant;
            bodyguard.setContainedAnt(this.ant); // save the existing ant inside bodyguard
            this.ant = bodyguard; // bodyguard is now the main ant in this place
            bodyguard.setPlace(this);
            System.out.println("BodyguardAnt is now protecting " + bodyguard.getContainedAnt());
        }
        else if (this.ant instanceof ants.BodyguardAnt)
        {
            // there is already a BodyguardAnt here - put the new ant inside it
            ants.BodyguardAnt bodyguard = (ants.BodyguardAnt) this.ant;
            if (bodyguard.getContainedAnt() == null)
            {
                bodyguard.setContainedAnt(ant); // store new ant inside bodyguard
                ant.setPlace(this);
                System.out.println("BodyguardAnt is now protecting " + ant);
            }
            else
            {
                System.out.println("Already an ant inside the BodyguardAnt in " + this);
            }
        }
        else
        {
            // two normal ants trying to share - not allowed
            System.out.println("Already an ant in " + this);
        }
    }

    /**
     * Adds a bee to the place
     * @param bee The bee to add to the place.
     */
    public void addInsect(Bee bee)
    {
        bees.add(bee);
        bee.setPlace(this);
    }

    /**
     * Removes the ant from the place.
     * If the ant being removed is a BodyguardAnt, the ant it was
     * protecting takes its place.
     * @param ant The ant to remove from the place
     */
    public void removeInsect(Ant ant)
    {
        if (this.ant == ant)
        {
            // removing the main ant
            if (this.ant instanceof ants.BodyguardAnt)
            {
                // if a BodyguardAnt dies, the ant it was protecting takes its place
                ants.BodyguardAnt bodyguard = (ants.BodyguardAnt) this.ant;
                Ant protected_ant = bodyguard.getContainedAnt();
                this.ant = protected_ant; // protected ant is now exposed
                if (protected_ant != null)
                {
                    protected_ant.setPlace(this);
                    System.out.println("BodyguardAnt died! " + protected_ant + " is now exposed.");
                }
                ant.setPlace(null);
            }
            else
            {
                // normal ant removal
                this.ant = null;
                ant.setPlace(null);
            }
        }
        else
        {
            System.out.println(ant + " is not in " + this);
        }
    }

    /**
     * Removes the bee from the place.
     * @param bee The bee to remove from the place.
     */
    public void removeInsect(Bee bee)
    {
        if(bees.contains(bee))
        {
            bees.remove(bee);
            bee.setPlace(null);
        }
        else
            System.out.println(bee + " is not in " + this);
    }

    public String toString()
    {
        return name;
    }
}