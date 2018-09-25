package com.vision4j.completion;

import java.io.InputStream;
import java.io.File;
import java.net.URL;
import java.io.IOException;
import com.vision4j.utils.*;

/**
 * Basic contract that every image completion model should fulfill.
 */
public interface Completion {

    /**
     * Predicts on an image read from input stream. Useful for example for APIs
     *
     * @param inputStream the stream from which the image should be read
     * @return the completed image
     * @throws IOException if exception happens while the model predicts
     */
    CompletionResult complete(InputStream inputStream) throws IOException;

    /**
     * Predicts on an image read from file. Useful for testing and debugging
     *
     * @param file the file from which the image should be read
     * @return the completed image
     * @throws IOException if exception happens while reading the image from the stream
     */
    CompletionResult complete(File file) throws IOException;

    /**
     * Predicts on an image by given its bytes
     *
     * @param imageBytes the bytes
     * @return the completed image
     * @throws IOException if exception happens while predicting
     */
    CompletionResult complete(byte[] imageBytes) throws IOException;

    /**
     * Predicts on an image from a given URL.
     *
     * @param imageURL the url from which the image can be downloaded
     * @return the completed image
     * @throws IOException if exception happens while predicting
     */
    CompletionResult complete(URL imageURL) throws IOException;
}
