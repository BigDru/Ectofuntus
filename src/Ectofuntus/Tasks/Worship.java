package Ectofuntus.Tasks;

import Ectofuntus.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;
import org.powerbot.script.rt4.Path;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 18/11/14
 * Time: 1:48 PM
 * Purpose: Worships Ectofuntus.
 */
public class Worship extends Task<ClientContext> {

    public Worship(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        boolean maxBonemeal = Toolbox.countItemInInventory(ctx, Ids.Bonemeal) == MiscConstants.maxNumPots;
        boolean maxBucketsOfSlime = Toolbox.countItemInInventory(ctx, Ids.BucketOfSlime) == MiscConstants.maxNumBuckets;
        return (maxBonemeal && maxBucketsOfSlime);
    }

    @Override
    public int execute() {
        System.out.println("worship");
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
        // worship
        if (!ctx.objects.select().id(Ids.Ectofuntus).poll().inViewport()) {
            ctx.camera.turnTo(ctx.objects.select().id(Ids.Ectofuntus).poll());
        }

        do {
            ctx.objects.select().id(Ids.Ectofuntus).poll().interact(true, Actions.Worship);
            do {
                Toolbox.sleep(2500);
            } while (!Toolbox.isPlayerIdle(ctx));
        } while (Toolbox.itemInInventory(ctx, Ids.BucketOfSlime));

        // get Ecto-tokens
        System.out.println("Collecting Ecto-tokens");
        Path path = ctx.movement.findPath(Tiles.GhostDisciple);
        do {
            path.traverse();
        } while (ctx.players.local().tile().distanceTo(Tiles.GhostDisciple) > 2);

        do {
            // talk to disciple
            do {
                Npc ghost = ctx.npcs.select().id(Ids.GhostDisciple).nearest().poll();
                ctx.camera.turnTo(ghost);
                ghost.click(true);
                Toolbox.sleep(1000);
            } while (ctx.widgets.select().id(Ids.Widget_GhostDisciple1).isEmpty());
            System.out.println("talking");
            do {
                System.out.println("stuck");
                ctx.widgets.component(Ids.Widget_GhostDisciple1, Ids.Widget_GhostDisciple1_Component).click(true);
                Toolbox.sleep(1000);
                ctx.widgets.component(Ids.Widget_GhostDisciple2, Ids.Widget_GhostDisciple2_Component).click(true);
                Toolbox.sleep(1000);
            } while (!ctx.widgets.select().id(Ids.Widget_GhostDisciple2).isEmpty());

            Toolbox.sleep(1250);
        } while (Toolbox.itemInInventory(ctx, Ids.EctoToken));

        return 0;
    }
}
