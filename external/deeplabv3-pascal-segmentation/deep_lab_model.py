import os
import tarfile

import numpy as np
import cv2
import tensorflow as tf
from PIL import Image
from io import BytesIO
import segmentation_pb2



_FROZEN_GRAPH_NAME = 'frozen_inference_graph'


def deserialize(data, shape):
    nparr = np.fromstring(data, np.uint8)
    img = cv2.cvtColor(cv2.imdecode(nparr, cv2.IMREAD_COLOR), cv2.COLOR_BGR2RGB)
    return Image.fromarray(img)


def serialize(result):
    return segmentation_pb2.SegmentationArray(result=result)


class DeepLabModel(object):
    """Class to load deeplab model and run inference."""

    INPUT_TENSOR_NAME = 'ImageTensor:0'
    OUTPUT_TENSOR_NAME = 'SemanticPredictions:0'
    INPUT_SIZE = 513

    def __init__(self, tarball_path):
        """Creates and loads pretrained deeplab model."""
        self.graph = tf.Graph()

        graph_def = None
        # Extract frozen graph from tar archive.
        tar_file = tarfile.open(tarball_path)
        for tar_info in tar_file.getmembers():
            if _FROZEN_GRAPH_NAME in os.path.basename(tar_info.name):
                file_handle = tar_file.extractfile(tar_info)
                graph_def = tf.GraphDef.FromString(file_handle.read())
                break

        tar_file.close()

        if graph_def is None:
            raise RuntimeError('Cannot find inference graph in tar archive.')

        with self.graph.as_default():
            tf.import_graph_def(graph_def, name='')

        self.sess = tf.Session(graph=self.graph)

    def run(self, image):
        """Runs inference on a single image.

        Args:
            image: A PIL.Image object, raw input image.

        Returns:
            resized_image: RGB image resized from original input image.
            seg_map: Segmentation map of `resized_image`.
        """
        width, height = image.size
        resize_ratio = 1.0 * self.INPUT_SIZE / max(width, height)
        target_size = (int(resize_ratio * width), int(resize_ratio * height))
        resized_image = image.convert('RGB').resize(target_size, Image.ANTIALIAS)
        batch_seg_map = self.sess.run(
            self.OUTPUT_TENSOR_NAME,
            feed_dict={self.INPUT_TENSOR_NAME: [np.asarray(resized_image)]})
        seg_map = batch_seg_map[0]
        return resized_image, seg_map

    def predict(self, img, original_width_height):
        resized_image, prediction = self.run(img)
        np_prediction = np.asarray(prediction, dtype=np.uint8)
        return Image.fromarray(np_prediction).resize(original_width_height)

    def predict_request(self, request):
        img = deserialize(request.image_data, (request.width, request.height, request.channels))
        original_width_height = (request.original_width, request.original_height)
        resized_prediction = self.predict(img, original_width_height)
        imgByteArr = BytesIO()
        resized_prediction.save(imgByteArr, format='png')
        imgByteArr = imgByteArr.getvalue()
        return serialize(imgByteArr)