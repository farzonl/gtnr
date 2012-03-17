#ifndef DFRduino_KeyLib_c
#define DFRduino_KeyLib_c

#include "Arduino.h"

#define KEYPIN   7
#define NUM_KEYS 5

/**
 * ===========================================================
 *  DFRduino Key Variables
 * ===========================================================
 */
static int  adc_key_val[5] = { 30, 150, 360, 535, 760 };
static int oldkey=-1;


/**
 * ===========================================================
 *  DFRduino Key Methods
 * ===========================================================
 */
int get_key_press();
int convert_key(unsigned int input);

/**
 * ===========================================================
 *  DFRduino Key Method Implementations
 * ===========================================================
 */

int get_key_press() {
  int keyPinVal = analogRead(KEYPIN);
  int key = -1;
  // get the key
  key = convert_key(keyPinVal);    // convert into key press
  if (key != oldkey) {   // if keypress is detected
    delay(50);      // wait for debounce time
    key = convert_key(keyPinVal);    // convert into key press
    if (key != oldkey)
      oldkey = key;
  }
  return key;
}

// Convert ADC value to key number
int convert_key(unsigned int input)
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


#endif
