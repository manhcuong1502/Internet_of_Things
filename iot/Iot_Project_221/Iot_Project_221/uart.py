import serial.tools.list_ports

def getPort():
    ports = serial.tools.list_ports.comports()
    N = len(ports)
    commPort = "None"
    for i in range(0, N):
        port = ports[i]
        strPort = str(port)
        if "USB Serial Device" in strPort:
            splitPort = strPort.split(" ")
            commPort = (splitPort[0])
    # return commPort : Neu ket noi voi phan cung thi xai cmd nay, con cmd duoi la minh biet trc(co san)
    return "COM3"

if getPort() != "None": #kiem tra neu
    ser = serial.Serial( port=getPort(), baudrate=115200) #open COM gate
    print(ser)

mess = ""
def processData(client, data): #do ngoai main nen phai co them client
    data = data.replace("!", "")
    data = data.replace("#", "")
    splitData = data.split(":")
    print(splitData)
    if splitData[1] == "T": #neu ma doc duoc chu 'T' thi nhan du lieu sau do
        client.publish("cambien1", splitData[2]) #temp
    elif splitData[1] == "L":
        client.publish("cambien2", splitData[2]) #light
    elif splitData[1] == "H":
        client.publish("cambien3", splitData[2]) #humid
mess = ""
def readSerial(client): #doc du lieu tu cong COM, doc tat ca nhung gi trong buffer
    bytesToRead = ser.inWaiting()
    if (bytesToRead > 0):
        global mess
        mess = mess + ser.read(bytesToRead).decode("UTF-8")
        while ("#" in mess) and ("!" in mess):
            start = mess.find("!")
            end = mess.find("#")
            processData(client, mess[start:end + 1])
            if (end == len(mess)):
                mess = ""
            else:
                mess = mess[end+1:]

def writeData(data):
    ser.write(str(data).encode()) #truy cap con tro serial nay de write data