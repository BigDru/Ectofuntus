package ectofuntus.tasks;

import ectofuntus.*;

import org.powerbot.script.Condition;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 24/11/14 - 1:46 PM
 * Last Modified: 23/02/15 - 3:21 PM
 * Purpose: Traverses Level 2
 */
public class TraverseLVL2 extends Task<ClientContext> {

    public TraverseLVL2(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return Areas.SLIME_LVL_2.contains(ctx.players.local().tile());
    }

    @Override
    public void execute() {
        // Antiban reaction buffer
        Condition.sleep();
        Coeus.getInstance().setCurrentTask("Traversing lvl 2");
        ctx.movement.findPath(Tiles.LVL2_STAIRS_DOWN).traverse();
        Condition.wait(new Condition.Check() {
            @Override
            public boolean poll() {
                return ctx.players.local().tile().distanceTo(Tiles.LVL2_STAIRS_DOWN) < 3;
            }
        }, 100, 3);

        // go down stairs to level 1
        ctx.camera.angle('w');
        ctx.camera.pitch(true);
        ctx.objects.select().id(Ids.STAIRS_TO_SLIME_ABOVE).poll().interact(Actions.CLIMB_DOWN);

        // wait to reach level 1
        Condition.wait(new Condition.Check() {
            @Override
            public boolean poll() {
                return ctx.players.local().tile().floor() == Tiles.LVL1_STAIRS_DOWN.floor();
            }
        }, 100, 10);
    }
}
