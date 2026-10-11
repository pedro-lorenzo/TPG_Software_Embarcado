package unmdp.fi.programacionc.nave;

import unmdp.fi.programacionc.comando.Asistente;
import unmdp.fi.programacionc.comando.AsistenteComando;
import unmdp.fi.programacionc.motor.MotorWarp;
import unmdp.fi.programacionc.recursos.Tanque;

/**
 * Patron Factory (E1-07): unico lugar del sistema que conoce las clases concretas de nave
 * y de asistente. El cliente pide un TipoNave y recibe una Nave (tipo abstracto), sin saber
 * que subclase es, y opera a traves de la interfaz Asistente.
 *
 * Para cada nave arma sus componentes en este orden:
 *   tanque (con la configuracion inicial del tipo) -> motor -> asistente que los opera -> nave.
 * Usar otra variante de asistente solo cambia el new de este archivo.
 *
 * Esta en el paquete nave porque los constructores de las naves son package-private:
 * nadie fuera de este paquete puede hacer new NaveCarguero(...).
 *
 * Agregar un tipo nuevo: un valor en TipoNave, la subclase y un case aca.
 * Ninguna otra clase del sistema cambia.
 */
public final class NaveFactory {

    // Contador compartido por todo el programa: cada nave recibe un id distinto
    private static int proximoId = 1;

    private NaveFactory() { }   // solo metodos estaticos, no se instancia

    /**
     * pre -> tipo != null
     * post -> devuelve una nave nueva, con id distinto a las anteriores, motor Disponible,
     *         desgaste 0 y combustible/energia segun su tipo (Ficha de Inicio E1)
     */
    public static Nave crear(TipoNave tipo) {
        assert tipo != null : "El tipo de nave no puede ser nulo";
        int id = proximoId++;
        Nave nave;
        switch (tipo) {
            case EXPLORADORA:
                nave = crearExploradora(id);
                break;
            case CARGUERO:
                nave = crearCarguero(id);
                break;
            case COMBATE:
                nave = crearCombate(id);
                break;
            default:
                throw new AssertionError("Tipo de nave no contemplado: " + tipo);
        }
        assert nave.getAsistente().estaDisponible() : "La nave debe empezar con el motor Disponible";
        assert nave.getAsistente().getDesgaste() == 0 : "La nave debe empezar sin desgaste";
        return nave;
    }

    private static Nave crearExploradora(int id) {
        Tanque tanque = new Tanque(NaveExploradora.COMBUSTIBLE_INICIAL, NaveExploradora.ENERGIA_INICIAL);
        MotorWarp motor = new MotorWarp();
        Asistente asistente = new AsistenteComando(tanque, motor);
        return new NaveExploradora(id, tanque, motor, asistente);
    }

    private static Nave crearCarguero(int id) {
        Tanque tanque = new Tanque(NaveCarguero.COMBUSTIBLE_INICIAL, NaveCarguero.ENERGIA_INICIAL);
        MotorWarp motor = new MotorWarp();
        Asistente asistente = new AsistenteComando(tanque, motor);
        return new NaveCarguero(id, tanque, motor, asistente);
    }

    private static Nave crearCombate(int id) {
        Tanque tanque = new Tanque(NaveCombate.COMBUSTIBLE_INICIAL, NaveCombate.ENERGIA_INICIAL);
        MotorWarp motor = new MotorWarp();
        Asistente asistente = new AsistenteComando(tanque, motor);
        return new NaveCombate(id, tanque, motor, asistente);
    }
}
