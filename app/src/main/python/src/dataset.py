import os
import glob

import torch
from torch.utils.data import Dataset

import cv2
from PIL import Image
import numpy as np


class Synth90kDataset(Dataset):
    CHARS = '0123456789abcdefghijklmnopqrstuvwxyz'
    CHAR2LABEL = {char: i + 1 for i, char in enumerate(CHARS)}
    LABEL2CHAR = {label: char for char, label in CHAR2LABEL.items()}

    def __init__(self, images=None, img_height=32, img_width=100):
        self.images = images
        self.img_height = img_height
        self.img_width = img_width

    def __len__(self):
        return len(self.images)

    def __getitem__(self, index):
        image = self.images[index]

        # Convert PIL Image to grayscale if it's not
        if image.mode != 'L':
            image = image.convert('L')

        image = image.resize((self.img_width, self.img_height), resample=Image.BILINEAR)
        image = np.array(image)
        image = image.reshape((1, self.img_height, self.img_width))
        image = (image / 127.5) - 1.0

        image = torch.FloatTensor(image)
        return image

def synth90k_collate_fn(batch):
    images, targets, target_lengths = zip(*batch)
    images = torch.stack(images, 0)
    targets = torch.cat(targets, 0)
    target_lengths = torch.cat(target_lengths, 0)
    return images, targets, target_lengths
