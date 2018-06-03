package com.vision4j.classification;

import com.vision4j.utils.Categories;
import com.vision4j.utils.Category;
import com.vision4j.utils.Constants;
import com.vision4j.utils.ImageSize;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.modelimport.keras.trainedmodels.Utils.ImageNetLabels;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.VGG16ImagePreProcessor;
import org.nd4j.linalg.factory.Nd4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Ready to use VGG16 classifier for the ImageNet dataset.
 */
public class Vgg16DeepLearning4jClassifier implements ImageClassifier {

    private static final Logger logger = LoggerFactory.getLogger(Vgg16DeepLearning4jClassifier.class);

    private static final String pretrainedModelUrl = "https://s3.amazonaws.com/vision4j/models/vgg16_dl4j_inference.zip";

    private ComputationGraph vgg16;
    private NativeImageLoader imageLoader;
    private DataNormalization scaler;
    private Categories categories;
    private ImageSize imageSize;

    public Vgg16DeepLearning4jClassifier() throws IOException {
        init(downloadComputationGraph());
    }

    public Vgg16DeepLearning4jClassifier(File computationGraph) throws IOException {
        if (!computationGraph.exists()) {
            throw new IllegalStateException("The file provided for the VGG16 model '" + computationGraph.getAbsolutePath() + "' does not exist");
        }
        init(computationGraph);
    }

    private void init(File computationGraph) throws IOException {
        this.vgg16 = ModelSerializer.restoreComputationGraph(computationGraph);
        this.scaler = new VGG16ImagePreProcessor();
        this.imageSize = new ImageSize(224, 224, 3);
        this.imageLoader = new NativeImageLoader(imageSize.getHeight(), imageSize.getWidth(), imageSize.channels());

        ArrayList<String> labels = ImageNetLabels.getLabels();
        String[] categoriesArray = Constants.IMAGENET_CATEGORIES;
        this.categories = new Categories(IntStream.range(0, categoriesArray.length)
                .mapToObj(i -> new Category(categoriesArray[i], i))
                .collect(Collectors.toList()));
    }

    private File downloadComputationGraph() throws IOException {
        try {
            Path targetPath = Paths.get(System.getProperty("user.dir"), "vgg16_dl4j_inference.zip");
            File targetFile = targetPath.toFile();
            if (targetFile.exists()) {
                logger.info("Model file {} exists", targetFile);
                return targetFile;
            }
            URL website = new URL(pretrainedModelUrl);
            logger.info("Downloading pretrained model from url {}", pretrainedModelUrl);
            try (InputStream inputStream = website.openStream()) {
                Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }
            return targetFile;
        } catch (MalformedURLException e) {
            logger.error("The url " + pretrainedModelUrl + " is malformed", e);
            throw new IOException(e);
        }
    }

    @Override
    public Categories getAcceptableCategories() {
        return categories;
    }

    @Override
    public Category predict(InputStream inputStream) throws IOException {
        INDArray image = imageLoader.asMatrix(inputStream);
        return predict(image);
    }

    @Override
    public Category predict(File file) throws IOException {
        INDArray image = imageLoader.asMatrix(file);
        return predict(image);
    }

    private Category predict(INDArray image) {
        scaler.transform(image);
        INDArray output = vgg16.outputSingle(image);
        int categoryIndex = Nd4j.argMax(output).getInt(0);
        return getAcceptableCategories().getCategories().get(categoryIndex);
    }
}
