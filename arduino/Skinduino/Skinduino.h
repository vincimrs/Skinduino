#ifndef SKINDUINO_h
#define SKINDUINO_h

#include "Arduino.h"
#include <Wire.h>

class Skinduino {
  public:
    unsigned char capacitiveTouchValues[15];
    
    Skinduino() {}
    
    void setCapacitiveTouchBaseline();
    void readCapacitiveTouchValues();
    
  private:
    int _counter;
    uint8_t _i2caddr_default = 0x25;
    void resetSensorValues();
};

#endif
