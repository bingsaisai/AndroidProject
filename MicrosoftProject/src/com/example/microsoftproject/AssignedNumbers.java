package com.example.microsoftproject;

public class AssignedNumbers {
	
	public static final byte FLAGS = 0x01;

	  /* Incomplete list of 16-bit service class UUIDs */
	  public static final byte UUID16_PART = 0x02;

	  /* Complete list of 16-bit service class UUIDs */
	  public static final byte UUID16 = 0x03;

	  /* Incomplete list of 128-bit service class UUIDs */
	  public static final byte UUID128_PART = 0x06;

	  /* Complete list of 128-bit service class UUIDs */
	  public static final byte UUID128 = 0x07;

	  /* Shortened local name */
	  public static final byte SHORT_NAME = 0x08;

	  /* Complete local name */
	  public static final byte COMPLETE_NAME = 0x09;

	  /* TX power level */
	  public static final byte TXPOWER = 0x0A;

	  // Out-Of-Band fields

	  /* Out Of Band Secure Simple Pairing Class of Device */
	  public static final byte OOB_COD = 0x0D;

	  /* Out Of Band Simple Pairing Hash C */
	  public static final byte OOB_HASH_C = 0x0E;

	  /* Out Of Band Simple Pairing Randomizer R */
	  public static final byte OOB_RANDOMIZER_R = 0x0F;

	  /* Device ID */
	  public static final byte ID = 0x10;

	  /* Manufacturer Specific Data */
	  public static final byte MANUFACTURER = (byte) 0xFF;

	  /* Service Data */
	  public static final byte SERVICE = 0x16;

	  /*
	   * Utility class has no constructor.
	   */
	  private AssignedNumbers() {}
}
