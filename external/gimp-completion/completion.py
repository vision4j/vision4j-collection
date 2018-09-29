def deserialize(request):
    raise NotImplementedError

def serialize(result):
    raise NotImplementedError


class GimpCompletion(object):

    def __init__(self):
        raise NotImplementedError


    def complete(self):
        raise NotImplementedError


    def complete_on_deserialized(self, request, deserialized):
        raise NotImplementedError


    def complete_request(self, request):
        deserialized = deserialize(request)
        result = self.complete_on_deserialized(request, deserialized)
        return serialize(result)
