from concurrent import futures
import time
import grpc
import face_detection_pb2
import face_detection_pb2_grpc
import sys
import subprocess

from face_detection import DlibFaceDetection

_ONE_DAY_IN_SECONDS = 60 * 60 * 24

model = DlibFaceDetection()

class FaceDetection(face_detection_pb2_grpc.FaceDetectionServicer):

    def Detect(self, request, context):
        return model.detect_request(request)


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=1))
    face_detection_pb2_grpc.add_FaceDetectionServicer_to_server(FaceDetection(), server)
    server.add_insecure_port('[::]:50055')
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
