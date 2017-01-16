#include "Skinduino.h"

int incomingByte = 0;
Skinduino skinduino;

void setup() { 
  Wire.begin();
  Serial.begin(115200);
  Serial1.begin(115200);
}

void loop() { 
  skinduino.readSensorValues(); 

  printSerialChar(skinduino.sensorValues[0]);
  for(int i=1; i<15; i++){
    printSerialString(",");
    printSerialChar(skinduino.sensorValues[i]);
  }

  newlineSerial();

  // set baseline
  getCommand();
  if (incomingByte == '1') { 
    skinduino.setCapacitiveBaseline();
    delay(1000);
  }
    
} //end loop

void getCommand() {
  if (Serial.available() > 0) {
    incomingByte = Serial.read();
  } else if (Serial1.available() > 0) {
    incomingByte = Serial1.read();
  } else {
    incomingByte = -1;
  }
}

void printSerialString(String s) {
  Serial.print(s);
  Serial1.print(s);
}

void printSerialChar(unsigned char c) {
  Serial.print(c);
  Serial1.print(c);
}

void newlineSerial() {
  Serial.println();
  Serial1.println();
}
