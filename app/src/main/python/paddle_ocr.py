from gradio_client import Client, file



client = Client("https://ocr-experiments-paddleocr.hf.space/")

def enqueue_paddle_ocr_text_detection(image_path, language):
    # Send the image file path to the OCR API and receive the result
    result = client.predict(
        file(image_path),
        language,
        api_name="/predict",
    )
    return result[1]

def enqueue_paddle_ocr_text_recognition(image_path, language):
    # Send the image file path to the OCR API and receive the result
    result = client.predict(
        file(image_path),
        language,
        api_name="/predict",
    )
    return result[2]