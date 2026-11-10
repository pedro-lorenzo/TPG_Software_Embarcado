# Diagrama de clases — Entregas 1 y 2

Los diagramas están escritos en [Mermaid](https://mermaid.js.org/): GitHub los dibuja directamente al abrir este archivo. Para obtener una imagen, se pueden pegar en <https://mermaid.live> y exportar como PNG o SVG.

Se divide el sistema en cinco diagramas para que cada uno se pueda leer. Muestran las clases y relaciones principales, no todos los métodos. Las responsabilidades de cada clase están en [`../diseno.md`](../diseno.md).

Convenciones: `<|--` herencia, `<|..` implementa una interfaz, `*--` composición (el todo crea y es dueño de la parte), `o--` agregación, `-->` conoce o usa, `..>` depende o crea.

## 1. Núcleo: universo, naves, asistente y motor

```mermaid
classDiagram
    class Universo {
        +registrar(Nave)
        +obtener(int) Nave
    }
    class NaveFactory {
        +crear(TipoNave) Nave$
    }
    class TipoNave {
        <<enumeration>>
        EXPLORADORA
        CARGUERO
        COMBATE
    }
    class Combatiente {
        <<interface>>
        +enfrentar(NaveEnemiga) ResultadoBatalla
    }
    class Nave {
        <<abstract>>
        -int id
        +getAsistente() Asistente
        +agregarTripulante(Tripulante)
        +tieneTripulacionValida() boolean
    }
    class NaveExploradora
    class NaveCarguero
    class NaveCombate
    class Asistente {
        <<interface>>
        +getEventos() List
        +getInformes() List
    }
    class AsistenteComando
    class Tanque {
        -int combustible
        -int energia
        -int desgaste
        +consumir(int, int, int)
        +cargarCombustible(int)
        +cargarEnergia(int)
        +estaAgotado() boolean
    }
    class MotorWarp {
        +saltar() int
        +saltarWarp() int
    }
    class EstadoMotor {
        <<interface>>
        +saltar(MotorWarp) int
        +saltarWarp(MotorWarp) int
    }
    class Disponible
    class PreparandoSalto
    class Warp
    class Enfriamiento
    class Bitacora {
        +registrar(TipoEvento, String)
        +getEventos() List
    }
    class EventoBitacora {
        <<inmutable>>
    }
    class TipoEvento {
        <<enumeration>>
    }
    class Tripulante {
        <<inmutable>>
    }
    class Cargo {
        <<enumeration>>
    }
    class Origen {
        <<enumeration>>
    }

    Universo o-- Nave
    NaveFactory ..> Nave : crea
    NaveFactory ..> TipoNave
    Combatiente <|.. Nave
    Nave <|-- NaveExploradora
    Nave <|-- NaveCarguero
    Nave <|-- NaveCombate
    Nave *-- Tanque
    Nave *-- MotorWarp
    Nave *-- Asistente
    Nave o-- Tripulante
    Asistente <|.. AsistenteComando
    AsistenteComando --> Tanque : opera
    AsistenteComando --> MotorWarp : opera
    AsistenteComando *-- Bitacora
    Bitacora *-- EventoBitacora
    EventoBitacora --> TipoEvento
    MotorWarp --> Tanque
    MotorWarp o-- EstadoMotor : estado actual
    EstadoMotor <|.. Disponible
    EstadoMotor <|.. PreparandoSalto
    EstadoMotor <|.. Warp
    EstadoMotor <|.. Enfriamiento
    Tripulante --> Cargo
    Tripulante --> Origen
```

## 2. Misiones, travesía y tablero

```mermaid
classDiagram
    class Mision {
        <<abstract>>
        +ejecutarCiclo() ResultadoMision$
        -preparar()
        -ejecutar()
        #evaluar() ResultadoMision
        #cerrar(ResultadoMision, String)
        #calcularObjetivo(Tablero) Posicion
        #verificarEnDestino() boolean
    }
    class MisionIntercepcion
    class MisionRecoleccion
    class MisionRetornoSeguro
    class ResultadoMision {
        <<enumeration>>
        EXITOSA
        FALLIDA
        RECHAZADA
    }
    class InformeMision {
        <<inmutable>>
    }
    class Travesia {
        +recorrer(List) boolean
    }
    class Ruta {
        +calcular(Posicion, Posicion) List$
    }
    class OperadorMision {
        <<interface>>
    }
    class NavePropia {
        <<interface>>
        +enfrentar(NaveEnemiga) ResultadoBatalla
        +recibirImpactoDeEnergia(int)
        +recargar(int, int)
        +destruir(String)
    }
    class Asistente {
        <<interface>>
    }
    class Tablero {
        +moverNave(Posicion)
        +inspeccionarPosicion() ElementoDelUniverso
        +observarAlrededores() ElementoDelUniverso[][]
        +obtenerObjetivo() Posicion
    }
    class Destino {
        +turnoDestino()
    }
    class Observable
    class Observador {
        <<interface>>
    }
    class Posicion
    class ElementoDelUniverso {
        <<interface>>
        +interactuar(NavePropia) String
    }
    class Asteroide
    class EstacionDeCarga
    class NaveEnemiga {
        <<abstract>>
        +recibirDeExplorador() ResultadoBatalla
        +recibirDeCarguero() ResultadoBatalla
        +recibirDeCombate() ResultadoBatalla
    }
    class NaveEnemigaExploradora
    class NaveEnemigaCarguero
    class NaveEnemigaCombate
    class ResultadoBatalla {
        <<enumeration>>
        PAZ
        GANA
        PIERDE
    }

    Mision <|-- MisionIntercepcion
    Mision <|-- MisionRecoleccion
    Mision <|-- MisionRetornoSeguro
    Mision --> OperadorMision : consulta y ordena
    Mision --> Tablero
    Mision --> Destino
    Mision ..> Travesia : usa
    Mision ..> Ruta : usa
    Mision ..> InformeMision : crea
    Mision ..> ResultadoMision
    NavePropia <|-- OperadorMision
    OperadorMision <|-- Asistente
    Travesia --> Tablero
    Travesia --> Destino
    Travesia --> OperadorMision
    Destino --> Tablero
    Tablero --|> Observable
    Observable o-- Observador
    Tablero o-- ElementoDelUniverso
    Tablero ..> Posicion
    ElementoDelUniverso <|.. Asteroide
    ElementoDelUniverso <|.. EstacionDeCarga
    ElementoDelUniverso <|.. NaveEnemiga
    NaveEnemiga <|-- NaveEnemigaExploradora
    NaveEnemiga <|-- NaveEnemigaCarguero
    NaveEnemiga <|-- NaveEnemigaCombate
    ElementoDelUniverso ..> NavePropia : interactuar
    NavePropia ..> NaveEnemiga : Double Dispatch
    NaveEnemiga ..> ResultadoBatalla
```

`Tablero`, `Destino`, `Observable`, `Observador` y `Posicion` son provistos por la cátedra.

## 3. Liquidación de haberes (Decorator)

```mermaid
classDiagram
    class ComponenteHaber {
        <<interface>>
        +getImporte() double
        +getAporte() double
        +getConcepto() String
        +getInterno() ComponenteHaber
    }
    class HaberBase
    class DecoratorHaber {
        <<abstract>>
    }
    class AdicionalAntiguedad
    class AdicionalConsejos
    class SubsidioOrigen
    class LiquidadorHaberes {
        +liquidar(Tripulante, int) ComponenteHaber$
    }
    class Tripulante

    ComponenteHaber <|.. HaberBase
    ComponenteHaber <|.. DecoratorHaber
    DecoratorHaber o-- ComponenteHaber : envuelve
    DecoratorHaber <|-- AdicionalAntiguedad
    DecoratorHaber <|-- AdicionalConsejos
    DecoratorHaber <|-- SubsidioOrigen
    LiquidadorHaberes ..> ComponenteHaber : arma la cadena
    LiquidadorHaberes ..> Tripulante
    HaberBase ..> Tripulante
```

## 4. Interfaz (MVC), sesión y persistencia

```mermaid
classDiagram
    class IVistaLogin { <<interface>> }
    class IVistaRegistro { <<interface>> }
    class IVistaCreacion { <<interface>> }
    class IVistaTripulantes { <<interface>> }
    class IVistaControl { <<interface>> }
    class IVistaMision { <<interface>> }
    class IVistaVisor { <<interface>> }

    class VentanaLogin
    class VentanaRegistro
    class PanelCreacion
    class PanelTripulantes
    class PanelControl
    class PanelMision
    class PanelVisor
    class VentanaPrincipal

    class ControladorLogin
    class ControladorRegistro
    class ControladorPrincipal
    class ControladorCreacion
    class ControladorTripulantes
    class ControladorControl
    class ControladorMision
    class ControladorVisor

    class ObservadorSesion { <<interface>> }
    class Sesion {
        +iniciarMision(String)
        +agregarObservador(ObservadorSesion)
        +notificar()
    }
    class ControlAcceso {
        +registrar(String, String)
        +iniciarSesion(String, String) boolean
    }
    class UsuarioDAO { <<interface>> }
    class UsuarioDAOJdbc
    class UsuarioDTO
    class Universo
    class Nave
    class Tablero
    class UniversoParalelo

    IVistaLogin <|.. VentanaLogin
    IVistaRegistro <|.. VentanaRegistro
    IVistaCreacion <|.. PanelCreacion
    IVistaTripulantes <|.. PanelTripulantes
    IVistaControl <|.. PanelControl
    IVistaMision <|.. PanelMision
    IVistaVisor <|.. PanelVisor

    ControladorLogin --> IVistaLogin
    ControladorLogin --> ControlAcceso
    ControladorLogin ..> ControladorPrincipal : abre
    ControladorRegistro --> IVistaRegistro
    ControladorRegistro --> ControlAcceso
    ControladorPrincipal --> VentanaPrincipal
    ControladorPrincipal --> Sesion
    ControladorPrincipal ..> ControladorCreacion : arma
    ControladorPrincipal ..> ControladorTripulantes : arma
    ControladorPrincipal ..> ControladorControl : arma
    ControladorPrincipal ..> ControladorMision : arma
    ControladorPrincipal ..> ControladorVisor : arma
    ControladorCreacion --> IVistaCreacion
    ControladorTripulantes --> IVistaTripulantes
    ControladorControl --> IVistaControl
    ControladorMision --> IVistaMision
    ControladorVisor --> IVistaVisor
    ControladorCreacion --> Sesion
    ControladorTripulantes --> Sesion
    ControladorControl --> Sesion
    ControladorMision --> Sesion
    ControladorVisor --> UniversoParalelo
    ObservadorSesion <|.. ControladorCreacion
    ObservadorSesion <|.. ControladorTripulantes
    ObservadorSesion <|.. ControladorControl
    ObservadorSesion <|.. ControladorMision
    Sesion o-- ObservadorSesion
    Sesion --> Universo
    Sesion --> Nave : nave actual
    Sesion --> Tablero : uno por nave
    ControlAcceso --> UsuarioDAO
    UsuarioDAO <|.. UsuarioDAOJdbc
    UsuarioDAO ..> UsuarioDTO
```

## 5. Universo paralelo (concurrencia)

```mermaid
classDiagram
    class EmisorEventos {
        <<interface>>
        +emitir(String)
    }
    class ObservadorVisor {
        <<interface>>
        +actualizar(String)
    }
    class UniversoParalelo {
        +agregarGeneradora()
        +agregarNave()
        +agregarObservador(ObservadorVisor)
        +detener()
    }
    class PlataformaDeCarga {
        <<monitor>>
        +depositar(String) synchronized
        +retirar(String) synchronized
    }
    class EntidadParalela {
        <<abstract>>
        +run()
        #ciclo()
        +detener()
    }
    class GeneradoraDeEnergia
    class NaveConsumidora
    class ControladorVisor

    EmisorEventos <|.. UniversoParalelo
    UniversoParalelo o-- ObservadorVisor
    UniversoParalelo *-- PlataformaDeCarga
    UniversoParalelo o-- EntidadParalela
    EntidadParalela <|-- GeneradoraDeEnergia
    EntidadParalela <|-- NaveConsumidora
    EntidadParalela --> EmisorEventos
    PlataformaDeCarga --> EmisorEventos
    GeneradoraDeEnergia --> PlataformaDeCarga : deposita
    NaveConsumidora --> PlataformaDeCarga : retira
    ObservadorVisor <|.. ControladorVisor
```