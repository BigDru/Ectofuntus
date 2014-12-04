package ectofuntus.tasks;

import ectofuntus.*;


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
        boolean hasEctophial = ctx.itemInInventory(Ids.ECTOPHIAL_FULL);
        boolean hasBuckets = ctx.itemInInventory(Ids.BUCKET);
        boolean hasPots = ctx.itemInInventory(Ids.POT);
        boolean hasBones = ctx.itemInInventory(Ids.BONES);
        boolean hasMaxPots = ctx.inventory.select().id(Ids.POT).size() == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;
        boolean hasMaxBones = ctx.inventory.select().id(Ids.BONES).size() == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;
        boolean hasMaxBuckets = ctx.inventory.select().id(Ids.BUCKET).size() == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;

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
        ctx.sleep(500);
        System.out.println("Teleport");

        // while not at ectofuntus
        int maxRetry = 5;
        while (!Areas.ECTOFUNTUS.contains(ctx.players.local().tile())) {
            if (ctx.itemInInventory(Ids.ECTOPHIAL_FULL)) {
                ctx.inventory.select().id(Ids.ECTOPHIAL_FULL).poll().interact(true, Actions.EMPTY);
                ctx.sleep(5000);
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