package ectofuntus;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Dru
 * Date: 21/02/15 - 12:45 AM
 * Last Modified: 21/02/15 - 2:45 PM
 * Purpose: Displays important information onscreen.
 */
public class UI extends Thread {
    private boolean show;
    private boolean stayAlive;
    private int screenWidth;

    // Items
    private final int UI_ITEM_HEIGHT = 20;
    private final int UI_ITEM_COUNT = 5;
    private final int UI_ITEM_LEFTRIGHT_SPACING = 5;
    private final int UI_ITEM_FONTSIZE = 11;
    private int uiSelectedItem = -9;
    private int[] uiSelectableItems = {};

    // General UI
    private final int UI_TITLE_FONTSIZE = 12;
    private final int UI_TITLE_POSITION = 17;
    private final int UI_AUTHOR_FONTSIZE = 11;
    private final int UI_AUTHOR_POSITION = 33;
    private final int UI_TITLE_BACKGROUND_HEIGHT = 40;
    private final int UI_BODY_BACKGROUND_HEIGHT = UI_ITEM_HEIGHT * UI_ITEM_COUNT;
    private final int UI_FOOTER_BACKGROUND_HEIGHT = 20;
    private final int UI_BACKGROUND_SPACE_SIZE = 2;
    private final int UI_WIDTH = 190;
    private final int UI_ARCSIZE = 10;
    private final int UI_RIGHTOFFSET = 2;
    private final int UI_TOPOFFSET = 2;
    private final int UI_HIGHLIGHT_HEIGHT = UI_ITEM_HEIGHT - 2;
    private final int UI_HIGHLIGHT_WIDTH = UI_WIDTH - 2;


    public UI() {
        show = true;
        stayAlive = true;
        screenWidth = 0;
    }

    public void run() {
        while (stayAlive) {
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
            }
        }
    }

    public void repaint(Graphics graphics, ClientContext ctx) {
        Graphics2D g = (Graphics2D) graphics;
        screenWidth = ctx.game.dimensions().width;

        // Title
        g.setColor(new Color(25, 25, 70, 200));
        g.fillRoundRect(screenWidth - UI_WIDTH - UI_RIGHTOFFSET,
                UI_TOPOFFSET,
                UI_WIDTH,
                UI_TITLE_BACKGROUND_HEIGHT,
                UI_ARCSIZE,
                UI_ARCSIZE);
        g.setColor(new Color(15, 15, 35, 225));
        g.drawRoundRect(screenWidth - UI_WIDTH - UI_RIGHTOFFSET,
                UI_TOPOFFSET,
                UI_WIDTH,
                UI_TITLE_BACKGROUND_HEIGHT,
                UI_ARCSIZE,
                UI_ARCSIZE);

        g.setColor(Color.GREEN);
        Font font = new Font("Times New Roman", Font.PLAIN, UI_TITLE_FONTSIZE);
        FontRenderContext frc = g.getFontRenderContext();
        TextLayout layout = new TextLayout("Master Ectofuntus", font, frc);
        layout.draw(g,
                screenWidth - UI_WIDTH + (UI_WIDTH - layout.getBounds().getBounds().width) / 2 - UI_RIGHTOFFSET,
                UI_TITLE_POSITION + UI_TOPOFFSET);

        g.setColor(Color.GREEN);
        font = new Font("Times New Roman", Font.PLAIN, UI_AUTHOR_FONTSIZE);
        frc = g.getFontRenderContext();
        layout = new TextLayout("- DaDru", font, frc);
        layout.draw(g,
                screenWidth - UI_WIDTH + (UI_WIDTH - layout.getBounds().getBounds().width) / 2 - UI_RIGHTOFFSET,
                UI_AUTHOR_POSITION + UI_TOPOFFSET);


        // Body
        if (show) {
            g.setColor(new Color(25, 25, 70, 200));
            g.fillRoundRect(screenWidth - UI_WIDTH - UI_RIGHTOFFSET,
                    UI_TOPOFFSET + UI_TITLE_BACKGROUND_HEIGHT + UI_BACKGROUND_SPACE_SIZE,
                    UI_WIDTH,
                    UI_BODY_BACKGROUND_HEIGHT,
                    UI_ARCSIZE,
                    UI_ARCSIZE);
            g.setColor(new Color(15, 15, 35, 225));
            g.drawRoundRect(screenWidth - UI_WIDTH - UI_RIGHTOFFSET,
                    UI_TOPOFFSET + UI_TITLE_BACKGROUND_HEIGHT + UI_BACKGROUND_SPACE_SIZE,
                    UI_WIDTH,
                    UI_BODY_BACKGROUND_HEIGHT,
                    UI_ARCSIZE,
                    UI_ARCSIZE);

            // Highlight item
            if (uiSelectedItem != -9) {
                g.setColor(new Color(175, 215, 20, 150));
                g.fillRoundRect(screenWidth - UI_WIDTH + (UI_WIDTH - UI_HIGHLIGHT_WIDTH) / 2 - UI_RIGHTOFFSET,
                        UI_TOPOFFSET + UI_TITLE_BACKGROUND_HEIGHT + UI_BACKGROUND_SPACE_SIZE + (uiSelectedItem * UI_ITEM_HEIGHT) + (UI_ITEM_HEIGHT - UI_HIGHLIGHT_HEIGHT) / 2,
                        UI_HIGHLIGHT_WIDTH,
                        UI_HIGHLIGHT_HEIGHT,
                        UI_ARCSIZE,
                        UI_ARCSIZE);
            }

            // Populate body
            // #0 - Current Task
            int itemLevel = 0;
            g.setColor(Color.GREEN);
            font = new Font("Times New Roman", Font.PLAIN, UI_ITEM_FONTSIZE);
            frc = g.getFontRenderContext();
            layout = new TextLayout("Current Task:", font, frc);
            layout.draw(g, screenWidth - UI_WIDTH - UI_RIGHTOFFSET + UI_ITEM_LEFTRIGHT_SPACING,
                    UI_TOPOFFSET + UI_TITLE_BACKGROUND_HEIGHT + UI_BACKGROUND_SPACE_SIZE + UI_ITEM_HEIGHT * itemLevel +
                            (UI_ITEM_HEIGHT - layout.getBounds().getBounds().height) / 2 + layout.getBounds().getBounds().height);

            font = new Font("Times New Roman", Font.PLAIN, UI_ITEM_FONTSIZE);
            frc = g.getFontRenderContext();
            layout = new TextLayout(Coeus.getInstance().getCurrentTask(), font, frc);
            layout.draw(g, screenWidth - UI_RIGHTOFFSET - UI_ITEM_LEFTRIGHT_SPACING - layout.getBounds().getBounds().width,
                    UI_TOPOFFSET + UI_TITLE_BACKGROUND_HEIGHT + UI_BACKGROUND_SPACE_SIZE + UI_ITEM_HEIGHT * itemLevel +
                            (UI_ITEM_HEIGHT - layout.getBounds().getBounds().height) / 2 + layout.getBounds().getBounds().height);

            // #1 - Time since start
            itemLevel = 1;
            g.setColor(Color.GREEN);
            font = new Font("Times New Roman", Font.PLAIN, UI_ITEM_FONTSIZE);
            frc = g.getFontRenderContext();
            layout = new TextLayout("Running For:", font, frc);
            layout.draw(g, screenWidth - UI_WIDTH - UI_RIGHTOFFSET + UI_ITEM_LEFTRIGHT_SPACING,
                    UI_TOPOFFSET + UI_TITLE_BACKGROUND_HEIGHT + UI_BACKGROUND_SPACE_SIZE + UI_ITEM_HEIGHT * itemLevel +
                            (UI_ITEM_HEIGHT - layout.getBounds().getBounds().height) / 2 + layout.getBounds().getBounds().height);

            font = new Font("Times New Roman", Font.PLAIN, UI_ITEM_FONTSIZE);
            frc = g.getFontRenderContext();
            layout = new TextLayout(getFormattedRunningTime(), font, frc);
            layout.draw(g, screenWidth - UI_RIGHTOFFSET - UI_ITEM_LEFTRIGHT_SPACING - layout.getBounds().getBounds().width,
                    UI_TOPOFFSET + UI_TITLE_BACKGROUND_HEIGHT + UI_BACKGROUND_SPACE_SIZE + UI_ITEM_HEIGHT * itemLevel +
                            (UI_ITEM_HEIGHT - layout.getBounds().getBounds().height) / 2 + layout.getBounds().getBounds().height);

            // #2 - Ectotokens earned
            itemLevel = 2;
            g.setColor(Color.GREEN);
            font = new Font("Times New Roman", Font.PLAIN, UI_ITEM_FONTSIZE);
            frc = g.getFontRenderContext();
            layout = new TextLayout("Ecto-tokens Earned:", font, frc);
            layout.draw(g, screenWidth - UI_WIDTH - UI_RIGHTOFFSET + UI_ITEM_LEFTRIGHT_SPACING,
                    UI_TOPOFFSET + UI_TITLE_BACKGROUND_HEIGHT + UI_BACKGROUND_SPACE_SIZE + UI_ITEM_HEIGHT * itemLevel +
                            (UI_ITEM_HEIGHT - layout.getBounds().getBounds().height) / 2 + layout.getBounds().getBounds().height);

            font = new Font("Times New Roman", Font.PLAIN, UI_ITEM_FONTSIZE);
            frc = g.getFontRenderContext();
            layout = new TextLayout(String.format("%d", Coeus.getInstance().getNumEctotokens()), font, frc);
            layout.draw(g, screenWidth - UI_RIGHTOFFSET - UI_ITEM_LEFTRIGHT_SPACING - layout.getBounds().getBounds().width,
                    UI_TOPOFFSET + UI_TITLE_BACKGROUND_HEIGHT + UI_BACKGROUND_SPACE_SIZE + UI_ITEM_HEIGHT * itemLevel +
                            (UI_ITEM_HEIGHT - layout.getBounds().getBounds().height) / 2 + layout.getBounds().getBounds().height);

            // #3 - Experience Gained
            itemLevel = 3;
            g.setColor(Color.GREEN);
            font = new Font("Times New Roman", Font.PLAIN, UI_ITEM_FONTSIZE);
            frc = g.getFontRenderContext();
            layout = new TextLayout("Prayer XP Gained:", font, frc);
            layout.draw(g, screenWidth - UI_WIDTH - UI_RIGHTOFFSET + UI_ITEM_LEFTRIGHT_SPACING,
                    UI_TOPOFFSET + UI_TITLE_BACKGROUND_HEIGHT + UI_BACKGROUND_SPACE_SIZE + UI_ITEM_HEIGHT * itemLevel +
                            (UI_ITEM_HEIGHT - layout.getBounds().getBounds().height) / 2 + layout.getBounds().getBounds().height);

            font = new Font("Times New Roman", Font.PLAIN, UI_ITEM_FONTSIZE);
            frc = g.getFontRenderContext();
            layout = new TextLayout(String.format("%,d", Coeus.getInstance().getPrayerExperienceGained()), font, frc);
            layout.draw(g, screenWidth - UI_RIGHTOFFSET - UI_ITEM_LEFTRIGHT_SPACING - layout.getBounds().getBounds().width,
                    UI_TOPOFFSET + UI_TITLE_BACKGROUND_HEIGHT + UI_BACKGROUND_SPACE_SIZE + UI_ITEM_HEIGHT * itemLevel +
                            (UI_ITEM_HEIGHT - layout.getBounds().getBounds().height) / 2 + layout.getBounds().getBounds().height);

            // #4 - Experience per hour
            itemLevel = 4;
            g.setColor(Color.GREEN);
            font = new Font("Times New Roman", Font.PLAIN, UI_ITEM_FONTSIZE);
            frc = g.getFontRenderContext();
            layout = new TextLayout("Prayer XP/Hour:", font, frc);
            layout.draw(g, screenWidth - UI_WIDTH - UI_RIGHTOFFSET + UI_ITEM_LEFTRIGHT_SPACING,
                    UI_TOPOFFSET + UI_TITLE_BACKGROUND_HEIGHT + UI_BACKGROUND_SPACE_SIZE + UI_ITEM_HEIGHT * itemLevel +
                            (UI_ITEM_HEIGHT - layout.getBounds().getBounds().height) / 2 + layout.getBounds().getBounds().height);

            font = new Font("Times New Roman", Font.PLAIN, UI_ITEM_FONTSIZE);
            frc = g.getFontRenderContext();
            layout = new TextLayout(getFormattedXPPerHour(), font, frc);
            layout.draw(g, screenWidth - UI_RIGHTOFFSET - UI_ITEM_LEFTRIGHT_SPACING - layout.getBounds().getBounds().width,
                    UI_TOPOFFSET + UI_TITLE_BACKGROUND_HEIGHT + UI_BACKGROUND_SPACE_SIZE + UI_ITEM_HEIGHT * itemLevel +
                            (UI_ITEM_HEIGHT - layout.getBounds().getBounds().height) / 2 + layout.getBounds().getBounds().height);

            // Footer
            g.setColor(new Color(25, 25, 70, 200));
            g.fillRoundRect(screenWidth - UI_WIDTH - UI_RIGHTOFFSET,
                    UI_TOPOFFSET + UI_TITLE_BACKGROUND_HEIGHT + 2 * UI_BACKGROUND_SPACE_SIZE + UI_BODY_BACKGROUND_HEIGHT,
                    UI_WIDTH,
                    UI_FOOTER_BACKGROUND_HEIGHT,
                    UI_ARCSIZE,
                    UI_ARCSIZE);
            g.setColor(new Color(15, 15, 35, 225));
            g.drawRoundRect(screenWidth - UI_WIDTH - UI_RIGHTOFFSET,
                    UI_TOPOFFSET + UI_TITLE_BACKGROUND_HEIGHT + 2 * UI_BACKGROUND_SPACE_SIZE + UI_BODY_BACKGROUND_HEIGHT,
                    UI_WIDTH,
                    UI_FOOTER_BACKGROUND_HEIGHT,
                    UI_ARCSIZE,
                    UI_ARCSIZE);

            g.setColor(Color.GREEN);
            font = new Font("Times New Roman", Font.PLAIN, UI_ITEM_FONTSIZE);
            frc = g.getFontRenderContext();
            layout = new TextLayout("Press Insert to Hide", font, frc);
            layout.draw(g,
                    screenWidth - UI_WIDTH + (UI_WIDTH - layout.getBounds().getBounds().width) / 2 - UI_RIGHTOFFSET,
                    UI_TOPOFFSET + UI_TITLE_BACKGROUND_HEIGHT + 2 * UI_BACKGROUND_SPACE_SIZE + UI_BODY_BACKGROUND_HEIGHT +
                            (UI_FOOTER_BACKGROUND_HEIGHT - layout.getBounds().getBounds().height) / 2 + layout.getBounds().getBounds().height);
        } else {
            // Footer
            g.setColor(new Color(25, 25, 70, 200));
            g.fillRoundRect(screenWidth - UI_WIDTH - UI_RIGHTOFFSET,
                    UI_TOPOFFSET + UI_TITLE_BACKGROUND_HEIGHT + UI_BACKGROUND_SPACE_SIZE,
                    UI_WIDTH,
                    UI_FOOTER_BACKGROUND_HEIGHT,
                    UI_ARCSIZE,
                    UI_ARCSIZE);
            g.setColor(new Color(15, 15, 35, 225));
            g.drawRoundRect(screenWidth - UI_WIDTH - UI_RIGHTOFFSET,
                    UI_TOPOFFSET + UI_TITLE_BACKGROUND_HEIGHT + UI_BACKGROUND_SPACE_SIZE,
                    UI_WIDTH,
                    UI_FOOTER_BACKGROUND_HEIGHT,
                    UI_ARCSIZE,
                    UI_ARCSIZE);

            g.setColor(Color.GREEN);
            font = new Font("Times New Roman", Font.PLAIN, UI_ITEM_FONTSIZE);
            frc = g.getFontRenderContext();
            layout = new TextLayout("Press Insert to Show", font, frc);
            layout.draw(g,
                    screenWidth - UI_WIDTH + (UI_WIDTH - layout.getBounds().getBounds().width) / 2 - UI_RIGHTOFFSET,
                    UI_TOPOFFSET + UI_TITLE_BACKGROUND_HEIGHT + UI_BACKGROUND_SPACE_SIZE +
                            (UI_FOOTER_BACKGROUND_HEIGHT - layout.getBounds().getBounds().height) / 2 + layout.getBounds().getBounds().height);
        }
    }

    public void finish() {
        stayAlive = false;
    }

    private String getFormattedRunningTime() {
        long milli = Coeus.getInstance().getTimeRunning();

        long hours = TimeUnit.MILLISECONDS.toHours(milli);
        milli -= TimeUnit.HOURS.toMillis(hours);
        long min = TimeUnit.MILLISECONDS.toMinutes(milli);
        milli -= TimeUnit.MINUTES.toMillis(min);
        long sec = TimeUnit.MILLISECONDS.toSeconds(milli);

        return String.format("%02d:%02d:%02d", hours, min, sec);
    }

    private String getFormattedXPPerHour() {
        long milli = Coeus.getInstance().getTimeRunning();
        int xp = Coeus.getInstance().getPrayerExperienceGained();

        double xpPerHour = (xp/(double) milli)*3600000;

        return String.format("%,.0f", xpPerHour);
    }

    private void toggleShow() {
        show = !show;
    }

    private boolean itemSelectable(int item) {
        for (int i = 0; i < uiSelectableItems.length; i++) {
            if (item == uiSelectableItems[i])
                return true;
        }
        return false;
    }

    private void selectPreviousItem() {
        if (uiSelectedItem != -9) {
            do {
                uiSelectedItem--;
                // if at first item, loop to bottom
                if (uiSelectedItem < uiSelectableItems[0])
                    uiSelectedItem = uiSelectableItems[uiSelectableItems.length - 1];
            } while (!itemSelectable(uiSelectedItem));
        }
    }

    private void selectNextItem() {
        if (uiSelectedItem != -9) {
            do {
                uiSelectedItem++;
                // if at first item, loop to bottom
                if (uiSelectedItem > uiSelectableItems[uiSelectableItems.length - 1])
                    uiSelectedItem = uiSelectableItems[0];
            } while (!itemSelectable(uiSelectedItem));
        }
    }

    public void handleInput(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_INSERT:
                toggleShow();
                break;
            case KeyEvent.VK_UP:
                selectPreviousItem();
                break;
            case KeyEvent.VK_DOWN:
                selectNextItem();
                break;
            case KeyEvent.VK_LEFT:
                break;
            case KeyEvent.VK_RIGHT:
                break;
        }
    }
}
