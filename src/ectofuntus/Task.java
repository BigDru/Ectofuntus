package ectofuntus;

import org.powerbot.script.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 18/11/14
 * Time: 1:58 AM
 * Purpose: Template for tasks.
 */
public abstract class Task<C extends ClientContext> extends ClientAccessor<C> {
    public Task(C ctx) {
        super(ctx);
    }

    public abstract boolean activate();

    public abstract int execute();
}
