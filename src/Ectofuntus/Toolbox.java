package Ectofuntus;

import org.powerbot.script.rt4.ClientContext;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 18/11/14
 * Time: 2:24 AM
 * Purpose: Provides useful static functions.
 */
public class Toolbox {
    public static boolean isPlayerIdle(ClientContext ctx){
        return ctx.players.local().animation()==-1;
    }

    public static void sleep(int x){
        try {
            Thread.sleep(x);
        } catch (InterruptedException e) {
        }
    }
}
