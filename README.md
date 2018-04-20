# vision4j-collection
Collection of computer vision models, ready to be included in a JVM project. The idea is to maintain
a list of implementations for different computer vision problems in a plug-and-play format.

## Problems

### Image classification

By a given image, find the category that the image belongs to. For example, if a model is trained to recognize
the categories: `lion`, `cheetah` and `tiger`, when given an image in one of those categories, it can recognize it.

| Input        | Output           
| ------------- |:-------------:| 
| ![alt text](img/lion.resized.jpg) | lion 
| ![alt text](img/cheetah.resized.jpg) | cheetah
| ![alt text](img/tiger.resized.jpg) | tiger

Implementations available for this problem:

#### Pretrained VGG16 on ImageNet using DeepLearning4j

Trained on the [ImageNet](www.image-net.org/) dataset



