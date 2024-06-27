"""
Copyright (c) 2019-present NAVER Corp.
MIT License
"""
# -*- coding: utf-8 -*-
import time
import argparse
import torch
import torch.backends.cudnn as cudnn
from torch.autograd import Variable
from PIL import Image

import cv2
import numpy as np
import craft_utils
import imgproc
import file_utils

from craft import CRAFT

from collections import OrderedDict

from predict import predict_text, delete_directory


def copyStateDict(state_dict):
    if list(state_dict.keys())[0].startswith("module"):
        start_idx = 1
    else:
        start_idx = 0
    new_state_dict = OrderedDict()
    for k, v in state_dict.items():
        name = ".".join(k.split(".")[start_idx:])
        new_state_dict[name] = v
    return new_state_dict

def str2bool(v):
    return v.lower() in ("yes", "y", "true", "t", "1")

parser = argparse.ArgumentParser(description='CRAFT Text Detection')
parser.add_argument('--trained_model', default='weights/craft_mlt_25k.pth', type=str, help='pretrained model')
parser.add_argument('--text_threshold', default=0.7, type=float, help='text confidence threshold')
parser.add_argument('--low_text', default=0.4, type=float, help='text low-bound score')
parser.add_argument('--link_threshold', default=0.4, type=float, help='link confidence threshold')
parser.add_argument('--cuda', default=True, type=str2bool, help='Use cuda for inference')
parser.add_argument('--canvas_size', default=1280, type=int, help='image size for inference')
parser.add_argument('--mag_ratio', default=1.5, type=float, help='image magnification ratio')
parser.add_argument('--poly', default=False, action='store_true', help='enable polygon type')
parser.add_argument('--show_time', default=False, action='store_true', help='show processing time')
parser.add_argument('--test_folder', default='/dataas/', type=str, help='folder path to input images')
parser.add_argument('--refine', default=False, action='store_true', help='enable link refiner')
parser.add_argument('--refiner_model', default='weights/craft_refiner_CTW1500.pth', type=str, help='pretrained refiner model')

args = parser.parse_args()




def test_net(net, image, text_threshold, link_threshold, low_text, cuda, poly, refine_net=None):
    t0 = time.time()

    # resize
    img_resized, target_ratio, size_heatmap = imgproc.resize_aspect_ratio(image, args.canvas_size, interpolation=cv2.INTER_LINEAR, mag_ratio=args.mag_ratio)
    ratio_h = ratio_w = 1 / target_ratio

    # preprocessing
    x = imgproc.normalizeMeanVariance(img_resized)
    x = torch.from_numpy(x).permute(2, 0, 1)    # [h, w, c] to [c, h, w]
    x = Variable(x.unsqueeze(0))                # [c, h, w] to [b, c, h, w]
    if cuda:
        x = x.cuda()

    # forward pass
    with torch.no_grad():
        y, feature = net(x)

    # make score and link map
    score_text = y[0,:,:,0].cpu().data.numpy()
    score_link = y[0,:,:,1].cpu().data.numpy()

    # refine link
    if refine_net is not None:
        with torch.no_grad():
            y_refiner = refine_net(y, feature)
        score_link = y_refiner[0,:,:,0].cpu().data.numpy()

    t0 = time.time() - t0
    t1 = time.time()

    # Post-processing
    boxes, polys = craft_utils.getDetBoxes(score_text, score_link, text_threshold, link_threshold, low_text, poly)

    # coordinate adjustment
    boxes = craft_utils.adjustResultCoordinates(boxes, ratio_w, ratio_h)
    polys = craft_utils.adjustResultCoordinates(polys, ratio_w, ratio_h)
    for k in range(len(polys)):
        if polys[k] is None: polys[k] = boxes[k]

    t1 = time.time() - t1

    # render results (optional)
    render_img = score_text.copy()
    render_img = np.hstack((render_img, score_link))
    ret_score_text = imgproc.cvt2HeatmapImg(render_img)

    if args.show_time : print("\ninfer/postproc time : {:.3f}/{:.3f}".format(t0, t1))

    return boxes, polys, ret_score_text

import os
import pyrebase


def model_predicts(images):
        net = CRAFT()  # initialize
        data = []

        result_folder = './demo/'
        if not os.path.isdir(result_folder):
            os.mkdir(result_folder)
        print('Loading weights from checkpoint (' + args.trained_model + ')')
        if args.cuda:
            net.load_state_dict(copyStateDict(torch.load(args.trained_model)))
        else:
            net.load_state_dict(copyStateDict(torch.load(args.trained_model, map_location='cpu')))

        if args.cuda:
            net = net.cuda()
            net = torch.nn.DataParallel(net)
            cudnn.benchmark = False

        net.eval()

        # LinkRefiner
        refine_net = None
        if args.refine:
            from refinenet import RefineNet
            refine_net = RefineNet()
            print('Loading weights of refiner from checkpoint (' + args.refiner_model + ')')
            if args.cuda:
                refine_net.load_state_dict(copyStateDict(torch.load(args.refiner_model)))
                refine_net = refine_net.cuda()
                refine_net = torch.nn.DataParallel(refine_net)
            else:
                refine_net.load_state_dict(copyStateDict(torch.load(args.refiner_model, map_location='cpu')))

            refine_net.eval()
            args.poly = True

        t = time.time()

        cropped_images = []
        image = images
    #for k, image in enumerate(images):
        print(image.shape)
        print("Test image {:d}/{:d}".format( 1, len(images)), end='\r')

        bboxes, polys, score_text = test_net(net, image, args.text_threshold, args.link_threshold, args.low_text,
                                             args.cuda, args.poly, refine_net)

        # save score text
        for i, box in enumerate(bboxes):
            # Convert coordinates to integers
            box = box.astype(int)
            # Crop the image using the box coordinates
            cropped_image = image[box[1][1]:box[3][1], box[0][0]:box[2][0]]
            cropped_image = Image.fromarray(cropped_image)
            cropped_images.append(cropped_image)
        print(len(cropped_images))
        data_array = np.array(data, dtype=int)
        words = predict_text(cropped_images)
        print(words)

        print("elapsed time : {}s".format(time.time() - t))


        #data_array = np.column_stack((data_array, words_array))

        return data_array, cropped_images
image = imgproc.loadImage('./dataas/Alex.jpg')
print(image.shape)
model_predicts(image)
