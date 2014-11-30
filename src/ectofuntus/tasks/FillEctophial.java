package ectofuntus.tasks;

import ectofuntus.*;
import org.powerbot.script.rt4.ClientContext;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 24/11/14
 * Time: 2:29 PM
 * Purpose: If you're at ectofuntus and you have an empty ectophial, fill it up
 */
public class FillEctophial extends Task<ClientContext> {

    public FillEctophial(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        boolean atEctofuntus = Areas.ECTOFUNTUS.contains(ctx.players.local().tile());
        boolean hasFullEctophial = Toolbox.itemInInventory(ctx, Ids.ECTOPHIAL_FULL);
        return (atEctofuntus && !hasFullEctophial);
    }

    @Override
    public int execute() {
        // Antiban reaction buffer
        Toolbox.sleep(500);
        System.out.println("Filling Ectophial");

        int maxRetry = 2;
        while (Toolbox.itemInInventory(ctx, Ids.ECTOPHIAL_EMPTY)) {
            // not full, fill ectophial
            ctx.inventory.select().id(Ids.ECTOPHIAL_EMPTY).poll().interact(false, Actions.USE);
            Toolbox.sleep(1500);
            ctx.camera.turnTo(ctx.objects.id(Ids.ECTOFUNTUS).nearest().poll());
            ctx.objects.select().id(Ids.ECTOFUNTUS).nearest().poll().interact(true, Actions.USE);
            Toolbox.sleep(1500);

            maxRetry--;
            if (maxRetry < 0) {
                return -1;
            }
        }
        System.out.println("done.");
        return 0;
    }
}
