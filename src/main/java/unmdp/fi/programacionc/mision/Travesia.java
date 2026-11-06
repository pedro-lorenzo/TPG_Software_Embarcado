package unmdp.fi.programacionc.mision;

import unmdp.fi.programacionc.destinoytablero.Destino;
import unmdp.fi.programacionc.destinoytablero.Posicion;
import unmdp.fi.programacionc.destinoytablero.Tablero;
import unmdp.fi.programacionc.tablero.NavePropia;
import unmdp.fi.programacionc.bitacora.TipoEvento;
import unmdp.fi.programacionc.excepciones.OperacionInvalidaException;
import unmdp.fi.programacionc.tablero.ElementoDelUniverso;

public class Travesia {

    private final Tablero tablero;
    private final Destino destino;
    private final OperadorMision asistente;
    private final NavePropia nave;
    private static final int PAUSA_MS = 300;

    public Travesia(Tablero tablero, Destino destino, OperadorMision asistente, NavePropia nave) {
        this.tablero = tablero;
        this.destino = destino;
        this.asistente = asistente;
        this.nave = nave;
    }

    /**
     * Recorre el tablero turno a turno hacia el objetivo.
     * @return true si la nave llegó al objetivo, false si fue destruida antes
     */
    public boolean recorrerHasta(Posicion objetivo) {
        while (!asistente.estaDestruida() && !tablero.getPosicionNave().equals(objetivo)) {
            turnoDeLaNave(objetivo);
            destino.turnoDestino();
            pausar();
        }
        return !asistente.estaDestruida();
    }

    private void turnoDeLaNave(Posicion objetivo) {
        Posicion actual = tablero.getPosicionNave();

        // 1. Inspección de la posición actual
        ElementoDelUniverso elemento = tablero.inspeccionarPosicion();
        if (elemento != null) {
            String consecuencia = elemento.interactuar(nave);
            asistente.registrarEvento(TipoEvento.MISION, consecuencia);
            if (asistente.estaDestruida()) {
                return;
            }
        }

        // 2. Escaneo con el radar de corto alcance
        Posicion siguiente = Ruta.pasoHacia(actual, objetivo);
        ElementoDelUniverso[][] alrededores = tablero.observarAlrededores();
        int fila = 1 + (siguiente.getX() - actual.getX());
        int columna = 1 + (siguiente.getY() - actual.getY());
        boolean hayObstaculo = alrededores[fila][columna] != null;

        // 3. Planificación y movimiento
        int avanzados;
        try {
            if (hayObstaculo && Ruta.distanciaA(actual, objetivo) > 1) {
                avanzados = asistente.saltarWarp();
            } else {
                avanzados = asistente.saltar();
            }
        } catch (OperacionInvalidaException e) {
            nave.destruir("sin combustible para seguir saltando");
            return;
        }

        Posicion nueva = actual;
        for (int i = 0; i < avanzados; i++) {
            nueva = Ruta.pasoHacia(nueva, objetivo);
        }
        tablero.moverNave(nueva);
    }

    private void pausar() {
        try {
            Thread.sleep(PAUSA_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}