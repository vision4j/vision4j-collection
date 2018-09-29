#!/bin/bash
DEVICE=$1
cp ../../models/grpc-completion/src/main/proto/completion.proto .
docker build -t vision4j/gimp-completion:$DEVICE -f Dockerfile.$DEVICE .