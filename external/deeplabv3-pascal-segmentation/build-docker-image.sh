#!/bin/bash
cp ../../models/grpc-segmentation/src/main/proto/segmentation.proto .
docker build -t deeplabv3-pascal-segmentation .