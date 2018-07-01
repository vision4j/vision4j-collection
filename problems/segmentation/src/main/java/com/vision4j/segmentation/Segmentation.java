package com.vision4j.segmentation;

import com.vision4j.utils.Categories;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Basic contract that every segmentation implementations should fulfill
 */
public interface Segmentation {

    Categories getCategories();

    /**
     * Predicts on an image read from input stream. Useful for example for APIs
     * @param inputStream the stream from which the image should be read
     * @return the segmentation result, containing image with categories and the encoding
     * @throws IOException if exception happens while reading the image from the stream
     */
    SegmentationResult segment(InputStream inputStream) throws IOException;

    /**
     * Predicts on an image read from file. Useful for testing and debugging
     * @param file the file from which the image should be read
     * @return the segmentation result, containing image with categories and the encoding
     * @throws IOException if exception happens while reading the image from the stream
     */
    SegmentationResult segment(File file) throws IOException;


    /**
     * Predicts on an image read from bytes
     * @param imageBytes the file from which the image should be read
     * @return the segmentation result, containing image with categories and the encoding
     * @throws IOException if exception happens while reading the image from the stream
     */
    SegmentationResult segment(byte[] imageBytes) throws IOException;


    /**
     * Predicts on an image read from a given URL
     * @param imageURL the file from which the image should be read
     * @return the segmentation result, containing image with categories and the encoding
     * @throws IOException if exception happens while reading the image from the stream
     */
    SegmentationResult segment(URL imageURL) throws IOException;

    /**
     * Predicts on a buffered image
     * @param bufferedImage the buffered image
     * @return the segmentation result, containing image with categories and the encoding
     * @throws IOException if exception happens while reading the image from the stream
     */
    SegmentationResult segment(BufferedImage bufferedImage) throws IOException;

}
