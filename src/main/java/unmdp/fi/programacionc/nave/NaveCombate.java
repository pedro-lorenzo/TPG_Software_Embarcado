package unmdp.fi.programacionc.nave;

import unmdp.fi.programacionc.comando.Asistente;
import unmdp.fi.programacionc.motor.MotorWarp;
import unmdp.fi.programacionc.recursos.Tanque;
import unmdp.fi.programacionc.tablero.NaveEnemiga;
import unmdp.fi.programacionc.tablero.ResultadoBatalla;
import unmdp.fi.programacionc.tripulacion.Tripulante;
import java.util.List;
/**
 * Nave de combate. En E1 se diferencia por su configuracion inicial (Ficha de Inicio E1).
 */
public class NaveCombate extends Nave {

    // Configuracion inicial: la usa la fabrica para crear el tanque de este tipo
    static final int COMBUSTIBLE_INICIAL = 80;
    static final int ENERGIA_INICIAL = 100;

    // Package-private: solo la fabrica (mismo paquete) puede crear naves
    NaveCombate(int id, Tanque tanque, MotorWarp motor, Asistente asistente, List<Tripulante> tripulantes) {
        super(id, TipoNave.COMBATE, tanque, motor, asistente, tripulantes);
    }

    @Override
    public ResultadoBatalla enfrentar(NaveEnemiga enemiga) {
        return enemiga.recibirDeCombate();
    }
}