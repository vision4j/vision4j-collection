from concurrent import futures
import time
import grpc
import completion_pb2
import completion_pb2_grpc
import sys
import subprocess

from completion import GimpCompletion

_ONE_DAY_IN_SECONDS = 60 * 60 * 24

model = GimpCompletion()

class Completion(completion_pb2_grpc.CompletionServicer):

    def Complete(self, request, context):
        return model.complete_request(request)


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=1))
    completion_pb2_grpc.add_CompletionServicer_to_server(Completion(), server)
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
