#include <Wire.h>

uint8_t i2caddr_default = 0x25;
unsigned char sensorValues[15];// = {'1','1','1','1','1','1','1','1','1','1','1','1','1','1','1'}; 
int counter = 0;
int incomingByte = 0;

void setup() { 
  Wire.begin();
  Serial.begin(115200);
  Serial1.begin(115200);
}

void loop() { 
  setSensorValuesToZero();
  readSensorValues(); 

  printSerialChar(sensorValues[0]);
  for(int i=1; i<15; i++){
    printSerialString(",");
    printSerialChar(sensorValues[i]);
  }

  newlineSerial();

  // set baseline
  getCommand();
  if (incomingByte == '1') { 
    setBaseline(i2caddr_default);
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

void readSensorValues() { 
  counter = 0;
  Wire.beginTransmission(i2caddr_default); 
  Wire.write(0x80);
  Wire.endTransmission(false);
  Wire.requestFrom(0x25,15);  
  while(Wire.available()) { 
    sensorValues[counter]= Wire.read();
    counter++;
  }
}

// TODO is this necessary??
void setSensorValuesToZero() { 
  for(int i=0; i<15; i++){
    sensorValues[i] = 0;
  }  
} //end setSensorValuesToZero

void setBaseline(uint8_t address) { 
  Wire.beginTransmission(address);
  Wire.write(0x04);
  Wire.write(0x01);
  Wire.endTransmission();
}

