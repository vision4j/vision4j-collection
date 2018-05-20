package com.vision4j.classification;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Categories {
    private List<Category> categories;
    private Map<String, Category> nameToCategoryMap;

    public Categories(List<Category> categories) {
        this.categories = categories;
        this.nameToCategoryMap = categories.stream()
                .collect(Collectors.toMap(Category::getCategoryName, Function.identity()));
    }

    public List<Category> getCategories() {
        return categories;
    }

    public Category get(int i) {
        return categories.get(i);
    }

    public Category get(String categoryName) {
        return nameToCategoryMap.get(categoryName);
    }

}
