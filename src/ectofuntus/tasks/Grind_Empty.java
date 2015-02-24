package ectofuntus.tasks;

import ectofuntus.*;
import org.powerbot.script.Condition;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 23/02/15 - 10:46 PM
 * Last Modified: 23/02/15 - 10:46 PM
 * Purpose: Empty bin to get bonemeal.
 */
public class Grind_Empty extends Task<ClientContext> {

    public Grind_Empty(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        boolean atGrinder = Areas.GRINDER.contains(ctx.players.local().tile());
        boolean hasPots = ctx.itemInInventory(Ids.POT);
        boolean readyToCollect = ctx.varpbits.varpbit(Ids.VARPBIT_GRINDER_ID) == Ids.VARPBIT_GRINDER_GROUND;

        return (atGrinder && hasPots && readyToCollect);
    }

    @Override
    public void execute() {
        ctx.objects.select().id(Ids.GRINDER_BIN).poll().interact(true, Actions.EMPTY);
        Condition.wait(new Condition.Check() {
            @Override
            public boolean poll() {
                return ctx.varpbits.varpbit(Ids.VARPBIT_GRINDER_ID) != Ids.VARPBIT_GRINDER_GROUND;
            }
        }, 500, 4);
    }
}
