from concurrent import futures
import time
import grpc
import classification_pb2
import classification_pb2_grpc
import sys
import subprocess

from classification import Vgg16Classifier

_ONE_DAY_IN_SECONDS = 60 * 60 * 24

model = Vgg16Classifier()

class Classification(classification_pb2_grpc.ClassificationServicer):

    def Predict(self, request, context):
        return model.predict_request(request)


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=1))
    classification_pb2_grpc.add_ClassificationServicer_to_server(Classification(), server)
    server.add_insecure_port('[::]:50051')
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
