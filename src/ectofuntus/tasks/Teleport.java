package ectofuntus.tasks;

import ectofuntus.*;
import org.powerbot.script.rt4.ClientContext;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 23/11/14
 * Time: 10:54 PM
 * Purpose: Teleports to base (Ectofuntus) when needed.
 */
public class Teleport extends Task<ClientContext> {

    public Teleport(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        // inventory booleans
        boolean hasEctophial = Toolbox.itemInInventory(ctx, Ids.ECTOPHIAL_FULL);
        boolean hasBuckets = Toolbox.itemInInventory(ctx, Ids.BUCKET);
        boolean hasPots = Toolbox.itemInInventory(ctx, Ids.POT);
        boolean hasBones = Toolbox.itemInInventory(ctx, Ids.BONES);
        boolean hasMaxPots = Toolbox.countItemInInventory(ctx, Ids.POT) == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;
        boolean hasMaxBones = Toolbox.countItemInInventory(ctx, Ids.BONES) == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;
        boolean hasMaxBuckets = Toolbox.countItemInInventory(ctx, Ids.BUCKET) == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;

        // location booleans
        boolean atEctofuntus = Areas.ECTOFUNTUS.contains(ctx.players.local().tile());
        boolean atBank = Areas.BANK.contains(ctx.players.local().tile());
        boolean atPool = Areas.POOL_OF_SLIME.contains(ctx.players.local().tile());
        boolean atGrinder = Areas.GRINDER.contains(ctx.players.local().tile());
        boolean onTheWayToPool = (Areas.SLIME_LVL_1.contains(ctx.players.local().tile())) ||
                (Areas.SLIME_LVL_2.contains(ctx.players.local().tile())) ||
                (Areas.SLIME_LVL_3.contains(ctx.players.local().tile()));
        boolean onTheWayToBank = (Areas.NORTH_OF_BARRIER.contains(ctx.players.local().tile())) ||
                (Areas.PORT_PHASMATYS.contains(ctx.players.local().tile()));

        // complex booleans
        boolean inKnownAreas = atEctofuntus || atBank || atPool || atGrinder || onTheWayToPool || onTheWayToBank;
        boolean doneBank = atBank && hasMaxPots && hasMaxBones && hasMaxBuckets;
        boolean doneSlime = atPool && !hasBuckets;
        boolean doneGrinder = atGrinder && (!hasPots || !hasBones);

        // return
        return hasEctophial && (!inKnownAreas || doneBank || doneSlime || doneGrinder);
    }

    @Override
    public int execute() {
        // Antiban reaction buffer
        Toolbox.sleep(500);
        System.out.println("Teleport");

        // while not at ectofuntus
        int maxRetry = 5;
        while (!Areas.ECTOFUNTUS.contains(ctx.players.local().tile())) {
            if (Toolbox.itemInInventory(ctx, Ids.ECTOPHIAL_FULL)) {
                ctx.inventory.select().id(Ids.ECTOPHIAL_FULL).poll().interact(true, Actions.EMPTY);
                Toolbox.sleep(5000);
            }

            maxRetry--;
            if (maxRetry < 0) {
                return -1;
            }
        }
        System.out.println("Done.");
        return 0;
    }
}