package ectofuntus.tasks;

import ectofuntus.*;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.GameObject;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 24/11/14 - 12:03 AM
 * Last Modified: 23/02/15 - 3:19 PM
 * Purpose: Start the quest for buckets of slime.
 */
public class GoDownTrapdoor extends Task<ClientContext> {

    public GoDownTrapdoor(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        boolean hasEctophial = ctx.itemInInventory(Ids.ECTOPHIAL_FULL);
        boolean hasBuckets = ctx.itemInInventory(Ids.BUCKET);
        boolean hasBonesOrBonemeals = (ctx.itemInInventory(Ids.BONES) || ctx.itemInInventory(Ids.BONEMEAL));
        boolean atEctofuntus = Areas.ECTOFUNTUS.contains(ctx.players.local().tile());

        return (hasEctophial && hasBuckets && atEctofuntus && hasBonesOrBonemeals);
    }

    @Override
    public void execute() {
        // Antiban reaction buffer
        Condition.sleep();
        Coeus.getInstance().setCurrentTask("Opening trapdoor");

        // walk to trapdoor
        ctx.movement.findPath(Tiles.TRAPDOOR).traverse();
        Condition.wait(new Condition.Check() {
            @Override
            public boolean poll() {
                return ctx.players.local().tile().distanceTo(Tiles.TRAPDOOR) < 2;
            }
        });

        // is trapdoor closed?
        Condition.wait(new Condition.Check() {
            @Override
            public boolean poll() {
                return ctx.objects.select().id(Ids.TRAPDOOR_CLOSED).isEmpty();
            }

            @Override
            public Boolean call() {
                GameObject closedTrapdoor = ctx.objects.select().id(Ids.TRAPDOOR_CLOSED).poll();
                ctx.camera.turnTo(closedTrapdoor);
                closedTrapdoor.interact(true, Actions.OPEN);
                return super.call();
            }
        });

        // climb down trap door
        ctx.objects.select().id(Ids.TRAPDOOR_OPEN).poll().interact(true, Actions.CLIMB_DOWN);
        Condition.wait(new Condition.Check() {
            @Override
            public boolean poll() {
                return !Areas.ECTOFUNTUS.contains(ctx.players.local().tile());
            }
        });
    }
}
