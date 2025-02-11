# Lab3
## Integrantes: Juan Cancelado y Diego Chicuazuque
Usando la herramienta JVisualVM  para ver el consumo actual de recursos al ejecutar el programa
![image](https://github.com/user-attachments/assets/b32ebd92-23d7-491e-b5c2-a4b9eb2edbe6)


![image](https://github.com/user-attachments/assets/cc1ed3a1-6d2d-4be5-a8b5-36e1786420f7)

¿A que se debe el alto consumo?
    El alto consumo de CPU se debe a que el consumidor está en un bucle infinito que verifica constantemente si hay elementos en la cola. Si la cola está vacía, el consumidor sigue ejecutando el bucle sin hacer nada, lo que consume recursos de CPU innecesariamente.

¿Cual es la clase responsable?

    La clase responsable de este alto consumo de CPU es la clase Consumer, específicamente el método run() que contiene el bucle while (true).

Evidencia en JVisualVM con los ajustes pertinentes

![alt text](image.png)
