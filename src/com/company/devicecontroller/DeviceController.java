package com.company.devicecontroller;

import jssc.SerialPort;
import jssc.SerialPortException;

import java.util.Map;

public abstract class DeviceController extends SerialPort {

    protected final StringBuilder buffer = new StringBuilder();

    DeviceController(String portName) {
        super(portName);
    }

    public abstract void initPort() throws SerialPortException;

    public abstract Map<String, Object> processing() throws SerialPortException;

}
