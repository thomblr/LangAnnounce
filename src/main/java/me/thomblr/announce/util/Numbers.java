package me.thomblr.announce.util;

import java.util.Random;

public class Numbers {

    public static int getRandom(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

}
