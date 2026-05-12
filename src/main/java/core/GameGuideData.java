package core;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class GameGuideData {
    public static final Map<String, GuideEntry> ANT_GUIDES = createAntGuides();
    public static final Map<String, GuideEntry> BEE_GUIDES = createBeeGuides();

    private GameGuideData() {
    }

    private static Map<String, GuideEntry> createAntGuides() {
        Map<String, GuideEntry> guides = new LinkedHashMap<>();
        guides.put("HarvesterAnt", new GuideEntry("Harvester Ant", "Produces 1 food every turn.",
                "Build early so you can afford stronger defenders later."));
        guides.put("ThrowerAnt", new GuideEntry("Thrower Ant", "Throws leaves at the nearest bee up to 3 places away.",
                "Use as your basic ranged attacker in most tunnels."));
        guides.put("ShortThrowerAnt", new GuideEntry("Short Thrower Ant", "Throws at bees from 0 to 2 places away.",
                "Place near the queen or where bees are already close."));
        guides.put("LongThrowerAnt", new GuideEntry("Long Thrower Ant", "Throws at bees 4 or more places away.",
                "Place near the queen to hit bees while they are still far out."));
        guides.put("FireAnt", new GuideEntry("Fire Ant", "Explodes for 3 damage to all bees in its place when it dies.",
                "Use as an emergency blocker against clustered bees."));
        guides.put("WallAnt", new GuideEntry("Wall Ant", "Has 4 armor and blocks bees without attacking.",
                "Use to stall bees while throwers attack from behind."));
        guides.put("NinjaAnt", new GuideEntry("Ninja Ant", "Does not block bees and attacks bees in its own place.",
                "Use where bees should pass through while taking damage."));
        guides.put("ScubaThrowerAnt",
                new GuideEntry("Scuba Thrower Ant", "A water-safe thrower with normal ranged damage.",
                        "Use in water tunnels or anywhere a durable thrower position is needed."));
        guides.put("HungryAnt", new GuideEntry("Hungry Ant", "Eats one bee instantly, then digests for 3 turns.",
                "Use against tough single bees rather than large swarms."));
        guides.put("BodyguardAnt", new GuideEntry("Bodyguard Ant", "Protects another ant in the same place.",
                "Use to keep valuable attackers or harvesters alive longer."));
        guides.put("QueenAnt", new GuideEntry("Queen Ant", "Water-safe royal thrower that strengthens the colony.",
                "Use carefully as a powerful support attacker."));
        guides.put("SlowThrowerAnt", new GuideEntry("Slow Thrower Ant", "Slows the nearest bee for 3 turns.",
                "Use to buy time for damage dealers in busy tunnels."));
        guides.put("StunThrowerAnt", new GuideEntry("Stun Thrower Ant", "Stuns the nearest bee for 1 turn.",
                "Use to stop dangerous bees at critical moments."));
        return Collections.unmodifiableMap(guides);
    }

    private static Map<String, GuideEntry> createBeeGuides() {
        Map<String, GuideEntry> guides = new LinkedHashMap<>();
        guides.put("Bee", new GuideEntry("Bee", "Moves toward the queen and stings blocking ants for 1 damage.",
                "Standard threat. Block it with ants and defeat it before it reaches the queen."));
        guides.put("ZombieBee",
                new GuideEntry("Zombie Bee",
                        "Attacks ants in its current place and the next 2 places toward the queen.",
                        "High threat. Use walls, control effects, and ranged damage before it reaches key defenders."));
        guides.put("GhostBee",
                new GuideEntry("Ghost Bee", "Ignores several direct ant attacks and still moves toward the queen.",
                        "Special threat. Slow or stun it and rely on defenses that can still affect its movement."));
        return Collections.unmodifiableMap(guides);
    }

    public static class GuideEntry {
        public final String name;
        public final String ability;
        public final String use;

        GuideEntry(String name, String ability, String use) {
            this.name = name;
            this.ability = ability;
            this.use = use;
        }
    }
}
