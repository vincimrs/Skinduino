/*pending functions to test
 - change device addr (OK!)
 - reset bias
 */

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

// TODO find out what this is
/*
unsigned char touchRegisters [4] = {'1','1','1','1'}; 
uint8_t i2caddr_ic2 = 0x77;
unsigned char c = 'a'; 
unsigned int touchX = 0; 
unsigned int touchY = 0;

void sendCommand(uint8_t c) { 
  Wire.beginTransmission(i2caddr_default);
  Wire.write(0x04);
  Wire.write(0x80);
  Wire.endTransmission();
}

void readTransaction() { 
  Wire.beginTransmission(i2caddr_default); 
  Wire.write(0x80);
  Wire.endTransmission(false);
  Wire.requestFrom(0x25,14);  
}

void readXYPosition() { 
  counter = 0;
  Wire.beginTransmission(i2caddr_default); 
  Wire.write(0x11);
  Wire.endTransmission(false);
  Wire.requestFrom(0x25,3);  
  while(Wire.available()) { 
    touchRegisters[counter]= Wire.read();
    counter++;

  }
  unsigned char ylsb = (touchRegisters[2] & B00001111); 
  unsigned char xlsb = (touchRegisters[2]>>4) & B00001111; 
  touchX = (touchRegisters[0]<<4) + xlsb;
  touchY= (touchRegisters[1]<<4) + ylsb;
}

void readXYPosition_IC2() { 
  counter = 0;
  Wire.beginTransmission(i2caddr_ic2); 
  Wire.write(0x11);
  Wire.endTransmission(false);
  Wire.requestFrom(0x77,3);  
  while(Wire.available()) { 
    touchRegisters[counter]= Wire.read();
    counter++;

  }
  unsigned char ylsb = (touchRegisters[2] & B00001111); 
  unsigned char xlsb = (touchRegisters[2]>>4) & B00001111; 
  touchX = (touchRegisters[0]<<4) + xlsb;
  touchY = (touchRegisters[1]<<4) + ylsb;
}//end readXYPostion_IC2
*/

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

//Code to change the address from 0x25 to 0x77
/*
#include <Wire.h>
int incomingByte = 0;

void setup() { 
  Wire.begin();
  Serial.begin(9600);
  Serial.println(" "); 
  Serial.println("Hello capSence_Change_address"); 
  Serial.println("press 'a' to change address");  
}//end setup
  
void loop() { 
  if (Serial.available() > 0) {
      incomingByte = Serial.read();
      if (incomingByte == 0x61) { 
          changeI2Caddr(0x25,0x77); //old, new
          Serial.println("address changed to 0x77");
       } //end if
                
  }//end if serial.avalable 
}

void changeI2Caddr(uint8_t oldAddress, uint8_t newAddress){
 // if(i2cAddrChange == 0){
    Wire.beginTransmission(oldAddress);
    Wire.write(0x43); //Register 0x43 has the address. 
    Wire.write(newAddress);
    Wire.endTransmission();
    
    //save to memory
    Wire.beginTransmission(oldAddress);
    Wire.write(0x04); 
    Wire.write(0x80); 
    Wire.endTransmission();
}
*/

