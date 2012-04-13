
void setup(){
  Serial.begin(115200);
  Serial1.begin(115200);
}

void loop() {
  int b = readByte();
  //if (b == 0xFA) {
  //  Serial.write(b);
  //  while (1);
  //}
  Serial.write(b);
}

int readByte() {
  while(!Serial1.available());
  return Serial1.read();
}



