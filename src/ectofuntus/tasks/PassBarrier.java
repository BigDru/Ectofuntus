package ectofuntus.tasks;

import ectofuntus.*;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.GameObject;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 25/11/14 - 9:30 PM
 * Last Modified: 23/02/15 - 3:21 PM
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
    public void execute() {
        // Antiban reaction buffer
        Condition.sleep();
        Coeus.getInstance().setCurrentTask("Walking to bank");

        GameObject barrier = ctx.objects.select().id(Ids.ENERGY_BARRIER).nearest().poll();
        ctx.camera.turnTo(barrier);
        Condition.sleep(1000);

        barrier.interact(Actions.PAY_TOLL);

        Condition.wait(new Condition.Check() {
            @Override
            public boolean poll() {
                return !Areas.NORTH_OF_BARRIER.contains(ctx.players.local().tile());
            }
        });
    }
}