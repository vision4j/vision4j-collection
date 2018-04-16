package com.vision4j.classification;

import jdk.internal.util.xml.impl.Input;
import org.datavec.image.loader.ImageLoader;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.modelimport.keras.trainedmodels.Utils.ImageNetLabels;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.VGG16ImagePreProcessor;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

public class Vgg16DeepLearning4jClassifier implements ImageClassifier {

    private ComputationGraph vgg16;
    private NativeImageLoader imageLoader;
    private DataNormalization scaler;
    private Categories categories;
    private ImageSize imageSize;

    public Vgg16DeepLearning4jClassifier(ComputationGraph vgg16) {
        this.vgg16 = vgg16;
        this.scaler = new VGG16ImagePreProcessor();
        this.imageSize = new ImageSize(224, 224, 3);
        this.imageLoader = new NativeImageLoader(imageSize.getHeight(), imageSize.getWidth(), imageSize.channels());
        List<Category> categories = ImageNetLabels
                .getLabels()
                .stream()
                .map(Category::new)
                .collect(Collectors.toList());
        this.categories = new Categories(categories);
    }

    @Override
    public Categories getAcceptableCategories() {
        return categories;
    }

    @Override
    public Category predict(InputStream inputStream) throws IOException {
        INDArray image = imageLoader.asMatrix(inputStream);
        scaler.transform(image);
        INDArray output = vgg16.outputSingle(image);
        return getAcceptableCategories().getCategory(output);
    }
}
