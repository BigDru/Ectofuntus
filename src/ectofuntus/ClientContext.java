package ectofuntus;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 03/12/14 - 6:52 PM
 * Last Modified: 21/02/15 - 12:07 AM
 * Purpose: Adds some useful methods to ClientContext
 */
public class ClientContext extends org.powerbot.script.rt4.ClientContext {

    public ClientContext(org.powerbot.script.rt4.ClientContext arg0) {
        super(arg0);
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
