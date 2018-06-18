from keras.applications.vgg16 import VGG16
from keras.applications.vgg16 import preprocess_input
from keras.preprocessing import image
from time import time
from PIL import Image
import numpy as np
import cv2
import tensorflow as tf
from keras.backend.tensorflow_backend import set_session
import classification_pb2


def deserialize(request):
    data = request.image_data
    shape = (request.width, request.height, request.channels)
    nparr = np.fromstring(data, np.uint8)
    img = cv2.cvtColor(cv2.imdecode(nparr, cv2.IMREAD_COLOR), cv2.COLOR_BGR2RGB).reshape(shape).astype(np.float64)
    return img

def serialize(result):
    return classification_pb2.Prediction(index=result)


class Vgg16Classifier(object):

    def __init__(self):
        self.model = VGG16()
        self.model._make_predict_function()

    def predict(self, img):
        return self.model.predict(img)

    def predict_on_deserialized(self, request, deserialized):
        img = deserialized
        img = np.expand_dims(preprocess_input(img), axis=0) 
        out = self.predict(img)
        idx = np.argmax(out)
        return idx

    def predict_request(self, request):
        deserialized = deserialize(request)
        prediction = self.predict_on_deserialized(request, deserialized)
        return serialize(prediction)


def main():
    model = Vgg16Classifier()

    total = 0.
    n = 100

    for i in range(n):
        start = time()
        img_path = '/home/hvrigazov/vision4j/vision4j-collection/img/cheetah.resized.jpg'
        img = image.load_img(img_path, target_size=(224, 224))
        img = preprocess_input(image.img_to_array(img))
        print(img)
        print(np.argmax(model.predict_image(img)))
        total += (time() - start)
        break
        

    print(str(total / n))

if __name__ == '__main__':
    main()