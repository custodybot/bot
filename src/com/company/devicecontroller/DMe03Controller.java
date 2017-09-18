package com.company.devicecontroller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.company.dataset.GoldMintDataSet;

import jssc.SerialPort;
import jssc.SerialPortException;

public class DMe03Controller extends DeviceController {

	private final static String MESSAGE_SUFFIX = "7B 23 26 7D";
	private final static String MESSAGE_PREFIX = "5B 24 25 5D";

	private final static String MESSAGE_DATA_PREFIX = "57 00";
	private final static String MESSAGE_DATA_SUFFIX = ".....";
	private final static String MESSAGE_MODE_PREFIX = "4D";

	private final static String EVENT_MESSAGE_WHITE_GOLD = "20";
	private final static String EVENT_MESSAGE_YELLOW_GOLD = "79";
	private final static String EVENT_MESSAGE_EXPERT = "65";
	private final static String EVENT_MESSAGE_BAD_CONNECTION = "70";

	public DMe03Controller(String portName) {
		super(portName);
	}

	@Override
	public void initPort() throws SerialPortException {
		openPort();
		setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
	}

	@Override
	public Map<String, Object> processing() throws SerialPortException {
		buffer.append(readHexString());
		buffer.delete(0, buffer.lastIndexOf(MESSAGE_PREFIX) + MESSAGE_PREFIX.length());

		int index = buffer.indexOf(MESSAGE_SUFFIX);
		if (index >= 0) {
			String msg = buffer.substring(0, index).trim();
			buffer.delete(0, index + MESSAGE_SUFFIX.length());

			return parseMessage(msg);
		}

		return Collections.emptyMap();
	}

	private Map<String, Object> parseMessage(String msg) {
		if (msg.startsWith(MESSAGE_MODE_PREFIX)) {
			// TODO добавить обработку сообщений состояний

			return Collections.emptyMap();
		}
		if (msg.startsWith(MESSAGE_DATA_PREFIX)) {
			String[] data = msg.substring(MESSAGE_DATA_PREFIX.length(), msg.length() - MESSAGE_DATA_SUFFIX.length())
					.trim().split(" ");

			short u1 = Short.parseShort(data[1] + data[0], 16);
			short u2 = Short.parseShort(data[3] + data[2], 16);

			Map<String, Object> result = new HashMap<>();
			result.put(GoldMintDataSet.GOLD_CONTENT.name(), calcGoldContent(u1, u2));

			return result;
		}
		// log ERROR unknown command: msg
		return Collections.emptyMap();
	}

	private int calcGoldContent(short u1, short u2) {
		if (u1 <= 980 && u1 >= 560 && u2 <= 690 && u2 >= 335)
			return 585;
		else
			return -1;
	}
}
