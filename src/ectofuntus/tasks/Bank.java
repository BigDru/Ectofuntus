package ectofuntus.tasks;

import ectofuntus.*;

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
        // what is in inventory?
        boolean hasMaxPots = ctx.inventory.select().id(Ids.POT).size() == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;
        boolean hasMaxBones = ctx.inventory.select().id(Ids.BONES).size() == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;
        boolean hasMaxBuckets = ctx.inventory.select().id(Ids.BUCKET).size() == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;
        boolean atBank = Areas.BANK.contains(ctx.players.local().tile());

        return (atBank && (!hasMaxPots || !hasMaxBones || !hasMaxBuckets));
    }

    @Override
    public int execute() {
        System.out.println("Bank");
        // what we have
        int numPotsInInventory = ctx.inventory.select().id(Ids.POT).size();
        int numBucketsInInventory = ctx.inventory.select().id(Ids.BUCKET).size();
        int numBonesInInventory = ctx.inventory.select().id(Ids.BONES).size();
        boolean hasEctophial = ctx.itemInInventory(Ids.ECTOPHIAL_FULL);

        // open bank
        int maxRetry = 7;
        while (!ctx.bank.opened()) {
            GameObject bank = ctx.objects.select().id(Ids.BANK_BOOTH).nearest().poll();
            if (!bank.inViewport()) {
                ctx.camera.turnTo(bank);
            }
            bank.interact(true, Actions.BANK);
            ctx.sleep(1500);

            maxRetry--;
            if (maxRetry < 0){
                return -2;
            }
        }

        // deposit junk
        System.out.println(" - Depositing Junk");
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
        System.out.println(" - Withdrawing");
        // ectophial
        if (!hasEctophial) {
            ctx.bank.withdraw(Ids.ECTOPHIAL_FULL, org.powerbot.script.rt4.Bank.Amount.ONE);
            ctx.sleep(500);
        }

        // pots
        if (numPotsInInventory < MiscConstants.MAX_COUNT_FOR_EACH_ITEM) {
            ctx.bank.withdraw(Ids.POT, MiscConstants.MAX_COUNT_FOR_EACH_ITEM - numPotsInInventory);
        } else if (numPotsInInventory > MiscConstants.MAX_COUNT_FOR_EACH_ITEM) {
            ctx.bank.deposit(Ids.POT, numPotsInInventory - MiscConstants.MAX_COUNT_FOR_EACH_ITEM);
        }
        ctx.sleep(500);

        // buckets
        if (numBucketsInInventory < MiscConstants.MAX_COUNT_FOR_EACH_ITEM) {
            ctx.bank.withdraw(Ids.BUCKET, MiscConstants.MAX_COUNT_FOR_EACH_ITEM - numBucketsInInventory);
        } else if (numBucketsInInventory > MiscConstants.MAX_COUNT_FOR_EACH_ITEM) {
            ctx.bank.deposit(Ids.BUCKET, numBucketsInInventory - MiscConstants.MAX_COUNT_FOR_EACH_ITEM);
        }
        ctx.sleep(500);

        // bones
        if (numBonesInInventory < MiscConstants.MAX_COUNT_FOR_EACH_ITEM) {
            ctx.bank.withdraw(Ids.BONES, MiscConstants.MAX_COUNT_FOR_EACH_ITEM - numBonesInInventory);
        } else if (numBonesInInventory > MiscConstants.MAX_COUNT_FOR_EACH_ITEM) {
            ctx.bank.deposit(Ids.BONES, numBonesInInventory - MiscConstants.MAX_COUNT_FOR_EACH_ITEM);
        }
        ctx.sleep(500);

        // done. Close & go back to ectofuntus
        ctx.bank.close();
        ctx.sleep(500);
        System.out.println("Done.");

        return 0;
    }
}
