package ectofuntus.tasks;

import ectofuntus.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;
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
        boolean atEctofuntus = Areas.ECTOFUNTUS.contains(ctx.players.local().tile());
        boolean hasBonemeal = Toolbox.itemInInventory(ctx, Ids.BONEMEAL);
        boolean hasBucketsOfSlime = Toolbox.itemInInventory(ctx, Ids.BUCKET_OF_SLIME);
        return (atEctofuntus && hasBonemeal && hasBucketsOfSlime);
    }

    @Override
    public int execute() {
        // Antiban reaction buffer
        Toolbox.sleep(500);
        System.out.println("Worship");

        // worship
        if (!ctx.objects.select().id(Ids.ECTOFUNTUS).poll().inViewport()) {
            ctx.camera.turnTo(ctx.objects.select().id(Ids.ECTOFUNTUS).poll());
            Toolbox.sleep(1000);
        }

        int maxRetry = 15;
        do {
            ctx.objects.select().id(Ids.ECTOFUNTUS).poll().interact(true, Actions.WORSHIP);
            Toolbox.sleep(4000);
            if (maxRetry < 0){
                return -1;
            }
            maxRetry--;
        } while (Toolbox.itemInInventory(ctx, Ids.BUCKET_OF_SLIME) && Toolbox.itemInInventory(ctx, Ids.BONEMEAL));

        // dump extra materials
        for (Item i : ctx.inventory.select()){
            if (i.id() == Ids.BONEMEAL || i.id() == Ids.BUCKET_OF_SLIME){
                i.interact(true, Actions.EMPTY);
            }
        }
        System.out.println("Done.");

        // get Ecto-tokens
        // Antiban reaction buffer
        Toolbox.sleep(500);
        System.out.println("Collecting Ecto-tokens");
        Path path = ctx.movement.findPath(Tiles.GHOST_DISCIPLE);
        maxRetry = 5;
        do {
            path.traverse();
            Toolbox.sleep(2000);
            if (maxRetry < 0){
                return -1;
            }
            maxRetry--;
        } while (ctx.players.local().tile().distanceTo(Tiles.GHOST_DISCIPLE) > 2);

        // talk to disciple
        maxRetry = 5;
        do {
            Npc ghost = ctx.npcs.select().id(Ids.GHOST_DISCIPLE).nearest().poll();
            ctx.camera.turnTo(ghost);
            ghost.click(true);
            Toolbox.sleep(2500);
            ctx.widgets.component(Ids.WIDGET_GHOST_DISCIPLE_1, Ids.WIDGET_GHOST_DISCIPLE_1_COMPONENT).click(true);
            Toolbox.sleep(1200);
            ctx.widgets.component(Ids.WIDGET_GHOST_DISCIPLE_2, Ids.WIDGET_GHOST_DISCIPLE_2_COMPONENT).click(true);
            Toolbox.sleep(1200);
            if (maxRetry < 0){
                return -2;
            }
        } while (!Toolbox.itemInInventory(ctx, Ids.ECTO_TOKEN));

        System.out.println("Done.");
        return 0;
    }
}
