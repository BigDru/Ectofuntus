package ectofuntus.tasks;

import ectofuntus.*;
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
        boolean maxBonemeal = Toolbox.countItemInInventory(ctx, Ids.BONEMEAL) == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;
        boolean maxBucketsOfSlime = Toolbox.countItemInInventory(ctx, Ids.BUCKET_OF_SLIME) == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;
        return (maxBonemeal && maxBucketsOfSlime);
    }

    @Override
    public int execute() {
        System.out.println("worship");
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
        // worship
        if (!ctx.objects.select().id(Ids.ECTOFUNTUS).poll().inViewport()) {
            ctx.camera.turnTo(ctx.objects.select().id(Ids.ECTOFUNTUS).poll());
        }

        do {
            ctx.objects.select().id(Ids.ECTOFUNTUS).poll().interact(true, Actions.WORSHIP);
            do {
                Toolbox.sleep(2500);
            } while (!Toolbox.isPlayerIdle(ctx));
        } while (Toolbox.itemInInventory(ctx, Ids.BUCKET_OF_SLIME));

        // get Ecto-tokens
        System.out.println("Collecting Ecto-tokens");
        Path path = ctx.movement.findPath(Tiles.GHOST_DISCIPLE);
        do {
            path.traverse();
        } while (ctx.players.local().tile().distanceTo(Tiles.GHOST_DISCIPLE) > 2);

        // talk to disciple
        do {
            Npc ghost = ctx.npcs.select().id(Ids.GHOST_DISCIPLE).nearest().poll();
            ctx.camera.turnTo(ghost);
            ghost.click(true);
            Toolbox.sleep(2500);
            ctx.widgets.component(Ids.WIDGET_GHOST_DISCIPLE_1, Ids.WIDGET_GHOST_DISCIPLE_1_COMPONENT).click(true);
            Toolbox.sleep(500);
            ctx.widgets.component(Ids.WIDGET_GHOST_DISCIPLE_2, Ids.WIDGET_GHOST_DISCIPLE_2_COMPONENT).click(true);
            Toolbox.sleep(500);
        } while (!Toolbox.itemInInventory(ctx, Ids.ECTO_TOKEN));

        return 0;
    }
}
