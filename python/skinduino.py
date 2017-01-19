import serial
import sys

class Skinduino:
    def __init__(self):
        self.ser = None

    def _check_if_connected(self):
        if self.ser == None:
            sys.stderr.write('Error: Not connected to Skinduino')
            sys.exit(1)

    def connect(self, port):
        self.ser = serial.Serial(port, 115200)

    def setCapacitiveTouchBaseline(self):
        self._check_if_connected()
        self.ser.write('1')

    def readCapacitiveTouchValues(self):
        self._check_if_connected()
        line = self.ser.readline()
        toks = line.split(',')
        values = []

        for t in toks:
            values.append(int(t))

        return values