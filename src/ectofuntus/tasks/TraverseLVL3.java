package ectofuntus.tasks;

import ectofuntus.*;

import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.Path;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 24/11/14
 * Time: 12:19 AM
 * Purpose: Traverses Level 3
 */
public class TraverseLVL3 extends Task<ClientContext> {

    public TraverseLVL3(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return Areas.SLIME_LVL_3.contains(ctx.players.local().tile());
    }

    @Override
    public int execute() {
        // Antiban reaction buffer
        ctx.sleep(500);
        System.out.println("lvl 3");
        // are we lvl 58+ in agility?
        if (ctx.skills.level(Constants.SKILLS_AGILITY) >= MiscConstants.WEATHERED_WALL_AGILITY_LEVEL_REQ) {
            // yes
            ctx.camera.angle('w');
            ctx.camera.pitch(true);
            ctx.sleep(500);
            ctx.objects.select().id(Ids.WEATHERED_WALL_ABOVE).poll().interact(true, Actions.JUMP_DOWN);
        } else {
            // no
            // walk to stairs level 3

            Path path = ctx.movement.findPath(Tiles.LVL3_STAIRS);
            int maxRetry = 5;
            do {
                path.traverse();
                ctx.sleep(500);
                if (maxRetry <= 0) {
                    return -1;
                }
                maxRetry--;
            } while (ctx.players.local().tile().distanceTo(Tiles.LVL3_STAIRS) > 3);

            // go down stairs to level 2
            ctx.camera.angle('e');
            ctx.camera.pitch(true);
            ctx.objects.select().id(Ids.STAIRS_TO_SLIME_ABOVE).poll().interact(true, Actions.CLIMB_DOWN);
        }
        ctx.sleep(1700);
        System.out.println("done.");
        return 0;
    }
}
