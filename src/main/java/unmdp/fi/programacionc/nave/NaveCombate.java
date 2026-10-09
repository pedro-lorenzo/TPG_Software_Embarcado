package unmdp.fi.programacionc.nave;

import unmdp.fi.programacionc.comando.Asistente;
import unmdp.fi.programacionc.motor.MotorWarp;
import unmdp.fi.programacionc.recursos.Tanque;

/**
 * Nave de combate. En E1 se diferencia por su configuracion inicial (Ficha de Inicio E1).
 */
public class NaveCombate extends Nave {

    // Configuracion inicial: la usa la fabrica para crear el tanque de este tipo
    static final int COMBUSTIBLE_INICIAL = 80;
    static final int ENERGIA_INICIAL = 100;

    // Package-private: solo la fabrica (mismo paquete) puede crear naves
    NaveCombate(int id, Tanque tanque, MotorWarp motor, Asistente asistente) {
        super(id, TipoNave.COMBATE, tanque, motor, asistente);
    }
}