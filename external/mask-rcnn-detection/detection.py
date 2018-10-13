def deserialize(request):
    raise NotImplementedError

def serialize(result):
    raise NotImplementedError


class MaskRCNNDetection(object):

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
