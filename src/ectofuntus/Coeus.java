package ectofuntus;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 20/02/15 - 11:43 PM
 * Last Modified: 23/02/15 - 10:55 AM
 * Purpose: Coeus - keeper of knowledge. This class stores the data used in this script.
 */
public class Coeus {
    private static Coeus instance = null;
    private int numEctotokens;
    private long timeRunning;
    private int prayerExperienceStart;
    private int prayerExperienceGained;
    private boolean startExperienceSet;

    private String currentTask;

    private Coeus() {
        numEctotokens = 0;
        timeRunning = 0;
        prayerExperienceGained = 0;
        startExperienceSet = false;
        currentTask = "---";
    }

    public static Coeus getInstance() {
        if (instance == null) {
            instance = new Coeus();
        }
        return instance;
    }

    public synchronized int getNumEctotokens() {
        return numEctotokens;
    }

    public synchronized void addToNumEctotokens(int num) {
        numEctotokens += num;
    }

    public synchronized void setTimeRunning(long time) {
        timeRunning = time;
    }

    public synchronized long getTimeRunning() {
        return timeRunning;
    }

    public synchronized void setCurrentTask(String task) {
        currentTask = task;
    }

    public synchronized String getCurrentTask() {
        return currentTask;
    }

    public synchronized void updateExperience(int xp) {
        if (!startExperienceSet) {
            prayerExperienceStart = xp;
            startExperienceSet = true;
        } else {
            prayerExperienceGained = xp - prayerExperienceStart;
        }
    }

    public synchronized int getPrayerExperienceGained() {
        return prayerExperienceGained;
    }
}
