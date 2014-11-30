package ectofuntus.tasks;

import ectofuntus.*;
import org.powerbot.script.rt4.ClientContext;

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
        boolean hasPots = Toolbox.itemInInventory(ctx, Ids.POT);
        boolean hasBones = Toolbox.itemInInventory(ctx, Ids.BONES);

        return (atGrinder && hasBones && hasPots);
    }

    @Override
    public int execute() {
        // Antiban reaction buffer
        Toolbox.sleep(500);
        System.out.println("Grind");

        int numBonesStart = Toolbox.countItemInInventory(ctx, Ids.BONES);
        int numBonemealsStart = Toolbox.countItemInInventory(ctx, Ids.BONEMEAL);
        int maxRepeat;

        // set camera
        ctx.camera.pitch(true);
        ctx.camera.angle('n');
        Toolbox.sleep(500);

        // load
        System.out.println(" - Load");
        maxRepeat = 5;
        do {
            ctx.inventory.select().id(Ids.BONES).poll().interact(true, Actions.USE);
            ctx.objects.select().id(Ids.GRINDER_LOADER).poll().interact(true, Actions.USE);
            Toolbox.sleep(4200);
            if (maxRepeat < 0) {
                return -1;
            }
            maxRepeat--;
        } while (Toolbox.countItemInInventory(ctx, Ids.BONES) == numBonesStart);

        // wind
        System.out.println(" - Wind");
        ctx.objects.select().id(Ids.GRINDER_GRINDER).poll().interact(true, Actions.WIND);
        Toolbox.sleep(4000);

        // empty
        System.out.println(" - Empty");
        maxRepeat = 10;
        do {
            ctx.objects.select().id(Ids.GRINDER_BIN).poll().interact(true, Actions.EMPTY);
            Toolbox.sleep(1000);
            if (maxRepeat < 0) {
                return -1;
                // note: if failed to collect bones from grinder, infinite loop.
                //       however, if buried bones, this is recoverable on next task loop.
            }
            maxRepeat--;
        } while (Toolbox.countItemInInventory(ctx, Ids.BONEMEAL) == numBonemealsStart);

        System.out.println("Done.");
        return 0;
    }
}
