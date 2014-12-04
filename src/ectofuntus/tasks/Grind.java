package ectofuntus.tasks;

import ectofuntus.*;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 25/11/14
 * Time: 4:41 PM
 * Purpose: Make Bonemeal.
 */
public class Grind extends Task<ClientContext> {

    public Grind(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        boolean atGrinder = Areas.GRINDER.contains(ctx.players.local().tile());
        boolean hasPots = ctx.itemInInventory(Ids.POT);
        boolean hasBones = ctx.itemInInventory(Ids.BONES);

        return (atGrinder && hasBones && hasPots);
    }

    @Override
    public int execute() {
        // Antiban reaction buffer
        ctx.sleep(500);
        System.out.println("Grind");

        int numBonesStart = ctx.inventory.select().id(Ids.BONES).size();
        int numBonemealsStart = ctx.inventory.select().id(Ids.BONEMEAL).size();
        int maxRepeat;

        // set camera
        ctx.camera.pitch(true);
        ctx.camera.angle('n');
        ctx.sleep(500);

        // load
        System.out.println(" - Load");
        maxRepeat = 5;
        do {
            ctx.inventory.select().id(Ids.BONES).poll().interact(true, Actions.USE);
            ctx.objects.select().id(Ids.GRINDER_LOADER).poll().interact(true, Actions.USE);
            ctx.sleep(4200);
            if (maxRepeat < 0) {
                return -1;
            }
            maxRepeat--;
        } while (ctx.inventory.select().id(Ids.BONES).size() == numBonesStart);

        // wind
        System.out.println(" - Wind");
        ctx.objects.select().id(Ids.GRINDER_GRINDER).poll().interact(true, Actions.WIND);
        ctx.sleep(4000);

        // empty
        System.out.println(" - Empty");
        maxRepeat = 10;
        do {
            ctx.objects.select().id(Ids.GRINDER_BIN).poll().interact(true, Actions.EMPTY);
            ctx.sleep(1000);
            if (maxRepeat < 0) {
                return -1;
                // note: if failed to collect bones from grinder, infinite loop.
                //       however, if buried bones, this is recoverable on next task loop.
            }
            maxRepeat--;
        } while (ctx.inventory.select().id(Ids.BONEMEAL).size() == numBonemealsStart);

        System.out.println("Done.");
        return 0;
    }
}
