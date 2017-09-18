package com.company;

import com.company.dataset.DataSet;
import com.company.devicecontroller.DeviceController;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import java.util.HashMap;
import java.util.Map;

class COMListener implements SerialPortEventListener {

    private final DataSet dataset;
    private final Map<String, DeviceController> controllers = new HashMap<>();

    public COMListener(DataSet dataset) {
        this.dataset = dataset;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        if (event.isRXCHAR() && event.getEventValue() > 0) {
            try {
                String portName = event.getPortName();
                Map<String, Object> result = controllers.get(portName).processing();
                dataset.updateDataset(result);
            } catch (SerialPortException e) {
                // log something bad happens with device
                e.printStackTrace();
            }
        }
    }

    public void registerDevice(DeviceController controller) throws SerialPortException {
        controller.initPort();
        controller.addEventListener(this, SerialPort.MASK_RXCHAR);
        controllers.put(controller.getPortName(), controller);
    }

}