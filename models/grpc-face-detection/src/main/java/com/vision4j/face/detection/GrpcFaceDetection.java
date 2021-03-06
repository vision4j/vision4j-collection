//Autogenerated
package com.vision4j.face.detection;

import com.google.protobuf.ByteString;
import com.vision4j.face.detection.grpc.BoundingBox;
import com.vision4j.face.detection.grpc.BoundingBoxes;
import com.vision4j.utils.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.vision4j.face.detection.grpc.FaceDetectionGrpc;
import com.vision4j.face.detection.grpc.Image;
import com.vision4j.face.detection.grpc.DetectedFaces;

public class GrpcFaceDetection implements FaceDetection {

    private final FaceDetectionGrpc.FaceDetectionBlockingStub face_detectionStub;

    public GrpcFaceDetection(ManagedChannel channel) {
        this.face_detectionStub = FaceDetectionGrpc.newBlockingStub(channel);
    }

    public GrpcFaceDetection(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext().build());
    }

    public GrpcFaceDetection() {
        this("localhost", 50055);
    }

    @Override()
    public FaceDetectionResult detect(InputStream image) throws IOException {
        return this.detect(VisionUtils.toByteArray(image));
    }

    @Override()
    public FaceDetectionResult detect(File image) throws IOException {
        return this.detect(VisionUtils.toByteArray(image));
    }

    @Override()
    public FaceDetectionResult detect(byte[] image) throws IOException {
        Image serializedImage = this.prepareGrpcBytes(image);
        DetectedFaces detectedfaces = face_detectionStub.detect(serializedImage);
        return this.convert(detectedfaces);
    }

    @Override()
    public FaceDetectionResult detect(URL image) throws IOException {
        return this.detect(VisionUtils.toByteArray(image));
    }

    private FaceDetectionResult convert(DetectedFaces detectedfaces) {
        FaceDetectionResult faceDetectionResult = new FaceDetectionResult();

        for (Map.Entry<String, BoundingBoxes> entry: detectedfaces.getFacesMapMap().entrySet()) {
            List<FaceDetectionResult.BoundingBox> boundingBoxes = entry
                    .getValue()
                    .getBoundingBoxesList()
                    .stream()
                    .map(boundingBox -> new FaceDetectionResult.BoundingBox(
                            entry.getKey(),
                            boundingBox.getLeft(),
                            boundingBox.getTop(),
                            boundingBox.getRight(),
                            boundingBox.getBottom()
                    ))
                    .collect(Collectors.toList());

            faceDetectionResult.put(entry.getKey(), boundingBoxes);
        }

        return faceDetectionResult;
    }

    private Image prepareGrpcBytes(byte[] image) throws IOException {
        SimpleImageInfo simpleImageInfo = new SimpleImageInfo(image);
        ByteString imageData = ByteString.copyFrom(image);
        Image.Builder imageBuilder = Image.newBuilder();
        imageBuilder.setWidth(simpleImageInfo.getWidth());
        imageBuilder.setHeight(simpleImageInfo.getHeight());
        imageBuilder.setChannels(3);
        imageBuilder.setImageData(imageData);
        return imageBuilder.build();
    }
}
