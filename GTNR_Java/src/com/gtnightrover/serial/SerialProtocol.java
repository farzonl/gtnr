package com.gtnightrover.serial;

public enum SerialProtocol {

	FWD('F'), REV('B'), RHT('R'), LFT('L'),STOP('S'), REQ('Q'), OFF('O');

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


