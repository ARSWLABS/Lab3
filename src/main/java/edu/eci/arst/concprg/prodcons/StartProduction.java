/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arst.concprg.prodcons;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StartProduction {

  /*
   * Metodo principal de la clase StartProduction
   * Crea una cola de enteros y un productor que añade elementos a la cola.
   * Luego de 5 segundos, crea un consumidor que consume los elementos de la cola.
   * @param args Argumentos de la linea de comandos
   */
  public static void main(String[] args) {
    //Crea una cola de tipo LinkedBlockingQueue, que es una implementación de cola segura para hilos.
    Queue<Integer> queue = new LinkedBlockingQueue<>();

    // Inicializa un hilo productor que añade elementos a la cola.
    new Producer(queue, Long.MAX_VALUE).start();

    //let the producer create products for 5 seconds (stock).
    try {
      // duerme el hilo principal por 5 segundos para que el productor pueda añadir elementos a la cola.
      Thread.sleep(5000);
    } catch (InterruptedException ex) {
      Logger
        .getLogger(StartProduction.class.getName())
        .log(Level.SEVERE, null, ex);
    }
    // Inicializa un hilo consumidor que consume elementos de la cola.
    new Consumer(queue).start();
  }
}
