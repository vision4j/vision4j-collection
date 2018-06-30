package com.vision4j.classification;

import com.vision4j.utils.Categories;
import com.vision4j.utils.Category;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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
     * @return the predicted category
     * @throws IOException if exception happens while reading the image from the stream
     */
    Category predict(File file) throws IOException;

    /**
     * Predicts on an image by given its bytes
     * @param imageBytes the bytes
     * @return the predicted category
     * @throws IOException if exception happens while predicting
     */
    Category predict(byte[] imageBytes) throws IOException;

    /**
     * Predicts on an image from a given URL.
     * @param imageURL the url from which the image can be downloaded
     * @return the predicted category
     * @throws IOException if exception happens while predicting
     */
    Category predict(URL imageURL) throws IOException;
}
