package com.vision4j.classification;

import com.vision4j.utils.ImageSize;
import io.grpc.ManagedChannel;

import static com.vision4j.utils.Constants.IMAGENET_CATEGORIES;


/**
 * A classifier trained on imagenet called through GRPC
 */
public class GrpcImageNetClassifier extends GrpcClassifier {
    public GrpcImageNetClassifier(ImageSize imageSize) {
        super(IMAGENET_CATEGORIES, imageSize);
    }

    public GrpcImageNetClassifier(String host, int port, ImageSize imageSize) {
        super(host, port, IMAGENET_CATEGORIES, imageSize);
    }

    public GrpcImageNetClassifier(ManagedChannel channel, ImageSize imageSize) {
        super(channel, IMAGENET_CATEGORIES, imageSize);
    }
}
