unsigned int distance[360];
unsigned int zeros[360];
int counts[90];
int count = 0;

void setup(){
  Serial.begin(115200);
  Serial1.begin(115200);
  for (int i=0;i<360;i++)
    distance[i]=0;
}

void loop() {
  /* ========================= \\
   || Pipe Lidar Data to Serial ||
   \\ ========================= */
  //  int temp = readByte();
  //  if (temp == 0xFA)
  //    Serial.println("");
  //  Serial.print(temp, HEX);
  //  Serial.print("\t");
  int i;
  if (readByte() == 0xFA) {
    //Serial.println(" ");
    handleData(readByte());
    count++;
  }
  if (count == 360) {
    count = 0;
    /*
    Serial.println("Counts:");
    for(i=0;i<90;i++) {
      Serial.print(i, DEC);
      Serial.print("\t");
      Serial.println(counts[i], DEC);
    }
    */
    //Serial.println("Distance:");
    for(i=0;i<360;i++) {
      Serial.print(i, DEC);
      Serial.print("\t");
      Serial.println(distance[i], DEC);
    }
    /*
    Serial.println("Zeros:");
    for(i=0;i<360;i++) {
      Serial.print(i, DEC);
      Serial.print("\t");
      Serial.println(zeros[i], DEC);
    }
    */
  }
}

int readByte() {
  while(!Serial1.available());
  return Serial1.read();
}

void handleData(int index) {
  index -= 0XA0;
  if (index >= 0 && index <= 90){
    counts[index]++;

    index *= 4;
    /*
  Serial.print(index, HEX);
     Serial.print(" ");
     */

    readByte();
    readByte();
    unsigned int temp = readByte()<<24;
    temp = temp + (readByte()<<16);
    temp = temp + (readByte()<<8);
    temp = temp + readByte();
    if (temp > 0)
      smoothData(index, temp);
    else
      zeros[index]++;

    temp = readByte()<<24;
    temp = temp + (readByte()<<16);
    temp = temp + (readByte()<<8);
    temp = temp + readByte();
    if (temp > 0)
      smoothData(index+1, temp);
    else
      zeros[index+1]++;

    temp = readByte()<<24;
    temp = temp + (readByte()<<16);
    temp = temp + (readByte()<<8);
    temp = temp + readByte();
    if (temp > 0)
      smoothData(index+2, temp);
    else
      zeros[index+2]++;

    temp = readByte()<<24;
    temp = temp + (readByte()<<16);
    temp = temp + (readByte()<<8);
    temp = temp + readByte();
    if (temp > 0)
      smoothData(index+3, temp);
    else
      zeros[index+3]++;
  }
}

void smoothData(int index, double val) {
  distance[index] = (distance[index] / 6) + (val / 4);
  //distance[index]++;
}



