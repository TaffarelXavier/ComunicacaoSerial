/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.embarcados.comunicaoserial;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPortRXTX {

    private OutputStream out1;
    private InputStream in1;

    public SerialPortRXTX() {
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
                serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();
                out1 = out;
                in1 = in;
                (new Thread(new SerialReader(in))).start();
                (new Thread(new SerialWriter(out))).start();

            } else {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }
    }

    /**
     *
     */
    public static class SerialReader implements Runnable {

        InputStream in;
        //
        public static String saida;

        public SerialReader(InputStream in) {
            this.in = in;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int len = -1;
            try {
                while ((len = this.in.read(buffer)) > -1) {
                    System.out.print(new String(buffer, 0, len));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     */
    public static class SerialWriter implements Runnable {

        OutputStream out;

        public SerialWriter(OutputStream out) {
            this.out = out;
        }

        public void run() {
            try {
                int c = 0;
                while ((c = System.in.read()) > -1) {
                    this.out.write(c);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void escreverNaPorta(String data) {
        try {
            out1.write(data.getBytes());

            out1.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void writeInPort() {
        SerialWriter out = new SerialWriter(out1);
        out.run();
    }

    /**
     * Buffer to hold the reading
     */
    private byte[] readBuffer = new byte[400];

    public void readSerial() {
        byte[] buffer = new byte[1024];
        int len = -1;
        try {
            while ((len = this.in1.read(buffer)) > -1) {
                System.out.print(new String(buffer, 0, len));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String lerPorta() {

        return null;
    }

//    public static void main(String[] args) {
//        try {
//
//            SerialPortRXTX ttt = new SerialPortRXTX();
//
//            ttt.connect("COM7");
//                
//            ttt.readSerial();
//            //System.out.println(ttt.in1.read());
//            /* while (true) {
//
//                Scanner entrada = new Scanner(System.in);
//
//                System.out.print("Insira um comando: ");
//
//                String data = entrada.next();
//
//                ttt.escreverNaPorta(data);
//
//                ttt.lerPorta();
//
//            }*/
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
}
