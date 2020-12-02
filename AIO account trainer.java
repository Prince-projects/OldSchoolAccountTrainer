
import org.dreambot.api.input.event.impl.InteractionEvent;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Map;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.Category;
import org.dreambot.api.wrappers.interactive.Entity;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.interactive.interact.Interactable;


@ScriptManifest(author = "Prince", name = "Account Trainer", version = 1.0, description = "AIO Account trainer", category = Category.MISC)
public class AIOaccounttrainer extends AbstractScript {
    public boolean taskType = true; // Intial task swap handler
    public boolean breakReq = true; // Var to swap task
    double prayerLevel = Skills.getRealLevel(Skill.PRAYER); // Math requirements, Declaring them seems to reduce instability with DB's inbuilt API as sometimes it returns wrong nums.
    int hpLevel = Skills.getRealLevel(Skill.HITPOINTS);
    int strLevel = Skills.getRealLevel(Skill.STRENGTH);
    double defLevel = Skills.getRealLevel(Skill.DEFENCE);
    int attLevel = Skills.getRealLevel(Skill.ATTACK);
    public double combatWeight = prayerLevel / 2 + (hpLevel + defLevel) / 4 + (strLevel + attLevel * 0.325) / 2; // Calculating the rough combat level of the player.
    int bonesBuriedTotal = 0; // Tracker variables for onPaint
    int fishcaughtTotal = 0;
    String currentTask = "";

    public void onStart() { // Run on start
    }

    public void onExit() {
        log("Thank you for running!");
    } //Exit code


    void combatTask() { // Fix farmer door to add cooking first 15 lvls.
        currentTask = "Combat";
        if (!taskType) { // Intial message. Alleged to reduce banrate.
            Keyboard.type("I'm in the mood for some killin'");
            taskType = true;
        }
        Tile chickenGateTile = new Tile(3236, 3295, 0); // Defining tiles and NPCs for later use.
        Tile chickenTile = new Tile(3231, 3295, 0);
        NPC chickenNPC = NPCs.closest("Chicken");
        Tile frogTile = new Tile(3200, 3180, 0);
        NPC frogNPC = NPCs.closest("Big Frog");
        Area chickenArea = new Area(3237, 3286, 3224, 3303);
        GameObject chickenGate = GameObjects.closest(1560);

        if (combatWeight < 40) {
            log("Looped");
            while (!chickenArea.contains(Players.localPlayer())) { // ensuring player is in the right area.
                log("Walking...");
                Walking.walkExact(chickenTile);
                sleep(1000);
            }

            if (chickenGate.exists() && chickenGateTile.distance() < 5 && chickenArea.contains(chickenGate)) {
                chickenGate.interact();
                sleep(5000);
                log("Gate Opened.");
            }
            if (!chickenNPC.isInCombat()) {
                sleep(Calculations.random(800, 900));// Attack block
                chickenNPC.interact("Attack");
                log("Attacked.");
                sleep(2500, 3500);
            }

            while (Players.localPlayer().isInCombat()) { // Method to ensure player doesn't cancel actions.
                log("In combat.");
                sleep(Calculations.random(200, 400));
            }

            while (Inventory.count("Bones") > Calculations.random(0, 4) && !Players.localPlayer().isInCombat()) { // Bone bury method.
                log("Burying..");
                Inventory.interact("Bones", "Bury");

                sleep(Calculations.random(1200, 1400));
                bonesBuriedTotal++;
            }


            while (Inventory.count(1944) > 0 || Inventory.count(2138) > 0) { // Drop any misclick pickups.
                log("Dropping Misclicks...");
                Inventory.dropAll(2140, 2144, 2138, 1944, 1351);
            }

            if (GroundItems.closest(314, 526).exists() && !Players.localPlayer().isInCombat() && Inventory.getEmptySlots() > 1) { // Checks to see if feather/bones exist, then picks. The exist checks are to prevent errors.
                sleep(Calculations.random(1800, 1900));
                if (chickenGate.exists() && chickenGateTile.distance() < 5 && chickenArea.contains(chickenGate)) {
                    chickenGate.interact();
                    sleep(5000);
                    log("Gate Opened.");
                }
                log("Collected feather.");
                if (GroundItems.closest(314).exists()) {
                    GroundItems.closest(314).interact("Take");
                }
                sleep(1500);
                if (GroundItems.closest(526).exists()) {
                    GroundItems.closest(526).interact("Take");
                }
            }

        }
        if (combatWeight > 40) {
            while (!Map.isTileOnMap(frogTile)) { // Same as above.
                Walking.walk(3200, 3180, 0);
            }
            log(Players.localPlayer().getHealthPercent());
            while (Players.localPlayer().getHealthPercent() < 40 && !Players.localPlayer().isInCombat()) {
                sleep(Calculations.random(800, 1200));
            }
            while (Inventory.count("Bones") > Calculations.random(0, 4) && !Players.localPlayer().isInCombat()) { // Bone bury method.
                log("Burying..");
                Inventory.interact("Bones", "Bury");
                sleep(Calculations.random(1200, 1400));
                bonesBuriedTotal++;
            }

            if (GroundItems.closest(526).exists() && !Players.localPlayer().isInCombat()) {
                GroundItems.closest(526).interact("Take");
            }
        }

            sleep(Calculations.random(1500, 2500)); // Delays to allow for game processing time.
            frogNPC.interact("Attack");

            while (Players.localPlayer().isInCombat()) {
                sleep(Calculations.random(1000, 2000));
            }
            sleep(1600);


        }//Done (Add cooking)

    void magicTask() {
        if (Inventory.count("Arrows") < 10) {
            breakReq = true;
        }
        Tile goblinMage = new Tile(3251, 3226, 0);
        NPC goblinNPC = NPCs.closest("Goblin");
        while (!Map.isTileOnMap(goblinMage)) { // Same as above.
            Walking.walkExact(goblinMage);
        }
        if (goblinNPC.exists() && !goblinNPC.isInCombat()) {
            goblinNPC.interact("Attack");
            while (Players.localPlayer().isInCombat()) {
                sleep(Calculations.random(1000, 2000));
            }
        }
    }

    void rangeTask() {
        if (Inventory.count("Arrows") < 10) {
            breakReq = true;
        }
        Tile goblinRange = new Tile(3251, 3226, 0);
        NPC goblinNPC = NPCs.closest("Goblin");
        while (!Map.isTileOnMap(goblinRange)) { // Same as above.
            Walking.walkExact(goblinRange);
        }
        if (goblinNPC.exists() && !goblinNPC.isInCombat()) {
            goblinNPC.interact("Attack");
            while (Players.localPlayer().isInCombat()) {
                sleep(Calculations.random(1000, 2000));
            }
        }
    }

    void smithTask() { //TODO

    }

    void craftTask() { // TODO

    }

    void questTask() { // TODO

    }

    void woodcutTask() {
        currentTask = "Woodcutting";
        if (!taskType) {
            Keyboard.type("Man, that was a long one. Let's relax with some lumberjacking.");
            taskType = true;
        }
        Tile wood = new Tile(3142, 3256, 0); // Area and object def
        Tile willow = new Tile(3142, 3256, 0);

        Tile fireput2 = new Tile(3152, 3245, 0);
        GameObject fire = GameObjects.closest(26185);
        int wcLvlCase = Skills.getRealLevel(Skill.WOODCUTTING); // Assigning skill to var to ensure adequate num gathering.

        if (wcLvlCase < 30) {
            while (Players.localPlayer().distance(wood) > 10) { // Ensures player is in correct location.
                log(Players.localPlayer().distance(wood));
                Walking.walk(wood);
            }
            if (Inventory.getEmptySlots() > 1) {
                GameObjects.closest(1278, 1276).interact(); // Interacts with the nearest tree.
                sleep(2000);
            }
            while (Players.localPlayer().isAnimating()) { // Waits until the animation for woodcutting is completed.
                sleep(1800, 1900);
            }

            while (Inventory.count("Logs") > 0) { // Lights the logs until there is none left.
                Tile fireput1 = new Tile(Calculations.random(3143, 3146), Calculations.random(3253, 3256), 0);
                if (Players.localPlayer().getSurroundingArea(5).contains(GameObjects.closest("Fire"))) {
                    sleep(3000);
                    Walking.walk(fireput1);
                }
                sleep(2000);
                Inventory.get("Tinderbox").useOn("Logs");
                sleep(3000);
                while (Players.localPlayer().isAnimating()) { // Waits until the animation for woodcutting is completed.
                    sleep(1800, 1900);
                }

            }
        }

        if (wcLvlCase > 30) {
            while (Players.localPlayer().distance(wood) > 10) { // Same as above.
                Walking.walk(wood);
                sleep(3000);
            }
            if (Players.localPlayer().distance(fire) < 1) {
                sleep(3000);
                Walking.walk(wood);
            }
            sleep(2000);
            GameObjects.closest(10820).interact(); //Interacts with Oak tree.

            while (Players.localPlayer().isAnimating()) {
                sleep(2800, 2900);
            }

            while (GroundItems.closest("Oak Logs").exists()) {
                GroundItems.closest("Oak Logs").interact("Light");
                log("Lit log");
            }

            while (Inventory.count("Oak Logs") > 0 && Skills.getRealLevel(Skill.FIREMAKING) < 15) { // Same as above.
                sleep(1000);
                Inventory.get("Tinderbox").useOn("Oak Logs");
            }

            while (Inventory.count("Oak Logs") > 0 && Skills.getRealLevel(Skill.FIREMAKING) > 15) { // If unable to light logs due to low fm level, drop instead.
                sleep(1000);
                Inventory.dropAll("Oak logs");
            }
        }

        if (wcLvlCase > 60) { // Same as above.
            while (Players.localPlayer().distance(willow) > 10) {
                Walking.walkExact(willow);
                sleep(3000);
            }
            if (Players.localPlayer().distance(fire) < 1) {
                sleep(3000);
                Walking.walk(willow);
            }
            sleep(2000);
            GameObjects.closest(10833, 10819).interact();
            while (Players.localPlayer().isAnimating()) {
                sleep(1800, 1900);
            }
            while (Inventory.count("Willow Logs") > 0 && Skills.getRealLevel(Skill.FIREMAKING) < 30) {
                sleep(1000);
                Inventory.get("Tinderbox").useOn("Willow Logs");
            }
            while (Inventory.count("Willow Logs") > 0 && Skills.getRealLevel(Skill.FIREMAKING) > 30) {
                sleep(1000);
                Inventory.dropAll("Willow logs");
            }
        }
    } // Done

    void miningTask() {
        currentTask = "Mining";
        if (!taskType) {
            Keyboard.type("Time for some digging!");
            taskType = true;
        }
        String currentPickaxe = ""; // Decs.
        int miningLvlCase = Skills.getRealLevel(Skill.MINING);

        if (miningLvlCase < 30) {
            Tile copperMine = new Tile(3227, 3148, 0); // Decs.
            while (!Map.isTileOnMap(copperMine)) { // Move to mine location.
                log(Map.exactDistance(copperMine));
                Walking.walk(3227, 3147, 0);
            }

            GameObject copperOre = GameObjects.closest(11161, 10943); // Interact with closest copper ore.
            copperOre.interact("Mine");
            log("Mining");

            while (Players.localPlayer().isAnimating()) { // Prevents task cancel early.
                sleep(Calculations.random(1200, 3050));
            }

            while (Inventory.count("Copper Ore") > Calculations.random(0, 7)) { // Drops copper ore.
                log("Dropping");
                sleep(Calculations.random(1000, 2300));
                Inventory.dropAll("Copper ore");
                Inventory.dropAll("Uncut");
            }
        }

        if (miningLvlCase >= 30) { // Same as above, replace copper with iron.
            Tile ironMine = new Tile(3402, 3169, 0);
            if (!Map.isTileOnMap(ironMine)) {
                log(Map.exactDistance(ironMine));
                Walking.walkExact(ironMine);
            }
            GameObject ironOre = GameObjects.closest(11365, 11364);
            ironOre.interact("Mine");
            while (Players.localPlayer().isAnimating()) {
                sleep(Calculations.random(1200, 3050));
            }
            while (Inventory.count("Iron Ore") > Calculations.random(0, 7)) {
                log("Dropping");
                sleep(Calculations.random(1000, 2300));
                Inventory.dropAll("Iron ore");
            }
        }
    } // Done

    void fishTask() {
        if (combatWeight < 30) { // Due to aggressive NPCs, if player is not high level enough go and level them up.
            combatTask();
        }
        currentTask = "Fishing";
        if (!taskType) {
            Keyboard.type("Time for some fishing, hopefully i'll catch a big one!");
        }

        int fishingLvlCase = Skills.getRealLevel(Skill.FISHING);// Another dec due to API issues.

        if (fishingLvlCase < 20) {
            Tile fishSpot = new Tile(2992, 3171, 0);// Spots decs
            Tile fishSpot1 = new Tile(2988, 3177, 0);
            Tile fishSpot2 = new Tile(2997, 3159, 0);
            NPC fishingNPC = NPCs.closest("Fishing Spot");// The fishing spot is considered an NPC. Cool shortcut jagex.

            if (!Map.isTileOnMap(fishSpot)) { // Ensure at fishing spot.
                Walking.walkExact(fishSpot);
            }

            fishingNPC.interact("Small Net"); //Interact block
            sleep(1400, 1900);

            while (Players.localPlayer().isAnimating()) { // Ensure task is not cancelled early.
                sleep(Calculations.random(1800, 2200));
            }
            while (Inventory.count(317) > Calculations.random(0, 2) || Inventory.count(321) > Calculations.random(0, 2)) { // If either possible fishing catches are full, drop.
                sleep(Calculations.random(400, 600));
                Inventory.dropAll(317);
                Inventory.dropAll(321);
            }

            //Search function in the event the fishing NPC is not on screen.
            if (!fishingNPC.exists()) {
                Walking.walk(fishSpot1);
            }
            if (!fishingNPC.exists()) {
                Walking.walk(fishSpot2);
            }
        }

        if (fishingLvlCase >= 20) { // Same as above
            if (Inventory.count(314) < 1000) {
                combatTask();
            }
            Tile flyfishTile = new Tile(3241, 3254, 0);
            NPC rodfishingNPC = NPCs.closest("Rod Fishing Spot");
            if (!Map.isTileOnMap(flyfishTile)) {
                Walking.walk(flyfishTile);
            }

            rodfishingNPC.interact("Lure");
            sleep(1400, 1900);
            while (Players.localPlayer().isAnimating()) {
                sleep(Calculations.random(1800, 2200));
            }
            if (Skills.getRealLevel(Skill.COOKING) < 15) {
                while (Inventory.count(335) > Calculations.random(0, 2) || Inventory.count(331) > Calculations.random(0, 2)) {
                    sleep(Calculations.random(400, 600));
                    Inventory.dropAll(335);
                    Inventory.dropAll(331);
                }
            }
            if (Skills.getRealLevel(Skill.COOKING) >= 15) {
                GameObject cookTree = GameObjects.closest("Tree");
                GameObject cookFire = GameObjects.closest("Fire");
                Tile cookTile = new Tile(3188, 3286, 0);
                Inventory.deselect();
                cookTree.interact();
                sleep(3000);
                while (Players.localPlayer().isAnimating()) {
                    sleep(500);
                }
                sleep(1200);
                Inventory.get("Tinderbox").useOn("Logs");
                sleep(3000);
                while (Players.localPlayer().isAnimating()) {
                    sleep(500);
                }
                sleep(3000);
                Inventory.get("Raw Trout").useOn(cookFire);
                sleep(3000);
                while (Players.localPlayer().isAnimating()) {
                    sleep(500);
                }
                sleep(5000);
                Widgets.getWidgetChild(270, 14).interact();
                sleep(5000);
                while (Players.localPlayer().isAnimating() || Inventory.count("Raw Trout") > 0 && cookFire.exists()) {
                    sleep(500);
                }
            }
        }
    } // Done

    /*public void onPaint (Graphics g) { Waiting for the rest of the tasks to be done.
        int copperMinedTotal = 10 + Calculations.random(1, 2);
    g.setColor(Color.blue);
    g.setFont(new Font("Arial", Font.PLAIN, 48));
    g.drawString("Bones Buried " + bonesBuriedTotal, 37, 240);
    }*/
    public int onLoop() {
// Drink at pub for breaks. Swap randomly between tasks. USE F A C T O R Y  P A T T E R N its best prac.

        combatTask();
        return Calculations.random(500, 600);
    }
}
