package ectofuntus.tasks;

import ectofuntus.*;
import org.powerbot.script.Condition;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 23/02/15 - 1:11 PM
 * Last Modified: 23/02/15 - 3:21 PM
 * Purpose: Loads one bone in inventory into grinder
 */
public class Grind_Load extends Task<ClientContext> {

    public Grind_Load(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        boolean atGrinder = Areas.GRINDER.contains(ctx.players.local().tile());
        boolean hasPots = ctx.itemInInventory(Ids.POT);
        boolean hasBones = ctx.itemInInventory(Ids.BONES);
        boolean grinderEmpty = ctx.varpbits.varpbit(Ids.VARPBIT_GRINDER_ID) == Ids.VARPBIT_GRINDER_EMPTY;

        return (atGrinder && hasBones && hasPots && grinderEmpty);
    }

    @Override
    public void execute() {
        Condition.sleep();
        Coeus.getInstance().setCurrentTask("Grinding bones");

        // set camera
        ctx.camera.pitch(true);
        ctx.camera.angle('n');

        // Load
        if (ctx.inventory.select().id(Ids.BONES).count() != 0)
            ctx.inventory.select().id(Ids.BONES).poll().interact(true, Actions.USE);
        if (ctx.objects.select().id(Ids.GRINDER_LOADER).size() != 0)
            ctx.objects.select().id(Ids.GRINDER_LOADER).poll().interact(true, Actions.USE);

        // wait till loaded
        Condition.wait(new Condition.Check() {
            @Override
            public boolean poll() {
                if (ctx.isPlayerIdle()) {
                    return ctx.varpbits.varpbit(Ids.VARPBIT_GRINDER_ID) != Ids.VARPBIT_GRINDER_EMPTY;
                }
                return false;
            }
        }, 500, 4);
    }
}
