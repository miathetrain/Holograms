package net.holograms.format;

import cn.nukkit.utils.TextFormat;

public class FormatUtil {


    public static String Format(String s) {
        s = TextFormat.colorize(s);
        return s;
    }

    private static String PlaceHolders(String s) {
        return s;
    }
}
