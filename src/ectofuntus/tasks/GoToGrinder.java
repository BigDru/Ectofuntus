package ectofuntus.tasks;

import ectofuntus.*;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.GameObject;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 25/11/14 - 5:03 PM
 * Last Modified: 23/02/15 - 3:21 PM
 * Purpose: Go to grinder.
 */
public class GoToGrinder extends Task<ClientContext>{

    public GoToGrinder(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        boolean hasEctophial = ctx.itemInInventory(Ids.ECTOPHIAL_FULL);
        boolean hasPots = ctx.itemInInventory(Ids.POT);
        boolean hasBones = ctx.itemInInventory(Ids.BONES);
        boolean atEctofuntus = Areas.ECTOFUNTUS.contains(ctx.players.local().tile());
        return (hasEctophial && hasPots && hasBones && atEctofuntus);
    }

    @Override
    public void execute() {
        // Antiban reaction buffer
        Condition.sleep();
        Coeus.getInstance().setCurrentTask("Walking to grinder");

        // Walk to stairs
        ctx.movement.findPath(Tiles.STAIRS_TO_GRINDER).traverse();
        Condition.wait(new Condition.Check() {
            @Override
            public boolean poll() {
                return ctx.players.local().tile().distanceTo(Tiles.STAIRS_TO_GRINDER) < 2;
            }
        });

        // Go up stairs
        Condition.wait(new Condition.Check() {
            @Override
            public boolean poll() {
                return ctx.players.local().tile().floor() == Tiles.GRINDER.floor();
            }

            @Override
            public Boolean call() {
                GameObject stairs = ctx.objects.select().id(Ids.STAIRS_TO_GRINDER).nearest().poll();
                ctx.camera.turnTo(stairs);
                stairs.interact(Actions.CLIMB_UP);
                return super.call();
            }
        });

        // walk to grinder
        ctx.movement.findPath(Tiles.GRINDER).traverse();
        Condition.wait(new Condition.Check() {
            @Override
            public boolean poll() {
                return ctx.players.local().tile().distanceTo(Tiles.GRINDER) < 3;
            }
        });
    }
}
