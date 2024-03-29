package com.gtnightrover.dfrduino;

public enum SerialProtocol {

	FWD('F'), REV('B'), RHT('R'), LFT('L'),STOP('S'), REQ('Q'), 
	OFF('O'), MOTOR_CONTROLLER('M'),
	POWER_CONTROLLER('P'), NA((char)0);

	private char aChar;
	
	SerialProtocol(char aChar)
	{
		this.aChar = aChar;
	}
	
	public char getChar()
	{
		return aChar;
	}
}


