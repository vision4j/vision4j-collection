//Autogenerated
package com.vision4j.segmentation;

import io.grpc.ManagedChannel;
import static com.vision4j.utils.Constants.*;

public class CocoSegmentation extends GrpcSegmentation {

    public CocoSegmentation() {
        super(COCO_CATEGORIES);
    }

    public CocoSegmentation(ManagedChannel channel) {
        super(COCO_CATEGORIES, channel);
    }

    public CocoSegmentation(String host, int port) {
        super(COCO_CATEGORIES, host, port);
    }
}
