package com.vision4j.segmentation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class GrpcSegmentation implements Segmentation {
    @Override
    public Categories getCategories() {
        return null;
    }

    @Override
    public SegmentationResult segment(InputStream inputStream) throws IOException {
        return null;
    }

    @Override
    public SegmentationResult segment(File file) throws IOException {
        return null;
    }
}
