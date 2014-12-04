package ectofuntus.tasks;

import ectofuntus.*;

import org.powerbot.script.rt4.Path;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 27/11/14
 * Time: 10:54 AM
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
    public int execute() {
        // Antiban reaction buffer
        ctx.sleep(500);
        System.out.println("Go to Barrier");
        Path path = ctx.movement.findPath(Areas.NORTH_OF_BARRIER.getCentralTile());
        path.traverse();

        ctx.sleep(500);
        System.out.println("Done.");
        return 0;
    }
}