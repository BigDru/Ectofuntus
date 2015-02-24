package ectofuntus.tasks;

import ectofuntus.*;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.GameObject;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 23/11/14 - 10:11 PM
 * Last Modified: 23/02/15 - 3:19 PM
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
    public void execute() {
        // Antiban reaction buffer
        Condition.sleep();
        Coeus.getInstance().setCurrentTask("Filling buckets");

        // Fill
        GameObject pool = ctx.objects.select().id(Ids.POOL_OF_SLIME).nearest().poll();
        ctx.inventory.select().id(Ids.BUCKET).poll().interact(true, Actions.USE);
        ctx.camera.turnTo(pool);
        pool.interact(true, Actions.USE);

        // wait till no more buckets with checks every 1 sec for 18 checks
        Condition.wait(new Condition.Check() {
            @Override
            public boolean poll() {
                return !ctx.itemInInventory(Ids.BUCKET);
            }
        }, 1000, 18);
    }
}