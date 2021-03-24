/**
 * 
 */
package org.pando.webapphardwarebridge.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fazecast.jSerialComm.SerialPort;

/**
 * @author prasadkrishnan
 *
 */
public class ComUtil {

	private static final Logger logger = LoggerFactory.getLogger(ComUtil.class);

	public static String readOnDemand(String comPortDesc) {
		SerialPort comPort = SerialPort.getCommPort(comPortDesc);
		if (comPort != null && comPort.getDescriptivePortName() != "Bad Port") {
			logger.info("Connected to " + comPortDesc);
			comPort.openPort();
			comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 0);
		}
		try {
			byte[] readBuffer = new byte[1024];
			int numRead = comPort.readBytes(readBuffer, readBuffer.length);
			System.out.println("Read " + numRead + " bytes.");
			return new String(readBuffer);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ComPort error", e);
		} finally {
			comPort.closePort();
		}
		logger.info("Unable to connect to " + comPortDesc);
		return "";
	}
}
