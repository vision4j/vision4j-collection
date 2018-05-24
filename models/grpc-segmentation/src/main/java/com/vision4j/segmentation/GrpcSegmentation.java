package com.vision4j.segmentation;

import com.google.protobuf.ByteString;
import com.vision4j.segmentation.grpc.Image;
import com.vision4j.segmentation.grpc.SegmentationArray;
import com.vision4j.segmentation.grpc.SegmentationGrpc;
import com.vision4j.utils.Categories;
import com.vision4j.utils.Category;
import com.vision4j.utils.SimpleImageInfo;
import com.vision4j.utils.VisionUtils;
import io.grpc.ManagedChannel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GrpcSegmentation implements Segmentation {

    private Categories categories;
    private SegmentationGrpc.SegmentationBlockingStub stub;

    public GrpcSegmentation(ManagedChannel channel, String[] categoriesArray) {
        this.stub = SegmentationGrpc.newBlockingStub(channel);
        this.categories = new Categories(IntStream.range(0, categoriesArray.length)
                .mapToObj(i -> new Category(categoriesArray[i], i))
                .collect(Collectors.toList()));
    }

    @Override
    public Categories getCategories() {
        return categories;
    }

    @Override
    public SegmentationResult segment(InputStream inputStream) throws IOException {
        // this is kind of inefficient. Is not it possible to directly create the ByteString?
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        return segment(bufferedImage);
    }

    private SegmentationResult segment(BufferedImage bufferedImage) throws IOException {
        //TODO: this should not be hardcoded
        byte[] imageBytes = VisionUtils.getBytes(bufferedImage, "jpg");
        SimpleImageInfo simpleImageInfo = new SimpleImageInfo(imageBytes);
        ByteString bytes = ByteString.copyFrom(imageBytes);
        Image image = Image.newBuilder()
                .setImageData(bytes)
                .setWidth(simpleImageInfo.getWidth())
                .setHeight(simpleImageInfo.getHeight())
                .setChannels(3)
                .setOriginalWidth(simpleImageInfo.getWidth())
                .setOriginalHeight(simpleImageInfo.getHeight())
                .build();
        SegmentationArray segmentationArray = stub.segment(image);
        byte[] result = segmentationArray.getResult().toByteArray();
        BufferedImage resultImage = ImageIO.read(new ByteArrayInputStream(result));
        return new SegmentationResult(resultImage, getCategories());
    }


    @Override
    public SegmentationResult segment(File file) throws IOException {
        return segment(ImageIO.read(file));
    }
}
