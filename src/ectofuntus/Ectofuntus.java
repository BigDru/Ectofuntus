package ectofuntus;

import ectofuntus.tasks.*;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 18/11/14
 * Time: 2:34 AM
 * Purpose: Main script.
 */
@Script.Manifest(name = "Master Ectofuntus", description = "Levels up Prayer FAST! Req: lots of bones, ectophial, completion of Ghosts Ahoy.")
public class Ectofuntus extends PollingScript<ClientContext> {
    private List<Task> taskList = new ArrayList<Task>();

    @Override
    public void start() {
        super.start();
        taskList.addAll(Arrays.asList(new Teleport(ctx),
                new FillEctophial(ctx),
                new GoToGrinder(ctx),
                new Grind(ctx),
                new GoDownTrapdoor(ctx),
                new TraverseLVL3(ctx),
                new TraverseLVL2(ctx),
                new TraverseLVL1(ctx),
                new FillBuckets(ctx),
                new GoToBarrier(ctx),
                new PassBarrier(ctx),
                new GoToBank_Midpoint(ctx),
                new GoToBank(ctx),
                new Bank(ctx),
                new Worship(ctx)));
    }

    @Override
    public void poll() {
        ctx.camera.pitch(true);
        for (Task t : taskList) {
            // check energy
            System.out.println(ctx.movement.energyLevel());
            if (ctx.movement.energyLevel() > 65) {
                if (!ctx.movement.running()) {
                    ctx.movement.running(true);
                }
            }
            if (t.activate()) {
                int r = t.execute();
                // if failed -2, stop script
                if (r == -2) {
                    ctx.controller.stop();
                }
            }
        }
    }
}
