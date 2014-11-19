package Ectofuntus.Tasks;

import Ectofuntus.*;
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
        boolean maxPots = Toolbox.countItemInInventory(ctx, Ids.Pot) == MiscConstants.maxNumPots;
        boolean maxBones = Toolbox.countItemInInventory(ctx, Ids.Bones) == MiscConstants.maxNumBones;
        boolean hasEctophial = Toolbox.itemInInventory(ctx, Ids.Ectophial_Full);
        return (maxBones && maxPots && hasEctophial);
    }

    @Override
    public int execute() {
        System.out.println("Bonemeal");
        // are we at ectofuntus?
        double distanceToEctofuntus = ctx.objects.select().id(Ids.Ectofuntus).nearest().poll().tile().distanceTo(ctx.players.local().tile());
        if (distanceToEctofuntus > 10 || distanceToEctofuntus == -1) {
            // no? tele there.
            ctx.inventory.select().id(Ids.Ectophial_Full).poll().interact(true, Actions.Empty);

            // wait to fill up ectophial again
            do {
                Toolbox.sleep(1000);
            } while (Toolbox.itemInInventory(ctx, Ids.Ectophial_Empty));
        }

        // we are at ectofuntus
        // find stairs
        Path path = ctx.movement.findPath(Tiles.StairsToGrinder);
        do {
            path.traverse();
        } while (ctx.players.local().tile().distanceTo(Tiles.StairsToGrinder) > 1);

        GameObject stairs = ctx.objects.select().id(Ids.StairsToGrinder).nearest().poll();
        while (!stairs.inViewport()) {
            ctx.camera.turnTo(stairs);
            Toolbox.sleep(500);
        }

        // go up stairs
        do {
            stairs.interact(true, Actions.ClimbUp);
            Toolbox.sleep(1000);
        } while (ctx.players.local().tile().floor() != Tiles.Grinder.floor());

        // walk to grinder
        path = ctx.movement.findPath(Tiles.Grinder);
        do {
            path.traverse();
        } while (ctx.players.local().tile().distanceTo(Tiles.Grinder) > 2);

        // set camera
        ctx.camera.pitch(true);
        ctx.camera.angle('n');

        // grind bones
        int maxRepeat = 15;
        do {
            // load
            ctx.inventory.select().id(Ids.Bones).poll().interact(true, Actions.Use);
            ctx.objects.select().id(Ids.Grinder_Loader).poll().interact(true, Actions.Use);
            do {
                Toolbox.sleep(2000);
            } while (!Toolbox.isPlayerIdle(ctx));

            // wind
            ctx.objects.select().id(Ids.Grinder_Grinder).poll().interact(true, Actions.Wind);
            do {
                Toolbox.sleep(2000);
            } while (!Toolbox.isPlayerIdle(ctx));

            // empty
            ctx.objects.select().id(Ids.Grinder_Bin).poll().interact(true, Actions.Empty);
            do {
                Toolbox.sleep(2000);
            } while (!Toolbox.isPlayerIdle(ctx));

            // handle infinite loops
            maxRepeat--;
            if (maxRepeat == 0) {
                return -1;
            }

        } while (Toolbox.countItemInInventory(ctx, Ids.Bonemeal) != MiscConstants.maxNumPots);

        // done, go back to ectophial
        Toolbox.sleep(500);
        ctx.inventory.select().id(Ids.Ectophial_Full).poll().interact(true, Actions.Empty);

        // wait to fill up ectophial again
        do {
            Toolbox.sleep(1000);
        } while (Toolbox.itemInInventory(ctx, Ids.Ectophial_Empty));

        return 0;
    }
}
