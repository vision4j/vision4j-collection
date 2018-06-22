from concurrent import futures
import time
import grpc
import segmentation_pb2
import segmentation_pb2_grpc
import sys
import subprocess

from segmentation import MaskRCNNSegmentation

_ONE_DAY_IN_SECONDS = 60 * 60 * 24

model = MaskRCNNSegmentation('/app/mask_rcnn_coco.h5')

class Segmentation(segmentation_pb2_grpc.SegmentationServicer):

    def Segment(self, request, context):
        return model.segment_request(request)


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=1))
    segmentation_pb2_grpc.add_SegmentationServicer_to_server(Segmentation(), server)
    server.add_insecure_port('[::]:50053')
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
