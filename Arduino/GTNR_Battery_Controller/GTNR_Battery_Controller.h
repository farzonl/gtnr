#ifndef GTNR_Batter_Controller_h
#define GTNR_Battery_Controller_h

// Define Temperature keywords
#define GTNR_5V      0
#define GTNR_12V     1
#define GTNR_Motors  2
#define GTNR_5V_Pin      3
#define GTNR_12V_Pin     4
#define GTNR_Motors_Pin  5


double current_vals[3];
double temperature_vals[3];

void handle_current(void);
void handle_temperature(void);

#endif
