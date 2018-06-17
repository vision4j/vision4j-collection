from keras.applications.vgg16 import VGG16
from keras.applications.vgg16 import preprocess_input
from keras.preprocessing import image
from time import time
from PIL import Image
import numpy as np
import cv2
import tensorflow as tf
from keras.backend.tensorflow_backend import set_session

def deserialize(data, shape):
    nparr = np.fromstring(data, np.uint8)
    img = cv2.cvtColor(cv2.imdecode(nparr, cv2.IMREAD_COLOR), cv2.COLOR_BGR2RGB).reshape(shape).astype(np.float64)
    return img


class Vgg16Classifier(object):

    def __init__(self):
        self.model = VGG16()
        self.model._make_predict_function()

    def predict(self, request):
        img = deserialize(request.image_data, (request.width, request.height, request.channels))
        img = np.expand_dims(preprocess_input(img), axis=0)
        out = self.model.predict(img)
        idx = np.argmax(out)
        return idx

    def predict_image(self, img):
        img = np.expand_dims(preprocess_input(img), axis=0) 
        out = self.model.predict(img)
        return out

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