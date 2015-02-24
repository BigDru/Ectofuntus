package ectofuntus.tasks;

import ectofuntus.*;

import org.powerbot.script.Condition;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 24/11/14 - 1:48 PM
 * Last Modified: 23/02/15 - 3:21 PM
 * Purpose: Traverses Level 1
 */
public class TraverseLVL1 extends Task<ClientContext> {

    public TraverseLVL1(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return Areas.SLIME_LVL_1.contains(ctx.players.local().tile());
    }

    @Override
    public void execute() {
        // Antiban reaction buffer
        Condition.sleep();
        Coeus.getInstance().setCurrentTask("Traversing lvl 1");
        ctx.movement.findPath(Tiles.LVL1_STAIRS_DOWN).traverse();
        Condition.wait(new Condition.Check() {
            @Override
            public boolean poll() {
                return ctx.players.local().tile().distanceTo(Tiles.LVL1_STAIRS_DOWN) < 3;
            }
        }, 100, 3);

        // go down stairs to level pool of slime
        ctx.camera.angle('e');
        ctx.camera.pitch(true);
        ctx.objects.select().id(Ids.STAIRS_TO_SLIME_ABOVE).poll().interact(Actions.CLIMB_DOWN);

        // wait to get to pool of slime
        Condition.wait(new Condition.Check() {
            @Override
            public boolean poll() {
                return ctx.players.local().tile().floor() == Areas.POOL_OF_SLIME.getCentralTile().floor();
            }
        }, 100, 10);
    }
}
