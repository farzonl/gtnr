#ifndef GTNR_Motor_Controller_h
#define GTNR_Motor_Controller_h

/**
 * ===========================================================
 *  Custom Keywords
 * ===========================================================
 */
#define FWD  0
#define LFT  1
#define BKD  2
#define RHT  3
#define AUT  4
#define STOP 5




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
 *  Other Variables
 * ===========================================================
 */
int count = 0;
int currSpeed = 0;
int currDirection = 0;
int autopilot = 1;
int MAX_SPEED = 255;

/**
 * ===========================================================
 *  Motor Controller Methods
 * ===========================================================
 */
void handle_key();
void go(int dir);
void halt(void);
void advance(char a,char b);
void back_off (char a,char b);
void turn_L (char a,char b);
void turn_R (char a,char b);

/**
 * ===========================================================
 *  Sensor Methods
 * ===========================================================
 */
void handle_encoder(int side);
void handle_ir(void);
int ir_to_cm(int analog);
double ir_to_voltage(int analog);
void handle_photocells(void);


#endif
