package ectofuntus.tasks;

import ectofuntus.*;
import org.powerbot.script.rt4.ClientContext;
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
        boolean containsEctophial = Toolbox.itemInInventory(ctx, Ids.ECTOPHIAL_FULL);
        boolean hasMaxPots = Toolbox.countItemInInventory(ctx, Ids.POT) == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;
        boolean hasMaxBones = Toolbox.countItemInInventory(ctx, Ids.BONES) == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;
        boolean hasMaxBonemeals = Toolbox.countItemInInventory(ctx, Ids.BONEMEAL) == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;
        boolean hasMaxBuckets = Toolbox.countItemInInventory(ctx, Ids.BUCKET) == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;
        boolean hasMaxBucketsOfSlime = Toolbox.countItemInInventory(ctx, Ids.BUCKET_OF_SLIME) == MiscConstants.MAX_COUNT_FOR_EACH_ITEM;
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
        Toolbox.sleep(500);
        System.out.println("Go to Barrier");
        Path path = ctx.movement.findPath(Areas.NORTH_OF_BARRIER.getCentralTile());
        path.traverse();

        Toolbox.sleep(500);
        System.out.println("Done.");
        return 0;
    }
}