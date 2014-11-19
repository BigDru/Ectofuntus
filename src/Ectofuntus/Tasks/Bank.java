package Ectofuntus.Tasks;

import Ectofuntus.*;
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
        boolean containsEctophial = Toolbox.itemInInventory(ctx, Ids.Ectophial_Full);
        boolean hasMaxPots = Toolbox.countItemInInventory(ctx, Ids.Pot) == MiscConstants.maxNumPots;
        boolean hasMaxBones = Toolbox.countItemInInventory(ctx, Ids.Bones) == MiscConstants.maxNumBones;
        boolean hasMaxBonemeals = Toolbox.countItemInInventory(ctx, Ids.Bonemeal) == MiscConstants.maxNumPots;
        boolean hasMaxBuckets = Toolbox.countItemInInventory(ctx, Ids.Bucket) == MiscConstants.maxNumBuckets;
        boolean hasMaxBucketsOfSlime = Toolbox.countItemInInventory(ctx, Ids.BucketOfSlime) == MiscConstants.maxNumBuckets;
        boolean inBank = ctx.players.local().tile().distanceTo(Tiles.Bank) < 9;

        // check conditions
        hasFullInventory = containsEctophial && hasMaxPots && hasMaxBones && hasMaxBuckets;
        hasSlimeBuckets = containsEctophial && hasMaxPots && hasMaxBones && hasMaxBucketsOfSlime;
        hasBonemeals = containsEctophial && hasMaxBonemeals && hasMaxBuckets;
        hasWorshipMaterial = containsEctophial && hasMaxBonemeals && hasMaxBucketsOfSlime;

        return ((!(hasFullInventory || hasSlimeBuckets || hasBonemeals || hasWorshipMaterial)) && inBank);
    }

    @Override
    public int execute() {
        System.out.println("Bank");
        // what we have
        int numPots = Toolbox.countItemInInventory(ctx, Ids.Pot);
        int numBuckets = Toolbox.countItemInInventory(ctx, Ids.Bucket);
        int numBones = Toolbox.countItemInInventory(ctx, Ids.Bones);
        // what we need
        boolean hasEctophial = Toolbox.itemInInventory(ctx, Ids.Ectophial_Full);

        // open bank
        while (!ctx.bank.opened()) {
            GameObject bank = ctx.objects.select().id(Ids.BankBooth).nearest().poll();
            if (!bank.inViewport()) {
                ctx.camera.turnTo(bank);
            }
            bank.interact(true, Actions.Bank);
            Toolbox.sleep(1500);
        }

        // deposit junk
        System.out.println("Depositing Junk");
        for (Item i : ctx.inventory.items()) {
            switch (i.id()) {
                case Ids.Ectophial_Full:
                case Ids.Bones:
                case Ids.Bucket:
                case Ids.Pot:
                    break;
                default:
                    // does not belong, deposit
                    ctx.bank.deposit(i.id(), org.powerbot.script.rt4.Bank.Amount.ALL);
                    break;
            }
        }

        // withdraw items
        System.out.println("Withdrawing");
        if (!hasEctophial) {
            ctx.bank.withdraw(Ids.Ectophial_Full, org.powerbot.script.rt4.Bank.Amount.ONE);
            Toolbox.sleep(500);
        }
        if (numPots < MiscConstants.maxNumPots) {
            ctx.bank.withdraw(Ids.Pot, MiscConstants.maxNumPots - numPots);
        } else if (numPots > MiscConstants.maxNumPots) {
            ctx.bank.deposit(Ids.Pot, numPots - MiscConstants.maxNumPots);
        }
        Toolbox.sleep(500);
        if (numBuckets < MiscConstants.maxNumBuckets) {
            ctx.bank.withdraw(Ids.Bucket, MiscConstants.maxNumBuckets - numBuckets);
        } else if (numBuckets > MiscConstants.maxNumBuckets) {
            ctx.bank.deposit(Ids.Pot, numBuckets - MiscConstants.maxNumBuckets);
        }
        Toolbox.sleep(500);
        if (numBones < MiscConstants.maxNumBones) {
            ctx.bank.withdraw(Ids.Bones, MiscConstants.maxNumBones - numBones);
        } else if (numBones > MiscConstants.maxNumBones) {
            ctx.bank.deposit(Ids.Pot, numBones - MiscConstants.maxNumBones);
        }
        Toolbox.sleep(500);
        ctx.bank.close();

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
