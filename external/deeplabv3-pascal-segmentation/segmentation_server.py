# Copyright 2015 gRPC authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
"""The Python implementation of the GRPC helloworld.Greeter server."""

from concurrent import futures
import time
from keras.optimizers import SGD
import grpc
import numpy as np
import cv2
from PIL import Image
import segmentation_pb2
import segmentation_pb2_grpc
import sys
import subprocess

from deep_lab_model import DeepLabModel

_ONE_DAY_IN_SECONDS = 60 * 60 * 24

model = DeepLabModel('/app/deeplabv3_pascal_trainval_2018_01_04.tar.gz')


class Segmentation(segmentation_pb2_grpc.SegmentationServicer):

    def Segment(self, request, context):
        return model.predict_request(request)


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=1))
    segmentation_pb2_grpc.add_SegmentationServicer_to_server(Segmentation(), server)
    server.add_insecure_port('[::]:50052')
    server.start()

    try:
        callback_command = sys.argv[1]
        subprocess.call(callback_command, shell=True)
    except:
        pass

    try:
        while True:
            time.sleep(_ONE_DAY_IN_SECONDS)
    except KeyboardInterrupt:
        server.stop(0)


if __name__ == '__main__':
    serve()
