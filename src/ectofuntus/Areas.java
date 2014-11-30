package ectofuntus;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 23/11/14
 * Time: 10:03 PM
 * Purpose: Keep track of Areas
 */
public class Areas {
    public static final Area ECTOFUNTUS = new Area(new Tile(3651, 3528, 0), new Tile(3668, 3511, 0));
    public static final Area GRINDER = new Area(new Tile(3651, 3528, 1), new Tile(3668, 3511, 1));
    public static final Area BANK = new Area(new Tile(3685, 3472, 0), new Tile(3692, 3465, 0));
    public static final Area SLIME_LVL_3 = new Area(new Tile(3667, 9904, 3), new Tile(3693, 9872, 3));
    public static final Area SLIME_LVL_2 = new Area(new Tile(3670, 9901, 2), new Tile(3690, 9875, 2));
    public static final Area SLIME_LVL_1 = new Area(new Tile(3672, 9901, 1), new Tile(3688, 9877, 1));
    public static final Area POOL_OF_SLIME = new Area(new Tile(3682, 9890, 0), new Tile(3684, 9886, 0));
    public static final Area NORTH_OF_BARRIER = new Area(new Tile(3655, 3512, 0), new Tile(3664, 3508, 0));
    public static final Area PORT_PHASMATYS = new Area(new Tile(3652, 3508, 0), new Tile(3710, 3458, 0));
}