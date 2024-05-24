import tensorflow as tf
from tensorflow.keras.models import load_model
from tensorflow.keras.preprocessing import image

def load_model_and_predict(image_tensor):
    # Load the model
    model = load_model('ag_detection.h5')

    # Ensure the image tensor is of the right shape for the model
    img = tf.expand_dims(image_tensor, axis=0)

    # Make a prediction
    prediction = model.predict(img)

    # Return the class with the highest probability
    return tf.argmax(prediction, axis=1).numpy()[0]