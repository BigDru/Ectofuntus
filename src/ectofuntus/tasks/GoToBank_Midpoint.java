package ectofuntus.tasks;

import ectofuntus.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Path;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 29/11/14
 * Time: 6:46 PM
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
    public int execute() {
        // Antiban reaction buffer
        Toolbox.sleep(500);
        System.out.println("Go To Midpoint");

        Path path = ctx.movement.findPath(Tiles.MIDWAY_BETWEEN_BANK_AND_BARRIER);
        path.traverse();

        Toolbox.sleep(1000);
        System.out.println("Done.");
        return 0;
    }
}