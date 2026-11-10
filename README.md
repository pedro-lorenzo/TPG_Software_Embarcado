# Software embarcado de una nave interestelar

Trabajo Práctico Grupal 2026 — Programación C — FI-UNMdP.

- **Entrega 1:** núcleo orientado a objetos (universo, naves, asistente de comando, motor warp, bitácora, misiones y liquidación de haberes).
- **Entrega 2:** la nave se mueve por un tablero, con interfaz gráfica (MVC), acceso con usuario y contraseña (DAO/DTO sobre H2) y un universo paralelo concurrente (productor-consumidor con monitores).

## Requisitos

- JDK 17 o superior.
- Maven 3.6 o superior (sirve el que trae IntelliJ IDEA).
- Conexión a Internet la primera vez, para que Maven descargue las dependencias (H2).

## Cómo ejecutar

**Todos los comandos se ejecutan desde la carpeta raíz del proyecto** (la que contiene `pom.xml`). La base de datos y el esquema se buscan con rutas relativas a esa carpeta (`data/` y `database/schema.sql`); si se ejecuta desde otro lugar, el acceso al sistema falla.

Maven arranca el programa con las aserciones habilitadas (`-ea`). Los contratos (precondiciones, postcondiciones e invariantes) están escritos con `assert`, así que solo se verifican con `-ea`.

| Qué se ejecuta | Comando |
|---|---|
| Interfaz gráfica (`AppGrafica`) | `mvn compile exec:exec -Dmain.class=unmdp.fi.programacionc.AppGrafica` |
| Demostración por consola (`Main`) | `mvn compile exec:exec` |
| Universo paralelo por consola (`concurrencia.modelo.Main`) | `mvn compile exec:exec -Dmain.class=unmdp.fi.programacionc.concurrencia.modelo.Main` |

Desde IntelliJ: ejecutar la clase deseada con `-ea` en *VM options* y con la carpeta raíz del proyecto como *Working directory*.

## Base de datos (H2) y carpeta `data/`

El único dato que se persiste son las **credenciales de los usuarios** (nombre de usuario y contraseña). Naves, tripulantes y misiones no se guardan.

- La base es **H2 embebida**: no hay que instalar ni configurar nada. Se declara como dependencia en el `pom.xml`.
- Al iniciar la interfaz se crea la carpeta `data/` en la raíz del proyecto, con el archivo `data/tpg.mv.db`. Allí quedan los usuarios entre ejecuciones.
- La tabla se crea ejecutando `database/schema.sql` (usa `CREATE TABLE IF NOT EXISTS`, así que puede correrse todas las veces).
- `data/` y los archivos `*.mv.db` son locales: no se versionan (están en `.gitignore`).
- Para empezar de cero, cerrar la aplicación y borrar la carpeta `data/`.

## Uso de la interfaz

1. **Registro e inicio de sesión:** crear un usuario y entrar. El registro rechaza datos vacíos, de más de 50 caracteres o un usuario ya existente; el inicio de sesión rechaza credenciales incorrectas.
2. **Creación:** elegir el tipo de nave (exploradora, carguero o combate), crearla y agregarle tripulantes. Hace falta **un capitán y al menos cuatro tripulantes más** para poder operar.
3. **Tripulantes:** listado con el detalle del salario de cada uno (cadena de decoradores) y botón para registrar consejos del período.
4. **Control de la nave:** recursos (combustible, energía, desgaste), cargas, mantenimiento, bitácora de eventos e historial de informes de misión.
5. **Misión:** iniciar M-01, M-02 o M-03 y ver en vivo el tablero turno a turno.
6. **Visor:** botones para agregar naves consumidoras y generadoras de energía al universo paralelo, y panel con el flujo de eventos de la plataforma de carga.

## Verificación

### Demostración por consola (`Main`)

`Main` simula al usuario y reproduce los escenarios de la Ficha de Inicio de la Entrega 1 (A a D) y la liquidación de haberes (E). Comando:

```bash
mvn compile exec:exec
```

Los resultados esperados no dependen del azar. El tablero de la cátedra sortea el objetivo de cada misión, así que **la cantidad de pasos de la ruta y el consumo exacto de combustible varían entre ejecuciones**; por eso los escenarios A y B arrancan con el tanque lleno y la tabla indica los resultados que se mantienen siempre.

| Escenario | Qué muestra | Resultado esperado |
|---|---|---|
| Alta de naves | La fábrica crea una nave de cada tipo y el universo las registra | Exploradora 60/80, Carguero 100/60, Combate 80/100 (combustible/energía), desgaste 0, motor Disponible |
| A — Ejecución correcta | Tanque lleno; M-01, M-02 y M-03 completas sobre la misma nave con su tripulación (un capitán y cuatro tripulantes) | Las tres **EXITOSA**. Un informe por misión en la bitácora (resultado, ruta, consumos y balance del cierre). M-01 transfiere el auxilio en el cierre; M-03 termina con **combustible 100, energía 100 y desgaste 0** |
| B — Recursos insuficientes y mantenimiento | Misión con combustible 2; misión con desgaste 80; mantenimiento; misión con el tanque lleno | **RECHAZADA** con recursos sin cambios (2/60/0); **RECHAZADA** por mantenimiento pendiente, sin cambios; mantenimiento lleva el desgaste de 80 a 0; la misión siguiente **EXITOSA** |
| C — Motor warp | Secuencia de saltos y un salto sin combustible | Ver la secuencia a continuación |
| D — Contrato inválido | Carga de combustible que excede la capacidad | **Rechazada**; los recursos siguen en 100/100/0 |
| E — Haberes | Liquidación de los cinco tripulantes (los 4 cargos y los 3 orígenes), un concepto por línea y el total | Capitán Ruiz 3020 PG; Consejera Sol 816 PG (incluye 6 PG por 3 consejos); Teniente Gomez 466 PG; Alférez Paz 221 PG; Teniente Luna 466 PG |

**Escenario C, secuencia esperada** (nave de combate, combustible 80 y energía 100):

| Orden | Avanza | Motor después | Recursos después |
|---|---|---|---|
| `saltar` | 1 | Preparando salto | combustible 78 |
| `saltar` | 1 | En warp | combustible 76 |
| `saltarWarp` | 2 | Enfriamiento | energía 95 |
| `saltar` (enfriamiento) | 1 | Disponible | combustible 74, desgaste 3 |
| `saltarWarp` fuera de Warp | 1 (salto simple) | Preparando salto | combustible 72 |
| `saltar` sin combustible | — | **Rechazada**: el estado sigue en Preparando salto | sin cambios |

### Verificación manual de la interfaz

| Caso | Resultado esperado |
|---|---|
| Registrar un usuario que ya existe | Mensaje de error; no se crea otro |
| Registrar con usuario o contraseña vacíos | Mensaje de error del modelo |
| Ingresar con contraseña incorrecta | Mensaje "Usuario o contrasenia incorrectos" |
| Cerrar y volver a abrir, e ingresar con un usuario ya registrado | Entra: el usuario quedó en `data/` |
| Iniciar una misión sin nave o con tripulación incompleta | No se inicia y se explica el motivo |
| Iniciar una misión con la tripulación completa | El tablero avanza turno a turno sin congelar la ventana; al terminar aparece el informe en el historial de la sección Control de la nave |
| Agregar generadoras y naves en el visor | El panel de eventos muestra las esperas cuando la plataforma (capacidad 3) está llena o vacía |

## Estructura

```
src/main/java/unmdp/fi/programacionc/
├── AppGrafica.java      punto de entrada de la interfaz gráfica
├── Main.java            demostración por consola (escenarios A a E)
├── vista/               ventanas y paneles Swing (interfaces IVista*)
├── controlador/         controladores MVC: atienden a la vista y llaman al modelo
├── aplicacion/          Sesion: estado de la sesión del operador, avisa a sus observadores
├── persistencia/        usuarios: ControlAcceso, UsuarioDAO, UsuarioDAOJdbc (H2) y UsuarioDTO
├── universo/            centro de control: registra y devuelve naves
├── nave/                Nave, sus tipos y NaveFactory
├── comando/             Asistente y AsistenteComando: toda orden a la nave pasa por él
├── recursos/            Tanque: combustible, energía y desgaste
├── motor/               MotorWarp y sus estados (State)
├── mision/              Mision (Template Method), M-01, M-02, M-03, Travesia, Ruta e InformeMision
├── tablero/             elementos del universo: asteroide, estación de carga y naves enemigas (Double Dispatch)
├── destinoytablero/     Tablero, Destino y Observable/Observador (provistos por la cátedra)
├── concurrencia/        universo paralelo: plataforma de carga, generadoras y naves consumidoras
├── bitacora/            Bitacora y eventos
├── tripulacion/         Tripulante, cargos y orígenes
├── haberes/             liquidación de haberes (Decorator)
└── excepciones/
database/schema.sql      tabla de usuarios
data/                    base H2 local (se crea al ejecutar; no se versiona)
```

## Documentación

- [Diseño: responsabilidades, patrones, concurrencia y contratos](docs/diseno.md)
- [Diagrama de clases](docs/uml/diagrama-clases.md)
- [Registro de uso de IA](docs/uso-ia.md)

## Entrega

- La Entrega 1 corresponde a la etiqueta `entrega-e1`.
- La Entrega 2 corresponde a la etiqueta `entrega-e2`.sponde a la etiqueta `entrega-e1`.