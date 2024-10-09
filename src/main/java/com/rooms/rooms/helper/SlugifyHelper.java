package com.rooms.rooms.helper;

import com.github.slugify.Slugify;

public class SlugifyHelper {
    private static final Slugify slugify;

    static {
        slugify = Slugify.builder().lowerCase(false).build();
    }

    public static String slugify(String input) {
        return slugify.slugify(input);
    }
}
