import cv2
import numpy as np
import face_detection_pb2
import face_recognition

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
        pass


    def detect(self, img):
        return face_recognition.face_locations(img)


    def detect_on_deserialized(self, request, deserialized):
        img = deserialized
        face_locations = self.detect(img)
        i = 0
        res = {}
        for face_location in face_locations:
            top, right, bottom, left = face_location
            key = 'auto' + str(i)
            res[key] = face_detection_pb2.BoundingBoxes()
            bbox = res[key].boundingBoxes.add()
            bbox.left = left
            bbox.right = right
            bbox.top = top
            bbox.bottom = bottom
            i += 1

        return res



    def detect_request(self, request):
        deserialized = deserialize(request)
        result = self.detect_on_deserialized(request, deserialized)
        return serialize(result)
