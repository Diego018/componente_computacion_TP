# Tiro Parabolico - Canon PASCO

Programa de consola en Java que analiza datos experimentales de un canon de tiro parabolico (modelo PASCO ME-6000). A partir de los datos medidos en el laboratorio, calcula las variables cinematicas del movimiento y muestra una grafica de trayectorias en la terminal.

## Que hace el programa

- Recibe 5 disparos con sus datos experimentales: angulo, altura inicial, distancia y tiempo de vuelo
- Calcula la velocidad inicial de salida y sus componentes horizontal y vertical
- Calcula la altura maxima y el tiempo de subida
- Compara el alcance y tiempo experimentales con los valores teoricos
- Muestra estadisticas generales: promedio y desviacion estandar de V0, mayor y menor alcance, disparo con menor error
- Grafica las trayectorias de los 5 disparos en ASCII directamente en la terminal

## Estructura del proyecto

```
src/main/java/tiroparabolico/
    App.java                 // Punto de entrada
    CanonParabolico.java     // Logica de la interfaz y presentacion
    ProyectilDisparo.java    // Calculos cinematicos
```

## Como correrlo

```bash
javac App.java CanonParabolico.java ProyectilDisparo.java
java App
```

## Datos que necesitas tener antes de correr el programa

Por cada disparo necesitas:

| Dato | Unidad |
|---|---|
| Angulo de lanzamiento | grados |
| Altura inicial del canon | metros (ej: 26 cm = 0.26) |
| Distancia horizontal medida | metros |
| Tiempo de vuelo medido | segundos |

## Formulas usadas

| Variable | Formula |
|---|---|
| Velocidad inicial | V0 = x / (cos(θ) · t) |
| Componente horizontal | Vx = V0 · cos(θ) |
| Componente vertical | Vy = V0 · sin(θ) |
| Altura maxima | Hmax = h0 + Vy² / (2g) |
| Tiempo teorico de vuelo | raiz positiva de: ½g·t² - Vy·t - h0 = 0 |
| Alcance teorico | Xteo = Vx · t_teorico |
| Error relativo | \|Xexp - Xteo\| / Xteo × 100 |
