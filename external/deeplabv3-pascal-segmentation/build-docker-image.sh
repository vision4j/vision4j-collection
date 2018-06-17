#!/bin/bash
DEVICE=$1
cp ../../models/grpc-segmentation/src/main/proto/segmentation.proto .
docker build -t vision4j/deeplabv3-pascal-voc-segmentation:$DEVICE -f Dockerfile.$DEVICE .