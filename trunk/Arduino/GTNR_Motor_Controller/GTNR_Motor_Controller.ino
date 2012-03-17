#include "DFRduino_KeyLib.c"
#include "GTNR_Motor_Controller.h"




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
  go(FWD);
  delay(500);
  go(STOP);  
} 


/**
 * ===========================================================
 *  Application Functionality
 * ===========================================================
 */

void loop() 
{
  handle_key();
}


/**
 * ===========================================================
 *  DFRduino Key Methods
 * ===========================================================
 */
void handle_key() {
  int key = get_key_press();
  if (key >= 0) {         
    if (autopilot && key == AUT) {
      autopilot = !autopilot;
      digitalWrite(13, autopilot);
      if (autopilot) {
        go(FWD);
      } 
      else {
        halt();
      }
    } else if (!autopilot) {
      go(key);
    }
  }
}


/**
 * ===========================================================
 *  DFRduino Motor Controller Methods
 * ===========================================================
 */

void go(int dir) {
  // Set new speed and new direction when necessary
  if (dir == currDirection && currSpeed < MAX_SPEED) {
    double temp = MAX_SPEED - currSpeed;
    currSpeed += temp * 0.3;
  } 
  else if (dir != currDirection) {
    currSpeed = 127;
    currDirection = dir;
  }
  // Stop if needed other wise advance as needed
  if (dir == STOP || dir < 0) {
    halt();
    currSpeed = 0;
    currDirection = STOP;
  } 
  else {
    switch(dir) {
    case FWD : 
      advance(currSpeed); 
      break;
    case LFT : 
      turn_L(currSpeed,currSpeed); 
      break;
    case BKD : 
      back_off(currSpeed); 
      break;
    case RHT : 
      turn_R(currSpeed,currSpeed); 
      break;
    default : 
      go(STOP);
    }
  }
}

void halt(void)                    //Stop
{
  digitalWrite(E1,LOW);   
  digitalWrite(E2,LOW);      
}   
void advance(char rate)          //Move forward
{
  analogWrite (E1,rate);      //PWM Speed Control
  digitalWrite(M1,LOW);    
  analogWrite (E2,rate);    
  digitalWrite(M2,HIGH);
}  
void back_off (char rate)          //Move backward
{
  analogWrite (E1,rate);
  digitalWrite(M1,HIGH);   
  analogWrite (E2,rate);    
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

/**
 * ===========================================================
 *  Sensor Methods
 * ===========================================================
 */
void handle_encoder(int side){
  // TODO:
}

int readIR(int side) {
  // TODO:
}

