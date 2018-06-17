#!/bin/bash
DEVICE=$1
cp ../../models/grpc-classifier/src/main/proto/classification.proto .
docker build -t vision4j/grpc-keras-vgg16-classification:$DEVICE -f Dockerfile.$DEVICE .