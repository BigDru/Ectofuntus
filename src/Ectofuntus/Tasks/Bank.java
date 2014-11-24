package ectofuntus.tasks;

import ectofuntus.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Item;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 18/11/14
 * Time: 12:36 PM
 * Purpose: Bank.
 */
public class Bank extends Task<ClientContext> {

    public Bank(ClientContext ctx) {
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
        boolean hasMaxBonemeal = Toolbox.countItemInInventory(ctx, Ids.BONEMEAL) == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;
        boolean hasMaxBuckets = Toolbox.countItemInInventory(ctx, Ids.BUCKET) == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;
        boolean hasMaxBucketsOfSlime = Toolbox.countItemInInventory(ctx, Ids.BUCKET_OF_SLIME) == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;
        boolean inBank = ctx.players.local().tile().distanceTo(Tiles.BANK) < 9;

        // check conditions
        hasFullInventory = containsEctophial && hasMaxPots && hasMaxBones && hasMaxBuckets;
        hasSlimeBuckets = containsEctophial && hasMaxPots && hasMaxBones && hasMaxBucketsOfSlime;
        hasBonemeals = containsEctophial && hasMaxBonemeal && hasMaxBuckets;
        hasWorshipMaterial = containsEctophial && hasMaxBonemeal && hasMaxBucketsOfSlime;

        return ((!(hasFullInventory || hasSlimeBuckets || hasBonemeals || hasWorshipMaterial)) && inBank);
    }

    @Override
    public int execute() {
        System.out.println("Bank");
        // what we have
        int numPotsInInventory = Toolbox.countItemInInventory(ctx, Ids.POT);
        int numBucketsInInventory = Toolbox.countItemInInventory(ctx, Ids.BUCKET);
        int numBonesInInventory = Toolbox.countItemInInventory(ctx, Ids.BONES);
        boolean hasEctophial = Toolbox.itemInInventory(ctx, Ids.ECTOPHIAL_FULL);

        // open bank
        while (!ctx.bank.opened()) {
            GameObject bank = ctx.objects.select().id(Ids.BANK_BOOTH).nearest().poll();
            if (!bank.inViewport()) {
                ctx.camera.turnTo(bank);
            }
            bank.interact(true, Actions.BANK);
            Toolbox.sleep(1500);
        }

        // deposit junk
        System.out.println("Depositing Junk");
        for (Item i : ctx.inventory.items()) {
            switch (i.id()) {
                case Ids.ECTOPHIAL_FULL:
                case Ids.BONES:
                case Ids.BUCKET:
                case Ids.POT:
                    break;
                default:
                    // does not belong, deposit
                    ctx.bank.deposit(i.id(), org.powerbot.script.rt4.Bank.Amount.ALL);
                    break;
            }
        }

        // withdraw items
        System.out.println("Withdrawing");
        // ectophial
        if (!hasEctophial) {
            ctx.bank.withdraw(Ids.ECTOPHIAL_FULL, org.powerbot.script.rt4.Bank.Amount.ONE);
            Toolbox.sleep(500);
        }

        // pots
        if (numPotsInInventory < MiscConstants.MAX_COUNT_FOR_EACH_ITEM) {
            ctx.bank.withdraw(Ids.POT, MiscConstants.MAX_COUNT_FOR_EACH_ITEM - numPotsInInventory);
        } else if (numPotsInInventory > MiscConstants.MAX_COUNT_FOR_EACH_ITEM) {
            ctx.bank.deposit(Ids.POT, numPotsInInventory - MiscConstants.MAX_COUNT_FOR_EACH_ITEM);
        }
        Toolbox.sleep(500);

        // buckets
        if (numBucketsInInventory < MiscConstants.MAX_COUNT_FOR_EACH_ITEM) {
            ctx.bank.withdraw(Ids.BUCKET, MiscConstants.MAX_COUNT_FOR_EACH_ITEM - numBucketsInInventory);
        } else if (numBucketsInInventory > MiscConstants.MAX_COUNT_FOR_EACH_ITEM) {
            ctx.bank.deposit(Ids.BUCKET, numBucketsInInventory - MiscConstants.MAX_COUNT_FOR_EACH_ITEM);
        }
        Toolbox.sleep(500);

        // bones
        if (numBonesInInventory < MiscConstants.MAX_COUNT_FOR_EACH_ITEM) {
            ctx.bank.withdraw(Ids.BONES, MiscConstants.MAX_COUNT_FOR_EACH_ITEM - numBonesInInventory);
        } else if (numBonesInInventory > MiscConstants.MAX_COUNT_FOR_EACH_ITEM) {
            ctx.bank.deposit(Ids.BONES, numBonesInInventory - MiscConstants.MAX_COUNT_FOR_EACH_ITEM);
        }
        Toolbox.sleep(500);

        // done. Close & go back to ectofuntus
        ctx.bank.close();
        Toolbox.sleep(500);
        ctx.inventory.select().id(Ids.ECTOPHIAL_FULL).poll().interact(true, Actions.EMPTY);

        // wait to fill up ectophial again
        do {
            Toolbox.sleep(1000);
        } while (Toolbox.itemInInventory(ctx, Ids.ECTOPHIAL_EMPTY));

        return 0;
    }
}
