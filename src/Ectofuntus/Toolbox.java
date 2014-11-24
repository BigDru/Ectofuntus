package ectofuntus;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 18/11/14
 * Time: 2:24 AM
 * Purpose: Provides useful static functions.
 */
public class Toolbox {
    public static boolean isPlayerIdle(ClientContext ctx) {
        return ctx.players.local().animation() == -1;
    }

    public static void sleep(int x) {
        Random r = new Random();
        int sign = r.nextInt(2);
        if (sign == 0)
            sign = -1;
        else
            sign = 1;

        int randomOffset = r.nextInt((int) (x*0.1));

        try {
            Thread.sleep(x + (sign*randomOffset));
        } catch (InterruptedException e) {
        }
    }

    public static boolean isInventoryFull(ClientContext ctx){
        return ctx.inventory.size() == MiscConstants.INVENTORY_SIZE;
    }

    public static boolean itemInInventory(ClientContext ctx, int id) {
        for (Item i : ctx.inventory.items()) {
            if (i.id() == id)
                return true;
        }
        return false;
    }

    public static int countItemInInventory(ClientContext ctx, int id){
        int count = 0;
        for (Item i : ctx.inventory.items()){
            if (i.id() == id)
                count++;
        }
        return count;
    }
}
