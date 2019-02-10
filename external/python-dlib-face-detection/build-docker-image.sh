#!/bin/bash
DEVICE=$1
cp ../../models/grpc-face-detection/src/main/proto/face_detection.proto .
docker build -t vision4j/python-dlib-face-detection:$DEVICE -f Dockerfile.$DEVICE .