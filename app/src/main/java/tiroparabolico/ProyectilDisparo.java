package tiroparabolico;

/**
 * ProyectilDisparo.java
 * Encapsula un disparo experimental y calcula toda la cinemática
 * del tiro parabólico.
 */
public class ProyectilDisparo {

    private final int    id;
    private final double anguloGrados;
    private final double alturaInicial;
    private final double distanciaExp;
    private final double tiempoExp;

    public static final double G = 9.8;

    private double velocidadInicial;
    private double vx, vy;
    private double alturaMaxima;
    private double tiempoSubida;
    private double tiempoTeorico;
    private double alcanceTeorico;

    public ProyectilDisparo(int id, double anguloGrados, double alturaInicial,
                            double distanciaExp, double tiempoExp) {

        this.id            = id;
        this.anguloGrados  = anguloGrados;
        this.alturaInicial = alturaInicial;
        this.distanciaExp  = distanciaExp;
        this.tiempoExp     = tiempoExp;
        calcular();

    }

    private void calcular() {

        double rad = Math.toRadians(anguloGrados);

        // V0 de: x = V0·cos(θ)·t
        if (Math.abs(Math.cos(rad)) < 1e-9) {

            velocidadInicial = (alturaInicial + 0.5 * G * tiempoExp * tiempoExp) / tiempoExp;

        } 
        else {

            velocidadInicial = distanciaExp / (Math.cos(rad) * tiempoExp);
            
        }

        vx = velocidadInicial * Math.cos(rad);
        vy = velocidadInicial * Math.sin(rad);

        tiempoSubida = vy / G;
        alturaMaxima = alturaInicial + vy * tiempoSubida - 0.5 * G * tiempoSubida * tiempoSubida;

        // Raíz positiva de: ½g·t² - vy·t - h0 = 0
        double a = 0.5 * G;
        double b = -vy;
        double c = -alturaInicial;
        tiempoTeorico  = (-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a);
        alcanceTeorico = vx * tiempoTeorico;
    }

    public int    getId()               { return id; }
    public double getAnguloGrados()     { return anguloGrados; }
    public double getAlturaInicial()    { return alturaInicial; }
    public double getDistanciaExp()     { return distanciaExp; }
    public double getTiempoExp()        { return tiempoExp; }
    public double getVelocidadInicial() { return velocidadInicial; }
    public double getVx()               { return vx; }
    public double getVy()               { return vy; }
    public double getAlturaMaxima()     { return alturaMaxima; }
    public double getTiempoSubida()     { return tiempoSubida; }
    public double getTiempoTeorico()    { return tiempoTeorico; }
    public double getAlcanceTeorico()   { return alcanceTeorico; }

    public double getErrorAlcance() {
        return Math.abs(distanciaExp - alcanceTeorico) / alcanceTeorico * 100.0;
    }
    public double getErrorTiempo() {
        return Math.abs(tiempoExp - tiempoTeorico) / tiempoTeorico * 100.0;
    }

    public double y(double t) {
        return alturaInicial + vy * t - 0.5 * G * t * t;
    }

    public double x(double t) {
        return vx * t;
    }
}