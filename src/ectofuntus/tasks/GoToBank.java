package ectofuntus.tasks;

import ectofuntus.*;

import org.powerbot.script.rt4.Path;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 18/11/14
 * Time: 1:47 PM
 * Purpose: Go to Bank while in port. (From midpoint)
 */
public class GoToBank extends Task<ClientContext> {

    public GoToBank(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        double threshold = 5;
        double distancePlayerToMid = ctx.players.local().tile().distanceTo(Tiles.MIDWAY_BETWEEN_BANK_AND_BARRIER);
        double distancePlayerToBank = ctx.players.local().tile().distanceTo(Areas.BANK.getCentralTile());
        double distanceMidToBank = Tiles.MIDWAY_BETWEEN_BANK_AND_BARRIER.distanceTo(Areas.BANK.getCentralTile());

        boolean inPort = Areas.PORT_PHASMATYS.contains(ctx.players.local().tile());
        boolean atBank = Areas.BANK.contains(ctx.players.local().tile());
        boolean atMid = distancePlayerToMid <= threshold;
        boolean isBankCloserThanMid = distancePlayerToBank <= distancePlayerToMid;
        boolean isBankCloserThanDistanceMidToBank = distancePlayerToBank <= distanceMidToBank;

        return (inPort && !atBank && (atMid || isBankCloserThanMid || isBankCloserThanDistanceMidToBank));
    }

    @Override
    public int execute() {
        // Antiban reaction buffer
        ctx.sleep(500);
        System.out.println("Go To Bank");

        Path path = ctx.movement.findPath(Areas.BANK.getCentralTile());
        path.traverse();

        ctx.sleep(1000);
        System.out.println("Done.");
        return 0;
    }
}