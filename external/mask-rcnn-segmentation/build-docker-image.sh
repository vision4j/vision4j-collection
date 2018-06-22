#!/bin/bash
DEVICE=$1
cp ../../models/grpc-segmentation/src/main/proto/segmentation.proto .
docker build -t vision4j/mask-rcnn-segmentation:$DEVICE -f Dockerfile.$DEVICE .