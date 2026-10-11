package unmdp.fi.programacionc;

import java.util.List;

import unmdp.fi.programacionc.bitacora.EventoBitacora;
import unmdp.fi.programacionc.comando.Asistente;
import unmdp.fi.programacionc.excepciones.OperacionInvalidaException;
import unmdp.fi.programacionc.mision.Mision;
import unmdp.fi.programacionc.mision.MisionIntercepcion;
import unmdp.fi.programacionc.mision.MisionRecoleccion;
import unmdp.fi.programacionc.mision.MisionRetornoSeguro;
import unmdp.fi.programacionc.nave.Nave;
import unmdp.fi.programacionc.nave.NaveFactory;
import unmdp.fi.programacionc.nave.TipoNave;
import unmdp.fi.programacionc.recursos.Tanque;
import unmdp.fi.programacionc.tripulacion.Cargo;
import unmdp.fi.programacionc.tripulacion.Origen;
import unmdp.fi.programacionc.tripulacion.Tripulante;
import unmdp.fi.programacionc.universo.Universo;

/**
 * Programa de demostracion: simula al usuario (Aclaracion R6). Es la unica clase que imprime
 * por consola; el modelo no depende de ella y se reemplazara por la interfaz grafica en E2.
 *
 * Main crea las naves con la fabrica, las da de alta en el Universo, crea las misiones,
 * se las encomienda al asistente de la nave elegida y las ejecuta.
 * Usa varias naves solo para mostrar cada escenario por separado.
 *
 * Escenarios de la Ficha de Inicio E1: A (ejecucion correcta), B (recursos insuficientes
 * y mantenimiento), C (motor warp) y D (contrato invalido).
 * Ejecutar con -ea para que tambien se verifiquen los contratos (assert).
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

        titulo("Universo: " + universo.cantidadNaves() + " naves registradas");
        for (Nave nave : new Nave[] { exploradora, carguero, combate }) {
            mostrarRecursos("Nave " + nave.getId() + " " + nave.getTipo(), nave.getAsistente());
        }

        escenarioA(universo, exploradora.getId());
        escenarioB(universo, carguero.getId());
        escenarioC(universo, combate.getId());
        escenarioD(universo, exploradora.getId());
    }

    // ===================== ESCENARIO A: ejecucion correcta =====================
    private static void escenarioA(Universo universo, int idNave) {
        titulo("ESCENARIO A - Ejecucion correcta");
        Nave nave = universo.obtener(idNave);
        asignarTripulacion(nave);
        Asistente asistente = nave.getAsistente();
        System.out.println("Nave " + nave.getId() + " (" + nave.getTipo() + ") con "
                + nave.getTripulantes().size() + " tripulantes");
        mostrarRecursos("Recursos iniciales", asistente);

        ejecutar(new MisionIntercepcion(asistente), asistente);
        ejecutar(new MisionRecoleccion(asistente), asistente);
        ejecutar(new MisionRetornoSeguro(asistente), asistente);

        mostrarRecursos("Recursos finales", asistente);
        mostrarBitacora(asistente);
    }

    // ============ ESCENARIO B: recursos insuficientes y mantenimiento ============
    private static void escenarioB(Universo universo, int idNave) {
        titulo("ESCENARIO B - Recursos insuficientes y mantenimiento");
        Asistente asistente = universo.obtener(idNave).getAsistente();

        // B.1: el combustible no alcanza -> la mision se rechaza y la nave no cambia
        asistente.consumir(98, 0, 0);
        mostrarRecursos("Antes", asistente);
        ejecutar(new MisionIntercepcion(asistente), asistente);
        mostrarRecursos("Despues (sin cambios)", asistente);

        // B.2: mantenimiento pendiente -> la mision se rechaza hasta realizarlo
        asistente.cargarCombustible(50);
        asistente.consumir(0, 0, Tanque.UMBRAL_MANTENIMIENTO);
        mostrarRecursos("Con mantenimiento pendiente", asistente);
        ejecutar(new MisionIntercepcion(asistente), asistente);
        asistente.realizarMantenimiento();
        mostrarRecursos("Despues del mantenimiento", asistente);
        ejecutar(new MisionIntercepcion(asistente), asistente);

        mostrarBitacora(asistente);
    }

    // ===================== ESCENARIO C: motor warp =====================
    private static void escenarioC(Universo universo, int idNave) {
        titulo("ESCENARIO C - Motor Warp");
        Asistente asistente = universo.obtener(idNave).getAsistente();
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
        Asistente asistente = universo.obtener(idNave).getAsistente();
        mostrarRecursos("Antes", asistente);

        try {
            asistente.cargarCombustible(100);     // excede la capacidad maxima de 100
            System.out.println("ERROR: la carga invalida no fue rechazada");
        } catch (OperacionInvalidaException e) {
            System.out.println("Rechazada: " + e.getMessage());
        }

        mostrarRecursos("Despues (sin cambios)", asistente);
    }

    // ===================== AUXILIARES DE LA DEMOSTRACION =====================

    // La mision ya esta encomendada al asistente que recibio por constructor; Main solo la ejecuta
    private static void ejecutar(Mision mision, Asistente asistente) {
        System.out.println(mision.getCodigo() + " " + mision.getNombre() + " -> " + mision.ejecutarCiclo());
        List<EventoBitacora> eventos = asistente.getEventos();
        System.out.println("   " + eventos.get(eventos.size() - 1));    // informe de la mision
    }

    // Tripulacion minima de la Ficha de Inicio: un capitan y cuatro tripulantes mas
    private static void asignarTripulacion(Nave nave) {
        nave.agregarTripulante(new Tripulante(1, "Capitan Ruiz", Cargo.CAPITAN, Origen.TERRICOLA, 10));
        nave.agregarTripulante(new Tripulante(2, "Consejera Sol", Cargo.CONSEJERO, Origen.VULCANO, 6));
        nave.agregarTripulante(new Tripulante(3, "Teniente Gomez", Cargo.TENIENTE, Origen.MARCIANO, 4));
        nave.agregarTripulante(new Tripulante(4, "Alferez Paz", Cargo.ALFEREZ, Origen.TERRICOLA, 1));
        nave.agregarTripulante(new Tripulante(5, "Teniente Luna", Cargo.TENIENTE, Origen.VULCANO, 3));
    }

    private static void mostrarRecursos(String etiqueta, Asistente a) {
        System.out.println(etiqueta + ": combustible " + a.getCombustible() + ", energia " + a.getEnergia()
                + ", desgaste " + a.getDesgaste() + ", motor " + a.getEstadoMotor());
    }

    private static void mostrarBitacora(Asistente a) {
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
