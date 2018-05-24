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

from io import BytesIO
from deep_lab_model import DeepLabModel

_ONE_DAY_IN_SECONDS = 60 * 60 * 24

model = DeepLabModel('/app/deeplabv3_pascal_trainval_2018_01_04.tar.gz')

def predict(img, original_width_height):
    resized_image, prediction = model.run(img)
    np_prediction = np.asarray(prediction, dtype=np.uint8)
    return Image.fromarray(np_prediction).resize(original_width_height)


def read_image_from_response(data, shape):
    nparr = np.fromstring(data, np.uint8)
    img = cv2.cvtColor(cv2.imdecode(nparr, cv2.IMREAD_COLOR), cv2.COLOR_BGR2RGB)
    return Image.fromarray(img)


class Classificator(segmentation_pb2_grpc.SegmentationServicer):

    def Segment(self, request, context):
        img = read_image_from_response(request.image_data, (request.width, request.height, request.channels))
        original_width_height = (request.original_width, request.original_height)
        resized_prediction = predict(img, original_width_height)
        imgByteArr = BytesIO()
        resized_prediction.save(imgByteArr, format='png')
        imgByteArr = imgByteArr.getvalue()
        return segmentation_pb2.SegmentationArray(result=imgByteArr)



def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=1))
    segmentation_pb2_grpc.add_SegmentationServicer_to_server(Classificator(), server)
    server.add_insecure_port('[::]:50052')
    server.start()
    try:
        while True:
            time.sleep(_ONE_DAY_IN_SECONDS)
    except KeyboardInterrupt:
        server.stop(0)


if __name__ == '__main__':
    serve()
