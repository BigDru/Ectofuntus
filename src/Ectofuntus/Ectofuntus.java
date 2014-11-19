package Ectofuntus;

import Ectofuntus.Tasks.*;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;

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
        taskList.addAll(Arrays.asList(new GoToBank(ctx), new Bank(ctx), new GetBonemeal(ctx), new GetSlimeBuckets(ctx), new Worship(ctx)));
        ctx.camera.pitch(true);
    }

    @Override
    public void poll() {
        for(Task t : taskList){
            // check energy
            if (ctx.movement.energyLevel() == 100){
                if (!ctx.movement.running()){
                    ctx.movement.running(true);
                }
            }
            if (t.activate()){
                t.execute();
            }
        }
    }
}
