package com.vision4j.classification;

import io.grpc.ManagedChannel;

import static com.vision4j.utils.Constants.VGG16_IMAGE_SIZE;

public class GrpcVggClassifier extends GrpcImageNetClassifier {

    public GrpcVggClassifier() {
        super(VGG16_IMAGE_SIZE);
    }

    public GrpcVggClassifier(String host, int port) {
        super(host, port, VGG16_IMAGE_SIZE);
    }

    public GrpcVggClassifier(ManagedChannel channel) {
        super(channel, VGG16_IMAGE_SIZE);
    }
}
