/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.embarcados.comunicaoserial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import gnu.io.*;
// Our RXTX package

public class TwoWaySerialComm {

    public TwoWaySerialComm() {
        super();
    }

    void connect(String portName) throws Exception {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if (portIdentifier.isCurrentlyOwned()) {
            System.out.println("Error: Port is currently in use");
        } else {
            CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);

            if (commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(57600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

                OutputStream out = serialPort.getOutputStream();

                (new Thread(new SerialWriter(out))).start();
                InputStream in = serialPort.getInputStream();
                // serialPort.notifyOnDataAvailable(true);
                serialPort.addEventListener(new SerialReader(in));

                serialPort.notifyOnDataAvailable(true);

            } else {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }
    }

    public static class SerialWriter implements Runnable {

        OutputStream out;

        public SerialWriter(OutputStream out) {
            this.out = out;
        }

        public void run() {
            try {
                int c = 0;
                while ((c = System.in.read()) > -1) {
                    this.out.write((byte) c);
                    System.out.println((byte) c);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }

    /**
     * Handles the input coming from the serial port. A new line character is treated as the end of a block in this example.
     */
    public static class SerialReader implements SerialPortEventListener {

        private InputStream in;

        private byte[] buffer = new byte[1024];

        public SerialReader(InputStream in) {
            this.in = in;
        }

        @Override
        public void serialEvent(SerialPortEvent arg0) {
            int data;

            try {
                data = in.read();
                int len = 0;
                while (in.available() > 0) {
                    data = in.read();
                    if (data == -1 || data == '\n') {
                        break;
                    }
                    buffer[len++] = (byte) data;
                }
                
                System.out.println("read" + new String(buffer, 0, len));
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.out.println("error");
                e.printStackTrace();
            }
        }

    }

    /**
     *
     */
    public static void main(String[] args) {
        try {
            TwoWaySerialComm ttt = new TwoWaySerialComm();
            ttt.connect("COM7");
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.print(e.toString());
        }
    }

}
