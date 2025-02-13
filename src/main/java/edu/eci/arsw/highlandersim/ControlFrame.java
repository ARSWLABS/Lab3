package edu.eci.arsw.highlandersim;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

/**
 * Clase que representa el control del juego.
 *
 * @Autor: Diego Chicuazuque - Juan Cancelado
 * @version 2.0 2/23/21
 */
public class ControlFrame extends JFrame {

  // Constantes para la salud y el daño por defecto
  private static final int DEFAULT_IMMORTAL_HEALTH = 100;
  private static final int DEFAULT_DAMAGE_VALUE = 10;

  // Variable para controlar la pausa del juego
  public static boolean isPaused = false;

  // Panel de contenido
  private JPanel contentPane;

  // Lista de inmortales
  private List<Immortal> immortals;

  // Área de texto para mostrar la salida
  private JTextArea output;

  // Etiqueta para mostrar la suma de la salud de los inmortales
  private JLabel statisticsLabel;

  // Panel de desplazamiento para la salida
  private JScrollPane scrollPane;

  // Campo de texto para ingresar el número de inmortales
  private JTextField numOfImmortals;

  /**
   * Método principal para lanzar la aplicación.
   *
   * @param args Argumentos de la línea de comandos.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(
      new Runnable() {
        public void run() {
          try {
            ControlFrame frame = new ControlFrame();
            frame.setVisible(true);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    );
  }

  /**
   * Constructor de la clase ControlFrame.
   */
  public ControlFrame() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 647, 248);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    contentPane.setLayout(new BorderLayout(0, 0));
    setContentPane(contentPane);

    // Crear la barra de herramientas
    JToolBar toolBar = new JToolBar();
    contentPane.add(toolBar, BorderLayout.NORTH);

    // Botón para iniciar el juego
    final JButton btnStart = new JButton("Start");
    btnStart.addActionListener(
      new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          immortals = setupInmortals();

          if (immortals != null) {
            for (Immortal im : immortals) {
              im.start();
            }
          }

          btnStart.setEnabled(false);
        }
      }
    );
    toolBar.add(btnStart);

    // Botón para pausar y verificar el juego
    JButton btnPauseAndCheck = new JButton("Pause and check");
    btnPauseAndCheck.addActionListener(
      new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          isPaused = true; // Pausar todos los inmortales
          int sum = 0;

          synchronized (immortals) { // Sincronizar el acceso a la lista
            for (Immortal im : immortals) {
              sum += im.getHealth();
            }
          }

          statisticsLabel.setText(
            "<html>" + immortals.toString() + "<br>Health sum: " + sum
          );
        }
      }
    );
    toolBar.add(btnPauseAndCheck);

    // Botón para reanudar el juego
    JButton btnResume = new JButton("Resume");

    btnResume.addActionListener(
      new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          isPaused = false; // Reanudar todos los inmortales
          synchronized (immortals) {
            immortals.notifyAll(); // Notificar a todos los inmortales para que continúen
          }
        }
      }
    );
    toolBar.add(btnResume);

    // Etiqueta para mostrar el número de inmortales
    JLabel lblNumOfImmortals = new JLabel("num. of immortals:");
    toolBar.add(lblNumOfImmortals);

    // Campo de texto para ingresar el número de inmortales
    numOfImmortals = new JTextField();
    numOfImmortals.setText("3");
    toolBar.add(numOfImmortals);
    numOfImmortals.setColumns(10);

    // Botón para detener el juego
    JButton btnStop = new JButton("STOP");
    btnStop.setForeground(Color.RED);
    btnStop.addActionListener(
      new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          // Detener todos los inmortales
          for (Immortal im : immortals) {
            im.interrupt(); // Interrumpir el hilo del inmortal
          }
          immortals.clear(); // Limpiar la lista de inmortales
          output.append("All immortals have been stopped.\n"); // Mensaje de salida
        }
      }
    );
    toolBar.add(btnStop);

    // Panel de desplazamiento para la salida
    scrollPane = new JScrollPane();
    contentPane.add(scrollPane, BorderLayout.CENTER);

    // Área de texto para mostrar la salida
    output = new JTextArea();
    output.setEditable(false);
    scrollPane.setViewportView(output);

    // Etiqueta para mostrar la suma de la salud de los inmortales
    statisticsLabel = new JLabel("Immortals total health:");
    contentPane.add(statisticsLabel, BorderLayout.SOUTH);
  }

  /**
   * Método para configurar los inmortales.
   *
   * @return La lista de inmortales configurados.
   */
  public List<Immortal> setupInmortals() {
    ImmortalUpdateReportCallback ucb = new TextAreaUpdateReportCallback(
      output,
      scrollPane
    );

    try {
      int ni = Integer.parseInt(numOfImmortals.getText());

      List<Immortal> il = new LinkedList<Immortal>();

      for (int i = 0; i < ni; i++) {
        Immortal i1 = new Immortal(
          "im" + i,
          il,
          DEFAULT_IMMORTAL_HEALTH,
          DEFAULT_DAMAGE_VALUE,
          ucb
        );
        il.add(i1);
      }
      return il;
    } catch (NumberFormatException e) {
      JOptionPane.showConfirmDialog(null, "Número inválido.");
      return null;
    }
  }
}

class TextAreaUpdateReportCallback implements ImmortalUpdateReportCallback {

  JTextArea ta;
  JScrollPane jsp;

  public TextAreaUpdateReportCallback(JTextArea ta, JScrollPane jsp) {
    this.ta = ta;
    this.jsp = jsp;
  }

  @Override
  public void processReport(String report) {
    ta.append(report);

    //move scrollbar to the bottom
    javax.swing.SwingUtilities.invokeLater(
      new Runnable() {
        public void run() {
          JScrollBar bar = jsp.getVerticalScrollBar();
          bar.setValue(bar.getMaximum());
        }
      }
    );
  }
}
