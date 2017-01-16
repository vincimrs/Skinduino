#include <Skinduino.h>

const int NUM_CAPTOUCH_PINS = 8;
int incomingByte = 0;
Skinduino skinduino;

void setup() { 
  Wire.begin();
  Serial.begin(115200);
  Serial1.begin(115200);
}

void loop() { 
  skinduino.readCapacitiveTouchValues(); 

  printSerialChar(skinduino.capacitiveTouchValues[0]);
  for(int i=1; i<NUM_CAPTOUCH_PINS; i++){
    printSerialString(",");
    printSerialChar(skinduino.capacitiveTouchValues[i]);
  }

  newlineSerial();

  // set baseline
  getCommand();
  if (incomingByte == '1') { 
    skinduino.setCapacitiveTouchBaseline();
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