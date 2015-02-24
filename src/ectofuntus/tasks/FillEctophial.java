package ectofuntus.tasks;

import ectofuntus.*;
import org.powerbot.script.Condition;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 24/11/14 - 2:29 PM
 * Last Modified: 23/02/15 - 3:19 PM
 * Purpose: If you're at ectofuntus and you have an empty ectophial, fill it up
 */
public class FillEctophial extends Task<ClientContext> {

    public FillEctophial(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        boolean atEctofuntus = Areas.ECTOFUNTUS.contains(ctx.players.local().tile());
        boolean hasFullEctophial = ctx.itemInInventory(Ids.ECTOPHIAL_FULL);
        return (atEctofuntus && !hasFullEctophial);
    }

    @Override
    public void execute() {
        // Antiban reaction buffer
        Condition.sleep();
        Coeus.getInstance().setCurrentTask("Filling Ectophial");

        // wait for automatic refill
        Condition.sleep(1500);

        // still empty? fill
        if (ctx.itemInInventory(Ids.ECTOPHIAL_EMPTY)) {
            ctx.camera.turnTo(ctx.objects.id(Ids.ECTOFUNTUS).nearest().poll());
            ctx.inventory.select().id(Ids.ECTOPHIAL_EMPTY).poll().interact(Actions.USE);
            ctx.objects.select().id(Ids.ECTOFUNTUS).nearest().poll().interact(true, Actions.USE);

            Condition.wait(new Condition.Check() {
                @Override
                public boolean poll() {
                    return ctx.itemInInventory(Ids.ECTOPHIAL_FULL);
                }
            });
        }
    }
}
