package com.vision4j.segmentation;

/**
 * TODO: This class should probably go to utils. Both classification and segmentation use it.
 */
public class Category {
    private String name;

    public String getName() {
        return name;
    }

    public byte getIndex() {
        return index;
    }

    private byte index;

    public Category(String name, byte index) {
        this.name = name;
        this.index = index;
    }
}
