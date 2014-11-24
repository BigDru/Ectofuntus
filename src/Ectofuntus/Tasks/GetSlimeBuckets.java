package ectofuntus.tasks;

import ectofuntus.*;
import org.powerbot.script.rt4.*;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 18/11/14
 * Time: 1:47 PM
 * Purpose: Gets Slime Buckets.
 */
public class GetSlimeBuckets extends Task<ClientContext> {

    public GetSlimeBuckets(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        boolean maxBuckets = Toolbox.countItemInInventory(ctx, Ids.BUCKET) == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;
        boolean hasEctophial = Toolbox.itemInInventory(ctx, Ids.ECTOPHIAL_FULL);
        return (maxBuckets && hasEctophial);
    }

    @Override
    public int execute() {
        System.out.println("Slime");
        // are we at ectofuntus?
        double distanceToEctofuntus = ctx.objects.select().id(Ids.ECTOFUNTUS).nearest().poll().tile().distanceTo(ctx.players.local().tile());
        if (distanceToEctofuntus > 10 || distanceToEctofuntus == -1) {
            // no? tele there.
            ctx.inventory.select().id(Ids.ECTOPHIAL_FULL).poll().interact(true, Actions.EMPTY);

            // wait to fill up ectophial again
            do {
                Toolbox.sleep(1000);
            } while (Toolbox.itemInInventory(ctx, Ids.ECTOPHIAL_EMPTY));
        }

        // we are at ectofuntus
        // walk to trapdoor
        Path path = ctx.movement.findPath(Tiles.TRAPDOOR);
        do {
            path.traverse();
            Toolbox.sleep(500);
        } while (ctx.players.local().tile().distanceTo(Tiles.TRAPDOOR) > 1);

        // is trapdoor closed?
        System.out.println("open trapdoor");
        while (!ctx.objects.select().id(Ids.TRAPDOOR_CLOSED).isEmpty()) {
            // open it
            GameObject closedTrapdoor = ctx.objects.select().id(Ids.TRAPDOOR_CLOSED).poll();
            if (!closedTrapdoor.inViewport()) {
                ctx.camera.turnTo(closedTrapdoor);
            }
            closedTrapdoor.interact(true, Actions.OPEN);
            Toolbox.sleep(500);
        }

        // climb down trap door
        System.out.println("go down trapdoor");
        do {
            ctx.objects.select().id(Ids.TRAPDOOR_OPEN).poll().interact(true, Actions.CLIMB_DOWN);
            Toolbox.sleep(500);
        } while (ctx.players.local().tile().floor() != Tiles.LVL3_LADDER.floor());

        // are we lvl 58+ in agility?
        if (ctx.skills.level(Constants.SKILLS_AGILITY) >= MiscConstants.WEATHERED_WALL_AGILITY_LEVEL_REQ) {
            System.out.println("agility 58+");
            // yes
            ctx.camera.angle('w');
            do {
                ctx.objects.select().id(Ids.WEATHERED_WALL_ABOVE).poll().interact(true, Actions.JUMP_DOWN);
                Toolbox.sleep(500);
            } while (ctx.players.local().tile().floor() != Tiles.LVL2_STAIRS_DOWN.floor());
        } else {
            // no
            // walk to stairs level 3
            path = ctx.movement.findPath(Tiles.LVL3_STAIRS);
            do {
                path.traverse();
            } while (ctx.players.local().tile().distanceTo(Tiles.LVL3_STAIRS) > 3);

            // go down stairs to level 2
            ctx.camera.angle('e');
            do {
                ctx.objects.select().id(Ids.STAIRS_TO_SLIME_ABOVE).poll().interact(true, Actions.CLIMB_DOWN);
                Toolbox.sleep(500);
            } while (ctx.players.local().tile().floor() != Tiles.LVL2_STAIRS_DOWN.floor());

            // walk to stairs level 2 (down)
            path = ctx.movement.findPath(Tiles.LVL2_STAIRS_DOWN);
            do {
                path.traverse();
            } while (ctx.players.local().tile().distanceTo(Tiles.LVL2_STAIRS_DOWN) > 3);
        }

        // go down stairs to level 1
        ctx.camera.angle('w');
        do {
            ctx.objects.select().id(Ids.STAIRS_TO_SLIME_ABOVE).poll().interact(true, Actions.CLIMB_DOWN);
            Toolbox.sleep(500);
        } while (ctx.players.local().tile().floor() != Tiles.LVL1_STAIRS_DOWN.floor());

        // walk to stairs level 1 (down)
        path = ctx.movement.findPath(Tiles.LVL1_STAIRS_DOWN);
        do {
            path.traverse();
        } while (ctx.players.local().tile().distanceTo(Tiles.LVL1_STAIRS_DOWN) > 3);

        // go down stairs to Pool of Slime
        ctx.camera.angle('e');
        do {
            ctx.objects.select().id(Ids.STAIRS_TO_SLIME_ABOVE).poll().interact(true, Actions.CLIMB_DOWN);
            Toolbox.sleep(500);
        } while (ctx.players.local().tile().floor() != Tiles.POOL_OF_SLIME.floor());

        // fill buckets with slime
        do {
            if (Toolbox.isPlayerIdle(ctx)){
                ctx.inventory.select().id(Ids.BUCKET).poll().interact(true, Actions.USE);
                ctx.objects.select().id(Ids.POOL_OF_SLIME).nearest().poll().interact(true, Actions.USE);
            }
            Toolbox.sleep(5000);
        } while (Toolbox.countItemInInventory(ctx, Ids.BUCKET_OF_SLIME) < MiscConstants.MAX_COUNT_FOR_EACH_ITEM);

        // done, go back to ectophial
        Toolbox.sleep(500);
        ctx.inventory.select().id(Ids.ECTOPHIAL_FULL).poll().interact(true, Actions.EMPTY);

        // wait to fill up ectophial again
        do {
            Toolbox.sleep(1000);
        } while (Toolbox.itemInInventory(ctx, Ids.ECTOPHIAL_EMPTY));

        return 0;
    }
}
