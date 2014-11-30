package ectofuntus.tasks;

import ectofuntus.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Path;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 25/11/14
 * Time: 5:03 PM
 * Purpose: Go to grinder.
 */
public class GoToGrinder extends Task<ClientContext>{

    public GoToGrinder(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        boolean hasEctophial = Toolbox.itemInInventory(ctx, Ids.ECTOPHIAL_FULL);
        boolean hasMaxPots = Toolbox.countItemInInventory(ctx, Ids.POT) == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;
        boolean hasMaxBones = Toolbox.countItemInInventory(ctx, Ids.BONES) == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;
        boolean atEctofuntus = Areas.ECTOFUNTUS.contains(ctx.players.local().tile());
        return (hasEctophial && hasMaxPots && hasMaxBones && atEctofuntus);
    }

    @Override
    public int execute() {
        // Antiban reaction buffer
        Toolbox.sleep(500);
        System.out.println("Go to Grinder.");

        Path path = ctx.movement.findPath(Tiles.STAIRS_TO_GRINDER);
        int maxRetry = 10;
        do {
            path.traverse();
            if (maxRetry < 0){
                return -1;
            }
            maxRetry--;
            Toolbox.sleep(1000);
        } while (ctx.players.local().tile().distanceTo(Tiles.STAIRS_TO_GRINDER) > 1);

        GameObject stairs = ctx.objects.select().id(Ids.STAIRS_TO_GRINDER).nearest().poll();
        maxRetry = 5;
        while (!stairs.inViewport()) {
            ctx.camera.turnTo(stairs);
            Toolbox.sleep(500);
            if (maxRetry < 0){
                return -1;
            }
            maxRetry--;
        }

        // go up stairs
        maxRetry = 5;
        do {
            stairs.interact(true, Actions.CLIMB_UP);
            Toolbox.sleep(1000);
            if (maxRetry < 0){
                return -1;
            }
            maxRetry--;
        } while (ctx.players.local().tile().floor() != Tiles.GRINDER.floor());

        // walk to grinder
        path = ctx.movement.findPath(Tiles.GRINDER);
        maxRetry = 10;
        do {
            path.traverse();
            if (maxRetry < 0){
                return -1;
            }
            maxRetry--;
        } while (ctx.players.local().tile().distanceTo(Tiles.GRINDER) > 2);
        System.out.println("Done.");
        return 0;
    }
}
