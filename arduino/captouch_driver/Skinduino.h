#ifndef SKINDUINO_h
#define SKINDUINO_h

#include "Arduino.h"
#include <Wire.h>

class Skinduino {
  public:
    unsigned char sensorValues[15];
    
    Skinduino() {}
    
    void setCapacitiveBaseline();
    void readSensorValues();
    
  private:
    int _counter;
    uint8_t _i2caddr_default = 0x25;
    void resetSensorValues();
};

#endif
