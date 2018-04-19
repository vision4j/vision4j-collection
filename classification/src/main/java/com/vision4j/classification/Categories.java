package com.vision4j.classification;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.List;

public class Categories {
    private List<Category> categories;

    public Categories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public Category getCategory(INDArray output) {
        int categoryIndex = Nd4j.argMax(output).getInt(0);
        return getCategories().get(categoryIndex);
    }
}
