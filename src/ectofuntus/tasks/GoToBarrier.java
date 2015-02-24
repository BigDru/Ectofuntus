package ectofuntus.tasks;

import ectofuntus.*;

import org.powerbot.script.Condition;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 27/11/14 - 10:54 AM
 * Last Modified: 23/02/15 - 3:21 PM
 * Purpose: Start journey to bank
 */
public class GoToBarrier extends Task<ClientContext> {

    public GoToBarrier(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        // more of a when not to activate function.
        // ectophial, 9 bones, 9 pots, 9 buckets
        boolean hasFreshInventory;
        // ectophial, 9 bones, 9 pots, 9 bucketsOfSlime
        boolean hasSlimeBuckets;
        // ectophial, 9 Bonemeals, 9 buckets
        boolean hasBonemeals;
        // ectophial, 9 Bonemeals, 9 bucketsOfSlime
        boolean hasWorshipMaterial;

        // what is in inventory?
        boolean containsEctophial = ctx.itemInInventory(Ids.ECTOPHIAL_FULL);
        boolean hasMaxPots = ctx.inventory.select().id(Ids.POT).size() == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;
        boolean hasMaxBones = ctx.inventory.select().id(Ids.BONES).size() == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;
        boolean hasMaxBonemeals = ctx.inventory.select().id(Ids.BONEMEAL).size() == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;
        boolean hasMaxBuckets = ctx.inventory.select().id(Ids.BUCKET).size() == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;
        boolean hasMaxBucketsOfSlime = ctx.inventory.select().id(Ids.BUCKET_OF_SLIME).size() == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;
        boolean atEctofuntus = Areas.ECTOFUNTUS.contains(ctx.players.local().tile());

        // check conditions
        hasFreshInventory = containsEctophial && hasMaxPots && hasMaxBones && hasMaxBuckets;
        hasSlimeBuckets = containsEctophial && hasMaxPots && hasMaxBones && hasMaxBucketsOfSlime;
        hasBonemeals = containsEctophial && hasMaxBonemeals && hasMaxBuckets;
        hasWorshipMaterial = containsEctophial && hasMaxBonemeals && hasMaxBucketsOfSlime;

        return ((!(hasFreshInventory || hasSlimeBuckets || hasBonemeals || hasWorshipMaterial)) && atEctofuntus);
    }

    @Override
    public void execute() {
        // Antiban reaction buffer
        Condition.sleep();
        Coeus.getInstance().setCurrentTask("Walking to bank");

        ctx.movement.findPath(Areas.NORTH_OF_BARRIER.getCentralTile()).traverse();

        // wait to get to barrier
        Condition.wait(new Condition.Check() {
            @Override
            public boolean poll() {
                return Areas.NORTH_OF_BARRIER.contains(ctx.players.local().tile());
            }
        });
    }
}