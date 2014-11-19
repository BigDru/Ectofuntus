package Ectofuntus.Tasks;

import Ectofuntus.*;
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

        return ((!(hasFullInventory || hasSlimeBuckets || hasBonemeals || hasWorshipMaterial)) && !inBank);
    }

    @Override
    public int execute() {
        System.out.println("GoToBank");
        // if ectophial not in inv.
        if (!Toolbox.itemInInventory(ctx, Ids.Ectophial_Full)) {
            // near extofuntus
            if (!ctx.objects.select().id(Ids.Ectofuntus).isEmpty()) {
                if (ctx.objects.select().id(Ids.Ectofuntus).poll().tile().distanceTo(ctx.players.local().tile()) >= 7) {
                    return -1;
                }
                // inside port
            } else if (Tiles.Bank.distanceTo(ctx.players.local().tile()) < 25) {
                // walk to bank
                Path pathToBank = ctx.movement.findPath(Tiles.Bank);
                do {
                    pathToBank.traverse();
                } while (ctx.players.local().tile().distanceTo(Tiles.Bank) > 1);
                return 0;
                // no idea where you are
            } else {
                return -1;
            }
        } else {
            // tele to ectofuntus
            ctx.inventory.select().id(Ids.Ectophial_Full).poll().interact(true, Actions.Empty);

            // wait to fill up ectophial again
            do {
                Toolbox.sleep(1000);
            } while (Toolbox.itemInInventory(ctx, Ids.Ectophial_Empty));
        }

        // At ectofuntus; Go to bank
        // Walk to Energy Barrier
        Path path = ctx.movement.findPath(Tiles.Barrier_ToPort);
        do {
            path.traverse();
        } while (ctx.players.local().tile().distanceTo(Tiles.Barrier_ToPort) > 1);
        Toolbox.sleep(1000);

        System.out.println("At barrier");
        // go to midway
        do {
            if (ctx.players.local().tile().distanceTo(ctx.objects.select().id(Ids.EnergyBarrier).nearest().poll().tile()) < 5) {
                ctx.objects.select().id(Ids.EnergyBarrier).nearest().poll().interact(true, Actions.PayToll);
                System.out.println("pay");
                Toolbox.sleep(5000);
                path = ctx.movement.findPath(Tiles.Midway_Bank_Barrier);
                path.traverse();
                System.out.println("walking to midpoint");
                Toolbox.sleep(5000);
            }
            path.traverse();
            Toolbox.sleep(500);
        } while (ctx.players.local().tile().distanceTo(Tiles.Midway_Bank_Barrier) > 1);

        // go to bank
        System.out.println("Walking to bank");
        path = ctx.movement.findPath(Tiles.Bank);
        do {
            path.traverse();
        } while (ctx.players.local().tile().distanceTo(Tiles.Bank) > 1);

        return 0;
    }
}
