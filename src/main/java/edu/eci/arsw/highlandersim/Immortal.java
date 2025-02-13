package edu.eci.arsw.highlandersim;

import java.util.List;
import java.util.Random;

/**
 * Clase que representa un inmortal en el juego.
 *
 * Esta clase extiende de Thread, lo que permite que cada inmortal se ejecute en su propio hilo.
 * Cada inmortal puede atacar a otros inmortales, y su salud se ve afectada por los ataques.
 *
 * @Autor: Diego Chicuazuque - Juan Cancelado
 * @version 2.0 2/23/21
 */
public class Immortal extends Thread {

  // Callback para reportar actualizaciones sobre el estado del inmortal
  private ImmortalUpdateReportCallback updateCallback = null;

  // Salud del inmortal
  private int health = 100;

  // Valor de daño que el inmortal puede infligir
  private int defaultDamageValue;

  // Lista de todos los inmortales en el juego
  private final List<Immortal> immortalsPopulation;

  // Nombre del inmortal
  private final String name;

  // Generador de números aleatorios
  private final Random r = new Random(System.currentTimeMillis());

  // Estado de pausa del inmortal
  private boolean paused = false;

  /**
   * Constructor de la clase Immortal.
   *
   * @param name Nombre del inmortal.
   * @param immortalsPopulation Lista de todos los inmortales en el juego.
   * @param health Salud inicial del inmortal.
   * @param defaultDamageValue Valor de daño que el inmortal puede infligir.
   * @param ucb Callback para reportar actualizaciones sobre el estado del inmortal.
   */
  public Immortal(
    String name,
    List<Immortal> immortalsPopulation,
    int health,
    int defaultDamageValue,
    ImmortalUpdateReportCallback ucb
  ) {
    super(name); // Llama al constructor de la clase Thread con el nombre del inmortal
    this.updateCallback = ucb;
    this.name = name;
    this.immortalsPopulation = immortalsPopulation;
    this.health = health;
    this.defaultDamageValue = defaultDamageValue;
  }

  /**
   * Método que se ejecuta cuando se inicia el hilo del inmortal.
   * Este método contiene la lógica principal del inmortal, que incluye
   * la selección de oponentes y la ejecución de peleas.
   */
  public void run() {
    while (true) {
      // Verificar si el hilo ha sido interrumpido
      if (Thread.currentThread().isInterrupted()) {
        updateCallback.processReport(this + " has been stopped.\n");
        return; // Salir del hilo si ha sido interrumpido
      }

      synchronized (immortalsPopulation) { // Sincronizar el acceso a la lista
        while (ControlFrame.isPaused) { // Comprobar si está en pausa
          try {
            immortalsPopulation.wait(); // Esperar si está en pausa
          } catch (InterruptedException e) {
            return; // Salir si se interrumpe
          }
        }

        // Verificar si el inmortal está muerto
        if (health <= 0) {
          immortalsPopulation.remove(this); // Remover inmortal si está muerto
          updateCallback.processReport(this + " has died!\n");
          return; // Salir si está muerto
        }

        // Verificar si hay inmortales vivos
        if (immortalsPopulation.size() <= 1) {
          // Si solo queda este inmortal o no hay inmortales, salir
          return;
        }

        // Seleccionar un oponente aleatorio
        Immortal opponent = null;
        while (
          opponent == null || opponent == this || opponent.getHealth() <= 0
        ) {
          int nextFighterIndex = r.nextInt(immortalsPopulation.size());
          opponent = immortalsPopulation.get(nextFighterIndex);
        }

        this.fight(opponent); // Luchar contra el oponente seleccionado
      }

      try {
        Thread.sleep(1); // Pausa breve para simular el tiempo de pelea
      } catch (InterruptedException e) {
        return; // Salir si se interrumpe
      }
    }
  }

  /**
   * Método que permite al inmortal pelear contra otro inmortal.
   *
   * @param i2 El inmortal contra el que se va a pelear.
   */
  public void fight(Immortal i2) {
    synchronized (immortalsPopulation) {
      if (i2.getHealth() > 0) {
        i2.changeHealth(i2.getHealth() - defaultDamageValue); // Reducir la salud del oponente
        this.health += defaultDamageValue; // Aumentar la salud del inmortal
        updateCallback.processReport("Fight: " + this + " vs " + i2 + "\n");
      } else {
        updateCallback.processReport(
          this + " says: " + i2 + " is already dead!\n"
        );
      }
    }
  }

  /**
   * Método para cambiar la salud del inmortal.
   *
   * @param v Nuevo valor de salud.
   */
  public void changeHealth(int v) {
    health = v;
  }

  /**
   * Método para obtener la salud actual del inmortal.
   *
   * @return La salud actual del inmortal.
   */
  public int getHealth() {
    return health;
  }

  /**
   * Método para representar el inmortal como una cadena.
   *
   * @return Una representación en cadena del inmortal, incluyendo su nombre y salud.
   */
  @Override
  public String toString() {
    return name + "[" + health + "]";
  }
}
