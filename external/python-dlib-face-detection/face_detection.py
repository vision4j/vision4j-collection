import cv2
import numpy as np
import face_detection_pb2

def deserialize(request):
    data = request.image_data
    shape = (request.width, request.height, request.channels)
    nparr = np.fromstring(data, np.uint8)
    img = cv2.cvtColor(cv2.imdecode(nparr, cv2.IMREAD_COLOR), cv2.COLOR_BGR2RGB)
    return img.astype(np.uint8)


def serialize(result):
    return face_detection_pb2.DetectedFaces(facesMap=result)


class DlibFaceDetection(object):

    def __init__(self):
        raise NotImplementedError


    def detect(self):
        raise NotImplementedError


    def detect_on_deserialized(self, request, deserialized):
        raise NotImplementedError


    def detect_request(self, request):
        deserialized = deserialize(request)
        result = self.detect_on_deserialized(request, deserialized)
        return serialize(result)
