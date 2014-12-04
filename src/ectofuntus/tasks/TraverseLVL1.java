package ectofuntus.tasks;

import ectofuntus.*;

import org.powerbot.script.rt4.Path;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 24/11/14
 * Time: 1:48 PM
 * Purpose: Traverses Level 1
 */
public class TraverseLVL1 extends Task<ClientContext> {

    public TraverseLVL1(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return Areas.SLIME_LVL_1.contains(ctx.players.local().tile());
    }

    @Override
    public int execute() {
        // Antiban reaction buffer
        ctx.sleep(500);
        System.out.println("lvl 1");
        Path path = ctx.movement.findPath(Tiles.LVL1_STAIRS_DOWN);
        int maxRetry = 5;
        do {
            path.traverse();
            ctx.sleep(500);
            if (maxRetry <= 0) {
                return -1;
            }
            maxRetry--;
        } while (ctx.players.local().tile().distanceTo(Tiles.LVL1_STAIRS_DOWN) > 3);

        // go down stairs to level 1
        ctx.camera.angle('e');
        ctx.camera.pitch(true);
        ctx.objects.select().id(Ids.STAIRS_TO_SLIME_ABOVE).poll().interact(true, Actions.CLIMB_DOWN);
        ctx.sleep(1700);
        System.out.println("done.");
        return 0;
    }
}
