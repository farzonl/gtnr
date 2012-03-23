#include "GTNR_Battery_Controller.h"

#define DEBUG   1

int time = 0;

void setup() {
  Serial.begin(115200);  

  // Start Lidar Motor
  analogWrite (5,255);      //PWM Speed Control
  digitalWrite(4,LOW);
}

void loop() {
  if (DEBUG) {
    Serial.print("Time: ");
    Serial.println(time, DEC);
  }
  handle_current();
  handle_temperature();
  time++;
  delay(1000);
}

void handle_current() {
  double temp = analogRead(GTNR_5V_Pin);
  //  temperature_vals[GTNR_5V] = (temperature_vals[GTNR_5V] * 0.4) + (temp * 0.6);
  current_vals[GTNR_5V] = temp;
  temp = analogRead(GTNR_12V_Pin);
  //  temperature_vals[GTNR_12V] = (temperature_vals[GTNR_12V] * 0.4) + (temp * 0.6);
  current_vals[GTNR_12V] = temp;
  temp = analogRead(GTNR_Motors_Pin);
  //  temperature_vals[GTNR_Motors] = (temperature_vals[GTNR_Motors] * 0.4) + (temp * 0.6);
  current_vals[GTNR_Motors] = temp;
  if (DEBUG) {
    Serial.print("\tGTNR_5V:     ");
    Serial.println(temperature_vals[GTNR_5V], DEC);
    Serial.print("\tGTNR_12V:    ");
    Serial.println(temperature_vals[GTNR_12V], DEC);
    Serial.print("\tGTNR_Motors: ");
    Serial.println(temperature_vals[GTNR_Motors], DEC);
  }
  // TODO: handle extreme values
}

void handle_temperature() {
  double temp = analogRead(BODY_Pin);
  //  temperature_vals[GTNR_5V] = (temperature_vals[GTNR_5V] * 0.4) + (temp * 0.6);
  temperature_vals[BODY] = temp;
  temp = analogRead(ATOM_Pin);
  //  temperature_vals[GTNR_12V] = (temperature_vals[GTNR_12V] * 0.4) + (temp * 0.6);
  temperature_vals[ATOM] = temp;
  temp = analogRead(BATT_Pin);
  //  temperature_vals[GTNR_Motors] = (temperature_vals[GTNR_Motors] * 0.4) + (temp * 0.6);
  temperature_vals[BATT] = temp;
  if (DEBUG) {
    Serial.print("\tBody Temp:     ");
    Serial.println(temperature_vals[GTNR_5V], DEC);
    Serial.print("\tAtom Temp:    ");
    Serial.println(temperature_vals[GTNR_12V], DEC);
    Serial.print("\tBattery Temp: ");
    Serial.println(temperature_vals[GTNR_Motors], DEC);
  }
  // TODO: handle extreme values
}


