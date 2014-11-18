package Ectofuntus;

import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 18/11/14
 * Time: 2:34 AM
 * Purpose: Main script.
 */
@Script.Manifest(name = "Master Ectofuntus", description = "Requires completion of Ghosts Ahoy. Requires Ectophial, 9 buckets, 9 pots, and lots of bones.")
public class Ectofuntus extends PollingScript<ClientContext> {
    private List<Task> taskList = new ArrayList<Task>();

    @Override
    public void start() {
        super.start();
        // add tasks
    }

    @Override
    public void poll() {
        // check energy
    }
}
