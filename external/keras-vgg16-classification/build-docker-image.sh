#!/bin/bash
DEVICE=$1
cp ../../models/grpc-classifier/src/main/proto/classification.proto .
docker build -t keras-vgg16-classification -f Dockerfile.$DEVICE .