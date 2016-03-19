/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.io.*;
import javax.sound.sampled.*;

/**
 *
 * @author David
 */
public class CapturadorAudio {

    AudioFormat audioFormat;
    TargetDataLine targetDataLine;
    AudioFileFormat.Type fileType;
    File audioFile;

    /**
     * Constructor por defecto de la clase.
     */
    public CapturadorAudio() {
        fileType = AudioFileFormat.Type.WAVE;
        audioFile = new File("tmp.wav");
    }

    /**
     * Método que inicia la captura de audio de un micrófono.
     *
     * Se crea la línea de datos
     */
    public void capturaAudio(int calidad) {
        try {
            //Prepara las cosas para la captura de audio
            audioFormat = getAudioFormat(calidad);
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);

            //Crea el hilo que va a capturar el audio del micrófono y lo va a
            // guardar en un archivo y además inicia la ejecución del hilo.
            new CaptureThread().start();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

    /**
     * Método que finaliza la captura de audio.
     *
     * Para la línea de datos y la cierra.
     */
    void paraCaptura() {
        targetDataLine.stop();
        targetDataLine.close();
    }

    /**
     * Método que crea un objeto del tipo AudioFormat con lo parámetros del
     * audio.
     *
     * Crea el objeto con los parámetros de audio establecidos y lo devuelve.
     *
     * @return AudioFormat con los parámetros del audio.
     */
    private AudioFormat getAudioFormat(int calidad) {
        float sampleRate;
        int sampleSizeInBits;
        int channels;

        if (calidad == 0) { //Calidad conversación
            sampleRate = 8000.0F;
            sampleSizeInBits = 8;
            channels = 1;
        } else if (calidad == 1) { //Calidad radio
            sampleRate = 22000.0F;
            sampleSizeInBits = 16;
            channels = 2;
        } else { //Calidad CD
            sampleRate = 44100.0F;
            sampleSizeInBits = 16;
            channels = 2;
        }

        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(
                sampleRate,
                sampleSizeInBits,
                channels,
                signed,
                bigEndian);
    }

    /**
     * Clase interna para la captura de datos del micrófono.
     *
     * Abre la línea de datos donde se encuentra el micrófono del que se va a
     * grabar el audio, y la inicia para empezar a escribir los datos que lee en
     * el archivo especificado.
     */
    class CaptureThread extends Thread {

        @Override
        public void run() {
            try {
                targetDataLine.open(audioFormat);
                targetDataLine.start();
                AudioSystem.write(
                        new AudioInputStream(targetDataLine),
                        fileType,
                        audioFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
