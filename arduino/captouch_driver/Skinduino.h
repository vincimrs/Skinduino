#ifndef SKINDUINO_h
#define SKINDUINO_h

#include <Wire.h>

class Skinduino {
  public:
    unsigned char sensorValues[15];
    
    Skinduino() {}
    
    void setCapacitiveBaseline() { 
      Wire.beginTransmission(i2caddr_default);
      Wire.write(0x04);
      Wire.write(0x01);
      Wire.endTransmission();
    }

    void readSensorValues() {
      resetSensorValues();
      
      counter = 0;
      Wire.beginTransmission(i2caddr_default); 
      Wire.write(0x81); ///only the 1-8 are broken out
      Wire.endTransmission(false);
      Wire.requestFrom(0x25,8);  
      while(Wire.available()) { 
        sensorValues[7-counter]= Wire.read();
        counter++;
      }
    }
    
  private:
    int counter = 0;
    uint8_t i2caddr_default = 0x25;


    void resetSensorValues() { 
      for(int i=0; i<15; i++){
        sensorValues[i] = 0;
      }  
    }
};

#endif
