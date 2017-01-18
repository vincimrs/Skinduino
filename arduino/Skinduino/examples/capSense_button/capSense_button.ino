/*
 Turns on an LED when a button is pressed 
 Button input into C1 (first value read by Skinduino) 

 LED is connected to A2(LED_PIN) and A1(GND)
 */

#include <Skinduino.h>

const int LED_PIN = A2;
const int NUM_CAPTOUCH_PINS = 8;
int incomingByte = 0;
Skinduino skinduino;

void setup() { 
  Wire.begin();
  Serial.begin(115200);
  Serial1.begin(115200);
  pinMode(A1, OUTPUT);
  digitalWrite(A1, LOW); //A0 is GND
}

void loop() { 
  skinduino.readCapacitiveTouchValues(); 
  int button;
  button = skinduino.capacitiveTouchValues[0];
  if (button > 250)
  {
   digitalWrite(LED_PIN, HIGH);
  }
  else
  {
    digitalWrite(LED_PIN, LOW);
  }

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
