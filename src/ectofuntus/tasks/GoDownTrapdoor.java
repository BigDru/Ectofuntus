package ectofuntus.tasks;

import ectofuntus.*;

import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Path;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 24/11/14
 * Time: 12:03 AM
 * Purpose: Start the quest for buckets of slime.
 */
public class GoDownTrapdoor extends Task<ClientContext> {

    public GoDownTrapdoor(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        boolean hasEctophial = ctx.itemInInventory(Ids.ECTOPHIAL_FULL);
        boolean hasMaxBuckets = ctx.inventory.select().id(Ids.BUCKET).size() == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;
        boolean hasBonesOrBonemeals = (ctx.itemInInventory(Ids.BONES) || ctx.itemInInventory(Ids.BONEMEAL));
        boolean atEctofuntus = Areas.ECTOFUNTUS.contains(ctx.players.local().tile());
        return (hasEctophial && hasMaxBuckets && atEctofuntus && hasBonesOrBonemeals);
    }

    @Override
    public int execute() {
        // Antiban reaction buffer
        ctx.sleep(500);
        System.out.println("Trapdoor");

        // walk to trapdoor
        int maxRetry = 10;
        Path path = ctx.movement.findPath(Tiles.TRAPDOOR);
        while (ctx.players.local().tile().distanceTo(Tiles.TRAPDOOR) > 1) {
            path.traverse();
            ctx.sleep(1000);

            if (maxRetry <= 0){
                return -1;
            }
            maxRetry--;
        }

        // is trapdoor closed?
        maxRetry = 5;
        while (!ctx.objects.select().id(Ids.TRAPDOOR_CLOSED).isEmpty()) {
            // open it
            GameObject closedTrapdoor = ctx.objects.select().id(Ids.TRAPDOOR_CLOSED).poll();
            if (!closedTrapdoor.inViewport()) {
                ctx.camera.turnTo(closedTrapdoor);
            }
            closedTrapdoor.interact(true, Actions.OPEN);
            ctx.sleep(500);

            if (maxRetry <= 0){
                return -1;
            }
            maxRetry--;
        }

        // climb down trap door
        maxRetry = 5;
        while (Areas.ECTOFUNTUS.contains(ctx.players.local().tile())) {
            ctx.objects.select().id(Ids.TRAPDOOR_OPEN).poll().interact(true, Actions.CLIMB_DOWN);
            ctx.sleep(500);

            if (maxRetry <= 0){
                return -1;
            }
            maxRetry--;
        }
        System.out.println("done.");
        return 0;
    }
}
