package unmdp.fi.programacionc.tablero;

import java.util.Objects;

/**
 * Posicion en el tablero. Inmutable: "moverse" es reemplazar la coordenada por otra nueva
 * (con desplazar), asi quien se quedo con una coordenada anterior (por ejemplo el origen
 * de una mision) no la ve cambiar.
 *
 * inv -> x >= 0 y y >= 0
 */
public final class Coordenada {
    private final int x;
    private final int y;

    /**
     * pre -> x >= 0 y y >= 0
     */
    public Coordenada(int x, int y) {
        assert x >= 0 && y >= 0 : "Las coordenadas no pueden ser negativas";
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    /**
     * pre -> el resultado no queda con coordenadas negativas
     * post -> devuelve una coordenada nueva; esta no cambia
     */


    public Coordenada desplazar(int dx, int dy) {
        return new Coordenada(x + dx, y + dy);
    }

    /**
     * Un paso de la ruta en L: primero se avanza en X hasta igualar la del destino,
     * despues en Y. Siempre una sola casilla.
     *
     * pre -> destino != null
     * pre -> destino distinto de esta coordenada (si ya se llego, no hay paso que dar)
     * post -> devuelve la coordenada vecina en direccion al destino; esta no cambia
     */
    public Coordenada pasoHacia(Coordenada destino) {
        assert destino != null : "El destino no puede ser nulo";
        assert !this.equals(destino) : "Ya se esta en el destino " + destino;
        if (this.x != destino.x) {
            return desplazar(Integer.signum(destino.x - this.x), 0);
        }
        return desplazar(0, Integer.signum(destino.y - this.y));
    }

    /**
     * Cantidad de pasos de la ruta en L hasta el destino (distancia Manhattan).
     * pre -> destino != null
     * post -> resultado >= 0; es 0 solo si destino es esta misma coordenada
     */
    public int distanciaA(Coordenada destino) {
        assert destino != null : "El destino no puede ser nulo";
        return Math.abs(destino.x - this.x) + Math.abs(destino.y - this.y);
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordenada)) return false;
        Coordenada c = (Coordenada) o;
        return x == c.x && y == c.y;
    }

    @Override public int hashCode() { return Objects.hash(x, y); }

    @Override public String toString() { return "(" + x + "," + y + ")"; }
}