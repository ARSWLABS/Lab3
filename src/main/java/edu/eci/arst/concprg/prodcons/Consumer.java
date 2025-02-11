/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arst.concprg.prodcons;

import java.util.Queue;

/**
 *
 * @author hcadavid
 */
public class Consumer extends Thread {

  // Cola de enteros
  private Queue<Integer> queue;

  /*
   * Constructor de la clase Consumer
   * @param queue Cola de enteros
   */
  public Consumer(Queue<Integer> queue) {
    this.queue = queue;
  }

  /*
   * Metodo que se encarga de consumir los elementos de la cola
   * Este metodo se ejecuta cuando el hilo es iniciado en un bucle infinito,
   * verifica si hay elementos en la cola. Si hay, consume el primer elemento(usando poll, que tambien
   * lo elimina de la cola) e imprime un mensaje.
   */
  @Override
  public void run() {
    while (true) {
      if (queue.size() > 0) {
        int elem = queue.poll();
        System.out.println("Consumer consumes " + elem);
      }
    }
  }
}
