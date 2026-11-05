package unmdp.fi.programacionc.mision;

import unmdp.fi.programacionc.destinoytablero.Posicion;

/**
 * Ruta en L entre dos posiciones: primero se avanza en x y despues en y, de a un casillero.
 * Solo calcula; no mueve nada (la posicion de la nave la tiene el Tablero).
 */
public final class Ruta {

    private Ruta() { }

    /**
     * Siguiente casillero de la ruta en L desde origen hacia destino.
     * pre -> origen != null y destino != null
     * post -> si origen equals destino devuelve origen; si no, devuelve una posicion
     *         a distancia 1 de origen (primero se corrige x, despues y)
     */
    public static Posicion pasoHacia(Posicion origen, Posicion destino) {
        assert origen != null && destino != null : "Las posiciones no pueden ser nulas";
        if (origen.getX() != destino.getX()) {
            return new Posicion(origen.getX() + Integer.signum(destino.getX() - origen.getX()), origen.getY());
        }
        if (origen.getY() != destino.getY()) {
            return new Posicion(origen.getX(), origen.getY() + Integer.signum(destino.getY() - origen.getY()));
        }
        return origen;
    }

    /**
     * Cantidad de casilleros de la ruta en L (distancia Manhattan).
     * pre -> origen != null y destino != null
     * post -> resultado >= 0; es 0 solo si las posiciones son iguales
     */
    public static int distanciaA(Posicion origen, Posicion destino) {
        assert origen != null && destino != null : "Las posiciones no pueden ser nulas";
        return Math.abs(destino.getX() - origen.getX()) + Math.abs(destino.getY() - origen.getY());
    }
}
