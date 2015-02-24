package ectofuntus.tasks;

import ectofuntus.*;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 24/11/14 - 12:19 AM
 * Last Modified: 23/02/15 - 3:21 PM
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
    public void execute() {
        // Antiban reaction buffer
        Condition.sleep();
        Coeus.getInstance().setCurrentTask("Traversing lvl 3");

        // camera
        ctx.camera.angle('e');
        ctx.camera.pitch(true);

        // are we lvl 58+ in agility?
        if (ctx.skills.level(Constants.SKILLS_AGILITY) >= MiscConstants.WEATHERED_WALL_AGILITY_LEVEL_REQ) {
            // yes
            ctx.objects.select().id(Ids.WEATHERED_WALL_ABOVE).poll().interact(Actions.JUMP_DOWN);
        } else {
            // no
            // walk to stairs level 3
            ctx.movement.findPath(Tiles.LVL3_STAIRS).traverse();
            Condition.wait(new Condition.Check() {
                @Override
                public boolean poll() {
                    return ctx.players.local().tile().distanceTo(Tiles.LVL3_STAIRS) < 3;
                }
            }, 100, 3);

            // go down stairs to level 2
            if (ctx.players.local().tile().distanceTo(Tiles.LVL3_STAIRS) >= 3)
                return;
            ctx.objects.select().id(Ids.STAIRS_TO_SLIME_ABOVE).poll().interact(Actions.CLIMB_DOWN);
        }

        // wait 1 sec for lvl 2 to load
        Condition.wait(new Condition.Check() {
            @Override
            public boolean poll() {
                return ctx.players.local().tile().floor() == Tiles.LVL2_STAIRS_DOWN.floor();
            }
        }, 100, 10);
    }
}
