package ectofuntus.tasks;

import ectofuntus.*;
import org.powerbot.script.Condition;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 23/02/15 - 10:45 PM
 * Last Modified: 23/02/15 - 10:45 PM
 * Purpose: Wind bone grinder
 */
public class Grind_Wind extends Task<ClientContext> {

    public Grind_Wind(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        boolean atGrinder = Areas.GRINDER.contains(ctx.players.local().tile());
        boolean hasPots = ctx.itemInInventory(Ids.POT);
        boolean needsWinding = ctx.varpbits.varpbit(Ids.VARPBIT_GRINDER_ID) == Ids.VARPBIT_GRINDER_LOADED;

        return (atGrinder && hasPots && needsWinding);
    }

    @Override
    public void execute() {
        Condition.sleep();
        Coeus.getInstance().setCurrentTask("Grinding bones");

        // set camera
        ctx.camera.pitch(true);
        ctx.camera.angle('n');

        // wind
        ctx.objects.select().id(Ids.GRINDER_GRINDER).poll().interact(true, Actions.WIND);
        Condition.sleep(1500);
        Condition.wait(new Condition.Check() {
            @Override
            public boolean poll() {
                if (ctx.isPlayerIdle()) {
                    return ctx.varpbits.varpbit(Ids.VARPBIT_GRINDER_ID) != Ids.VARPBIT_GRINDER_LOADED;
                }
                return false;
            }
        }, 500, 4);
    }
}
