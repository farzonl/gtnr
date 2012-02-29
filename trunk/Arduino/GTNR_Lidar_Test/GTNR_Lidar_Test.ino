int count = 0;

/**
 * ===========================================================
 *  DFRduino Key Variables
 * ===========================================================
 */
int  adc_key_val[5] ={
  30, 150, 360, 535, 760 };
int NUM_KEYS = 5;
int adc_key_in;
int key=-1;
int oldkey=-1;
int autopilot = 1;


/**
 * ===========================================================
 *  DFRduino Motor Variables
 * ===========================================================
 */

//Standard PWM DC control
int E1 = 5;     //M1 Speed Control
int E2 = 6;     //M2 Speed Control
int M1 = 4;    //M1 Direction Control
int M2 = 7;    //M1 Direction Control


/**
 * ===========================================================
 *  Application Initialize
 * ===========================================================
 */
void setup(void) 
{ 
  int i;
  for(i=4;i<=7;i++)
    pinMode(i, OUTPUT);  
  Serial.begin(115200);      //Set Baud Rate

  pinMode(13, OUTPUT);
  advance(255,255);  
} 


/**
 * ===========================================================
 *  Application Functionality
 * ===========================================================
 */

void loop() 
{ 
  int temp = Serial.read();
  if (temp == 0xFA)
    Serial.println(" ");
  Serial.print(temp, HEX);
  Serial.print("\t");
  //if (count++>22) {
  //Serial.println(" ");
  //count = 0;
  //}

  /* =========================== \\
   || Key and Motor Functionality ||
   \\ =========================== */

  adc_key_in = analogRead(7);    // read the value from the sensor  
  // get the key
  key = get_key(adc_key_in);    // convert into key press
  if (key != oldkey) {   // if keypress is detected
    delay(50);      // wait for debounce time
    adc_key_in = analogRead(7);    // read the value from the sensor  
    key = get_key(adc_key_in);    // convert into key press
    if (key != oldkey) {         
      oldkey = key;
      if (key >=0){
        switch(key) {
        case 0 : 
          { 
            if (!autopilot) {
              advance(255,255);
            }
          } 
          break;
        case 1 : 
          { 
            if (!autopilot) {
              turn_L (255,255);
              //advance(255,150);
            }
          } 
          break;
        case 2 : 
          { 
            if (!autopilot) {
              back_off (255,255);   //move back in max speed
            }
          } 
          break;
        case 3 : 
          { 
            if (!autopilot) {
              turn_R(255,255);
              //advance(150,255);
            }
          } 
          break;
        case 4 : 
          {
            autopilot = !autopilot;
            digitalWrite(13, autopilot);
            if (autopilot) {
              advance(255,255);
            } 
            else {
              stop();
            }
          } 
          break;
        }
      }
    }
  }
}

/**
 * ===========================================================
 *  DFRduino Key Methods
 * ===========================================================
 */

// Convert ADC value to key number
int get_key(unsigned int input)
{   
  int k;
  for (k = 0; k < NUM_KEYS; k++)
  {
    if (input < adc_key_val[k])
    {  
      return k;  
    }
  }
  if (k >= NUM_KEYS)
    k = -1;     // No valid key pressed
  return k;
}


/**
 * ===========================================================
 *  DFRduino Motor Controller Methods
 * ===========================================================
 */

void stop(void)                    //Stop
{
  digitalWrite(E1,LOW);   
  digitalWrite(E2,LOW);      
}   
void advance(char a,char b)          //Move forward
{
  analogWrite (E1,a);      //PWM Speed Control
  digitalWrite(M1,LOW);    
  analogWrite (E2,b);    
  digitalWrite(M2,HIGH);
}  
void back_off (char a,char b)          //Move backward
{
  analogWrite (E1,a);
  digitalWrite(M1,HIGH);   
  analogWrite (E2,b);    
  digitalWrite(M2,LOW);
}
void turn_L (char a,char b)             //Turn Left
{
  analogWrite (E1,a);
  digitalWrite(M1,HIGH);    
  analogWrite (E2,b);    
  digitalWrite(M2,HIGH);
}
void turn_R (char a,char b)             //Turn Right
{
  analogWrite (E1,a);
  digitalWrite(M1,LOW);    
  analogWrite (E2,b);    
  digitalWrite(M2,LOW);
}



