package com.vision4j.utils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Categories {
    private List<Category> categories;
    private Map<String, Category> nameToCategoryMap;

    public Categories(String[] categoriesArray) {
        this(IntStream.range(0, categoriesArray.length)
                .mapToObj(i -> new Category(categoriesArray[i], i))
                .collect(Collectors.toList()));
    }

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
