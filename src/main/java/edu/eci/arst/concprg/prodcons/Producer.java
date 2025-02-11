/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arst.concprg.prodcons;

import java.util.Queue;
import java.util.Random;

/**
 *
 * Clase Producer que extiende de Thread y se encarga de añadir elementos a la cola
 * Implementa un productor que añade elementos a la cola de enteros
 *
 * @author Diego Chicuazuque
 * @version v1.0
 */
public class Producer extends Thread {

  // Cola de enteros
  private Queue<Integer> queue = null;
  // Semilla de datos que va a incrementar
  private int dataSeed = 0;
  // Random para generar numeros aleatorios
  private Random rand = null;
  // Limite de stock (No se usa)
  private final long stockLimit;

  /*
   * Constructor de la clase Producer
   * @param queue Cola de enteros
   * @param stockLimit Limite de stock
   */
  public Producer(Queue<Integer> queue, long stockLimit) {
    this.queue = queue;
    // Inicia el random con la semilla del tiempo actual
    rand = new Random(System.currentTimeMillis());
    this.stockLimit = stockLimit;
  }

  /*
   * Actualización del método cambiando para que ahora halla un limine de stock(antes no se estaba usando la variable stockLimit)
   */
  @Override
  public void run() {
    while (true) {
      // Se sincroniza la cola para evitar problemas de concurrencia
      synchronized (queue) {
        while (queue.size() >= stockLimit) {
          try {
            queue.wait(); // Espera hasta que haya espacio en la cola
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Manejo de interrupciones
          }
        }
        // Genera un nuevo número aleatorio
        dataSeed += rand.nextInt(100);
        System.out.println("Producer added " + dataSeed);
        // Añade el nuevo número a la cola
        queue.add(dataSeed);
        queue.notifyAll(); // Notifica a los consumidores que hay un nuevo elemento
      }
      try {
        Thread.sleep(100); // Se cambio de 1000 a 100 para que el productor sea mas rapido
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
      }
    }
  }
  /*
   * En un bucle infinito, genera un nuevo número aleatorio,
   * lo añade a la cola y duerme el hilo durante 1 segundo.
   * Imprime el número que ha añadido.
   */
  //@Override
  //public void run() {
  //  while (true) {
  //    dataSeed = dataSeed + rand.nextInt(100);
  //    System.out.println("Producer added " + dataSeed);
  //    queue.add(dataSeed);

  //    try {
  //      Thread.sleep(1000);
  //    } catch (InterruptedException ex) {
  //      Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
  //    }
  //  }
  // }
}
