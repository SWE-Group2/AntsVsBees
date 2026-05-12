package core;

import ants.QueenAnt;
import exceptions.InsufficientFoodException;
import exceptions.InvalidPlacementException;
import java.util.ArrayList;
import places.WaterPlace;

/**
 * An entire colony of ants and their tunnels. Uses custom exceptions for better error handling.
 *
 * @author Joel
 * @version Fall 2014
 */
public class AntColony {
    public static final String QUEEN_NAME = "AntQueen";
    public static final int MAX_TUNNEL_LENGTH = 8;

    private int food;
    private Place queenPlace;
    private ArrayList<Place> places;
    private ArrayList<Place> beeEntrances;

    /**
     * Creates a new ant colony with the given layout.
     * 
     * @param numTunnels
     *            The number of tunnels (paths)
     * @param tunnelLength
     *            The length of each tunnel
     * @param moatFrequency
     *            The frequency of water areas. 0 means no water
     * @param startingFood
     *            The starting food for this colony
     */
    public AntColony(int numTunnels, int tunnelLength, int moatFrequency, int startingFood) {
        this.food = startingFood;
        places = new ArrayList<Place>();
        beeEntrances = new ArrayList<Place>();
        queenPlace = new Place(QUEEN_NAME);
        int remainingMoatPlaces = moatFrequency;

        tunnelLength = Math.min(tunnelLength, MAX_TUNNEL_LENGTH);
        Place curr, prev;
        for (int tunnel = 0; tunnel < numTunnels; tunnel++) {
            int moatIndex = -1;
            if (remainingMoatPlaces > 1) {
                moatIndex = (int) (Math.random() * (tunnelLength - 1 - 0 + 1)) + 0;
            }
            curr = queenPlace;
            for (int step = 0; step < tunnelLength; step++) {
                prev = curr;
                if (step == moatIndex) {
                    curr = new WaterPlace("tunnel[" + tunnel + "-" + step + "]", prev);
                } else {
                    curr = new Place("tunnel[" + tunnel + "-" + step + "]", prev);
                }
                prev.setEntrance(curr);
                places.add(curr);
            }
            beeEntrances.add(curr);
        }
    }

    /**
     * Returns an array of Places in this colony.
     * 
     * @return The tunnels in this colony
     */
    public Place[] getPlaces() {
        return places.toArray(new Place[0]);
    }

    /**
     * Returns an array of places that the bees can enter into the colony.
     * 
     * @return Places the bees can enter
     */
    public Place[] getBeeEntrances() {
        return beeEntrances.toArray(new Place[0]);
    }

    /**
     * Returns the place where the real QueenAnt is located.
     * 
     * @return The place of the real queen, or null if not placed yet
     */
    public Place getRealQueenColonyPlace() {
        for (Place p : places) {
            Ant mainAnt = p.getMainAnt();
            if (mainAnt instanceof QueenAnt && ((QueenAnt) mainAnt).isRealQueen()) {
                return p;
            }
        }
        return null;
    }

    /**
     * Returns the queen's location.
     * 
     * @return The queen's location
     */
    public Place getQueenPlace() {
        return queenPlace;
    }

    /**
     * Returns the amount of available food.
     * 
     * @return the amount of available food
     */
    public int getFood() {
        return food;
    }

    /**
     * Increases the amount of available food. Pre-condition: amount must be greater than 0
     * 
     * @param amount
     *            The amount to increase by
     */
    public void increaseFood(int amount) {
        assert amount > 0 : "Food increase amount must be greater than 0";
        food += amount;
    }

    /**
     * Returns if there are any bees in the queen's location.
     * 
     * @return if there are any bees in the queen's location
     */
    public boolean queenHasBees() {
        Place queenColonyPlace = getRealQueenColonyPlace();
        if (queenColonyPlace != null) {
            return queenColonyPlace.getBees().length > 0 || queenPlace.getBees().length > 0;
        }
        return this.queenPlace.getBees().length > 0;
    }

    /**
     * Places the given ant in the given tunnel IF there is enough available food. Throws custom exceptions instead of
     * printing error messages.
     *
     * @param place
     *            Where to place the ant
     * @param ant
     *            The ant to place
     * @throws InsufficientFoodException
     *             if not enough food
     * @throws InvalidPlacementException
     *             if ant cannot be placed here
     */
    public void deployAnt(Place place, Ant ant) {
        if (ant instanceof QueenAnt && getRealQueenColonyPlace() == null) {
            ((QueenAnt) ant).setRealQueen(true);
        }

        // check if placement is valid (e.g. watersafe check)
        if (!place.canAddInsect(ant)) {
            throw new InvalidPlacementException(ant.getClass().getSimpleName() + " cannot be placed in "
                    + place.getName() + " — invalid placement.");
        }

        // check if enough food
        if (this.food < ant.getFoodCost()) {
            throw new InsufficientFoodException(ant.getFoodCost(), this.food);
        }

        this.food -= ant.getFoodCost();
        place.addInsect(ant);
    }

    /**
     * Removes the ant inhabiting the given Place.
     * 
     * @param place
     *            Where to remove the ant from
     */
    public void removeAnt(Place place) {
        if (place.getAnt() != null) {
            if (place.getAnt() instanceof QueenAnt && ((QueenAnt) place.getAnt()).isRealQueen()) {
                System.out.println("Cannot remove the real queen!");
                return;
            }
            place.removeInsect(place.getAnt());
        }
    }

    /**
     * Returns a list of all the ants currently in the colony.
     * 
     * @return a list of all the ants currently in the colony
     */
    public ArrayList<Ant> getAllAnts() {
        ArrayList<Ant> ants = new ArrayList<Ant>();
        for (Place p : places) {
            if (p.getAnt() != null)
                ants.add(p.getAnt());
        }
        return ants;
    }

    /**
     * Returns a list of all the bees currently in the colony.
     * 
     * @return a list of all the bees currently in the colony
     */
    public ArrayList<Bee> getAllBees() {
        ArrayList<Bee> bees = new ArrayList<Bee>();
        for (Place p : places) {
            for (Bee b : p.getBees())
                bees.add(b);
        }
        return bees;
    }

    public String toString() {
        return "Food: " + this.food + "; " + getAllBees() + "; " + getAllAnts();
    }
}