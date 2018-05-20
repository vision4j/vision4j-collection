package com.vision4j.segmentation;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * TODO: This class should probably go to utils. Both classification and segmentation use it.
 */
public class Categories {
    private List<Category> categories;
    private Map<String, Category> nameToCategoryMap;

    public Categories(List<Category> categories) {
        this.categories = categories;
        nameToCategoryMap = categories.stream()
                .collect(Collectors.toMap(Category::getName, Function.identity()));
    }

    public List<Category> getCategories() {
        return categories;
    }

    public Category get(int index) {
        return categories.get(index);
    }

    public Category get(String name) {
        return nameToCategoryMap.get(name);
    }

}
