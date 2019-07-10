package br.com.embarcados.comunicaoserial;

import java.io.IOException;
import static java.lang.System.in;
import static java.lang.System.out;
import javax.swing.JButton;

/**
 * @author klauder
 */
public class Arduino {

    private final ControlePorta arduino;

    private static String getDataInputSerial = null;

    public static String getGetDataInputSerial() {
        return getDataInputSerial;
    }

    public static void setGetDataInputSerial(String getDataInputSerial) {
        Arduino.getDataInputSerial = getDataInputSerial;
    }

    /**
     * Construtor da classe Arduino
     */
    public Arduino() {
        arduino = new ControlePorta("COM7", 9600);//Windows - porta e taxa de transmissão
    }

    /**
     * Envia o comando para a porta serial
     *
     * @param button - Botão que é clicado na interface Java
     */
    public void comunicacaoArduino(JButton button) {
        if ("Ligar".equals(button.getActionCommand())) {
            arduino.enviaDados('a');
            System.out.println(button.getText());//Imprime o nome do botão pressionado
        } else if ("Desligar".equals(button.getActionCommand())) {
            arduino.enviaDados('b');
            System.out.println(button.getText());
        } else {
            this.closeConn();
            System.out.println(button.getText());//Imprime o nome do botão pressionado
        }
    }

    /**
     *
     */
    public void closeConn() {
        try {
            arduino.close();
            in.close();
            out.close();
        } catch (IOException ex) {
            // don't care
        }
        // Close the port.
        arduino.close();
    }

    public void lerDados() {
        arduino.iniciaLeitura();
    }
}
