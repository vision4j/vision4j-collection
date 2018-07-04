package com.vision4j.classification;

import io.grpc.ManagedChannel;

import static com.vision4j.utils.Constants.IMAGENET_CATEGORIES;


/**
 * A classifier trained on imagenet called through GRPC
 */
public class GrpcImageNetClassifier extends GrpcClassifier {
    public GrpcImageNetClassifier() {
        super(IMAGENET_CATEGORIES);
    }

    public GrpcImageNetClassifier(String host, int port) {
        super(IMAGENET_CATEGORIES, host, port);
    }

    public GrpcImageNetClassifier(ManagedChannel channel) {
        super(IMAGENET_CATEGORIES, channel);
    }
}
