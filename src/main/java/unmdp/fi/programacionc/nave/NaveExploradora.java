package unmdp.fi.programacionc.nave;

import unmdp.fi.programacionc.comando.Asistente;
import unmdp.fi.programacionc.motor.MotorWarp;
import unmdp.fi.programacionc.recursos.Tanque;

/**
 * Nave exploradora. En E1 se diferencia por su configuracion inicial (Ficha de Inicio E1).
 */
public class NaveExploradora extends Nave {

    // Configuracion inicial: la usa la fabrica para crear el tanque de este tipo
    static final int COMBUSTIBLE_INICIAL = 60;
    static final int ENERGIA_INICIAL = 80;

    // Package-private: solo la fabrica (mismo paquete) puede crear naves
    NaveExploradora(int id, Tanque tanque, MotorWarp motor, Asistente asistente) {
        super(id, TipoNave.EXPLORADORA, tanque, motor, asistente);
    }
}
