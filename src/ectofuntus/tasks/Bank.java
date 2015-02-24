package ectofuntus.tasks;

import ectofuntus.*;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Item;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 18/11/14 - 12:36 PM
 * Last Modified: 23/02/15 - 3:19 PM
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
    public void execute() {
        // Antiban reaction buffer
        Condition.sleep();
        Coeus.getInstance().setCurrentTask("Banking");

        // what we have
        int numPotsInInventory = ctx.inventory.select().id(Ids.POT).size();
        int numBucketsInInventory = ctx.inventory.select().id(Ids.BUCKET).size();
        int numBonesInInventory = ctx.inventory.select().id(Ids.BONES).size();
        boolean hasEctophial = ctx.itemInInventory(Ids.ECTOPHIAL_FULL);

        // open bank
        if (!ctx.bank.opened()) {
            GameObject bank = ctx.objects.select().id(Ids.BANK_BOOTH).nearest().poll();
            if (!bank.inViewport()) {
                ctx.camera.turnTo(bank);
            }
            bank.interact(true, Actions.BANK);
            Condition.wait(new Condition.Check() {
                @Override
                public boolean poll() {
                    return ctx.bank.opened();
                }
            });

            // verify opened
            if (!ctx.bank.opened()) {
                return;
            }
        }

        // Change to all items tab
        ctx.widgets.component(Ids.WIDGET_BANK_ALLTAB, Ids.WIDGET_BANK_ALLTAB_COMPONENT).component(Ids.WIDGET_BANK_ALLTAB_SUBCOMPONENT).click(true);

        // deposit junk
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
        // ectophial
        if (!hasEctophial) {
            ctx.bank.withdraw(Ids.ECTOPHIAL_FULL, org.powerbot.script.rt4.Bank.Amount.ONE);
            Condition.sleep(500);
        }

        // pots
        if (numPotsInInventory < MiscConstants.MAX_COUNT_FOR_EACH_ITEM) {
            ctx.bank.withdraw(Ids.POT, MiscConstants.MAX_COUNT_FOR_EACH_ITEM - numPotsInInventory);
        } else if (numPotsInInventory > MiscConstants.MAX_COUNT_FOR_EACH_ITEM) {
            ctx.bank.deposit(Ids.POT, numPotsInInventory - MiscConstants.MAX_COUNT_FOR_EACH_ITEM);
        }
        Condition.sleep(500);

        // buckets
        if (numBucketsInInventory < MiscConstants.MAX_COUNT_FOR_EACH_ITEM) {
            ctx.bank.withdraw(Ids.BUCKET, MiscConstants.MAX_COUNT_FOR_EACH_ITEM - numBucketsInInventory);
        } else if (numBucketsInInventory > MiscConstants.MAX_COUNT_FOR_EACH_ITEM) {
            ctx.bank.deposit(Ids.BUCKET, numBucketsInInventory - MiscConstants.MAX_COUNT_FOR_EACH_ITEM);
        }
        Condition.sleep(500);

        // bones
        if (numBonesInInventory < MiscConstants.MAX_COUNT_FOR_EACH_ITEM) {
            ctx.bank.withdraw(Ids.BONES, MiscConstants.MAX_COUNT_FOR_EACH_ITEM - numBonesInInventory);
        } else if (numBonesInInventory > MiscConstants.MAX_COUNT_FOR_EACH_ITEM) {
            ctx.bank.deposit(Ids.BONES, numBonesInInventory - MiscConstants.MAX_COUNT_FOR_EACH_ITEM);
        }
        Condition.sleep(500);

        ctx.bank.close();
    }
}
