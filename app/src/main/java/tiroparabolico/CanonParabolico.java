package tiroparabolico;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CanonParabolico {

    static final int NUM_DISPAROS = 5;

    private static final List<ProyectilDisparo> disparos = new ArrayList<>();
    private static final Scanner sc = new Scanner(System.in);

    // Codigos de escape ANSI para dar color al texto en la terminal.
    // La terminal los interpreta como instrucciones de formato, no los imprime.
    // Formato: \u001B[ = caracter ESC, luego el codigo, luego 'm' (ESTO ME LO RECOMENDA LA IA)
    private static final String R  = "\u001B[0m";   
    private static final String B  = "\u001B[1m";   
    private static final String CY = "\u001B[36m";  
    private static final String GR = "\u001B[32m";  
    private static final String YE = "\u001B[33m";  
    private static final String BL = "\u001B[34m";  
    private static final String MA = "\u001B[35m";  
    private static final String RE = "\u001B[31m";  
    private static final String DI = "\u001B[2m";   
    private static final String WH = "\u001B[37m";  



    public static void main(String[] args) {

        limpiarPantalla();
        mostrarBanner();

        for (int i = 1; i <= NUM_DISPAROS; i++) {

            ingresarDisparo(i);

        }

        System.out.print(DI + "\n  Presiona Enter para ver los resultados..." + R);
        sc.nextLine();
        limpiarPantalla();
        mostrarResultadosIndividuales();

        System.out.print(DI + "\n  Presiona Enter para ver las estadisticas..." + R);
        sc.nextLine();
        mostrarEstadisticas();

        System.out.print(DI + "\n  Presiona Enter para ver la grafica ASCII..." + R);
        sc.nextLine();
        mostrarGraficaASCII();

        System.out.println("\n" + CY + B + "  Programa finalizado." + R + "\n");
        sc.close();
    }


    private static void ingresarDisparo(int numero) {

        System.out.println();
        System.out.println(CY + B + "  DISPARO " + numero + " de " + NUM_DISPAROS + R);
        System.out.println(DI + "  Ingresa los datos medidos en el experimento." + R);
        System.out.println();

        double angulo    = pedirDouble("  Angulo de lanzamiento    (grados) : ", 0, 90);
        double altura    = pedirDouble("  Altura inicial del canon (m)      : ", 0, 100);
        double distancia = pedirDouble("  Distancia experimental   (m)      : ", 0.001, 10000);
        double tiempo    = pedirDouble("  Tiempo de vuelo          (s)      : ", 0.001, 10000);

        ProyectilDisparo d = new ProyectilDisparo(numero, angulo, altura, distancia, tiempo);
        disparos.add(d);

        System.out.println();
        System.out.println(GR + "  [OK] Disparo " + numero + " registrado." + R);
        
    }

    private static void mostrarResultadosIndividuales() {

        System.out.println(B + CY + "\n  RESULTADOS CINEMATICOS - " + NUM_DISPAROS + " DISPAROS" + R);

        for (ProyectilDisparo d : disparos) {

            System.out.println();
            System.out.println(B + WH + "  Disparo #" + d.getId()
                    + "  (angulo = " + d.getAnguloGrados() + " grados)"
                    + "  h0 = " + d.getAlturaInicial() + " m" + R);

            System.out.println();
            System.out.println(DI + "  VELOCIDADES" + R);
            campo("Velocidad inicial   V0", d.getVelocidadInicial(), "m/s");
            campo("Componente horiz.   Vx", d.getVx(),               "m/s");
            campo("Componente vert.    Vy", d.getVy(),               "m/s");

            System.out.println();
            System.out.println(DI + "  TRAYECTORIA" + R);
            campo("Tiempo de subida    ts", d.getTiempoSubida(),     "s  ");
            campo("Altura maxima     Hmax", d.getAlturaMaxima(),     "m  ");

            System.out.println();
            System.out.println(DI + "  EXPERIMENTAL  vs  TEORICO" + R);
            campoDual("Alcance", d.getDistanciaExp(), d.getAlcanceTeorico(), "m", d.getErrorAlcance());
            campoDual("Tiempo ", d.getTiempoExp(),    d.getTiempoTeorico(),  "s", d.getErrorTiempo());

        }

    }



    private static void mostrarEstadisticas() {
        System.out.println();
        System.out.println(B + BL + "  ESTADISTICAS GENERALES" + R);

        // Promedio de V0
        double sumaV0 = 0;
        for (ProyectilDisparo d : disparos) sumaV0 += d.getVelocidadInicial();
        double promedioV0 = sumaV0 / disparos.size();

        // Desviacion estandar de V0
        double sumaVarianza = 0;
        
        for (ProyectilDisparo d : disparos)
    
            sumaVarianza += Math.pow(d.getVelocidadInicial() - promedioV0, 2);

        double desviacionV0 = Math.sqrt(sumaVarianza / disparos.size());

        System.out.println();
        System.out.println(B + "  Velocidad inicial V0:" + R);
        System.out.printf("    Promedio           prom = " + GR + B + "%10.4f" + R + "  m/s%n", promedioV0);
        System.out.printf("    Desv. estandar    sigma = " + YE + B + "%10.4f" + R + "  m/s%n", desviacionV0);
        System.out.printf("    Coef. variacion     CV%% = " + MA + B + "%10.2f" + R + "  %%%n",
                (desviacionV0 / promedioV0) * 100);

        // V0 por disparo
        System.out.println();
        System.out.println(DI + "  V0 por disparo:" + R);

        for (ProyectilDisparo d : disparos) {

            double diff  = d.getVelocidadInicial() - promedioV0;
            String signo = diff >= 0 ? "+" : "";
            System.out.printf("    Disparo #%d  angulo=%-5.1f grados  V0=%8.4f m/s  (prom %s%.4f)%n",
                    d.getId(), d.getAnguloGrados(), d.getVelocidadInicial(), signo, diff);

        }


        // Mayor y menor alcance teorico
        ProyectilDisparo mayor = disparos.get(0), menor = disparos.get(0);

        for (ProyectilDisparo d : disparos) {

            if (d.getAlcanceTeorico() > mayor.getAlcanceTeorico()) mayor = d;
            if (d.getAlcanceTeorico() < menor.getAlcanceTeorico()) menor = d;

        }

        System.out.println();
        System.out.println(B + "  Alcances:" + R);
        System.out.printf("    Mayor alcance  -> Disparo #%d  angulo=%.1f grados  Xteo=" + GR + "%.4f m" + R + "%n",
                mayor.getId(), mayor.getAnguloGrados(), mayor.getAlcanceTeorico());
        System.out.printf("    Menor alcance  -> Disparo #%d  angulo=%.1f grados  Xteo=" + RE + "%.4f m" + R + "%n",
                menor.getId(), menor.getAnguloGrados(), menor.getAlcanceTeorico());

        // Menor error y promedio de errores
        ProyectilDisparo menorError = disparos.get(0);
        for (ProyectilDisparo d : disparos)
            if (d.getErrorAlcance() < menorError.getErrorAlcance()) menorError = d;

        double sumaErr = 0;
        for (ProyectilDisparo d : disparos) sumaErr += d.getErrorAlcance();

        System.out.println();
        System.out.println(B + "  Precision experimental:" + R);
        System.out.printf("    Menor error    -> Disparo #%d  angulo=%.1f grados  Error=" + GR + "%.2f%%" + R + "%n",
                menorError.getId(), menorError.getAnguloGrados(), menorError.getErrorAlcance());
        System.out.printf("    Promedio error                                   = " + YE + "%.2f%%" + R + "%n",
                sumaErr / disparos.size());

        // Tabla resumen
        System.out.println();
        System.out.println(B + DI + "  Tabla resumen:" + R);
        System.out.printf(B + "  %-5s %-10s %-10s %-10s %-10s %-10s %-8s%n" + R,
                "#", "angulo(g)", "V0 (m/s)", "Hmax (m)", "Xteo (m)", "Xexp (m)", "Err%");
        System.out.println(DI + "  " + rep('-', 68) + R);
        for (ProyectilDisparo d : disparos) {
            String col = d.getErrorAlcance() < 5 ? GR : d.getErrorAlcance() < 15 ? YE : RE;
            System.out.printf("  %-5d %-10.1f %-10.4f %-10.4f %-10.4f %-10.4f " + col + "%-8.2f" + R + "%n",
                    d.getId(), d.getAnguloGrados(), d.getVelocidadInicial(),
                    d.getAlturaMaxima(), d.getAlcanceTeorico(), d.getDistanciaExp(),
                    d.getErrorAlcance());
        }
        System.out.println(DI + "  " + rep('-', 68) + R);
        System.out.printf(B + "  %-5s %-10s %-10.4f %-10s %-10s %-10s %-8.2f%n" + R,
                "prom", "-", promedioV0, "-", "-", "-", sumaErr / disparos.size());
    }


    private static void mostrarGraficaASCII() {

        final int ANCHO = 72;
        final int ALTO  = 24;

        double xMax = 0, yMaxFinal = 0;
        for (ProyectilDisparo d : disparos) {
            xMax      = Math.max(xMax,      d.getAlcanceTeorico());
            yMaxFinal = Math.max(yMaxFinal, d.getAlturaMaxima());
        }
        xMax      *= 1.05;
        yMaxFinal *= 1.15;
        if (yMaxFinal < 0.1) yMaxFinal = 1.0;

        char[][]   canvas = new char[ALTO][ANCHO];
        String[][] colMap = new String[ALTO][ANCHO];

        for (int r = 0; r < ALTO; r++)

            for (int c = 0; c < ANCHO; c++) { 
                
                canvas[r][c] = ' '; colMap[r][c] = R; 
            
            }

        char[]   marcas  = {'*', '#', '@', '+', 'o'};
        String[] colores = {CY, YE, MA, GR, BL};

        for (int idx = 0; idx < disparos.size(); idx++) {

            ProyectilDisparo d = disparos.get(idx);

            int pasos = 600;

            double dt = d.getTiempoTeorico() / pasos;

            for (int p = 0; p <= pasos; p++) {

                double t  = p * dt;
                double px = d.x(t);
                double py = d.y(t);

                if (py < 0) break;

                int col = (int) Math.round((px / xMax) * (ANCHO - 1));
                int row = ALTO - 1 - (int) Math.round((py / yMaxFinal) * (ALTO - 1));

                if (col >= 0 && col < ANCHO && row >= 0 && row < ALTO) {

                    canvas[row][col] = marcas[idx % marcas.length];
                    colMap[row][col] = colores[idx % colores.length];

                }

            }

        }


        System.out.println();
        System.out.println(B + CY + "  GRAFICA DE TRAYECTORIAS" + R);
        System.out.println();

        for (int r = 0; r < ALTO; r++) {

            if (r == 0)
                System.out.printf(DI + "  %6.2fm |" + R, yMaxFinal);
            else if (r == ALTO / 2)
                System.out.printf(DI + "  %6s |" + R, "h(m)");
            else
                System.out.printf("  %6s |", "");

            for (int c = 0; c < ANCHO; c++)
                System.out.print(colMap[r][c] + B + canvas[r][c] + R);
            System.out.println();

        }

        System.out.printf("  %7s +%s%n", "", rep('-', ANCHO));
        System.out.printf("  %8s 0%s%.2f m%n", "", rep(' ', ANCHO - 8), xMax);

        System.out.println();
        System.out.println(DI + "  Leyenda:" + R);

        for (int idx = 0; idx < disparos.size(); idx++) {

            ProyectilDisparo d = disparos.get(idx);
            System.out.printf("  %s%s%c%s  Disparo #%d  angulo=%.1f grados  V0=%.4f m/s  Hmax=%.4f m  Xteo=%.4f m%n",
                    colores[idx % colores.length], B, marcas[idx % marcas.length], R,
                    d.getId(), d.getAnguloGrados(),
                    d.getVelocidadInicial(), d.getAlturaMaxima(), d.getAlcanceTeorico());

        }

    }

    
    //  HELPERS
  

    private static double pedirDouble(String msg, double min, double max) {

        while (true) {

            System.out.print(WH + msg + R);
            String linea = sc.nextLine().trim();

            try {

                double v = Double.parseDouble(linea);

                if (v < min || v > max)
                    System.out.println(RE + "  [ERROR] El valor debe estar entre " + min + " y " + max + R);
                else
                    return v;
            } catch (NumberFormatException e) {

                System.out.println(RE + "  [ERROR] Ingresa un numero valido (usa punto decimal, ej: 1.5)" + R);

            }

        }

    }

    private static void campo(String nombre, double valor, String unidad) {

        System.out.printf("  %-26s = " + GR + B + "%10.4f" + R + "  %s%n", nombre, valor, unidad);

    }

    private static void campoDual(String nombre, double exp, double teo,
                                   String unidad, double error) {

        String col = error < 5 ? GR : error < 15 ? YE : RE;
        System.out.printf("  %-8s  exp=" + CY + "%8.4f" + R +
                          "  teo=" + GR + "%8.4f" + R +
                          " %s  error=" + col + B + "%.2f%%" + R + "%n",
                nombre, exp, teo, unidad, error);

    }

    private static String rep(char c, int n) {

        return String.valueOf(c).repeat(Math.max(0, n));

    }

    private static void limpiarPantalla() {

        System.out.print("\033[H\033[2J");
        System.out.flush(); // Esto lo implemento para poder forzar para que se limpie la pantalla en algunas terminales que no lo hacen con el código de escape solo.

    }

    private static void mostrarBanner() {

        System.out.println(CY + B);
        System.out.println("  ANALISIS DE TIRO PARABOLICO - CANON");
        System.out.println("  Ingresa " + NUM_DISPAROS + " disparos experimentales");
        System.out.println("  g = 9.8 m/s2");
        System.out.println(R);
        System.out.println(DI + "  Se pediran 4 datos por disparo: angulo, altura, distancia y tiempo." + R);
    }

}