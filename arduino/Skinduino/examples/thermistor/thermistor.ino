/*
 Measures temperature of ambient air or the surface touching the thermistor
 NTC Thermistor (negative coefficient thermistor) 
 Resistance decreases non-linearly as temperature increases
 Basic voltage divider circuit
 Converts resistance to temperature using a simplified steinhart equation

 Series resistor connected to A0(VCC) and A2(THERMISTORPIN)
 Thermistor connected to A2(THERMISTORPIN) and A1(GND)
 */
const int THERMISTORPIN = A2; 
const int LED_PIN = A3;
const float VCC = 3.3; 
const float THRESHOLD = 30;
const float SERIESRESISTOR = 10000.0;
const int BCOEFFICIENT = 3380; //dependent on datasheet for thermistor
const int TEMPERATURENOMINAL = 25;
const int THERMISTORNOMINAL = 10000;
const int NUMSAMPLES = 5; 
int samples[NUMSAMPLES]; 

void setup() {
  // put your setup code here, to run once:
pinMode(A0, OUTPUT);
digitalWrite(A0, HIGH); //vcc = 3.3v
pinMode(A1, OUTPUT);
digitalWrite(A1, LOW); //gnd

Serial.begin(115200);
}

void loop(void) {
  uint8_t i;
  float average;
 
  // take N samples in a row, with a slight delay
  for (i=0; i< NUMSAMPLES; i++) {
   samples[i] = analogRead(THERMISTORPIN);
   delay(10);
  }
 
  // average all the samples out
  average = 0;
  for (i=0; i< NUMSAMPLES; i++) {
     average += samples[i];
  }
  average /= NUMSAMPLES;
 
  Serial.print("Average analog reading "); 
  Serial.println(average);
 
  // convert the value to resistance
  average = 1023 / average - 1;
  average = SERIESRESISTOR / average;
  Serial.print("Thermistor resistance "); 
  Serial.println(average);
 
  float steinhart;
  steinhart = average / THERMISTORNOMINAL;     // (R/Ro)
  steinhart = log(steinhart);                  // ln(R/Ro)
  steinhart /= BCOEFFICIENT;                   // 1/B * ln(R/Ro)
  steinhart += 1.0 / (TEMPERATURENOMINAL + 273.15); // + (1/To)
  steinhart = 1.0 / steinhart;                 // Invert
  steinhart -= 273.15;                         // convert to C
 
  Serial.print("Temperature "); 
  Serial.print(steinhart);
  Serial.println(" *C");

  if (steinhart > THRESHOLD)
  {
    digitalWrite(LED_PIN,HIGH);
  }
  else
  {
    digitalWrite(LED_PIN, LOW);
  }
 
  delay(1000);
}
