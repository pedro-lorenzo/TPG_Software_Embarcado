# Software embarcado de una nave interestelar

Trabajo Práctico Grupal 2026 — Programación C — FI-UNMdP.

- **Entrega 1:** núcleo orientado a objetos: universo (centro de control), naves y su fábrica, asistente de comando, motor warp, bitácora, tripulación, misiones y liquidación de haberes.
- **Entrega 2:** la nave recorre un tablero con asteroides, estaciones de carga y naves enemigas; interfaz gráfica Swing (MVC + Observer); acceso con usuario y contraseña (DAO/DTO sobre H2); y un universo paralelo concurrente (productor-consumidor con monitores).

## Requisitos

- JDK 17 o superior (el `pom.xml` compila con `release 17`).
- Maven 3.6.3 o superior (sirve el que trae IntelliJ IDEA).
- Conexión a Internet la primera vez, para que Maven descargue los plugins y la dependencia H2.

No hace falta instalar ninguna base de datos: H2 corre embebida dentro del programa.

## Cómo ejecutar

**Todos los comandos se ejecutan desde la carpeta raíz del proyecto** (la que contiene `pom.xml`). La base de datos y el esquema se buscan con rutas relativas a esa carpeta (`data/` y `database/schema.sql`); si se ejecuta desde otro lugar, el acceso al sistema falla.

Maven arranca el programa con las aserciones habilitadas (`-ea`). Los contratos (precondiciones, postcondiciones e invariantes) están escritos con `assert`, así que solo se verifican con `-ea`.

| Qué se ejecuta | Comando |
|---|---|
| Compilar | `mvn compile` |
| Interfaz gráfica (`AppGrafica`) | `mvn compile exec:exec -Dmain.class=unmdp.fi.programacionc.AppGrafica` |
| Demostración por consola (`Main`) | `mvn compile exec:exec` |
| Universo paralelo por consola (`concurrencia.modelo.Main`) | `mvn compile exec:exec -Dmain.class=unmdp.fi.programacionc.concurrencia.modelo.Main` |

**Desde IntelliJ IDEA:** ejecutar la clase deseada con `-ea` en *VM options* y con la carpeta raíz del proyecto como *Working directory*.

## Configuración local: base de datos (H2) y carpeta `data/`

El único dato que se persiste son las **credenciales de los usuarios** (nombre de usuario y contraseña). Naves, tripulantes y misiones no se guardan, como pide el enunciado.

- La base es **H2 embebida**, declarada como dependencia en el `pom.xml`. No requiere configuración.
- Al iniciar la interfaz se crea la carpeta `data/` en la raíz del proyecto, con el archivo `data/tpg.mv.db`. Ahí quedan los usuarios entre ejecuciones.
- La tabla se crea ejecutando `database/schema.sql`, que usa `CREATE TABLE IF NOT EXISTS`, así que se puede correr todas las veces.
- Los archivos de la base (`*.mv.db` y `*.trace.db`) son locales y están excluidos en `.gitignore`.
- Para empezar de cero: cerrar la aplicación y borrar la carpeta `data/`.

## Uso de la interfaz

Al iniciar se abre la ventana de acceso. Después del ingreso, la ventana principal tiene una pestaña por sección.

1. **Acceso:** *Crear cuenta* abre el registro (usuario, contraseña y repetición de la contraseña). Con la cuenta creada se vuelve al login y se ingresa con *Ingresar*.
2. **Creación:** elegir el tipo de nave (exploradora, carguero o combate) y crearla. Después agregar tripulantes con nombre, cargo, origen y antigüedad. Para operar hace falta **un capitán y al menos cuatro tripulantes más**. Crear otra nave la reemplaza como nave en uso.
3. **Tripulantes:** listado con cargo, origen, antigüedad, estado (activo o fallecido) y salario. Al elegir un tripulante se ve el detalle del salario concepto por concepto (cadena de decoradores). El botón *Registrar consejo* suma un consejo del período (solo para consejeros).
4. **Control de la nave:** estado del motor y de la nave, barras de combustible, energía y desgaste, gemas, cargas de combustible y energía, mantenimiento, bitácora de eventos e historial de informes de misión.
5. **Misión:** elegir M-01, M-02 o M-03 e iniciarla. El tablero de 12 × 12 se actualiza solo en cada turno. Referencias: ◆ nave propia, `A` asteroide, `E` estación de carga, `X` `C` `K` naves enemigas (exploradora, carguera y de combate).
6. **Visor:** botones *Crear generadora de energía* y *Crear nave consumidora* para el universo paralelo, y un panel con el flujo de eventos de la plataforma de carga (capacidad 3) en tiempo real.

## Verificación

No hay pruebas automatizadas. La verificación se hace con dos demostraciones por consola reproducibles y con casos manuales sobre la interfaz.

### Demostración por consola (`Main`)

`Main` simula al usuario y reproduce los escenarios A a D de la Ficha de Inicio de la Entrega 1 y la liquidación de haberes (E). Usa tableros vacíos de 7 × 7, sin elementos del universo. Comando:

```bash
mvn compile exec:exec
```

Los **resultados** de la tabla son siempre los mismos: los pedidos de auxilio y las gemas usan semillas fijas. Lo que puede variar entre ejecuciones es la **cantidad de pasos de cada ruta**, porque el tablero de la cátedra sortea el objetivo de cada misión, y con eso los consumos exactos del recorrido.

| Escenario | Qué muestra | Resultado esperado |
|---|---|---|
| Alta de naves | La fábrica crea una nave de cada tipo y el universo las registra | Exploradora 60/80, Carguero 100/60, Combate 80/100 (combustible/energía), desgaste 0, motor Disponible |
| A — Ejecución correcta | Recarga total antes de cada misión; M-01, M-02 y M-03 sobre la misma nave con su tripulación (un capitán y cuatro tripulantes) | Las tres **EXITOSA**. Cada misión deja su informe en la bitácora: resultado, ruta, balance del recorrido (consumido, ganado y perdido) y balance del cierre. M-01 transfiere 22 de combustible y 12 de energía; M-02 obtiene 98 gemas y las canjea por combustible; M-03 termina con **combustible 100, energía 100 y desgaste 0** |
| B — Recursos insuficientes y mantenimiento | Misión con combustible 2; misión con desgaste 80; mantenimiento; misión con 52 de combustible | **RECHAZADA** con recursos sin cambios (2/60/0); **RECHAZADA** por mantenimiento pendiente, sin cambios; el mantenimiento lleva el desgaste de 80 a 0; la misión siguiente **EXITOSA** (transfiere 22 de combustible y 12 de energía) |
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
| `saltarWarp` fuera de En warp | 1 (salto simple) | Preparando salto | combustible 72 |
| `saltar` sin combustible | — | **Rechazada**: el estado sigue en Preparando salto | sin cambios |

### Universo paralelo por consola (`concurrencia.modelo.Main`)

```bash
mvn compile exec:exec -Dmain.class=unmdp.fi.programacionc.concurrencia.modelo.Main
```

Arranca con dos generadoras y una nave consumidora, a los 4 segundos agrega otra nave y otra generadora mientras la simulación corre, y a los 8 segundos detiene todo. Se espera ver los eventos de producción, depósito, solicitud, retiro y espera ("esperando a que se libere espacio", "esperando por falta de baterías"), el ingreso de las entidades nuevas, un mensaje "se detuvo" por cada hilo y, al final, "Se termino la simulacion".

### Verificación manual de la interfaz

**Acceso y persistencia**

| Caso | Resultado esperado |
|---|---|
| Registrar un usuario que ya existe | Mensaje de error; no se crea otro |
| Registrar con usuario o contraseña vacíos, o de más de 50 caracteres | Mensaje de error del modelo (`ControlAcceso`) |
| Registrar con dos contraseñas distintas | "Las contrasenias no coinciden" (control del formulario) |
| Ingresar con contraseña incorrecta | "Usuario o contrasenia incorrectos" |
| Cerrar, volver a abrir e ingresar con un usuario ya registrado | Entra: el usuario quedó guardado en `data/` |

**Nave, tripulación y misiones**

| Caso | Resultado esperado |
|---|---|
| Agregar un segundo capitán o una antigüedad negativa | Mensaje de error; la tripulación no cambia |
| Ir a Misión sin nave o con tripulación incompleta | El botón *Iniciar misión* está deshabilitado y el estado explica el motivo |
| Cargar combustible o energía por encima de 100 | "La carga excede la capacidad maxima..."; los recursos no cambian y el rechazo queda en la bitácora |
| Iniciar una misión con la tripulación completa | El tablero avanza turno a turno sin congelar la ventana. Al terminar, el informe aparece en el historial de *Control de la nave* |
| Pasar por una estación de carga o chocar un asteroide | La bitácora registra "+25 combustible, +25 energia" o "-20 de energia", y el informe lo muestra como **ganado** o **perdido** |
| Encontrarse con una nave enemiga | Resultado según la tabla del enunciado (Double Dispatch). Si pierde, la nave queda destruida, la misión FALLIDA y los tripulantes figuran como "Fallecido" |
| Desgaste de 80 o más | Se rechaza toda misión salvo M-03, que vuelve a la base, recarga combustible y energía y deja el desgaste en 0. Si el despegue llevara el desgaste a 100 (desgaste 96 o más), también M-03 se rechaza y hay que hacer el mantenimiento desde *Control de la nave* |
| Recursos que no alcanzan para la ruta (incluido quedar justo en 0) | La misión se rechaza en la preparación y la nave no cambia |

**Visor**

| Caso | Resultado esperado |
|---|---|
| Agregar varias generadoras y ninguna nave | Cuando la plataforma llega a 3 baterías, las generadoras esperan |
| Agregar naves sin generadoras | Las naves esperan por falta de baterías |
| Cerrar la ventana principal | Se detienen los hilos del universo paralelo |

### Valores del universo (para interpretar las verificaciones)

| Elemento o acción | Efecto |
|---|---|
| Despegue de M-01 / M-02 / M-03 | 4 de combustible y 4 de desgaste, más 5 / 10 / 0 de energía |
| Salto simple | 2 de combustible, avanza 1 casilla |
| Salto warp | 5 de energía, avanza 2 casillas; requiere 2 turnos de preparación y 1 de enfriamiento (3 de desgaste) |
| Asteroide | −20 de energía |
| Estación de carga | +25 de combustible y +25 de energía (sin superar el máximo de 100) |
| Nave destruida | Combustible 0, energía 0 o desgaste 100 |

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
├── mision/              Mision (Template Method), M-01, M-02, M-03, Travesia, Ruta,
│                        BalanceRecursos e InformeMision
├── tablero/             elementos del universo: asteroide, estación de carga y naves enemigas (Double Dispatch)
├── destinoytablero/     Tablero, Destino y Observable/Observador (provistos por la cátedra)
├── concurrencia/        universo paralelo: plataforma de carga, generadoras y naves consumidoras
├── bitacora/            Bitacora y eventos
├── tripulacion/         Tripulante, cargos y orígenes
├── haberes/             liquidación de haberes (Decorator)
└── excepciones/
database/schema.sql      tabla de usuarios
data/                    base H2 local (se crea al ejecutar; no se versiona)
docs/                    diseño, diagrama de clases, registro de uso de IA y enunciados
```

## Documentación

- [Diseño: responsabilidades, patrones, concurrencia, contratos y decisiones](docs/diseno.md)
- [Diagrama de clases](docs/uml/diagrama-clases.md) (en Mermaid: GitHub lo dibuja al abrir el archivo)
- [Registro de uso de IA](docs/uso-ia.md)

## Entregas

Cada entrega se identifica con una etiqueta Git que señala el commit presentado para evaluación:

| Entrega | Etiqueta |
|---|---|
| Entrega 1 | `entrega-e1` |
| Entrega 2 | `entrega-e2` |