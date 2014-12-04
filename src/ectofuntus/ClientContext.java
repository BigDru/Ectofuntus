package ectofuntus;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 03/12/14
 * Time: 6:52 PM
 * Purpose: Adds some useful methods to ClientContext
 */
public class ClientContext extends org.powerbot.script.rt4.ClientContext {

    public ClientContext(org.powerbot.script.rt4.ClientContext arg0) {
        super(arg0);
    }

    public void sleep(int x) {
        Random r = new Random();
        int sign = r.nextInt(2);
        if (sign == 0)
            sign = -1;
        else
            sign = 1;

        int randomOffset = r.nextInt((int) (x * 0.1));

        try {
            Thread.sleep(x + (sign * randomOffset));
        } catch (InterruptedException e) {
        }
    }

    public boolean isInventoryFull() {
        return inventory.size() == MiscConstants.INVENTORY_SIZE;
    }

    public boolean itemInInventory(int id) {
        return !inventory.select().id(id).isEmpty();
    }

    public boolean isPlayerIdle(){
        return players.local().animation() == -1;
    }
}
