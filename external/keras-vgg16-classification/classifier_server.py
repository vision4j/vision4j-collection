from concurrent import futures
import time
from keras.optimizers import SGD
import grpc
import numpy as np
import cv2
from PIL import Image
from classifier import Vgg16Classifier
import classification_pb2
import classification_pb2_grpc

_ONE_DAY_IN_SECONDS = 60 * 60 * 24

model = Vgg16Classifier()



class Classificator(classification_pb2_grpc.ClassificationServicer):

    def Predict(self, request, context):
        result = model.predict(request)
        return classification_pb2.Prediction(index=result)


    def SendImage(self, request, context):
        result = model.predict(request)
        return helloworld_pb2.HelloReply(message=str(result))


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=1))
    classification_pb2_grpc.add_ClassificationServicer_to_server(Classificator(), server)
    server.add_insecure_port('[::]:50051')
    server.start()
    try:
        while True:
            time.sleep(_ONE_DAY_IN_SECONDS)
    except KeyboardInterrupt:
        server.stop(0)


if __name__ == '__main__':
    serve()