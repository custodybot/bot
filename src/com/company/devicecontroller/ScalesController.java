package com.company.devicecontroller;

import com.company.dataset.GoldMintDataSet;
import jssc.SerialPort;
import jssc.SerialPortException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ScalesController extends DeviceController {

    private final static String CURRENT_WEIGHT_PREFIX = "GS";
    private final static String CURRENT_WEIGHT_SUFFIX = "ozt\r\n";

    public ScalesController(String portName) {
        super(portName);
    }

    @Override
    public void initPort() throws SerialPortException {
        openPort();
        setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        setFlowControlMode(SerialPort.FLOWCONTROL_XONXOFF_IN | SerialPort.FLOWCONTROL_XONXOFF_OUT);
    }

    @Override
    public Map<String, Object> processing() throws SerialPortException {
        buffer.append(readString());
        int prefix_index = buffer.lastIndexOf(CURRENT_WEIGHT_PREFIX);
        int suffix_index = buffer.indexOf(CURRENT_WEIGHT_SUFFIX, prefix_index);
        if (prefix_index >= 0 && suffix_index >= 0) {
            String msg = buffer.substring(prefix_index + CURRENT_WEIGHT_PREFIX.length(), suffix_index).trim();
            buffer.delete(0, suffix_index + CURRENT_WEIGHT_SUFFIX.length());

            Map<String, Object> result = new HashMap<>();
            result.put(GoldMintDataSet.WEIGHT.name(), Double.parseDouble(msg));

            return result;
        }
        return Collections.emptyMap();
    }
}
