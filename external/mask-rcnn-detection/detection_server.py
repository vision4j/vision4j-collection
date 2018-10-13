from concurrent import futures
import time
import grpc
import detection_pb2
import detection_pb2_grpc
import sys
import subprocess

from detection import MaskRCNNDetection

_ONE_DAY_IN_SECONDS = 60 * 60 * 24

model = MaskRCNNDetection('/app/mask_rcnn_coco.h5')

class Detection(detection_pb2_grpc.DetectionServicer):

    def Detect(self, request, context):
        return model.detect_request(request)


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=1))
    detection_pb2_grpc.add_DetectionServicer_to_server(Detection(), server)
    server.add_insecure_port('[::]:50054')
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
