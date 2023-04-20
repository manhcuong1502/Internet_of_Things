print("Iot Project 221")
import sys
from Adafruit_IO import MQTTClient
import time
import random
from simple_ai import *
from uart import *

AIO_FEED_IDs = ["nutnhan1", "nutnhan2"]
AIO_USERNAME = "tranngocminhdiep"
AIO_KEY = "aio_TQtX18Fi5jfFBqw7ocxKgldFdwwq"

def connected(client):
    print("Ket noi thanh cong ...")
    for topic in AIO_FEED_IDs:
        client.subscribe(topic)

def subscribe(client , userdata , mid , granted_qos):
    print("Subscribe thanh cong ...")

def disconnected(client):
    print("Ngat ket noi ...")
    sys.exit (1)

def message(client , feed_id , payload):
    print("Nhan du lieu: " + payload + " , feed id: " + feed_id)
    #gui du lieu xuong phan cung
    if feed_id == "nutnhan1": #dinh nghia 4 lenh khac nhau danh cho 2 nutnhan
        if payload == "0":
            writeData("1")
        else:
            writeData("2")
    if feed_id == "nutnhan2":
        if payload == "0":
            writeData("3")
        else:
            writeData("4")

client = MQTTClient(AIO_USERNAME , AIO_KEY)
client.on_connect = connected
client.on_disconnect = disconnected
client.on_message = message
client.on_subscribe = subscribe
client.connect()
client.loop_background()
counter = 10
sensor_type = 0
counter_ai = 5 #5s detect 1 lan
ai_result = ""
previous_result = ""
while True:
    # counter = counter - 1 #gưi cam bien len server (server la feed)
    # if counter <= 0:
    #     counter = 5 #wait 5s
    #     #TODO
    #     print("Random data is publishing...")
    #     if sensor_type == 0: # print temp, then light and humid
    #         print("Temperature: ")
    #         temp = random.randint(0,50)
    #         client.publish("cambien1", temp)
    #         sensor_type == 1
    #     elif sensor_type == 1:
    #         print("Light tensity: ")
    #         light = random.randint(50,100000)
    #         client.publish("cambien2", light)
    #         sensor_type == 2
    #     elif sensor_type == 2:
    #         print("Humidity: ")
    #         humid = random.randint(0,100)
    #         client.publish("cambien3", humid)
    #         sensor_type == 0

    counter_ai = counter_ai - 1 #Phan AI
    if counter_ai <= 0:
        counter_ai = 5
        previous_result = ai_result
        ai_result = image_detector()
        print("AI Output: ", ai_result)
        if previous_result != ai_result: #neu khac gia tri cu thi moi cap nhat
            client.publish("ai", ai_result)

    readSerial(client) #nhan du lieu tu phan cung va gui len server
    time.sleep(1)
    pass
