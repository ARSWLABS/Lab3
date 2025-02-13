package edu.eci.arsw.highlandersim;

import java.util.List;
import java.util.Random;

public class Immortal extends Thread {

  private ImmortalUpdateReportCallback updateCallback = null;
  private int health = 100;
  private int defaultDamageValue;
  private final List<Immortal> immortalsPopulation;
  private final String name;
  private final Random r = new Random(System.currentTimeMillis());
  private boolean paused = false;

  public Immortal(
    String name,
    List<Immortal> immortalsPopulation,
    int health,
    int defaultDamageValue,
    ImmortalUpdateReportCallback ucb
  ) {
    super(name);
    this.updateCallback = ucb;
    this.name = name;
    this.immortalsPopulation = immortalsPopulation;
    this.health = health;
    this.defaultDamageValue = defaultDamageValue;
  }

  public void run() {
    while (true) {
      synchronized (immortalsPopulation) { // Sincronizar el acceso a la lista
        while (ControlFrame.isPaused) { // Comprobar si está en pausa
          try {
            immortalsPopulation.wait(); // Esperar si está en pausa
          } catch (InterruptedException e) {
            return; // Salir si se interrumpe
          }
        }

        if (health <= 0) {
          immortalsPopulation.remove(this); // Remover inmortal si está muerto
          updateCallback.processReport(this + " has died!\n");
          return; // Salir si está muerto
        }

        // Lógica de pelea...
        int myIndex = immortalsPopulation.indexOf(this);
        int nextFighterIndex = r.nextInt(immortalsPopulation.size());

        // Evitar pelea consigo mismo
        if (nextFighterIndex == myIndex) {
          nextFighterIndex =
            (nextFighterIndex + 1) % immortalsPopulation.size();
        }

        Immortal im = immortalsPopulation.get(nextFighterIndex);
        this.fight(im);
      }

      try {
        Thread.sleep(1); // Pausa breve para simular el tiempo de pelea
      } catch (InterruptedException e) {
        return; // Salir si se interrumpe
      }
    }
  }

  public void fight(Immortal i2) {
    synchronized (immortalsPopulation) {
      if (i2.getHealth() > 0) {
        i2.changeHealth(i2.getHealth() - defaultDamageValue);
        this.health += defaultDamageValue;
        updateCallback.processReport("Fight: " + this + " vs " + i2 + "\n");
      } else {
        updateCallback.processReport(
          this + " says:" + i2 + " is already dead!\n"
        );
      }
    }
  }

  public void changeHealth(int v) {
    health = v;
  }

  public int getHealth() {
    return health;
  }

  public synchronized void pause() {
    paused = true;
  }

  public synchronized void resumeImmortal() {
    paused = false;
    notify();
  }

  @Override
  public String toString() {
    return name + "[" + health + "]";
  }
}
