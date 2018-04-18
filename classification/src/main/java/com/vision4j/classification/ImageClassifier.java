package com.vision4j.classification;

import java.io.IOException;
import java.io.InputStream;

public interface ImageClassifier {

    Categories getAcceptableCategories();

    Category predict(InputStream inputStream) throws IOException;
}
