import segmentation_pb2
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
    return segmentation_pb2.SegmentationArray(result=result)



class MaskRCNNSegmentation(object):

    def __init__(self, weights_path):
        config = InferenceConfig()
        self.model = modellib.MaskRCNN(mode="inference", model_dir='.', config=config)
        self.model.load_weights(weights_path, by_name=True)
        self.model.keras_model._make_predict_function()


    def segment(self, img):
        results = self.model.detect([img])
        return results[0]


    def segment_on_deserialized(self, request, deserialized):
        img = deserialized
        r = self.segment(img)
        masks = r['masks'].astype(np.uint8)
        class_ids = r['class_ids'].astype(np.uint8)
        res = (masks * class_ids)
        converted = np.sum(res, axis=2, dtype=np.uint8)
        converted_pil_image = Image.fromarray(converted)
        imgByteArr = BytesIO()
        converted_pil_image.save(imgByteArr, format='png')
        imgByteArr = imgByteArr.getvalue()
        return imgByteArr


    def segment_request(self, request):
        deserialized = deserialize(request)
        result = self.segment_on_deserialized(request, deserialized)
        return serialize(result)
