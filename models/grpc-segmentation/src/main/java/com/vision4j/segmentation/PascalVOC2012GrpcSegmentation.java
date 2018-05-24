package com.vision4j.segmentation;

import io.grpc.ManagedChannel;

public class PascalVOC2012GrpcSegmentation extends GrpcSegmentation {
    private final static String[] categoriesArray = new String[] {
            "background", "aeroplane", "bicycle", "bird", "boat", "bottle", "bus", "car", "cat", "chair", "cow",
            "diningtable", "dog", "horse", "motorbike", "person", "pottedplant", "sheep", "sofa", "train", "tv"
    };

    public PascalVOC2012GrpcSegmentation() {
        super(categoriesArray);
    }

    public PascalVOC2012GrpcSegmentation(ManagedChannel channel) {
        super(channel, categoriesArray);
    }
}
