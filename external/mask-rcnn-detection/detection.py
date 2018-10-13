import detection_pb2
import cv2
import numpy as np
from PIL import Image
from io import BytesIO
import tensorflow as tf
import keras

import mrcnn.model as modellib
from mrcnn.config import Config

# source: https://github.com/matterport/Mask_RCNN/commit/cbff80f3e3f653a9eeee43d0d383a0385aba546b
class CocoConfig(Config):
    """Configuration for training on MS COCO.
    Derives from the base Config class and overrides values specific
    to the COCO dataset.
    """
    # Give the configuration a recognizable name
    NAME = "coco"

    # We use a GPU with 12GB memory, which can fit two images.
    # Adjust down if you use a smaller GPU.
    IMAGES_PER_GPU = 2

    # Uncomment to train on 8 GPUs (default is 1)
    # GPU_COUNT = 8

    # Number of classes (including background)
    NUM_CLASSES = 1 + 80  # COCO has 80 classes


class InferenceConfig(CocoConfig):
    # Set batch size to 1 since we'll be running inference on
    # one image at a time. Batch size = GPU_COUNT * IMAGES_PER_GPU
    GPU_COUNT = 1
    IMAGES_PER_GPU = 1


def deserialize(request):
    data = request.image_data
    shape = (request.width, request.height, request.channels)
    nparr = np.fromstring(data, np.uint8)
    img = cv2.cvtColor(cv2.imdecode(nparr, cv2.IMREAD_COLOR), cv2.COLOR_BGR2RGB)
    return img.astype(np.uint8)


def serialize(result):
    return detection_pb2.DetectionBoundingBoxes(categoriesToBoundingBoxes=result)


class MaskRCNNDetection(object):

    def __init__(self, weights_path):
        config = InferenceConfig()
        self.model = modellib.MaskRCNN(mode="inference", model_dir='.', config=config)
        self.model.load_weights(weights_path, by_name=True)
        self.model.keras_model._make_predict_function()


    def detect(self, img):
        results = self.model.detect([img])
        return results[0]


    def detect_on_deserialized(self, request, deserialized):
        img = deserialized
        r = self.detect(img)
        masks = r['rois']
        class_ids = r['class_ids']
        res = {}

        n = len(masks) # does not matter the len of masks or of class_ids
        for i in range(n):
            mask = masks[i]
            class_id = class_ids[i]

            if not class_id in res:
                res[class_id] = detection_pb2.BoundingBoxes()

            bounding_box = res[class_id].boundingBoxes.add()
            bounding_box.left = mask[0]
            bounding_box.top = mask[1]
            bounding_box.right = mask[2]
            bounding_box.bottom = mask[3]

        return res


    def detect_request(self, request):
        deserialized = deserialize(request)
        result = self.detect_on_deserialized(request, deserialized)
        return serialize(result)
