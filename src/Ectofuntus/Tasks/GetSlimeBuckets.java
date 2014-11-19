package Ectofuntus.Tasks;

import Ectofuntus.*;
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
        boolean maxBuckets = Toolbox.countItemInInventory(ctx, Ids.Bucket) == MiscConstants.maxNumBuckets;
        boolean hasEctophial = Toolbox.itemInInventory(ctx, Ids.Ectophial_Full);
        return (maxBuckets && hasEctophial);
    }

    @Override
    public int execute() {
        System.out.println("Slime");
        // are we at ectofuntus?
        double distanceToEctofuntus = ctx.objects.select().id(Ids.Ectofuntus).nearest().poll().tile().distanceTo(ctx.players.local().tile());
        if (distanceToEctofuntus > 10 || distanceToEctofuntus == -1) {
            // no? tele there.
            ctx.inventory.select().id(Ids.Ectophial_Full).poll().interact(true, Actions.Empty);

            // wait to fill up ectophial again
            do {
                Toolbox.sleep(1000);
            } while (Toolbox.itemInInventory(ctx, Ids.Ectophial_Empty));
        }

        // we are at ectofuntus
        // walk to trapdoor
        Path path = ctx.movement.findPath(Tiles.Trapdoor);
        do {
            path.traverse();
            Toolbox.sleep(500);
        } while (ctx.players.local().tile().distanceTo(Tiles.Trapdoor) > 1);

        // is trapdoor closed?
        System.out.println("open trapdoor");
        while (!ctx.objects.select().id(Ids.Trapdoor_Closed).isEmpty()) {
            // open it
            GameObject closedTrapdoor = ctx.objects.select().id(Ids.Trapdoor_Closed).poll();
            if (!closedTrapdoor.inViewport()) {
                ctx.camera.turnTo(closedTrapdoor);
            }
            closedTrapdoor.interact(true, Actions.Open);
            Toolbox.sleep(500);
        }

        // climb down trap door
        System.out.println("go down trapdoor");
        do {
            ctx.objects.select().id(Ids.Trapdoor_Open).poll().interact(true, Actions.ClimbDown);
            Toolbox.sleep(500);
        } while (ctx.players.local().tile().floor() != Tiles.Level3_Ladder.floor());

        // are we lvl 58+ in agility?
        if (ctx.skills.level(Constants.SKILLS_AGILITY) >= MiscConstants.agilityLevelForWeatheredWall) {
            System.out.println("agility 58+");
            // yes
            ctx.camera.angle('w');
            do {
                ctx.objects.select().id(Ids.WeatheredWall_FromAbove).poll().interact(true, Actions.JumpDown);
                Toolbox.sleep(500);
            } while (ctx.players.local().tile().floor() != Tiles.Level2_Stairs_down.floor());
        } else {
            // no
            // walk to stairs level 3
            path = ctx.movement.findPath(Tiles.Level3_Stairs);
            do {
                path.traverse();
            } while (ctx.players.local().tile().distanceTo(Tiles.Level3_Stairs) > 3);

            // go down stairs to level 2
            ctx.camera.angle('e');
            do {
                ctx.objects.select().id(Ids.StairsToSlime_FromAbove).poll().interact(true, Actions.ClimbDown);
                Toolbox.sleep(500);
            } while (ctx.players.local().tile().floor() != Tiles.Level2_Stairs_down.floor());

            // walk to stairs level 2 (down)
            path = ctx.movement.findPath(Tiles.Level2_Stairs_down);
            do {
                path.traverse();
            } while (ctx.players.local().tile().distanceTo(Tiles.Level2_Stairs_down) > 3);
        }

        // go down stairs to level 1
        ctx.camera.angle('w');
        do {
            ctx.objects.select().id(Ids.StairsToSlime_FromAbove).poll().interact(true, Actions.ClimbDown);
            Toolbox.sleep(500);
        } while (ctx.players.local().tile().floor() != Tiles.Level1_Stairs_down.floor());

        // walk to stairs level 1 (down)
        path = ctx.movement.findPath(Tiles.Level1_Stairs_down);
        do {
            path.traverse();
        } while (ctx.players.local().tile().distanceTo(Tiles.Level1_Stairs_down) > 3);

        // go down stairs to Pool of Slime
        ctx.camera.angle('e');
        do {
            ctx.objects.select().id(Ids.StairsToSlime_FromAbove).poll().interact(true, Actions.ClimbDown);
            Toolbox.sleep(500);
        } while (ctx.players.local().tile().floor() != Tiles.PoolOfSlime.floor());

        // fill buckets with slime
        do {
            if (Toolbox.isPlayerIdle(ctx)){
                ctx.inventory.select().id(Ids.Bucket).poll().interact(true, Actions.Use);
                ctx.objects.select().id(Ids.PoolOfSlime).nearest().poll().interact(true, Actions.Use);
            }
            Toolbox.sleep(5000);
        } while (Toolbox.countItemInInventory(ctx, Ids.BucketOfSlime) < MiscConstants.maxNumBuckets);

        // done, go back to ectophial
        Toolbox.sleep(500);
        ctx.inventory.select().id(Ids.Ectophial_Full).poll().interact(true, Actions.Empty);

        // wait to fill up ectophial again
        do {
            Toolbox.sleep(1000);
        } while (Toolbox.itemInInventory(ctx, Ids.Ectophial_Empty));

        return 0;
    }
}
