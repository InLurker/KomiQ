from gradio_client import Client, file



client = Client("https://ocr-experiments-paddleocr.hf.space/--replicas/6yxd0/")

def enqueuePaddleOCR(image_path, language):
    # Send the image file path to the OCR API and receive the result
    result = client.predict(
        file(image_path),
        language,
        api_name="/predict",
    )
    return result
