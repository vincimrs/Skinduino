/*
 Measures brightness by converting into a square wave at 50% duty cycle
 Frequency output changes with brightness, more bright, higher frequency
 LED lights up if frequency is above threshold
 
 Pin 1 connected to A1(GND)
 Pin 2 connected to A0(VCC)
 Pin 3 connected to A2(SENSOR_PIN)
 LED is attached from A3(LED_PIN) to A1(GND)
 */
const int LED_PIN = A3;
const int SENSOR_PIN = A2;
const float VCC = 3.3; 
const float R_DIV = 4700.0;
const float THRESHOLD = 5000.0; //arbitrary


void setup() {
  // put your setup code here, to run once:
pinMode(A0, OUTPUT);
digitalWrite(A0, HIGH); //vcc = 3.3v
pinMode(A1, OUTPUT);
digitalWrite(A1, LOW); //gnd
interrupts(); //need to enable interrupts for pulseIn() to work

Serial.begin(115200);
}

void loop() {
 //reads the amount of time that the pin is high in microseconds
 float pulse_width_us = (float)pulseIn(SENSOR_PIN, HIGH); 
 
 if (pulse_width_us > 10)
 {
   float period_us = 2*pulse_width_us;
   float frequency = 1/(period_us * .000001); //TODO: get rid of floating point division
   Serial.println("pulse_width: " + String(pulse_width_us) + " us");
   Serial.println("frequency: " + String(frequency) + " hz");
   if (frequency > THRESHOLD) 
   {
    digitalWrite(LED_PIN, HIGH);
   }
  
   else
   {
    digitalWrite(LED_PIN, LOW);
   }
 }
  Serial.println();
  delay(500);
 }


