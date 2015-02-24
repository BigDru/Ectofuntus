package ectofuntus;

import ectofuntus.tasks.*;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 18/11/14 - 2:34 AM
 * Last Modified: 20/02/15 - 8:18 PM
 * Purpose: Main script.
 */
@Script.Manifest(name = "Master Ectofuntus", description = "Levels up Prayer FAST! Req: lots of bones, ectophial, completion of Ghosts Ahoy", properties = "client=4;")
public class Ectofuntus extends PollingScript<ClientContext> implements KeyListener, PaintListener {
    private List<Task> taskList = new ArrayList<Task>();
    private UI uiThread = new UI();

    @Override
    public void start() {
        super.start();
        uiThread.start();

        taskList.addAll(Arrays.asList(
                new Teleport(ctx),
                new FillEctophial(ctx),
                new GoToGrinder(ctx),
                new Grind_Load(ctx),
                new Grind_Wind(ctx),
                new Grind_Empty(ctx),
                new GoDownTrapdoor(ctx),
                new TraverseLVL3(ctx),
                new TraverseLVL2(ctx),
                new TraverseLVL1(ctx),
                new FillBuckets(ctx),
                new Worship(ctx),
                new GoToBarrier(ctx),
                new PassBarrier(ctx),
                new GoToBank_Midpoint(ctx),
                new GoToBank(ctx),
                new Bank(ctx)));
    }


    @Override
    public void poll() {
        checkGameState();
        ctx.camera.pitch(true);
        for (Task t : taskList) {
            // check energy
            //System.out.println(ctx.movement.energyLevel());
            if (ctx.movement.energyLevel() > 65) {
                if (!ctx.movement.running()) {
                    ctx.movement.running(true);
                }
            }
            if (t.activate()) {
                t.execute();
            }
        }
    }

    private void checkGameState() {
        switch (ctx.game.clientState()){
            case 10:
                Coeus.getInstance().setCurrentTask("Login Screen");
                break;
            case 20:
                Coeus.getInstance().setCurrentTask("Logging in...");
                break;
            case 11:
                Coeus.getInstance().setCurrentTask("Authenticator");
                break;
        }
    }

    @Override
    public void stop() {
        uiThread.finish();
        super.stop();
    }

    @Override
    public void repaint(Graphics graphics) {
        Coeus.getInstance().setTimeRunning(getRuntime());
        uiThread.repaint(graphics, ctx);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        uiThread.handleInput(e);
    }
}
