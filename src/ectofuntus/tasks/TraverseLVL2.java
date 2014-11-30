package ectofuntus.tasks;

import ectofuntus.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Path;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 24/11/14
 * Time: 1:46 PM
 * Purpose: Traverses Level 2
 */
public class TraverseLVL2 extends Task<ClientContext> {

    public TraverseLVL2(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return Areas.SLIME_LVL_2.contains(ctx.players.local().tile());
    }

    @Override
    public int execute() {
        // Antiban reaction buffer
        Toolbox.sleep(500);
        System.out.println("lvl 2");
        Path path = ctx.movement.findPath(Tiles.LVL2_STAIRS_DOWN);
        int maxRetry = 5;
        do {
            path.traverse();
            Toolbox.sleep(500);
            if (maxRetry <= 0) {
                return -1;
            }
            maxRetry--;
        } while (ctx.players.local().tile().distanceTo(Tiles.LVL2_STAIRS_DOWN) > 3);

        // go down stairs to level 1
        ctx.camera.angle('w');
        ctx.camera.pitch(true);
        ctx.objects.select().id(Ids.STAIRS_TO_SLIME_ABOVE).poll().interact(true, Actions.CLIMB_DOWN);
        Toolbox.sleep(1700);
        System.out.println("done.");
        return 0;
    }
}
