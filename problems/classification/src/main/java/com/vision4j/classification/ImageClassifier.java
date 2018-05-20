package com.vision4j.classification;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Basic contract that every image classifier should fulfill.
 */
public interface ImageClassifier {

    /**
     * Gets a list of the categories that the classifier can recognize. The model will output the index of the category
     */
    Categories getAcceptableCategories();

    /**
     * Predicts on an image read from input stream. Useful for example for APIs
     * @param inputStream the stream from which the image should be read
     * @return the predicted category
     * @throws IOException if exception happens while reading the image from the stream
     */
    Category predict(InputStream inputStream) throws IOException;

    /**
     * Predicts on an image read from file. Useful for testing and debugging
     * @param file the file from which the image should be read
     * @return
     * @throws IOException if exception happens while reading the image from the stream
     */
    Category predict(File file) throws IOException;
}
