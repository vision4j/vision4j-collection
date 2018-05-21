package com.vision4j.classification;

import com.google.protobuf.ByteString;
import com.vision4j.classification.grpc.ClassificationGrpc;
import com.vision4j.classification.grpc.Image;
import com.vision4j.classification.grpc.Prediction;
import com.vision4j.utils.Categories;
import com.vision4j.utils.Category;
import com.vision4j.utils.VisionUtils;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GrpcClassifier implements ImageClassifier {

    private final Categories categories;
    private final ImageSize imageSize;
    private final ClassificationGrpc.ClassificationBlockingStub classificationStub;

    public GrpcClassifier(String[] categoriesArray, ImageSize imageSize) {
        this("localhost", 50051, categoriesArray, imageSize);
    }

    public GrpcClassifier(String host, int port, String[] categoriesArray, ImageSize imageSize) {
        this(ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build(), categoriesArray, imageSize);
    }

    public GrpcClassifier(ManagedChannel channel, String[] categoriesArray, ImageSize imageSize) {
        this.categories = new Categories(IntStream.range(0, categoriesArray.length)
                .mapToObj(i -> new Category(categoriesArray[i], i))
                .collect(Collectors.toList()));

        this.imageSize = imageSize;
        this.classificationStub = ClassificationGrpc.newBlockingStub(channel);
    }

    //TODO: this could go to utils as well
    private byte[] getBytes(BufferedImage resizedImage) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //TODO: this should not be hardcoded
        ImageIO.write(resizedImage, "jpg", baos);
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();
        return imageInByte;
    }

    @Override
    public Categories getAcceptableCategories() {
        return categories;
    }

    @Override
    public Category predict(InputStream inputStream) throws IOException {
        BufferedImage originalImage = ImageIO.read(inputStream);
        return getCategory(originalImage);
    }

    private Category getCategory(BufferedImage originalImage) throws IOException {
        BufferedImage resizedImage = VisionUtils.resize(originalImage, imageSize.getWidth(), imageSize.getHeight());
        byte[] imageBytes = getBytes(resizedImage);
        ByteString imageData = ByteString.copyFrom(imageBytes);
        Image image = Image.newBuilder()
                .setWidth(imageSize.getWidth())
                .setHeight(imageSize.getHeight())
                .setChannels(imageSize.channels())
                .setImageData(imageData)
                .build();
        Prediction prediction = classificationStub.predict(image);
        int index = prediction.getIndex();
        return getAcceptableCategories().getCategories().get(index);
    }

    @Override
    public Category predict(File file) throws IOException {
        BufferedImage originalImage = ImageIO.read(file);
        return getCategory(originalImage);
    }

}
