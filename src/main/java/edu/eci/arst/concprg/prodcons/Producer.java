/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arst.concprg.prodcons;

import java.util.Queue;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
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
   * En un bucle infinito, genera un nuevo número aleatorio,
   * lo añade a la cola y duerme el hilo durante 1 segundo.
   * Imprime el número que ha añadido.
   */
  @Override
  public void run() {
    while (true) {
      dataSeed = dataSeed + rand.nextInt(100);
      System.out.println("Producer added " + dataSeed);
      queue.add(dataSeed);

      try {
        Thread.sleep(1000);
      } catch (InterruptedException ex) {
        Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
}
