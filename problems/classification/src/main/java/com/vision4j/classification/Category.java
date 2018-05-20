package com.vision4j.classification;

public class Category {
    private int index;

    private String categoryName;

    public Category(int index, String categoryName) {
        this.index = index;
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getIndex() {
        return index;
    }
}
