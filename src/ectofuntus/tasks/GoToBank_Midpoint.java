package ectofuntus.tasks;

import ectofuntus.*;

import org.powerbot.script.Condition;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 29/11/14 - 6:46 PM
 * Last Modified: 23/02/15 - 3:19 PM
 * Purpose: Go to midpoint once in port.
 */
public class GoToBank_Midpoint extends Task<ClientContext> {

    public GoToBank_Midpoint(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        double threshold = 5;
        double distancePlayerToMid = ctx.players.local().tile().distanceTo(Tiles.MIDWAY_BETWEEN_BANK_AND_BARRIER);
        double distancePlayerToBank = ctx.players.local().tile().distanceTo(Areas.BANK.getCentralTile());
        double distanceMidToBank = Tiles.MIDWAY_BETWEEN_BANK_AND_BARRIER.distanceTo(Areas.BANK.getCentralTile());

        boolean inPort = Areas.PORT_PHASMATYS.contains(ctx.players.local().tile());
        boolean atMid = distancePlayerToMid <= threshold;
        boolean isMidCloserThanBank = distancePlayerToMid < distancePlayerToBank;
        boolean isBankFartherThanDistanceMidToBank = distancePlayerToBank > distanceMidToBank;

        return (inPort && isMidCloserThanBank && !atMid && isBankFartherThanDistanceMidToBank);
    }

    @Override
    public void execute() {
        // Antiban reaction buffer
        Condition.sleep();
        Coeus.getInstance().setCurrentTask("Walking to bank");

        ctx.movement.findPath(Tiles.MIDWAY_BETWEEN_BANK_AND_BARRIER).traverse();

        Condition.wait(new Condition.Check() {
            @Override
            public boolean poll() {
                return ctx.players.local().tile().distanceTo(Tiles.MIDWAY_BETWEEN_BANK_AND_BARRIER) < 3;
            }
        }, 200, 3);
    }
}