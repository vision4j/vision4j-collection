#!/bin/bash
DEVICE=$1
cp ../../models/grpc-detection/src/main/proto/detection.proto .
docker build -t vision4j/mask-rcnn-detection:$DEVICE -f Dockerfile.$DEVICE .