package com.vision4j.utils;

/**
 * Represents a category in a computer vision problem. The index is the number which represents the category
 * when the data is vectorized.
 */
public class Category {
    private String name;

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    private int index;

    public Category(String name, int index) {
        this.name = name;
        this.index = index;
    }
}
