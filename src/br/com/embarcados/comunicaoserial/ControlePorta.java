package br.com.embarcados.comunicaoserial;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ControlePorta {

    private OutputStream serialOut;
    private static InputStream serialIn;
    private int taxa;
    private String portaCOM;
    private static boolean portaEncontrada = true;
    private SerialPort portaSerial;

    public OutputStream getSerialOut() {
        return serialOut;
    }

    public void setSerialOut(OutputStream serialOut) {
        this.serialOut = serialOut;
    }

    public int getTaxa() {
        return taxa;
    }

    public void setTaxa(int taxa) {
        this.taxa = taxa;
    }

    public String getPortaCOM() {
        return portaCOM;
    }

    public void setPortaCOM(String portaCOM) {
        this.portaCOM = portaCOM;
    }

    public static boolean isPortaEncontrada() {
        return portaEncontrada;
    }

    public static void setPortaEncontrada(boolean portaEncontrada) {
        ControlePorta.portaEncontrada = portaEncontrada;
    }

    /**
     * Construtor da classe ControlePorta
     *
     * @param portaCOM - Porta COM que será utilizada para enviar os dados para o arduino
     * @param taxa - Taxa de transferência da porta serial geralmente é 9600
     */
    public ControlePorta(String portaCOM, int taxa) {
        this.portaCOM = portaCOM;
        this.taxa = taxa;
        this.initialize();
    }

    /**
     * Médoto que verifica se a comunicação com a porta serial está ok
     */
    private void initialize() {
        try {
            //Define uma variável portId do tipo CommPortIdentifier para realizar a comunicação serial
            CommPortIdentifier portId = null;
            try {
                //Tenta verificar se a porta COM informada existe
                portId = CommPortIdentifier.getPortIdentifier(this.portaCOM);

                //portId.open(FileDescriptor.in);
            } catch (NoSuchPortException npe) {
                setPortaEncontrada(false);
                //Caso a porta COM não exista será exibido um erro 
                JOptionPane.showMessageDialog(null, "Porta COM não encontrada.",
                        "Porta COM", JOptionPane.PLAIN_MESSAGE);
            }
            //Abre a porta COM 
            try {
                SerialPort port = (SerialPort) portId.open("Comunicação serial", this.taxa);

                serialOut = port.getOutputStream();

                serialIn = port.getInputStream();

                port.setSerialPortParams(this.taxa, //taxa de transferência da porta serial 
                        SerialPort.DATABITS_8, //taxa de 10 bits 8 (envio)
                        SerialPort.STOPBITS_1, //taxa de 10 bits 1 (recebimento)
                        SerialPort.PARITY_NONE); //receber e enviar dados
            } catch (NullPointerException ex) {
                System.out.println(Arrays.toString(ex.getStackTrace()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void iniciaLeitura() {
        try {
            System.out.println(serialIn.read());
        } catch (IOException ex) {
            Logger.getLogger(ControlePorta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método que fecha a comunicação com a porta serial
     */
    public void close() {
        try {
            serialOut.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível fechar porta COM.",
                    "Fechar porta COM", JOptionPane.PLAIN_MESSAGE);
        }
    }

    /**
     * @param opcao - Valor a ser enviado pela porta serial
     */
    public void enviaDados(char opcao) {
        try {
            serialOut.write(opcao);//escreve o valor na porta serial para ser enviado
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Não foi possível enviar o dado. ",
                    "Enviar dados", JOptionPane.PLAIN_MESSAGE);
        }
    }

    public void readDados() {
        try {
            serialOut.flush();
        } catch (IOException ex) {
            Logger.getLogger(ControlePorta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
