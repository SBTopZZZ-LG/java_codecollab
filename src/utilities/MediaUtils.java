package utilities;

import javax.swing.*;
import java.awt.*;

public class MediaUtils {
    public static ImageIcon imageIconResize(final ImageIcon imageIcon, final int width, final int height) {
        return new ImageIcon(imageIcon.getImage().getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH));
    }
}
