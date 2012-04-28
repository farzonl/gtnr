int curr_speed = 0;
int max_speed = 175;
int comm_engaged = 0;

void setup() {
  delay(5000);
  Serial.begin(9600);
  pinMode(4, OUTPUT);
  pinMode(5, OUTPUT);
  digitalWrite(4, LOW);
  analogWrite(5, 180);
}

void loop() {
  int temp = checkSpeed();
  if(temp < 0 && curr_speed < max_speed)
    ++curr_speed;
  else if (temp > 0)
    --curr_speed;
  if (temp)
    analogWrite(5, curr_speed);
}

int checkSpeed() {
  int temp = 0;
  if (Serial.available() > 0) {
    temp = Serial.read();
    switch(temp) {
    case 's' : {
      comm_engaged = 1;
      temp = Serial.read();
      Serial.read();
      return temp < 0x49 ? -1 : temp > 0x49 ? 1 : 0;
    }
    }
  }
  return curr_speed < max_speed && !comm_engaged ? -1 : 0;
}

