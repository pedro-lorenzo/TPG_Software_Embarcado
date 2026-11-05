package unmdp.fi.programacionc.mision;

import unmdp.fi.programacionc.destinoytablero.Posicion;

/**
 * Ruta en L sobre el tablero de la catedra: primero se avanza en X hasta igualar la del
 * destino y despues en Y. Siempre una sola casilla por paso.
 */
public final class Ruta {

    private Ruta() { }

    /**
     * pre -> origen y destino != null
     * pre -> origen distinto de destino
     * post -> devuelve la casilla vecina en direccion al destino
     */
    public static Posicion pasoHacia(Posicion origen, Posicion destino) {
        assert origen != null && destino != null : "Origen y destino no pueden ser nulos";
        assert !origen.equals(destino) : "Ya se esta en el destino";
        if (origen.getX() != destino.getX()) {
            return new Posicion(origen.getX() + Integer.signum(destino.getX() - origen.getX()), origen.getY());
        }
        return new Posicion(origen.getX(), origen.getY() + Integer.signum(destino.getY() - origen.getY()));
    }

    /**
     * Cantidad de pasos de la ruta en L (distancia Manhattan).
     * pre -> origen y destino != null
     * post -> resultado >= 0; es 0 solo si son la misma casilla
     */
    public static int distanciaA(Posicion origen, Posicion destino) {
        assert origen != null && destino != null : "Origen y destino no pueden ser nulos";
        return Math.abs(destino.getX() - origen.getX()) + Math.abs(destino.getY() - origen.getY());
    }
}