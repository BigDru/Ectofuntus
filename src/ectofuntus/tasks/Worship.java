package ectofuntus.tasks;

import ectofuntus.*;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Npc;
import org.powerbot.script.rt4.Skills;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 18/11/14 - 1:48 PM
 * Last Modified: 23/02/15 - 3:21 PM
 * Purpose: Worships Ectofuntus.
 */
public class Worship extends Task<ClientContext> {

    public Worship(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        boolean atEctofuntus = Areas.ECTOFUNTUS.contains(ctx.players.local().tile());
        boolean hasBonemeal = ctx.itemInInventory(Ids.BONEMEAL);
        boolean hasBucketsOfSlime = ctx.itemInInventory(Ids.BUCKET_OF_SLIME);

        return (atEctofuntus && hasBonemeal && hasBucketsOfSlime);
    }

    @Override
    public void execute() {
        // Antiban reaction buffer
        Condition.sleep();
        Coeus.getInstance().setCurrentTask("Worshiping");

        if (!ctx.objects.select().id(Ids.ECTOFUNTUS).poll().inViewport()) {
            ctx.camera.turnTo(ctx.objects.select().id(Ids.ECTOFUNTUS).poll());
        }

        Condition.wait(new Condition.Check() {
            @Override
            public Boolean call() {
                if (ctx.isPlayerIdle()) {
                    Coeus.getInstance().updateExperience(ctx.skills.experience(Skills.PRAYER));
                    ctx.objects.select().id(Ids.ECTOFUNTUS).poll().interact(true, Actions.WORSHIP);
                }
                return super.call();
            }

            @Override
            public boolean poll() {
                return !ctx.itemInInventory(Ids.BONEMEAL) || !ctx.itemInInventory(Ids.BUCKET_OF_SLIME);
            }
        }, 250, 120);

        // check worship complete
        if (ctx.itemInInventory(Ids.BONEMEAL) && ctx.itemInInventory(Ids.BUCKET_OF_SLIME))
            return;

        // dump extra materials
        for (Item i : ctx.inventory.select()) {
            if (i.id() == Ids.BONEMEAL || i.id() == Ids.BUCKET_OF_SLIME) {
                i.interact(true, Actions.EMPTY);
                Condition.sleep(500);
            }
        }

        // get Ecto-tokens
        // Antiban reaction buffer
        Condition.sleep();
        Coeus.getInstance().setCurrentTask("Collecting Ecto-tokens");

        // walk to disciple (max 6 sec)
        ctx.movement.findPath(Tiles.GHOST_DISCIPLE).traverse();
        Condition.wait(new Condition.Check() {
            @Override
            public boolean poll() {
                return ctx.players.local().tile().distanceTo(Tiles.GHOST_DISCIPLE) < 3;
            }
        }, 250, 24);

        // talk to disciple
        Condition.wait(new Condition.Check() {
            @Override
            public Boolean call() {
                Npc ghost = ctx.npcs.select().id(Ids.GHOST_DISCIPLE).nearest().poll();
                ctx.camera.turnTo(ghost);
                ghost.click(true);
                Condition.sleep(2500);
                ctx.widgets.component(Ids.WIDGET_GHOST_DISCIPLE_1, Ids.WIDGET_GHOST_DISCIPLE_1_COMPONENT).click(true);
                Condition.sleep(1200);
                ctx.widgets.component(Ids.WIDGET_GHOST_DISCIPLE_2, Ids.WIDGET_GHOST_DISCIPLE_2_COMPONENT).click(true);
                Condition.sleep(1200);

                return super.call();
            }

            @Override
            public boolean poll() {
                return ctx.itemInInventory(Ids.ECTO_TOKEN);
            }
        }, 5000, 5);

        // update UI
        if (ctx.inventory.select().id(Ids.ECTO_TOKEN).count() != 0) {
            Coeus.getInstance().addToNumEctotokens(ctx.inventory.select().id(Ids.ECTO_TOKEN).poll().stackSize());
        }

        return;
    }
}
