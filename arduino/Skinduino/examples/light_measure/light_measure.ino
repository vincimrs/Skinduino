/*
 Measures brightness with a photoresistor in a voltage divider configuration
 photoresistor changes restance with brightness 
 LED lights up if the photoresistor's resistance is above the threshold

 R_DIV is attached from A0(VCC) to A2(LIGHT_PIN)
 Photoresistor is attached from A2(LIGHT_PIN) to A1(GND)
 LED is attached from A3(LED_PIN) to A1(GND)
 */
const int LIGHT_PIN = A2; 
const int LED_PIN = A3;
const float VCC = 3.3; 
const float R_DIV = 4700.0;
const float THRESHOLD = 5000.0;


void setup() {
  // put your setup code here, to run once:
pinMode(A0, OUTPUT);
digitalWrite(A0, HIGH); //vcc = 3.3v
pinMode(A1, OUTPUT);
digitalWrite(A1, LOW); //gnd

Serial.begin(115200);
}

void loop() {

 int lightADC = analogRead(LIGHT_PIN); 
 if (lightADC > 0)
 {
  float lightVoltage = (lightADC * VCC) / 1023.0;
  float lightResistance = (lightVoltage * R_DIV)/(VCC-lightVoltage);
  Serial.println("voltage: " + String(lightVoltage) + " V");
  Serial.println("Resistance: "+ String(lightResistance) + " ohms");

  if (lightResistance > THRESHOLD)
  {
    digitalWrite(LED_PIN, HIGH);
  }
  else
  {
    digitalWrite(LED_PIN, LOW);
  }
  Serial.println();
  delay(500);
 }
}

