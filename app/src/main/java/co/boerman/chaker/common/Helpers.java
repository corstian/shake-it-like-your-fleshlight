package co.boerman.chaker.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Corstian on 30/03/2017.
 */

public class Helpers {
    public static void saveDataToPreferences(Context context, String key,
                                             String value) {
        SharedPreferences prefs = context.getSharedPreferences("_",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public static String getDataFromPreferences(Context context, String key) {

        SharedPreferences prefs = context.getSharedPreferences("_",
                Context.MODE_PRIVATE);
        return prefs.getString(key, null);
    }

    public static int randomWithRange(int min, int max) {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }

    public static RGBColor getRandomRGBColor() {
        return new RGBColor(
                (byte)randomWithRange(0, 255),
                (byte)randomWithRange(0, 255),
                (byte)randomWithRange(0, 255)
        );
    }

    public static String rgbColorToHexString(RGBColor color) {
        StringBuilder sb = new StringBuilder();
        sb.append("#");
        sb.append(String.format("%02X", color.R));
        sb.append(String.format("%02X", color.G));
        sb.append(String.format("%02X", color.B));
        return sb.toString();
    }

    public static RGBColor getInverse(RGBColor color) {
        return new RGBColor(
                (byte)(255 - color.R),
                (byte)(255 - color.G),
                (byte)(255 - color.B)
        );
    }

    public static HSLColor fromRGB(RGBColor rgbColor) {
        float r = (rgbColor.R / 255);
        float g = (rgbColor.G / 255);
        float b = (rgbColor.B / 255);

        float min = Math.min(Math.min(r, g), b);
        float max = Math.max(Math.max(r, g), b);
        float delta = max - min;

        float h = 0;
        float s = 0;
        float l = ((max + min) / 2.0f);

        if (delta != 0) {
            if (l < .5f)
                s = delta/(max + min);
            else
                s = delta / (2.0f - max - min);

            if (r == max)
                h = (g - b) / delta;
            else if (g == max)
                h = 2f + (b - r)/delta;
            else if (b == max)
                h = 4f + (r - g) / delta;
        }

        return new HSLColor(h, s, l);
    }
}
