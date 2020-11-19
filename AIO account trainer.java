import javafx.animation.Animation;
import javafx.concurrent.ScheduledService;
import org.dreambot.api.input.event.impl.InteractionEvent;
import org.dreambot.api.input.event.impl.mouse.impl.click.ClickEvent;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.combat.Combat;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankMode;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Interactables;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Map;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.walking.TileObstacle;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.walking.pathfinding.impl.obstacle.PathObstacle;
import org.dreambot.api.methods.walking.pathfinding.impl.obstacle.impl.PassableObstacle;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.Category;
import org.dreambot.api.wrappers.interactive.Character;
import org.dreambot.api.wrappers.interactive.Entity;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.interactive.interact.Interactable;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.widgets.WidgetChild;
import org.dreambot.api.wrappers.widgets.chatbox.ChatboxMessage;

import javax.management.timer.Timer;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.TimerTask;



@ScriptManifest(author = "Prince", name = "Ring Script", version = 1.0, description = "Sapphire Ring Bot", category = Category.MONEYMAKING)

public class SapphireRing extends AbstractScript {
    public boolean taskType = true;
    public boolean breakReq = true;
    double prayerLevel = Skills.getRealLevel(Skill.PRAYER);
    int hpLevel = Skills.getRealLevel(Skill.HITPOINTS);
    int strLevel = Skills.getRealLevel(Skill.STRENGTH);
    double defLevel = Skills.getRealLevel(Skill.DEFENCE);
    int attLevel = Skills.getRealLevel(Skill.ATTACK);
    public double combatWeight = prayerLevel / 2 + (hpLevel + defLevel) / 4 + (strLevel + attLevel * 0.325) / 2;
    boolean newTask = true;
    int bonesBuriedTotal = 0;

    public void onStart() {
    }

    public void onExit() {
        log("Thank you for running!");
    }


    void combatTask() {
        if (!taskType) {
                Keyboard.type("I'm in the mood for some killin'");
                taskType = true;
            }
            Tile chickenGateTile = new Tile(3237, 3295, 0);
            Tile chickenTile = new Tile(3231, 3295, 0);
            NPC chickenNPC = NPCs.closest("Chicken");
            Tile frogTile = new Tile(3200, 3180, 0);
            NPC frogNPC = NPCs.closest("Giant Frog");
            Area chickenArea = new Area(3237, 3287, 3225, 3300);
            Entity chickenGateEntity = GameObjects.getTopObjectOnTile(chickenGateTile);
            log(combatWeight);
            if (combatWeight < 40) {
                while (!chickenArea.contains(Players.localPlayer())) {
                    log("Walking...");
                    Walking.walkExact(chickenTile);
                    sleep(1000);
                    }
                while (chickenArea.contains(chickenGateEntity)) {
                    chickenGateEntity.interact();
                }

                sleep(Calculations.random(800, 900));
                chickenNPC.interact("Attack");
                log("Attacked.");
                sleep(2500, 3500);
                while (Players.localPlayer().isInCombat()) {
                    log("In combat.");
                    sleep(Calculations.random(200, 400));
                }
                while (Inventory.count("Bones") > Calculations.random(0, 4) && !Players.localPlayer().isInCombat() ) {
                    log("Burying..");
                    Inventory.interact("Bones", "Bury");
                    sleep(Calculations.random(1200, 1400));
                    bonesBuriedTotal++;
                }

                while (GroundItems.closest(314).exists() && !Players.localPlayer().isInCombat()) {
                    sleep(Calculations.random(1800, 1900));
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
                if (!Map.isTileOnMap(frogTile)) {
                    Walking.walk(3200, 3180, 0);
                }
                sleep(Calculations.random(1500, 2500));
                frogNPC.interact("Attack");
                while (Players.localPlayer().isInCombat()) {
                    sleep(Calculations.random(1000, 2000));
                }
                sleep(Calculations.random(1500, 2500));
                if (GroundItems.closest("Big Bones").exists() && !Players.localPlayer().isInCombat()) {
                    GroundItems.closest("Big Bones").interact("Take");
                }
                sleep(Calculations.random(2500, 3500));
                while (Inventory.count("Big Bones") > Calculations.random(0, 4) && !Players.localPlayer().isInCombat()) {
                    Inventory.interact("Big Bones", "Bury");
                }
            }

        } //Done
    void smithTask() {

    }
    void craftTask() {

    }
    void questTask() {

    }
    void woodcutTask() { // TODO - Write axe selection. Make sure fm level is adequate
        if (!taskType) {
            Keyboard.type("Man, that was a long one. Let's relax with some lumberjacking.");
            taskType = true;
        }
        Tile wood = new Tile(3142, 3256, 0);
        Tile willow = new Tile(3142, 3256, 0);
        Area woodArea = new Area(3155, 3262, 3137, 3246);
        Area willowArea = new Area(3158, 3275, 3179, 3263);
        int wcLvlCase = Skills.getRealLevel(Skill.WOODCUTTING);
        if (wcLvlCase < 15) {
            if (Players.localPlayer().distance(wood) > 50) {
                Walking.walk(wood);
            }
            sleep(2000);
            GameObjects.closest(1278, 1276).interact();
            while (Players.localPlayer().isAnimating()) {
                sleep(800, 900);
            }
            while (Inventory.count("Logs") > 0) {
                sleep(1000);
                Inventory.get("Tinderbox").useOn("Logs");
            }
        }

         if (wcLvlCase < 30) {
             while (Players.localPlayer().distance(wood) > 50) {
                 Walking.walk(wood);
             }
             sleep(2000);
             GameObjects.closest(10820).interact();
             while (Players.localPlayer().isAnimating()) {
                 sleep(800, 900);
             }
             while (Inventory.count("Oak Logs") > 0 && Skills.getRealLevel(Skill.FIREMAKING) < 15) {
                 sleep(1000);
                 Inventory.get("Tinderbox").useOn("Oak Logs");
             }
             while (Inventory.count("Oak Logs") > 0 && Skills.getRealLevel(Skill.FIREMAKING) > 15) {
                 sleep(1000);
                 Inventory.dropAll("Oak log");
             }
         }
         if (wcLvlCase < 60) {
             while (!willowArea.contains(Players.localPlayer())) {
                 Walking.walkExact(willow);
             }
             sleep(2000);
             GameObjects.closest(10833, 10819).interact();
             while (Players.localPlayer().isAnimating()) {
                 sleep(800, 900);
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
    }
    void miningTask() { // TODO - Write pickaxe selection.

        if (!taskType) {
            Keyboard.type("Time for some digging!");
            taskType = true;
        }
        String currentPickaxe = "";
        int miningLvlCase = Skills.getRealLevel(Skill.MINING);
        if (Inventory.count(currentPickaxe) > 1) {
            Bank.open();
            Bank.withdraw(currentPickaxe);
            Bank.close();
        }
        if (miningLvlCase < 40 ) {
            Tile copperMine = new Tile(3227, 3148, 0);
            while (!Map.isTileOnMap(copperMine)) {
                log(Map.exactDistance(copperMine));
                Walking.walk(3227, 3147, 0);
            }
            GameObject copperOre = GameObjects.closest(11161, 10943);
            copperOre.interact("Mine");
            log("Mining");

            while (Players.localPlayer().isAnimating());
            {
                sleep(Calculations.random(1200, 3050));
            }
            while (Inventory.count("Copper Ore") > Calculations.random(0,7)) {
                log("Dropping");
                sleep(Calculations.random(1000, 2300));
                Inventory.dropAll("Copper ore");
            }
        }

        if (miningLvlCase > 40 ) {
            Tile ironMine = new Tile(3227, 3148, 0);
            if (!Map.isTileOnMap(ironMine)) {
                log(Map.exactDistance(ironMine));
                Walking.walk(3227, 3147, 0);
            }
            GameObject ironOre = GameObjects.closest("Iron Rock");
            ironOre.interact("Mine");
            while (Players.localPlayer().isAnimating());
            {
                sleep(Calculations.random(1200, 3050));
            }
            while (Inventory.count("Iron Ore") > Calculations.random(0,7)) {
                log("Dropping");
                sleep(Calculations.random(1000, 2300));
                Inventory.dropAll("Iron ore");
            }
        }
    } // Done
    void fishTask() {
        if (combatWeight < 30) {
            breakReq = true;
        }
        if (!taskType) {
            Keyboard.type("Time for some fishing, hopefully i'll catch a big one!");
            Walking.walk(3276, 3142, 0);
        }
        int fishingLvlCase = Skills.getRealLevel(Skill.FISHING);
        if (fishingLvlCase < 20) {
            Tile fishSpot = new Tile(2992, 3171, 0);
            Tile fishSpot1 = new Tile(2988, 3177, 0);
            Tile fishSpot2 = new Tile(2997, 3159, 0);
            NPC fishingNPC = NPCs.closest("Fishing Spot");
            if (!Map.isTileOnMap(fishSpot)) {
                Walking.walk(fishSpot);
            }
            fishingNPC.interact("Small Net");
            sleep(1400, 1900);
            while (Players.localPlayer().isAnimating()) {
                sleep(Calculations.random(1800, 2200));
            }
            while (Inventory.count(317) > Calculations.random(0, 2) || Inventory.count(321) > Calculations.random(0, 2)) {
                sleep(Calculations.random(400, 600));
                Inventory.dropAll(317);
                Inventory.dropAll(321);
            }
            if (!fishingNPC.exists()) {
                Walking.walk(fishSpot1);
            }
            if (!fishingNPC.exists()) {
                Walking.walk(fishSpot2);
            }
        }

        if (fishingLvlCase >= 20) {
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
            while (Inventory.count(335) > Calculations.random(0, 2) || Inventory.count(331) > Calculations.random(0, 2)) {
                sleep(Calculations.random(400, 600));
                Inventory.dropAll(335);
                Inventory.dropAll(331);
            }

        }

        }// Done
    void cookTask() {
        if (!taskType) {
            Keyboard.type("Time to cook up a tasty feast!");
            taskType = true;
        }
        int cookLvlCase = Skills.getRealLevel(Skill.COOKING);
        if (cookLvlCase < 15) {
            Tile cowTile = new Tile(3208, 3292, 0);
            NPC cowNPC = NPCs.closest("Cow");
            if (!Map.isTileOnMap(cowTile) && Inventory.getEmptySlots() < 2) {
                Walking.walk(cowTile);
            }
            while (Players.localPlayer().isInCombat()) {
                sleep(400, 500);
            }
            while (Inventory.getEmptySlots() < 2) {
                GameObject cookTree = GameObjects.closest("Tree");
                GameObject cookFire = GameObjects.closest("Fire");
                Tile cookTile = new Tile(3188, 3286, 0);
                Inventory.deselect();
                cookTree.interact();
                sleep(3000);
                while (Players.localPlayer().isAnimating()) {
                    sleep(500);
                }
                Walking.walkExact(cookTile);
                sleep(1200);
                Inventory.get("Tinderbox").useOn("Logs");
                sleep(3000);
                while (Players.localPlayer().isAnimating()) {
                    sleep(500);
                }
                sleep(3000);
                Inventory.get("Raw Beef").useOn(cookFire);
                sleep(3000);
                while (Players.localPlayer().isAnimating()) {
                    sleep(500);
                }
                sleep(5000);
                Widgets.getWidgetChild(270, 14).interact();
                sleep(5000);
                while (Players.localPlayer().isAnimating() || Inventory.count("Raw beef") > 0 && cookFire.exists()) {
                    sleep(500);
                }
            }
            sleep(3000);
            cowNPC.interact();
            if (GroundItems.closest(2132).exists() && !Players.localPlayer().isInCombat()) {
                sleep(Calculations.random(1800, 1900));
                log("Collected beef.");
                GroundItems.closest(2132).interact("Take");
                sleep(1500);
            }
            while (Inventory.count(2142) > 0) {
                Inventory.dropAll(2142);
            }
            while (Inventory.count(2146) > 0) {
                Inventory.dropAll(2146);
            }

        }
        }

    /*public void onPaint (Graphics g) {
        int copperMinedTotal = 10 + Calculations.random(1, 2);
    g.setColor(Color.blue);
    g.setFont(new Font("Arial", Font.PLAIN, 48));
    g.drawString("Bones Buried " + bonesBuriedTotal, 37, 240);
    }*/
    public int onLoop() {
// Drink at pub for breaks. Swap randomly between tasks. USE F A C T O R Y  P A T T E R N its best prac.
        cookTask();
        return Calculations.random(500, 600);
    }
}