package ectofuntus.tasks;

import ectofuntus.*;

import org.powerbot.script.rt4.GameObject;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 25/11/14
 * Time: 9:30 PM
 * Purpose: Pass barrier on way to bank north of Port Phasmatys.
 */
public class PassBarrier extends Task<ClientContext> {

    public PassBarrier(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return Areas.NORTH_OF_BARRIER.contains(ctx.players.local().tile());
    }

    @Override
    public int execute() {
        // Antiban reaction buffer
        ctx.sleep(500);
        System.out.println("Passing Barrier");

        GameObject barrier = ctx.objects.select().id(Ids.ENERGY_BARRIER).nearest().poll();
        ctx.camera.turnTo(barrier);
        ctx.sleep(1000);

        barrier.interact(true, Actions.PAY_TOLL);
        ctx.sleep(4000);

        System.out.println("Done.");
        return 0;
    }
}