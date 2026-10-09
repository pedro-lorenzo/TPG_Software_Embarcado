package unmdp.fi.programacionc.nave;

import unmdp.fi.programacionc.comando.AsistenteComando;
import unmdp.fi.programacionc.motor.MotorWarp;
import unmdp.fi.programacionc.recursos.Tanque;

// Nave carguero. Por ahora se diferencia por su configuracion inicial (Ficha de Inicio E1).
public class NaveCarguero extends Nave {

    // Configuracion inicial: la usa la fabrica para crear el tanque de este tipo
    static final int COMBUSTIBLE_INICIAL = 100;
    static final int ENERGIA_INICIAL = 60;

    // Package-private: solo la fabrica (mismo paquete) puede crear naves
    NaveCarguero(int id, Tanque tanque, MotorWarp motor, AsistenteComando asistente) {
        super(id, TipoNave.CARGUERO, tanque, motor, asistente);
    }
}
