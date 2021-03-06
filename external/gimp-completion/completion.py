
from gimpfu import *
import gimpcolor
import time
import sys
import shortuuid
import completion_pb2
import numpy as np
import cv2

from PIL import Image
from scipy.ndimage.morphology import binary_dilation, binary_erosion


def drawable_of_file(filename):
    '''
    Load a file and return the the drawable.
    A call to this can be used as an actual parameter, see instance below.
    '''
    image = pdb.gimp_file_load(filename, filename, run_mode=RUN_NONINTERACTIVE)
    pdb.gimp_image_flatten(image)
    drawable = pdb.gimp_image_get_active_layer(image)
    return drawable


def drawable_of_file_with_anti_selection(filename, select):
    '''
    Load a file and return the the drawable.
    A call to this can be used as an actual parameter, see instance below.
    '''
    image = pdb.gimp_file_load(filename, filename, run_mode=RUN_NONINTERACTIVE)
    pdb.gimp_image_flatten(image)
    pdb.gimp_rect_select(image, select[0], select[1], select[2], select[3], 0, False, 0)
    pdb.gimp_selection_invert(image)
    drawable = pdb.gimp_image_get_active_layer(image)
    return drawable


def completion(infilepath, mask_filepath, outfilepath):
    # since some tests use the same input file, cat the testname to make an outfilename
    # out and reference have same name, different directories
    # open test file
    try:
        image = pdb.gimp_file_load(infilepath, infilepath, run_mode=RUN_NONINTERACTIVE)
        drawable = pdb.gimp_image_get_active_layer(image)
        mask = pdb.gimp_file_load_layer(image, mask_filepath)
        pdb.gimp_image_add_layer(image, mask, 0)
        pdb.gimp_image_select_color(image, CHANNEL_OP_REPLACE, mask, gimpcolor.RGB(255, 255, 255))
        pdb.gimp_image_remove_layer(image, mask)
        # if select is not None:
        #   # Make selection    x,y, width, height
        #   # pdb.gimp_rect_select(image, 100, 90, 100, 50, 0, False, 0)
        #   pdb.gimp_rect_select(image, select[0], select[1], select[2], select[3], 0, False, 0)
    except:
        print("IMPROPER preprocessing")
        return

    # Invoke the test
    # Formerly: eval(teststring) but eval only takes expressions, not statements
    start = time.time()
    try:
        # exec teststring
        pdb.python_fu_heal_selection(image, drawable, 50, 1, 1, run_mode=RUN_NONINTERACTIVE)
    except RuntimeError:
        print ("EXCEPTION")
        return
    print("Completion time: " + str(time.time() - start))


    # test post processing
    try:
        # !!! Refresh drawable in case the plugin returned a new image
        drawable = pdb.gimp_image_get_active_drawable(image)

        # Save altered or new image in a temp directory.
        # !!! Why do you need to pass drawable, doesn't it save all the layers?  This bit me.
        if pdb.gimp_drawable_has_alpha(drawable):
            pdb.gimp_image_flatten(image)  # Since saving ppm, get rid of alpha
            drawable = pdb.gimp_image_get_active_drawable(image)
        pdb.gimp_file_save(image, drawable, outfilepath, outfilepath, run_mode=RUN_NONINTERACTIVE)
        pdb.gimp_image_delete(image)
    except:
        print("IMPROPER post processing")
        return


def inpaint(image_id, img, prediction):
    int_mask = (prediction != 0).astype(np.uint8)
    binary_dilated = binary_dilation(int_mask, iterations=10).astype(np.uint8) * 255
    input_filename = 'inputs/{}.png'.format(image_id)
    Image.fromarray(img).save(input_filename)
    mask_filename = 'masks/{}.png'.format(image_id)
    Image.fromarray(binary_dilated).save(mask_filename)
    output_filename = 'results/{}.png'.format(image_id)
    completion(input_filename, mask_filename, output_filename)


def create_image_from_protobuf(image):
    data = image.image_data
    shape = (image.width, image.height, image.channels)
    nparr = np.fromstring(data, np.uint8)
    img = cv2.cvtColor(cv2.imdecode(nparr, cv2.IMREAD_COLOR), cv2.COLOR_BGR2RGB)
    return img.astype(np.uint8)


def deserialize(request):
    return create_image_from_protobuf(request.image), create_image_from_protobuf(request.mask)


def serialize(result):
    return completion_pb2.CompletedImage(result=result)


class GimpCompletion(object):

    def __init__(self):
        pass


    def complete(self, image_id, image, mask):
        inpaint(image_id, image, mask)
        output_filename = 'results/{}.png'.format(image_id)
        with open(output_filename, "rb") as f:
            return f.read()


    def complete_on_deserialized(self, request, deserialized):
        image_id = shortuuid.uuid()
        image, mask = deserialized
        result = self.complete(image_id, image, mask)
        return result


    def complete_request(self, request):
        deserialized = deserialize(request)
        result = self.complete_on_deserialized(request, deserialized)
        return serialize(result)
