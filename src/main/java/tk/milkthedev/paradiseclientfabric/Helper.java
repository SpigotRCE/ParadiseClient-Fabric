package tk.milkthedev.paradiseclientfabric;

import java.awt.*;

public class Helper
{
    public static Color getChroma(int delay, float saturation, float brightness)
    {
        double chroma = Math.ceil((double) (System.currentTimeMillis() + delay) / 20);
        chroma %= 360;
        return Color.getHSBColor((float) (chroma / 360), saturation, brightness);
    }

}
