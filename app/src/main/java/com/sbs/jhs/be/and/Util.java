package com.sbs.jhs.be.and;

public class Util {
    public static int getAsInt(Object arg) {
        if (arg instanceof Double) {
            return Integer.parseInt(String.valueOf(Math.round((Double) arg)));
        } else if (arg instanceof Long) {
            return (Integer) arg;
        } else if (arg instanceof Integer) {
            return (Integer) arg;
        }

        return -1;
    }
}
