package com.vision4j.segmentation;

import com.vision4j.utils.Constants;
import io.grpc.ManagedChannel;

public class PascalVOC2012GrpcSegmentation extends GrpcSegmentation {

    public PascalVOC2012GrpcSegmentation() {
        super(Constants.PASCAL_VOC_2012_CATEGORIES);
    }

    public PascalVOC2012GrpcSegmentation(ManagedChannel channel) {
        super(Constants.PASCAL_VOC_2012_CATEGORIES, channel);
    }
}
