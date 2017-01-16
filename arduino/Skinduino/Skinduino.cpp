#include <Skinduino.h>

void Skinduino::setCapacitiveTouchBaseline() { 
  Wire.beginTransmission(_i2caddr_default);
  Wire.write(0x04);
  Wire.write(0x01);
  Wire.endTransmission();
}

void Skinduino::readCapacitiveTouchValues() {
  resetSensorValues();
  
  _counter = 0;
  Wire.beginTransmission(_i2caddr_default); 
  Wire.write(0x81); ///only the 1-8 are broken out
  Wire.endTransmission(false);
  Wire.requestFrom(0x25,8);  
  while(Wire.available()) { 
    capacitiveTouchValues[7-_counter]= Wire.read();
    _counter++;
  }
}

void Skinduino::resetSensorValues() { 
  for(int i=0; i<15; i++){
    capacitiveTouchValues[i] = 0;
  }  
}
