package ectofuntus.tasks;

import ectofuntus.*;
import org.powerbot.script.Condition;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 23/11/14 - 10:54 PM
 * Last Modified: 23/02/15 - 3:21 PM
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
        boolean grinderEmpty = ctx.varpbits.varpbit(Ids.VARPBIT_GRINDER_ID) == Ids.VARPBIT_GRINDER_EMPTY;
        boolean doneGrinder = atGrinder && !hasBones && grinderEmpty;

        // return
        return hasEctophial && (!inKnownAreas || doneBank || doneSlime || doneGrinder);
    }

    @Override
    public void execute() {
        // Antiban reaction buffer
        Condition.sleep();
        Coeus.getInstance().setCurrentTask("Teleporting");

        // while not at ectofuntus
        ctx.inventory.select().id(Ids.ECTOPHIAL_FULL).poll().interact(Actions.EMPTY);
        Condition.wait(new Condition.Check() {
            @Override
            public boolean poll() {
                return Areas.ECTOFUNTUS.contains(ctx.players.local().tile());
            }
        });
    }
}