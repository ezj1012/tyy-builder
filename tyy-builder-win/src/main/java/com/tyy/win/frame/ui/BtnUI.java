package com.tyy.win.frame.ui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicButtonUI;

import sun.swing.SwingUtilities2;

@SuppressWarnings("restriction")
public class BtnUI extends BasicButtonUI {

    private String btnText;

    private String drawText;

    int offx;

    protected void paintIcon(Graphics g, JComponent c, Rectangle iconRect) {
        iconRect.x = 0;
        iconRect.y = 0;
        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();
        Icon icon = b.getIcon();
        Icon tmpIcon = null;

        if (icon == null) { return; }

        Icon selectedIcon = null;

        /* the fallback icon should be based on the selected state */
        if (model.isSelected()) {
            selectedIcon = b.getSelectedIcon();
            if (selectedIcon != null) {
                icon = selectedIcon;
            }
        }

        if (!model.isEnabled()) {
            if (model.isSelected()) {
                tmpIcon = b.getDisabledSelectedIcon();
                if (tmpIcon == null) {
                    tmpIcon = selectedIcon;
                }
            }

            if (tmpIcon == null) {
                tmpIcon = b.getDisabledIcon();
            }
        } else if (model.isPressed() && model.isArmed()) {
            tmpIcon = b.getPressedIcon();
            if (tmpIcon != null) {
                // revert back to 0 offset
                clearTextShiftOffset();
            }
            g.setColor(new Color(150, 150, 240, 170));
            g.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 10, 10);
        } else if (b.isRolloverEnabled() && model.isRollover()) {
            if (model.isSelected()) {
                tmpIcon = b.getRolloverSelectedIcon();
                if (tmpIcon == null) {
                    tmpIcon = selectedIcon;
                }
            }

            if (tmpIcon == null) {
                tmpIcon = b.getRolloverIcon();
            }
            g.setColor(new Color(180, 180, 240, 128));
            g.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 10, 10);
        }

        if (tmpIcon != null) {
            icon = tmpIcon;
        }
        iconRect.x = (c.getWidth() - icon.getIconWidth()) / 2;
        iconRect.y = (c.getHeight() - icon.getIconHeight()) / 2;
        if (model.isPressed() && model.isArmed()) {
            icon.paintIcon(c, g, iconRect.x + getTextShiftOffset() + 1, iconRect.y + getTextShiftOffset() + 1);
        } else {
            icon.paintIcon(c, g, iconRect.x, iconRect.y);
        }

    }

    protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {

        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();
        FontMetrics fm = SwingUtilities2.getFontMetrics(c, g);
        int mnemonicIndex = b.getDisplayedMnemonicIndex();

        int maxW = c.getWidth() - 6;
        String a = ((AbstractButton) c).getText();
        if (a == null) { return; }
        if (btnText == null || !a.equals(btnText)) {
            btnText = a;
            drawText = "";
            for (int i = 0; i < a.length(); i++) {
                String t = drawText + a.charAt(i);
                if (SwingUtilities2.stringWidth(c, fm, t) > maxW) {
                    break;
                } else {
                    drawText += a.charAt(i);
                }
            }
            offx = (c.getWidth() - SwingUtilities2.stringWidth(c, fm, text)) / 2;
        }
        text = drawText;
        textRect.x = 0;
        if (model.isEnabled()) {
            g.setColor(b.getForeground());
            if (model.isPressed() && model.isArmed()) {
                SwingUtilities2.drawStringUnderlineCharAt(c, g, text, mnemonicIndex, textRect.x + offx, textRect.y + fm.getAscent() + getTextShiftOffset() + 1);
            } else {
                SwingUtilities2.drawStringUnderlineCharAt(c, g, text, mnemonicIndex, textRect.x + offx - 1, textRect.y + fm.getAscent() + getTextShiftOffset());
            }

        } else {
            /*** paint the text disabled ***/
            g.setColor(b.getBackground().brighter());
            SwingUtilities2.drawStringUnderlineCharAt(c, g, text, mnemonicIndex, textRect.x + offx, textRect.y + fm.getAscent());
            g.setColor(b.getBackground().darker());
            SwingUtilities2.drawStringUnderlineCharAt(c, g, text, mnemonicIndex, textRect.x + offx - 1, textRect.y + fm.getAscent() - 1);
        }
    }

}
