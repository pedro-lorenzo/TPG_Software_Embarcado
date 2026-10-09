package unmdp.fi.programacionc;
import java.util.List;
import unmdp.fi.programacionc.bitacora.EventoBitacora;
import unmdp.fi.programacionc.comando.AsistenteComando;
import unmdp.fi.programacionc.excepciones.OperacionInvalidaException;
import unmdp.fi.programacionc.mision.Mision;
import unmdp.fi.programacionc.mision.MisionIntercepcion;
import unmdp.fi.programacionc.mision.MisionRecoleccion;
import unmdp.fi.programacionc.mision.MisionRetornoSeguro;
import unmdp.fi.programacionc.nave.Nave;
import unmdp.fi.programacionc.nave.NaveFactory;
import unmdp.fi.programacionc.nave.TipoNave;
import unmdp.fi.programacionc.tripulacion.Cargo;
import unmdp.fi.programacionc.tripulacion.Origen;
import unmdp.fi.programacionc.tripulacion.Tripulante;
import unmdp.fi.programacionc.universo.Universo;

/**
 * Programa de demostracion: simula al usuario (R6). Es el unico lugar que imprime por consola.
 *
 * Reparto de roles:
 *  - Main:     crea las naves (con la fabrica), las da de alta en el Universo, crea las misiones
 *              y se las encomienda al asistente de la nave elegida.
 *  - Universo: sabe que naves existen y las devuelve por id.
 *  - Para operar una nave puntual, Main se la pide al Universo y le pide su asistente.
 *
 * Los cuatro escenarios de la Ficha de Inicio E1 (A, B, C y D).
 * Ejecutar con -ea para que tambien se verifiquen las precondiciones (assert).
 */
public class Main {

    public static void main(String[] args) {
        Universo universo = new Universo();

        // Alta de naves: la fabrica las crea, el universo las registra
        Nave exploradora = NaveFactory.crear(TipoNave.EXPLORADORA);
        Nave carguero = NaveFactory.crear(TipoNave.CARGUERO);
        Nave combate = NaveFactory.crear(TipoNave.COMBATE);
        universo.registrar(exploradora);
        universo.registrar(carguero);
        universo.registrar(combate);
        int idExploradora = exploradora.getId();
        int idCarguero = carguero.getId();
        int idCombate = combate.getId();

        titulo("Universo: " + universo.cantidadNaves() + " naves registradas");

        escenarioA(universo, idExploradora);
        escenarioB(universo, idCarguero);
        escenarioC(universo, idCombate);
        escenarioD(universo, idExploradora);
    }

    // ===================== ESCENARIO A: ejecucion correcta =====================
    private static void escenarioA(Universo universo, int idNave) {
        titulo("ESCENARIO A - Ejecucion correcta");
        Nave nave = universo.obtener(idNave);
        asignarTripulacion(nave);
        AsistenteComando asistente = nave.getAsistente();
        System.out.println("Nave " + nave.getId() + " (" + nave.getTipo() + ") con "
                + nave.getTripulantes().size() + " tripulantes");
        mostrarRecursos("Recursos iniciales", asistente);

        ejecutar(new MisionIntercepcion(asistente), asistente);
        ejecutar(new MisionRecoleccion(asistente), asistente);
        ejecutar(new MisionRetornoSeguro(asistente), asistente);

        mostrarRecursos("Recursos finales", asistente);
        mostrarBitacora(asistente);
        // El informe de cada mision (E1-10) es el evento "informe" de la Bitacora (Aclaracion R5)
    }

    // ===================== ESCENARIO B: recursos insuficientes =====================
    private static void escenarioB(Universo universo, int idNave) {
        titulo("ESCENARIO B - Recursos insuficientes");
        AsistenteComando asistente = universo.obtener(idNave).getAsistente();
        asistente.consumir(98, 0, 0);     // deja el combustible en 2: no alcanza para los 4 que pide la mision
        mostrarRecursos("Antes", asistente);

        ejecutar(new MisionIntercepcion(asistente), asistente);

        mostrarRecursos("Despues (deben ser iguales)", asistente);
        mostrarBitacora(asistente);
    }

    // ===================== ESCENARIO C: motor warp =====================
    private static void escenarioC(Universo universo, int idNave) {
        titulo("ESCENARIO C - Motor Warp");
        AsistenteComando asistente = universo.obtener(idNave).getAsistente();
        System.out.println("Estado inicial: " + asistente.getEstadoMotor());

        asistente.prepararSalto();
        System.out.println("-> " + asistente.getEstadoMotor());
        asistente.saltar();
        System.out.println("-> " + asistente.getEstadoMotor());
        asistente.enfriar();
        System.out.println("-> " + asistente.getEstadoMotor());
        asistente.completarEnfriamiento();
        System.out.println("-> " + asistente.getEstadoMotor());

        // Transicion invalida: no hay "saltar" desde Disponible
        try {
            asistente.saltar();
            System.out.println("ERROR: la transicion invalida no fue rechazada");
        } catch (OperacionInvalidaException e) {
            System.out.println("Rechazada: " + e.getMessage());
        }
        System.out.println("Estado final: " + asistente.getEstadoMotor());
        mostrarBitacora(asistente);
    }

    // ===================== ESCENARIO D: contrato invalido =====================
    private static void escenarioD(Universo universo, int idNave) {
        titulo("ESCENARIO D - Contrato invalido");
        AsistenteComando asistente = universo.obtener(idNave).getAsistente();
        mostrarRecursos("Antes", asistente);

        try {
            asistente.cargarCombustible(100);     // excede la capacidad maxima de 100
            System.out.println("ERROR: la carga invalida no fue rechazada");
        } catch (OperacionInvalidaException e) {
            System.out.println("Rechazada: " + e.getMessage());
        }

        mostrarRecursos("Despues (deben ser iguales)", asistente);
    }

    // ===================== AUXILIARES DE LA DEMOSTRACION =====================

    // La mision se encomienda al asistente que recibio por constructor; Main solo la ejecuta
    private static void ejecutar(Mision mision, AsistenteComando asistente) {
        System.out.println(mision.getCodigo() + " " + mision.getNombre() + " -> " + mision.ejecutarCiclo());
        List<EventoBitacora> eventos = asistente.getEventos();
        System.out.println("   " + eventos.get(eventos.size() - 1));    // el informe de cierre
    }

    // Tripulacion minima de la Ficha: un capitan y cuatro tripulantes mas
    private static void asignarTripulacion(Nave nave) {
        nave.agregarTripulante(new Tripulante(1, "Capitan Ruiz", Cargo.CAPITAN, Origen.TERRICOLA, 10));
        nave.agregarTripulante(new Tripulante(2, "Consejera Sol", Cargo.CONSEJERO, Origen.VULCANO, 6));
        nave.agregarTripulante(new Tripulante(3, "Teniente Gomez", Cargo.TENIENTE, Origen.MARCIANO, 4));
        nave.agregarTripulante(new Tripulante(4, "Alferez Paz", Cargo.ALFEREZ, Origen.TERRICOLA, 1));
        nave.agregarTripulante(new Tripulante(5, "Teniente Luna", Cargo.TENIENTE, Origen.VULCANO, 3));
    }

    private static void mostrarRecursos(String etiqueta, AsistenteComando a) {
        System.out.println(etiqueta + ": combustible " + a.getCombustible() + ", energia " + a.getEnergia()
                + ", desgaste " + a.getDesgaste() + ", motor " + a.getEstadoMotor());
    }

    private static void mostrarBitacora(AsistenteComando a) {
        System.out.println("--- Bitacora ---");
        for (EventoBitacora evento : a.getEventos()) {
            System.out.println(evento);
        }
    }

    private static void titulo(String texto) {
        System.out.println();
        System.out.println("=== " + texto + " ===");
    }
}