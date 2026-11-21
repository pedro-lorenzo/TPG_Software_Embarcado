package unmdp.fi.programacionc.mision;

import java.util.List;

import unmdp.fi.programacionc.bitacora.TipoEvento;
import unmdp.fi.programacionc.destinoytablero.Destino;
import unmdp.fi.programacionc.destinoytablero.Posicion;
import unmdp.fi.programacionc.destinoytablero.Tablero;
import unmdp.fi.programacionc.excepciones.OperacionInvalidaException;
import unmdp.fi.programacionc.tablero.ElementoDelUniverso;
import unmdp.fi.programacionc.tablero.NavePropia;

public class Travesia {

    private static final int PAUSA_MS = 1000;

    private final Tablero tablero;
    private final Destino destino;
    private final OperadorMision asistente;
    private final NavePropia nave;
    private final BalanceRecursos balance;

    private int paso;                    // cantidad de casillas de la ruta ya recorridas
    private boolean sinPoderAvanzar;

    /**
     * pre -> balance != null (la mision lo usa despues para el informe)
     */
    public Travesia(Tablero tablero, Destino destino, OperadorMision asistente, NavePropia nave,
                    BalanceRecursos balance) {
        assert balance != null : "La travesia necesita donde anotar el balance";
        this.tablero = tablero;
        this.destino = destino;
        this.asistente = asistente;
        this.nave = nave;
        this.balance = balance;
    }

    /**
     * Recorre la ruta turno a turno.
     * pre -> ruta != null (puede estar vacia)
     * @return true si la nave completo la ruta; false si fue destruida o quedo sin poder avanzar
     */
    public boolean recorrer(List<Posicion> ruta) {
        paso = 0;
        sinPoderAvanzar = false;
        while (!asistente.estaDestruida() && !sinPoderAvanzar && paso < ruta.size()) {
            synchronized (tablero) {
                turnoDeLaNave(ruta);
                destino.turnoDestino();
            }
            pausar();
        }
        return !asistente.estaDestruida() && paso >= ruta.size();
    }

    private void turnoDeLaNave(List<Posicion> ruta) {
        Posicion actual = tablero.getPosicionNave();

        // 1. Inspección de la posición actual
        ElementoDelUniverso elemento = tablero.inspeccionarPosicion();
        if (elemento != null) {
            balance.anotarAntes(asistente);
            String consecuencia = elemento.interactuar(nave);
            balance.registrarEncuentro(asistente);
            asistente.registrarEvento(TipoEvento.MISION, consecuencia);
            if (asistente.estaDestruida()) {
                return;
            }
        }

        // 2. Escaneo con el radar de corto alcance
        Posicion siguiente = ruta.get(paso);
        ElementoDelUniverso[][] alrededores = tablero.observarAlrededores();
        int fila = 1 + (siguiente.getX() - actual.getX());
        int columna = 1 + (siguiente.getY() - actual.getY());
        ElementoDelUniverso adelante = alrededores[fila][columna];
        boolean hayObstaculo = adelante != null && adelante.esObstaculo();
        boolean quedanDosCasillas = ruta.size() - paso > 1;

        // 3. Planificación y movimiento
        int avanzados;
        balance.anotarAntes(asistente);
        try {
            if (hayObstaculo && quedanDosCasillas) {
                avanzados = asistente.saltarWarp();
            } else {
                avanzados = asistente.saltar();
            }
        } catch (OperacionInvalidaException e) {
            sinPoderAvanzar = true;
            return;
        }
        balance.registrarConsumo(asistente);
        paso += avanzados;
        tablero.moverNave(ruta.get(paso - 1));
    }

    private void pausar() {
        try {
            Thread.sleep(PAUSA_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}