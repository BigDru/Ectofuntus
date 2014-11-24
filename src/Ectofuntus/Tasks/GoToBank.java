package ectofuntus.tasks;

import ectofuntus.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Path;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 18/11/14
 * Time: 1:47 PM
 * Purpose: Tavels to Bank.
 */
public class GoToBank extends Task<ClientContext> {

    public GoToBank(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        // more of a when not to activate function.
        // ectophial, 9 bones, 9 pots, 9 buckets
        boolean hasFullInventory;
        // ectophial, 9 bones, 9 pots, 9 bucketsOfSlime
        boolean hasSlimeBuckets;
        // ectophial, 9 Bonemeals, 9 buckets
        boolean hasBonemeals;
        // ectophial, 9 Bonemeals, 9 bucketsOfSlime
        boolean hasWorshipMaterial;

        // what is in inventory?
        boolean containsEctophial = Toolbox.itemInInventory(ctx, Ids.ECTOPHIAL_FULL);
        boolean hasMaxPots = Toolbox.countItemInInventory(ctx, Ids.POT) == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;
        boolean hasMaxBones = Toolbox.countItemInInventory(ctx, Ids.BONES) == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;
        boolean hasMaxBonemeals = Toolbox.countItemInInventory(ctx, Ids.BONEMEAL) == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;
        boolean hasMaxBuckets = Toolbox.countItemInInventory(ctx, Ids.BUCKET) == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;
        boolean hasMaxBucketsOfSlime = Toolbox.countItemInInventory(ctx, Ids.BUCKET_OF_SLIME) == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;
        boolean inBank = ctx.players.local().tile().distanceTo(Tiles.BANK) < 9;

        // check conditions
        hasFullInventory = containsEctophial && hasMaxPots && hasMaxBones && hasMaxBuckets;
        hasSlimeBuckets = containsEctophial && hasMaxPots && hasMaxBones && hasMaxBucketsOfSlime;
        hasBonemeals = containsEctophial && hasMaxBonemeals && hasMaxBuckets;
        hasWorshipMaterial = containsEctophial && hasMaxBonemeals && hasMaxBucketsOfSlime;

        return ((!(hasFullInventory || hasSlimeBuckets || hasBonemeals || hasWorshipMaterial)) && !inBank);
    }

    @Override
    public int execute() {
        System.out.println("GoToBank");
        // if ectophial not in inv.
        if (!Toolbox.itemInInventory(ctx, Ids.ECTOPHIAL_FULL)) {
            // near extofuntus
            if (!ctx.objects.select().id(Ids.ECTOFUNTUS).isEmpty()) {
                if (ctx.objects.select().id(Ids.ECTOFUNTUS).poll().tile().distanceTo(ctx.players.local().tile()) >= 7) {
                    return -1;
                }
                // inside port
            } else if (Tiles.BANK.distanceTo(ctx.players.local().tile()) < 25) {
                // walk to bank
                Path pathToBank = ctx.movement.findPath(Tiles.BANK);
                do {
                    pathToBank.traverse();
                } while (ctx.players.local().tile().distanceTo(Tiles.BANK) > 1);
                return 0;
                // no idea where you are
            } else {
                return -1;
            }
        } else {
            // tele to ectofuntus
            ctx.inventory.select().id(Ids.ECTOPHIAL_FULL).poll().interact(true, Actions.EMPTY);

            // wait to fill up ectophial again
            do {
                Toolbox.sleep(1000);
            } while (Toolbox.itemInInventory(ctx, Ids.ECTOPHIAL_EMPTY));
        }

        // At ectofuntus; Go to bank
        // Walk to Energy Barrier
        Path path = ctx.movement.findPath(Tiles.BARRIER_TO_PORT);
        do {
            path.traverse();
        } while (ctx.players.local().tile().distanceTo(Tiles.BARRIER_TO_PORT) > 1);
        Toolbox.sleep(1000);

        System.out.println("At barrier");
        // go to midway
        do {
            if (ctx.players.local().tile().distanceTo(ctx.objects.select().id(Ids.ENERGY_BARRIER).nearest().poll().tile()) < 5) {
                ctx.objects.select().id(Ids.ENERGY_BARRIER).nearest().poll().interact(true, Actions.PAY_TOLL);
                System.out.println("pay");
                Toolbox.sleep(5000);
                path = ctx.movement.findPath(Tiles.MIDWAY_BETWEEN_BANK_AND_BARRIER);
                path.traverse();
                System.out.println("walking to midpoint");
                Toolbox.sleep(5000);
            }
            path.traverse();
            Toolbox.sleep(500);
        } while (ctx.players.local().tile().distanceTo(Tiles.MIDWAY_BETWEEN_BANK_AND_BARRIER) > 1);

        // go to bank
        System.out.println("Walking to bank");
        do {
            path = ctx.movement.findPath(Tiles.BANK);
            path.traverse();
        } while (ctx.players.local().tile().distanceTo(Tiles.BANK) > 1);

        return 0;
    }
}
