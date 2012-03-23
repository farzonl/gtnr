#include "DFRduino_KeyLib.c"
#include "GTNR_Motor_Controller.h"
#include <Encoder.h>

#define DEBUG_SPEED       1
#define DEBUG_DISTANCE    0
#define DEBUG_PHOTOCELLS  0
#define DEBUG_IR          0

#define NUM_PHOTOCELLS    4
#define NUM_IR            2

Encoder left_motor(10,11);
Encoder right_motor(8,9);
long positionLeft  = -999;
long positionRight = -999;
double distanceLeft  = 0;
double distanceRight = 0;
double last = 0;

double photocells[NUM_PHOTOCELLS];
double irs[NUM_IR];

int startSpeed = 75;

char alive = 0;
char keyDown = 0;

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
  handle_encoder(3);
  handle_photocells();
  handle_ir();
  //delay(250);
  
  // blink alive
  digitalWrite(13, alive);
  alive = !alive;
}


/**
 * ===========================================================
 *  DFRduino Key Methods
 * ===========================================================
 */
void handle_key() {
  int key = get_key_press();
  if (key >= 0 && !keyDown) {
    keyDown = 1;    
    if (autopilot && key == AUT) {
      autopilot = !autopilot;
      digitalWrite(13, autopilot);
      if (autopilot) {
        go(FWD);
      } 
      else {
        halt();
      }
    } 
    else if (!autopilot) {
      go(key);
    }
  } 
  else if(key < 0) {
    keyDown = 0;
  }
}


/**
 * ===========================================================
 *  DFRduino Motor Controller Methods
 * ===========================================================
 */

void go(int dir) {
  // Set new speed and new direction when necessary
  if (dir == currDirection && currSpeed < MAX_SPEED && currSpeed > 0) {
    //double temp = MAX_SPEED - currSpeed;
    //Serial.print("diff: ");
    //Serial.println(temp, DEC);
    //currSpeed += temp * 0.3;
    currSpeed += 20;
    if (currSpeed > MAX_SPEED)
    currSpeed = MAX_SPEED;
  } 
  else if (dir != currDirection || currSpeed == 0) {
    currSpeed = startSpeed;
    currDirection = dir;
  }
  if (DEBUG_SPEED) {
    Serial.print("currSpeed: ");
    Serial.println(currSpeed, DEC);
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
  digitalWrite(M1,HIGH);    
  analogWrite (E2,rate);    
  digitalWrite(M2,LOW);
}  
void back_off (char rate)          //Move backward
{
  analogWrite (E1,rate);
  digitalWrite(M1,LOW);   
  analogWrite (E2,rate);    
  digitalWrite(M2,HIGH);
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
  long newLeft, newRight;
  newLeft = left_motor.read();
  newRight = right_motor.read();
  if (newLeft != positionLeft || newRight != positionRight) {
    positionLeft = newLeft;
    positionRight = newRight;
    distanceLeft += newLeft * 2.75; // add number of mm moved
    distanceRight += newRight * 2.75; // add number of mm moved
    if (distanceLeft - last > 20 && DEBUG_DISTANCE) {
      last = distanceLeft;
      Serial.print(distanceLeft);
      Serial.print(",\t");
      Serial.println(distanceRight);
    }
    //test
    left_motor.write(0);
    right_motor.write(0);
  }
  // if a character is sent from the serial monitor,
  // reset both back to zero.
  if (Serial.available() && 0) {
    Serial.read();
    left_motor.write(0);
    right_motor.write(0);
  }
}

/*
 * 10cm - 550
 * 20cm - 460
 * 30cm - 350
 * 40cm - 260
 * 50cm - 215
 * 60cm - 185
 * 70cm - 160
 * 80cm - 140
 * 90cm - 115
 * 1m   - 105
 * open - 40-90
 */
void handle_ir() {
  for (int i=0;i<NUM_IR;i++)
    irs[i] = ir_to_cm(analogRead(i+NUM_PHOTOCELLS));
  if (DEBUG_IR) {
    for (int i=0;i<NUM_IR;i++){
      Serial.print(irs[i], DEC);
      Serial.print("\t");
    }
    Serial.println();
  }
}

int ir_to_cm(int analog) {
  return (int)(30431 * pow (analog,-1.169));
}

double ir_to_voltage(int analog) {
  return (((double)analog) * 5.0) / 1024.0;
}

void handle_photocells() {
  for (int i=0;i<NUM_PHOTOCELLS;i++)
    photocells[i] = analogRead(i);
  if (DEBUG_PHOTOCELLS) {
    for (int i=0;i<NUM_PHOTOCELLS;i++){
      Serial.print(photocells[i], DEC);
      Serial.print("\t");
    }
    Serial.println();
  }
}
