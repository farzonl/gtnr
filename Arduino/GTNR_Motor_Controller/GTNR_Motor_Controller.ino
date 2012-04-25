/**
 *         //=========================\\
 *         ||                         ||
 *         ||  GTNR Motor Controller  ||
 *         ||                         ||
 *         \\=========================//
 *
 *
 *
 *   Status:
 *   -------
 *      Compiles. Sensor acquisition code tested and functional. Pending 
 *      functionality testing.
 *
 *
 *
 *   Functionality Summary:
 *   ----------------------
 *
 *
 *
 *
 *   Current Questions/Issues:
 *   -------------------------
 *     1) How to handle extreme IR sensors? Send Atom Board interrupt or wait for
 *          further guidence. What if Lidar doesn't see obstacle, deadlock?
 *
 *     2) How to calculate degrees for turning? 
 *
 *     3) How to keep track of distance/time for motion/STOP? Total distance? Keep
 *          monitoring sensors while stopped, no need here right?
 *
 *
 *
 *   Expected Request Format:
 *   ------------------------
 *     0. Sync byte       {  0xFF, 0xFF, 0xFF, 0xFF }
 *     1. Command byte    {   REQ,   M*,   R*, STOP }
 *     2. Data high byte  {   N/A,  mmH, degH, timH }
 *     3. Data low byte   {   N/A,  mmL, degL, timL }
 *     4. Data2           { REQID,  SPD,  SPD,  N/A }
 *
 *          *NOTE: M* = { FWD, REV }, 
 *                 R* = { LFT, RHT }
 *
 *
 *
 *   Response Format:
 *   ----------------
 *     0. Sync byte                 (0xFF or -1)
 *     1. Request ID                (send as com->data2 or last byte of command)
 *     2. Photocell 0: Front Right  (8 bit analog)
 *     3. Photocell 1: Front Left   (8 bit analog)
 *     4. Photocell 2: Back Right   (8 bit analog)
 *     5. Photocell 3: Back Left    (8 bit analog)
 *     6. IR Right                  (mm)
 *     7. IR Left                   (mm)
 *
 *
 *
 *   Arduino Pin Assignments:
 *   ------------------------
 *     0 - Rx Atomboard (USB)
 *     1 - Tx Atomboard (USB)
 *     2 - Rx Battery Controller
 *     3 - Tx Battery Controller
 *     4 - M1 Direction Control
 *     5 - M1 Speed Control
 *     6 - M2 Speed Control
 *     7 - M1 Direction Control
 *     8 - Right Encoder A
 *     9 - Right Encoder B
 *    10 - Left Encoder A
 *    11 - Left Encoder B
 *    12 - 
 *    13 - Status LED
 *    A0 - Photocell0
 *    A1 - Photocell1
 *    A2 - Photocell2
 *    A3 - Photocell3
 *    A4 - IR0
 *    A5 - IR1
 *    A6 - 
 *    A7 - 
 */

#include <GTNR.h>
#include "GTNR_Motor_Controller.h"
#include <Encoder.h>
#include <NewSoftSerial.h>

#define DEBUG_SPEED       0
#define DEBUG_DISTANCE    0
#define DEBUG_PHOTOCELLS  0
#define DEBUG_IR          0

#define NUM_PHOTOCELLS    4
#define NUM_IR            2
#define DEFAULT_SPEED     255

Encoder left_motor(10,11);
Encoder right_motor(8,9);
NewSoftSerial batteryController(2,3);

long positionLeft  = -999;
long positionRight = -999;
double distanceLeft  = 0;
double distanceRight = 0;
unsigned long distanceTotal = 0;
unsigned long distanceTotal2 = 0;
double last = 0;

double photocells[NUM_PHOTOCELLS];
double irs[NUM_IR];

GTNR_Com *head;
GTNR_Com *tail;
unsigned int list_size = 0;

GTNR_Com *head_movement;
GTNR_Com *tail_movement;
unsigned int list_size_movement = 0;

GTNR_Com *last_move = 0;
int stopped_time = 0;
int turn_time = 0;

char alive = 0;
char keyDown = 0;


void setup(void) 
{ 
  int i;
  for(i=4;i<=7;i++)
    pinMode(i, OUTPUT);

  Serial.begin(115200);      //Set Baud Rate
  batteryController.begin(9600);
  pinMode(13, OUTPUT);
}

void loop() 
{
  check_serial();
  handle_list();
  handle_encoder(3);
  handle_ir();

  // blink alive
  digitalWrite(13, alive);
  alive = !alive;
}

/* Used to read commands forwarded from Atom Board and Battery Controller.
 * If bytes are in the serial buffer, read them and convert
 * them to commands. Add the converted commands to a linked list.
 */
void check_serial() {
  // ATOM BOARD
  // Make sure there are enough bytes
  while (Serial.available() > 3) {
    // skip bad data... continue to the next synch bit
    // TODO: Send error message to Atom Board
    while(Serial.read() != -1 && Serial.available() > 0);
    // if too many bytes were skipped exit
    if (Serial.available() < 3)
      break;
    GTNR_Com com;
    com.command = Serial.read();
    com.data = (Serial.read() << 8) | Serial.read();
    com.data2 = Serial.read();
    // add com to a linked list to be handled later
    switch(com.command) {
      case FWD:
      case REV:
      case LFT:
      case RHT:
      case STOP:
        add_to_list(&com, &head_movement, &tail_movement);
        list_size_movement++;
        break;
      case REQ:
      case LOG:
      case OFF:
        add_to_list(&com, &head_movement, &tail_movement);
        list_size++;
        break;
    }
  }
}

/* Add command to list. If list is empty, set head and tail pointers.
 *
 */
void add_to_list(GTNR_Com *com, GTNR_Com **_head, GTNR_Com **_tail) {
      if (!head) {
      *_head = com;
      *_tail = com;
    } 
    else {
      (*_tail)->next = com;
      *_tail = com;
    }
}

/* If there are commands in the Atom Board linked list then remove 
 * them from the list and execute them.
 */
void handle_list() {
  // TODO: implement logging
  while (list_size > 0 && head) {
    GTNR_Com *com = head;
    head = head->next;
    list_size--;
    if(com->data2 == BATTERY_CONTROLLER) {
      forward_to_battery_controller(com);
      continue;
    }
    switch(com->command) {
    case REQ : 
      if (com->data == MOTOR_CONTROLLER) {
        send_current_info(com);
        break;
      }
    case LOG : 
    case OFF : 
      break;
    case FWD :
    case REV :
    case RHT :
    case LFT :
    case STOP :
      go(com);
    }
  }
  if (!head)
    list_size = 0;
    // TODO: implement logging
  if (movement_complete() && list_size_movement > 0 && head_movement) {
    last_move = head_movement;
    head_movement = head_movement->next;
    list_size_movement--;
    go(last_move);
  }
  if (!head_movement)
    list_size_movement = 0;
}

int movement_complete() {
  if (!last_move)
    return 1;
  if (last_move->command = STOP) {
    int temp = millis() - stopped_time;
    if (temp >= last_move->data) {
      last_move = 0;
      distanceLeft = 0;
      distanceRight = 0;
    }
  } else if (last_move->command == FWD || last_move->command == REV) {
    int temp = (distanceLeft + distanceRight) / 1;
    if (temp >= last_move->data) {
      last_move = 0;
      // if overflow then add to a high counter
      if (distanceTotal + temp < distanceTotal)
        distanceTotal2++;
      distanceTotal += temp;
      distanceLeft = 0;
      distanceRight = 0;
    }
  } else if (last_move->command == LFT || last_move->command == RHT) {
    // TODO: figure out degrees turned.
    int temp = millis() - turn_time;
    if (temp >= last_move->data) {
      last_move = 0;
      distanceLeft = 0;
      distanceRight = 0;
    }
  }
  return !last_move;
}

/* When the Atom board requests data send each analog value.
 * Message response will be formatted as follows:
 *     0. Sync byte                 (0xFF or -1)
 *     1. Request ID                (send as com->data2 or last byte of command)
 *     2. Photocell 0: Front Right  (8 bit analog)
 *     3. Photocell 1: Front Left   (8 bit analog)
 *     4. Photocell 2: Back Right   (8 bit analog)
 *     5. Photocell 3: Back Left    (8 bit analog)
 *     6. IR Right                  (mm)
 *     7. IR Left                   (mm)
 */
void send_current_info(GTNR_Com *com) {
  // TODO: sync with Java code
  Serial.write(0xFF);
  Serial.write(com->data2);
  Serial.write(6); // size of data
  handle_photocells();
  handle_ir();
  for(int i=0;i< 4;i++)
    Serial.write(photocells[i]);
  for(int i=0;i< 2;i++)
    Serial.write(irs[i]);
}

/* Send the current command to the battery controller. After sent,
 * wait for a response and forward it to Atom Board if necessary.
 */
void forward_to_battery_controller(GTNR_Com *com) {
  batteryController.write(0xFF);
  batteryController.write(com->command);
  batteryController.write((com->data >> 8) & 0xFF);
  batteryController.write(com->command & 0xFF);
  batteryController.write(com->data2);
  // TODO: implement logging
  if (com->command == REQ) {
    // wait for battery controller to respond
    while(batteryController.available() <= 0);
    // read battery controller response
    for(int i=0;i<9;i++) {
      Serial.write(batteryController.read());
      delay(1);
    }
  }
}

/* Takes a GTNR_Com pointer and executes the motion command. If given an
 * invalid command, the controller will halt the motors.
 */
void go(GTNR_Com *com) {
  // TODO: handle distance/degrees
  if (!com->data2)
    com->data2 = DEFAULT_SPEED;
  if (DEBUG_SPEED) {
    Serial.print("Speed: ");
    Serial.println(com->data2, DEC);
  }
  switch(com->command) {
  case FWD : 
    advance(com->data2); 
    distanceLeft = 0;
    distanceRight = 0;
    break;
  case LFT : 
    turn_L(com->data2,com->data2); 
    distanceLeft = 0;
    distanceRight = 0;
    break;
  case REV : 
    back_off(com->data2); 
    turn_time = millis();
    break;
  case RHT : 
    turn_R(com->data2,com->data2); 
    turn_time = millis();
    break;
  default : 
    halt();
    currDirection = STOP;
    stopped_time = millis();
  }
}

/* Stops motors.
 *
 */
void halt(void) {
  digitalWrite(E1,LOW);   
  digitalWrite(E2,LOW);      
}

/* Move forward
 *
 */
void advance(char rate) {
  analogWrite (E1,rate);
  digitalWrite(M1,HIGH);
  analogWrite (E2,rate);
  digitalWrite(M2,LOW);
}

/* Move backward
 *
 */  
void back_off (char rate) {
  analogWrite (E1,rate);
  digitalWrite(M1,LOW);
  analogWrite (E2,rate);
  digitalWrite(M2,HIGH);
}

/* Point turn left
 *
 */
void turn_L (char a,char b) {
  analogWrite (E1,a);
  digitalWrite(M1,HIGH);
  analogWrite (E2,b);
  digitalWrite(M2,HIGH);
}

/* Point turn right
 *
 */
void turn_R (char a,char b) {
  analogWrite (E1,a);
  digitalWrite(M1,LOW);
  analogWrite (E2,b);
  digitalWrite(M2,LOW);
}

/* Read encoder, reset encoder for future movement. Convert reading
 * to mm. Add distance traveled to total distance for left and right
 * side accordingly.
 */
void handle_encoder(int side){
  long newLeft, newRight;
  newLeft = left_motor.read();
  newRight = right_motor.read();
  if (newLeft != positionLeft || newRight != positionRight) {
    positionLeft = newLeft;
    positionRight = newRight;
    // TODO: re-calculate for new wheels?
    distanceLeft += newLeft * 2.75; // add number of mm moved
    distanceRight += newRight * 2.75; // add number of mm moved
    if (distanceLeft - last > 20 && DEBUG_DISTANCE) {
      last = distanceLeft;
      Serial.print(distanceLeft);
      Serial.print(",\t");
      Serial.println(distanceRight);
    }
    // Re-align after each reading.
    left_motor.write(0);
    right_motor.write(0);
  }
  // TODO: implement some reset functionality for distance.
}

/* Reads both IR sensors and writes the values to an array.
 *
 */
void handle_ir() {
  // TODO: logging
  for (int i=0;i<NUM_IR;i++)
    irs[i] = ir_to_cm(analogRead(i+NUM_PHOTOCELLS));
  if (DEBUG_IR) {
    for (int i=0;i<NUM_IR;i++){
      Serial.print(irs[i], DEC);
      Serial.print("\t");
    }
    Serial.println();
  }
  // TODO: handle extreme values, i.e. hard stop, Atom Board interrupt
}

/* Takes an analog value from IR and converts it to cm.
 * 
 *   Distance chart:
 *   ---------------
 *   10cm - 550
 *   20cm - 460
 *   30cm - 350
 *   40cm - 260
 *   50cm - 215
 *   60cm - 185
 *   70cm - 160
 *   80cm - 140
 *   90cm - 115
 *   1m   - 105
 *   open - 40-90
 */
int ir_to_cm(int analog) {
  return (int)(30431 * pow (analog,-1.169));
}


/* Takes an analog value from IR and converts it to a voltage 0-5.
 */
double ir_to_voltage(int analog) {
  return (((double)analog) * 5.0) / 1024.0;
}

/* Reads the photocell sensors and writes the values to an array.
 *
 */
void handle_photocells() {
  // TODO: logging
  for (int i=0;i<NUM_PHOTOCELLS;i++)
    photocells[i] = analogRead(i);
  if (DEBUG_PHOTOCELLS) {
    for (int i=0;i<NUM_PHOTOCELLS;i++){
      Serial.print(photocells[i], DEC);
      Serial.print("\t");
    }
    Serial.println();
  }
  // TODO: handle extreme values
}

