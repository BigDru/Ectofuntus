package ectofuntus.tasks;

import ectofuntus.*;
import org.powerbot.script.rt4.GameObject;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 23/11/14
 * Time: 10:11 PM
 * Purpose: Gets bucket of slime
 */
public class FillBuckets extends Task<ClientContext> {

    public FillBuckets(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        boolean hasEctophial = ctx.itemInInventory(Ids.ECTOPHIAL_FULL);
        boolean atPool = Areas.POOL_OF_SLIME.contains(ctx.players.local().tile());
        boolean haveBucket = ctx.itemInInventory(Ids.BUCKET);

        return (hasEctophial && atPool && haveBucket);
    }

    @Override
    public int execute() {
        // Antiban reaction buffer
        ctx.sleep(500);
        System.out.println("Fill buckets");
        // Fill
        boolean filledUp = false;
        int maxRepeat = 5;
        GameObject pool = ctx.objects.select().id(Ids.POOL_OF_SLIME).nearest().poll();
        do {
            // click on pool
            if (ctx.isPlayerIdle()) {
                ctx.inventory.select().id(Ids.BUCKET).poll().interact(true, Actions.USE);
                ctx.camera.turnTo(pool);
                pool.interact(true, Actions.USE);
            }
            // reached max num?
            filledUp = ctx.inventory.select().id(Ids.BUCKET_OF_SLIME).size() == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;
            maxRepeat--;

            // wait 8 sec or until buckets are filled
            for (int i = 0; i < 9; i++){
                if (ctx.itemInInventory(Ids.BUCKET)){
                    ctx.sleep(1000);
                } else {
                    System.out.println("Done.");
                    return 0;
                }
            }
        } while (!filledUp && maxRepeat > 0);
        System.out.println("Done.");
        return 0;
    }
}