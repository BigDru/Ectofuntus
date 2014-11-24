package ectofuntus.tasks;

import ectofuntus.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Path;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 18/11/14
 * Time: 1:47 PM
 * Purpose: Gets Bonemeal.
 */
public class GetBonemeal extends Task<ClientContext> {

    public GetBonemeal(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        boolean maxPots = Toolbox.countItemInInventory(ctx, Ids.POT) == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;
        boolean maxBones = Toolbox.countItemInInventory(ctx, Ids.BONES) == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;
        boolean hasEctophial = Toolbox.itemInInventory(ctx, Ids.ECTOPHIAL_FULL);
        return (maxBones && maxPots && hasEctophial);
    }

    @Override
    public int execute() {
        System.out.println("Bonemeal");
        // are we at ectofuntus?
        double distanceToEctofuntus = ctx.objects.select().id(Ids.ECTOFUNTUS).nearest().poll().tile().distanceTo(ctx.players.local().tile());
        if (distanceToEctofuntus > 10 || distanceToEctofuntus == -1) {
            // no? tele there.
            ctx.inventory.select().id(Ids.ECTOPHIAL_FULL).poll().interact(true, Actions.EMPTY);

            // wait to fill up ectophial again
            do {
                Toolbox.sleep(1000);
            } while (Toolbox.itemInInventory(ctx, Ids.ECTOPHIAL_EMPTY));
        }

        // we are at ectofuntus
        // find stairs
        Path path = ctx.movement.findPath(Tiles.STAIRS_TO_GRINDER);
        do {
            path.traverse();
        } while (ctx.players.local().tile().distanceTo(Tiles.STAIRS_TO_GRINDER) > 1);

        GameObject stairs = ctx.objects.select().id(Ids.STAIRS_TO_GRINDER).nearest().poll();
        while (!stairs.inViewport()) {
            ctx.camera.turnTo(stairs);
            Toolbox.sleep(500);
        }

        // go up stairs
        do {
            stairs.interact(true, Actions.CLIMB_UP);
            Toolbox.sleep(1000);
        } while (ctx.players.local().tile().floor() != Tiles.GRINDER.floor());

        // walk to grinder
        path = ctx.movement.findPath(Tiles.GRINDER);
        do {
            path.traverse();
        } while (ctx.players.local().tile().distanceTo(Tiles.GRINDER) > 2);

        // set camera
        ctx.camera.pitch(true);
        ctx.camera.angle('n');

        // grind bones
        int maxRepeat = 15;
        do {
            // load
            ctx.inventory.select().id(Ids.BONES).poll().interact(true, Actions.USE);
            ctx.objects.select().id(Ids.GRINDER_LOADER).poll().interact(true, Actions.USE);
            do {
                Toolbox.sleep(2000);
            } while (!Toolbox.isPlayerIdle(ctx));

            // wind
            ctx.objects.select().id(Ids.GRINDER_GRINDER).poll().interact(true, Actions.WIND);
            do {
                Toolbox.sleep(2000);
            } while (!Toolbox.isPlayerIdle(ctx));

            // empty
            ctx.objects.select().id(Ids.GRINDER_BIN).poll().interact(true, Actions.EMPTY);
            do {
                Toolbox.sleep(2000);
            } while (!Toolbox.isPlayerIdle(ctx));

            // handle infinite loops
            maxRepeat--;
            if (maxRepeat == 0) {
                return -1;
            }

        } while (Toolbox.countItemInInventory(ctx, Ids.BONEMEAL) != MiscConstants.MAX_COUNT_FOR_EACH_ITEM);

        // done, go back to ectophial
        Toolbox.sleep(500);
        ctx.inventory.select().id(Ids.ECTOPHIAL_FULL).poll().interact(true, Actions.EMPTY);

        // wait to fill up ectophial again
        do {
            Toolbox.sleep(1000);
        } while (Toolbox.itemInInventory(ctx, Ids.ECTOPHIAL_EMPTY));

        return 0;
    }
}
