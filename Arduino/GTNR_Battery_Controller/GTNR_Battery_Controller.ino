/**
 *         //===========================\\
 *         ||                           ||
 *         ||  GTNR Battery Controller  ||
 *         ||                           ||
 *         \\===========================//
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
 *   Function Summary:
 *   -----------------
 *
 *
 *
 *   Current Questions/Issues:
 *   -------------------------
 *     1) Should the lidar motor voltage be monitored? No analog 
 *          pins left, ADC?
 *
 *     2) Implement logging feature? i.e. Save a history of values 
 *          for greater logging resolution.
 *
 *     3) Implement ability for battery controller to cut power to
 *          specific devices using too much current. Automatically
 *          or by request from Atom Board.
 *
 *     4) How to handle extreme values for temperature or current.
 *
 *
 *
 *   Expected Request Format:
 *   ------------------------
 *     0. Sync byte       {  0xFF,   0xFF, 0xFF }
 *     1. Command byte    {   REQ,    OFF,  LOG }
 *     2. Data high byte  {   N/A,    N/A,  N/A }
 *     3. Data low byte   {   N/A,    N/A,  N/A }
 *     4. Data2           { REQID, DEV_ID,  N/A }
 *
 *
 *
 *   Response Format:
 *   ----------------
 *     0. Sync byte                (0xFF or -1)
 *     1. Request ID               (send as com->data2 or last byte of command)
 *     2. Current 5V               (mA)
 *     3. Current 12V              (mA)
 *     4. Current Motors           (mA)
 *     5. Temperature Body         (deg F)
 *     6. Temperature Atom Board   (deg F)
 *     7. Temperature Battery      (deg F)
 *
 *
 *
 *   Arduino Pin Assignments:
 *   ------------------------
 *     0 - Rx Motor Controller
 *     1 - Tx Motor Controller
 *     2 - 
 *     3 - 
 *     4 - M1 Direction Control
 *     5 - M1 Speed Control
 *     6 - 
 *     7 - 
 *     8 - 
 *     9 - 
 *    10 - 
 *    11 - 
 *    12 - 
 *    13 - Status LED
 *    A0 - Current 5V
 *    A1 - Current 12V
 *    A2 - Current Motors
 *    A3 - Termperature Body
 *    A4 - Termperature Atom Board
 *    A5 - Termperature Battery
 *    A6 - 
 *    A7 - 
 */
#include "GTNR_Battery_Controller.h"
#include <GTNR.h>

#define DEBUG   1

int resolution = 100;
int delay_time;
GTNR_Com *head;
GTNR_Com *tail;
unsigned int list_size = 0;

double current_vals[3];
double temperature_vals[3];

// Lidar Driver Variables
int curr_speed = 0;
int max_speed = 175;
int comm_engaged = 0;

void setup() {
  Serial.begin(9600);  

  // Start Lidar Motor
  digitalWrite(4,LOW);
  analogWrite (5, max_speed);

  delay_time = 1000 / resolution;
}

void loop() {
  check_serial();
  handle_list();
  handle_current();
  handle_temperature();
  delay(delay_time);
}

/* Used to read commands forwarded from Motor Controller.
 * If bytes are in the serial buffer, read them and convert
 * them to commands. Add the converted commands to a linked list.
 */
void check_serial() {
  while (Serial.available() > 3) {
    while(Serial.read() != -1);  // skip bad data... continue to the next synch bit
    GTNR_Com com;
    com.command = Serial.read();
    com.data = (Serial.read() << 8) | Serial.read();
    com.data2 = Serial.read();
    if (!head) {
      head = &com;
      tail = &com;
      list_size = 1;
    } 
    else {
      tail->next = &com;
      tail = &com;
      list_size++;
    }
  }
}

/* If there are commands in the linked list then remove them from the
 * list and execute them.
 */
void handle_list() {
  while (list_size > 0) {
    GTNR_Com *com = head;
    head = head->next;
    list_size--;

    switch(com->command) {
    case REQ : 
      send_current_info(com);
      break;
    case SPD : 
      handle_lidar_speed(com);
      break;
    case LOG : 
      break;
    case OFF : // TODO: implement with mosfets?
      break;
    }
  }
}

/* When the Atom board requests data send each analog value.
 * Message response will be formatted as follows:
 *     0. Sync byte                (0xFF or -1)
 *     1. Request ID               (send as com->data2 or last byte of command)
 *     2. Current 5V               (mA)
 *     3. Current 12V              (mA)
 *     4. Current Motors           (mA)
 *     5. Temperature Body         (deg F)
 *     6. Temperature Atom Board   (deg F)
 *     7. Temperature Battery      (deg F)
 */
void send_current_info(GTNR_Com *com) {
  Serial.write(0xFF);
  Serial.write(com->data2);
  Serial.write(6);
  for(int i=0;i< 3;i++)
    Serial.write(current_vals[i]);
  for(int i=0;i< 3;i++)
    Serial.write(temperature_vals[i]);
}

/* Takes a command of the form:
 *    { sync, 's', [spdH, spdL], BatCon }
 * Specifically, command => 's'peed, data contains lidar rotational speed data,
 * data2 contains BatteryController serial address.
 */
void handle_lidar_speed(GTNR_Com *com) {
  comm_engaged = 1;
  if(com->data < 0x4900 && curr_speed < max_speed)
    ++curr_speed;
  else if (com->data > 0x4A00 && curr_speed > 0)
    --curr_speed;
  if (temp != 0x49)
    analogWrite(5, curr_speed);
}

/* Read the each current value, convert 10 bit analog to mA, smooth
 * value based on previous measurements and save to array.
 */
void handle_current() {
  // TODO: Smoothing
  // TODO: Convert to mA
  double temp = analogRead(GTNR_5V_Pin);
  current_vals[GTNR_5V] = temp;
  temp = analogRead(GTNR_12V_Pin);
  current_vals[GTNR_12V] = temp;
  temp = analogRead(GTNR_Motors_Pin);
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

/* Read the each temperature value, convert 10 bit analog to degrees F or C, 
 * smooth value based on previous measurements and save to array.
 */
void handle_temperature() {
  // TODO: Smoothing
  // TODO: Convert to degrees C or F
  double temp = analogRead(BODY_Pin);
  temperature_vals[BODY] = temp;
  temp = analogRead(ATOM_Pin);
  temperature_vals[ATOM] = temp;
  temp = analogRead(BATT_Pin);
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

