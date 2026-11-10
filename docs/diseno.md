# Diseño — Entregas 1 y 2

Documentación de responsabilidades, patrones, principios SOLID, concurrencia y contratos.
Diagrama de clases: [`uml/diagrama-clases.md`](uml/diagrama-clases.md).

## 1. Alcance

**Entrega 1** (según la aclaración de la cátedra, R1–R6):

- Un **universo** (centro de control) registra naves listas para operar y las devuelve cuando se le piden.
- Las naves se crean con una **fábrica**. Toda consulta u orden a una nave pasa por su **asistente de comando**, y cada asistente opera una sola nave.
- El **motor warp** se modela con el patrón State.
- Las **misiones** siguen siempre preparar → ejecutar → evaluar → cerrar.
- Cada asistente lleva una **bitácora** inmutable.
- Tripulación y liquidación de haberes (E1-04 y E1-08).

**Entrega 2** (sobre el mismo proyecto, sin reemplazar nada):

- Las misiones M-01, M-02 y M-03 se ejecutan sobre un **tablero** de 20 × 20 con asteroides, estaciones de carga y naves enemigas (batallas con Double Dispatch).
- El **motor warp** pasa a la especificación del anexo (preparación de dos turnos, costos de salto y enfriamiento).
- **Acceso al sistema** con usuario y contraseña persistidos en H2 (DAO/DTO, JDBC).
- **Interfaz Swing** organizada en MVC, con actualización automática mediante Observer.
- **Universo paralelo** concurrente: productor-consumidor con monitores.

## 2. Arquitectura

```
Vista (vista/)  ──ActionListener──▶  Controlador (controlador/)  ──▶  Modelo
   ▲                                        │                          ├─ Sesion (aplicacion/): estado de la sesión
   │                                        │                          ├─ ControlAcceso → UsuarioDAO (persistencia/)
   └──────── Observer (Sesion, ObservadorVisor) ◀── avisos del modelo ─┤─ Universo → Nave → Asistente (dominio)
                                                                        └─ UniversoParalelo (concurrencia/)
```

- La **vista** solo conoce interfaces (`IVistaLogin`, `IVistaControl`, etc.) y expone textos y un `ActionListener`. No contiene reglas.
- El **controlador** recibe la acción, llama al modelo, y le dice a la vista qué mostrar. Conoce la vista por su interfaz.
- El **modelo** no conoce Swing ni la consola. `Main` y `AppGrafica` son dos clientes distintos del mismo modelo.
- La separación de responsabilidades del acceso es la que pide la guía: la **vista** valida formato, el **controlador** coordina, `ControlAcceso` aplica las reglas y lanza excepciones, y el **DAO** hace las consultas.

## 3. Responsabilidades

### Dominio (Entrega 1)

| Clase | Responsabilidad |
|---|---|
| `Universo` | Registrar naves y devolverlas por id. No las crea ni las opera. |
| `NaveFactory` | Único lugar que conoce las clases concretas de nave y de asistente. Arma tanque, motor y asistente, y los conecta con la nave. Asigna ids únicos. |
| `Nave` | Identidad, tipo, componentes y tripulación. Protege las reglas de la tripulación: no admite ids repetidos ni más de un capitán, y `tieneTripulacionValida()` indica si puede operar. |
| `Asistente` | Interfaz con todo lo que se le puede consultar u ordenar a una nave. Extiende `OperadorMision`. |
| `AsistenteComando` | Implementa `Asistente`: recibe órdenes, las delega en el componente que corresponde y registra en su bitácora el cambio (antes → después) y los errores. Guarda los informes de misión. |
| `Tanque` | Combustible, energía y desgaste, con sus límites y la condición de agotamiento. |
| `MotorWarp` + estados | Transiciones y costos del salto. Cada estado decide qué proceso ejecuta. |
| `Bitacora`, `EventoBitacora` | Registro temporal e inmutable de eventos. |
| `Tripulante` | Datos de un tripulante (inmutable), con marca de fallecido. |
| `ComponenteHaber` y decoradores, `LiquidadorHaberes` | Cálculo del haber mensual, concepto por concepto. |

### Misiones y tablero (Entrega 2)

| Clase | Responsabilidad |
|---|---|
| `Mision` | Template Method del ciclo. Calcula el objetivo y la ruta al preparar, consume los requisitos al ejecutar, y arma el informe. |
| `MisionIntercepcion`, `MisionRecoleccion`, `MisionRetornoSeguro` | Requisitos propios, objetivo, verificación en destino, condición de éxito y cierre de cada misión. |
| `Travesia` | Bucle por turnos: turno de la nave (inspeccionar, escanear, saltar) y turno del tablero. Pausa breve por iteración (`Thread.sleep`). |
| `Ruta` | Calcula la ruta (casilla por casilla) entre dos posiciones. |
| `InformeMision` | Informe final inmutable: resultado, ruta, consumos del recorrido, balance del cierre, estado del motor y observaciones. |
| `OperadorMision` | Lo que una misión puede consultar y ordenar a la nave. Extiende `NavePropia`. |
| `NavePropia` | La nave propia tal como la ven los elementos del tablero: enfrentar, recibir impacto, recargar, destruir. |
| `ElementoDelUniverso`, `Asteroide`, `EstacionDeCarga`, `NaveEnemiga` y sus tres subclases | Lo que hay en una casilla y qué le hace a la nave. |
| `Tablero`, `Destino`, `Observable`, `Observador`, `Posicion` | Provistos por la cátedra: no se modifican. |

### Aplicación, interfaz y persistencia (Entrega 2)

| Clase | Responsabilidad |
|---|---|
| `Sesion` | Modelo de la aplicación: usuario, universo, nave actual, consejos del período, tablero y misión en curso. Avisa a sus observadores cuando algo cambia. |
| `Controlador*` | Un controlador por sección. `ControladorPrincipal` arma la ventana y conecta cada panel con su controlador. |
| `Ventana*`, `Panel*`, `IVista*` | Vistas Swing y sus interfaces. |
| `ControlAcceso` | Reglas de registro e inicio de sesión. |
| `UsuarioDAO`, `UsuarioDAOJdbc`, `UsuarioDTO` | Acceso a datos de usuarios sobre H2. |
| `UniversoParalelo`, `PlataformaDeCarga`, `GeneradoraDeEnergia`, `NaveConsumidora`, `EntidadParalela` | Universo paralelo concurrente. |

## 4. Patrones

### State — motor warp
- **Problema:** el comportamiento del motor depende de su fase operativa (anexo del enunciado).
- **Aplicación:** `MotorWarp` es el contexto y solo expone `saltar()` y `saltarWarp()`. Cada una delega en el `EstadoMotor` actual, que decide el proceso y el próximo estado. Solo los estados cambian el estado del motor (`setEstado` es package-private). `saltarWarp` hace por defecto el salto simple; solo `Warp` lo sobreescribe.
- **Transiciones:**
    - `Disponible` → `Preparando salto` con `saltar`.
    - `Preparando salto` → `En warp` con `saltar` si hay al menos 5 de energía; si no, se queda.
    - `En warp` → `Enfriamiento` con `saltarWarp` (avanza 2 y consume 5 de energía); si falta energía hace un salto simple y vuelve a `Preparando salto`.
    - `Enfriamiento` → `Disponible` con `saltar` (suma 3 de desgaste).
    - Todo salto simple cuesta 2 de combustible y avanza 1; si no alcanza, se lanza `OperacionInvalidaException` y el estado no cambia.
- **Consecuencia:** agregar un estado es agregar una clase; ni el motor ni la nave tienen `switch` o `if` sobre el estado.
- **Evidencia:** escenario C de `Main`.

### Factory — creación de naves
- **Problema:** el cliente debe pedir un tipo sin conocer las clases concretas, y toda nave debe nacer en un estado válido.
- **Aplicación:** `NaveFactory.crear(tipo)` devuelve una `Nave` (tipo abstracto). Los constructores son package-private. La fábrica asigna ids únicos y verifica al final que la nave tenga el motor disponible y desgaste 0.
- **Consecuencia:** un tipo nuevo requiere un valor en `TipoNave`, una subclase y un `case` en la fábrica.
- **Evidencia:** alta de naves en `Main` y sección Creación de la interfaz.

### Template Method — ciclo de la misión
- **Problema:** toda misión sigue las mismas cuatro etapas en el mismo orden, pero cada una tiene sus propios requisitos, objetivo y condición de éxito.
- **Aplicación:** `Mision.ejecutarCiclo()` es `final` y fija el orden. `preparar()` y `ejecutar()` son pasos comunes privados. Cada misión implementa los ganchos: `getCombustibleRequerido()`, `getEnergiaRequerida()`, `calcularObjetivo()`, `verificarEnDestino()`, `evaluar()` y `cerrar()`.
    - **Preparar:** verifica nave viva, mantenimiento y recursos mínimos; calcula el objetivo y almacena la ruta.
    - **Ejecutar:** consume el despegue y recorre la ruta con `Travesia`; al llegar, verifica en destino si hay que entregar recursos.
    - **Evaluar:** decide EXITOSA o FALLIDA.
    - **Cerrar:** impacta en la nave lo ganado o entregado (M-01 transfiere el auxilio, M-02 canjea gemas por combustible, M-03 recarga y hace el mantenimiento).
- **Consecuencia:** una misión nueva es una subclase nueva, sin modificar las existentes ni el ciclo.
- **También se usa** en `EntidadParalela`: `run()` fija el bucle y la detención; generadoras y naves consumidoras definen su ciclo.

### Decorator — liquidación de haberes
- **Problema:** el haber combina remuneración por cargo, antigüedad, subsidio por origen y consejos; una subclase por combinación sería inmanejable.
- **Aplicación:** `HaberBase` implementa `ComponenteHaber`; `AdicionalAntiguedad`, `AdicionalConsejos` y `SubsidioOrigen` extienden `DecoratorHaber`, que envuelve otro componente y le suma su aporte. `LiquidadorHaberes` arma la cadena. El total es `getImporte()` y recorriendo `getInterno()` se obtiene cada concepto.
- **Consecuencia:** un concepto nuevo es un decorador nuevo. La antigüedad se calcula sobre la remuneración del cargo, no sobre el total acumulado.
- **Uso en la interfaz:** la sección Tripulantes liquida cada salario con esta misma cadena.

### Double Dispatch — batallas entre naves
- **Problema:** el resultado de un encuentro depende del tipo de la nave propia **y** del tipo de la enemiga, sin `if` ni `switch` sobre el tipo.
- **Aplicación:** `NaveEnemiga.interactuar(nave)` llama a `nave.enfrentar(this)` (primer despacho: decide la clase de la nave propia), y cada nave propia llama a `enemiga.recibirDeExplorador()`, `recibirDeCarguero()` o `recibirDeCombate()` (segundo despacho: decide la clase de la enemiga). El resultado es `PAZ`, `GANA` o `PIERDE` y coincide con la tabla del enunciado. Si pierde, la nave propia se destruye con toda su tripulación.
- **Consecuencia:** un tipo de nave nuevo agrega un método y una fila de resultados, sin condicionales.

### Observer — actualizaciones automáticas
Hay tres usos, todos con el mismo criterio: el observable no conoce a quien lo observa.

| Observable | Observador | Qué avisa |
|---|---|---|
| `Tablero` (cátedra) | `Sesion` | Cada turno del tablero, para redibujar la pantalla. |
| `Sesion` | Controladores de cada sección (`ObservadorSesion`) | Cambió el estado de la nave, la tripulación, los recursos o la misión. |
| `UniversoParalelo` | `ControladorVisor` (`ObservadorVisor`) | Cada evento de la plataforma de carga, como texto. |

### MVC
Ver la sección 2. Cada pantalla tiene su interfaz de vista, su controlador y comparte el modelo (`Sesion`). Las acciones del usuario siguen el recorrido vista → `ActionListener` → controlador → modelo → controlador → vista.

### DAO y DTO
`ControlAcceso` depende de la interfaz `UsuarioDAO`; `UsuarioDAOJdbc` la implementa sobre H2 con `PreparedStatement`. `UsuarioDTO` transporta nombre y contraseña entre capas. Cambiar el almacenamiento es escribir otro DAO, sin tocar el servicio ni los controladores.

### Productor-consumidor con monitores
`PlataformaDeCarga` es el recurso compartido (capacidad 3). Sus métodos `depositar` y `retirar` son `synchronized`, evalúan la condición de guarda en un `while` y usan `wait()` y `notifyAll()`. Las generadoras (productores) y las naves consumidoras corren cada una en su propio hilo, y se pueden agregar en vivo.

## 5. Concurrencia

| Hilo | Qué hace | Cuidado |
|---|---|---|
| Hilo de Swing (EDT) | Dibuja y atiende botones | Nunca ejecuta una misión: se congelaría la ventana. |
| Hilo de la misión | `Sesion.iniciarMision` lanza `ejecutarCiclo()` en un hilo aparte | Sus avisos pasan por un *despachador* (`SwingUtilities::invokeLater`) antes de tocar la pantalla. |
| Hilos de generadoras y naves | Un hilo por entidad del universo paralelo | El emisor de eventos debe volver rápido y no llamar de vuelta al universo; la vista encola con `invokeLater`. |

- `Sesion.misionEnCurso` es `volatile`, y solo permite una misión a la vez.
- `Bitacora.registrar` es `synchronized` (la numeración no se repite). Los informes se guardan en una lista concurrente.
- Al cerrar la ventana se llama a `UniversoParalelo.detener()`, que baja la bandera e interrumpe los hilos que esperan en `wait()` o `sleep()`.

## 6. Principios SOLID

| Principio | Dónde se aplica |
|---|---|
| Responsabilidad única | Los límites de recursos están solo en `Tanque`; las transiciones solo en los estados; las reglas de acceso solo en `ControlAcceso`; la vista solo dibuja. |
| Abierto/Cerrado | Misiones, estados del motor, conceptos de haber, elementos del tablero y entidades paralelas se extienden agregando clases. |
| Sustitución de Liskov | `MotorWarp` funciona con cualquier `EstadoMotor`; `ejecutarCiclo()` con cualquier `Mision`; el tablero con cualquier `ElementoDelUniverso`. |
| Segregación de interfaces | `OperadorMision` expone a la misión solo lo que usa; `NavePropia` solo lo que los elementos del tablero le hacen a la nave; cada pantalla tiene su `IVista*`. |
| Inversión de dependencias | `Mision` depende de `OperadorMision` (definida en el paquete `mision`); los controladores dependen de interfaces de vista; `ControlAcceso` depende de `UsuarioDAO`. |

### Pedidos de diseño de la aclaración

| Pedido | Cómo se cumple |
|---|---|
| El centro de control no depende de cómo se construyen los objetos que guarda | `Universo` solo conoce `Nave` (abstracta). La construcción está en `NaveFactory`. |
| Otra variante de asistente se registra sin cambiar el centro de control | La variante implementa `Asistente`; solo cambia el `new` en `NaveFactory`. |
| Un nuevo tipo de misión no modifica las existentes | Template Method. |
| Un nuevo estado no llena la nave de condicionales | State. |
| Reemplazar el programa principal por una pantalla no cambia el modelo | `Main` y `AppGrafica` usan el mismo modelo. Ninguna clase del modelo usa Swing ni lee de la consola. |

## 7. Contratos

### Criterio

- **Precondiciones, postcondiciones e invariantes** se escriben en el Javadoc (`pre ->`, `post ->`, `inv ->`) y se verifican con `assert`. Solo se verifican ejecutando con `-ea`.
- Una condición que el llamador **puede garantizar** (argumento no nulo, carga positiva) es precondición.
- Una condición que depende del **estado actual** y el llamador no controla (capacidad disponible, id repetido, usuario existente) **no** es precondición: si no se cumple se lanza una excepción, sin modificar nada.

### Invariantes

| Clase | Invariante |
|---|---|
| `Tanque` | `0 <= combustible <= 100`, `0 <= energia <= 100`, `0 <= desgaste <= 100` |
| `MotorWarp` | `estado != null` y `tanque != null`; arranca en Disponible |
| `Nave` | `id > 0`; tipo, tanque, motor y asistente no nulos; ids de tripulantes sin repetir y a lo sumo un capitán |
| `Universo` | `0 <= cantidad <= CAPACIDAD`; naves no nulas y sin ids repetidos |
| `AsistenteComando` | tanque, motor y bitácora no nulos |
| `Mision` | código, nombre y descripción no vacíos; `asistente != null` |
| `Bitacora` | eventos con número de secuencia creciente |
| `EventoBitacora`, `InformeMision` | inmutables |
| `Tripulante` | nombre, cargo y origen no nulos; `antiguedad >= 0` |
| `DecoratorHaber` | `componente != null`; aporte finito y `>= 0` |
| `Sesion` | usuario no vacío; universo no nulo |
| `PlataformaDeCarga` | `0 <= baterias <= capacidad` |

### Métodos centrales

| Método | Pre | Post | Excepción (sin cambios en el objeto) |
|---|---|---|---|
| `NaveFactory.crear(tipo)` | `tipo != null` | Nave nueva con id único, motor Disponible, desgaste 0 y recursos según su tipo | — |
| `Nave.agregarTripulante(t)` | `t != null` | El tripulante queda agregado | Id repetido o segundo capitán |
| `Universo.registrar(nave)` | `nave != null`; id no registrado | La nave queda registrada | Universo lleno |
| `Universo.obtener(id)` | — | Devuelve la nave con ese id | No existe una nave con ese id |
| `Tanque.cargarCombustible(c)` / `cargarEnergia(c)` | `c > 0` | Recurso = anterior + `c` | La carga excede la capacidad |
| `Tanque.consumir(c, e, d)` | `c, e, d >= 0` | Recursos descontados y desgaste incrementado | Algún recurso no alcanza o el desgaste quedaría fuera de rango |
| `MotorWarp.saltar()` / `saltarWarp()` | — | Transición según el estado; devuelve casilleros avanzados | Combustible insuficiente (el estado no cambia) |
| Órdenes de `AsistenteComando` | Las del componente al que delega | Cambio registrado en la bitácora (antes → después) | Registra el error y relanza la del componente |
| `Mision.ejecutarCiclo()` | La misión no fue ejecutada | Misión finalizada; informe en la bitácora y en la lista de informes | — (un rechazo es un resultado, no una excepción) |
| `ControlAcceso.registrar(u, c)` | — | Usuario guardado | Datos vacíos o de más de 50 caracteres; usuario ya existente |
| `ControlAcceso.iniciarSesion(u, c)` | — | `true` si las credenciales coinciden | — |
| `PlataformaDeCarga.depositar` / `retirar` | — | Una batería más / una menos | — (espera con `wait()` si está llena / vacía) |

### Excepciones

| Excepción | Quién la lanza | Quién la maneja |
|---|---|---|
| `OperacionInvalidaException` | `Tanque`, motor, `Nave`, `Universo`, `ControlAcceso` y DAO | `AsistenteComando` la registra y la relanza; los controladores la atrapan y le piden a la vista que muestre el mensaje. |
| `MisionNoEjecutableException` | `Mision.preparar()`: nave destruida, mantenimiento pendiente o recursos insuficientes | `Mision.ejecutarCiclo()` la captura y cierra la misión como RECHAZADA sin modificar la nave. |

## 8. Decisiones de diseño

- **`Tanque` separado de `Nave`:** las invariantes de recursos están en una sola clase.
- **La nave no reenvía órdenes:** toda consulta u orden pasa por el asistente (R2).
- **El asistente registra y relanza:** la bitácora conserva cada rechazo y quien dio la orden no pierde el error.
- **Informe de misión como clase (Entrega 2):** en la Entrega 1 el informe era un evento de la bitácora (R5). La Parte 2 pide un historial de informes finales, así que se incorporó `InformeMision` (inmutable), que además se registra como evento en la bitácora. No hay dos fuentes de verdad: el informe se arma con las diferencias reales de recursos antes y después, no con constantes.
- **Balance del cierre aparte:** el informe separa lo consumido en el recorrido de lo que cambia al cerrar (entrega de auxilio, canje de gemas, recarga), como indica el enunciado.
- **Verificación estricta en destino (M-01):** la nave debe conservar *más* de lo que entrega (`>`), para que la entrega no la deje en cero y la destruya.
- **Recursos mínimos de la preparación:** el despegue más dos de combustible por cada paso de la ruta (el peor caso, sin warp). Si no alcanza, la misión se rechaza antes de modificar nada.
- **Mantenimiento pendiente:** con desgaste ≥ 80 se rechaza toda misión salvo M-03, que justamente vuelve a la base a repararse.
- **Destrucción de la nave:** desgaste 100, energía 0 o combustible 0 la destruyen y marcan fallecida a la tripulación (condición de `Tanque.estaAgotado()`, revisada por el asistente).
- **Base de M-03:** el centro del tablero, que es donde comienza la nave.
- **Un tablero por nave:** conserva su posición entre misiones, para que M-03 vuelva a la base.
- **Misiones en otro hilo:** `Sesion` las lanza y permite una sola a la vez.
- **Enfriamiento** existe y es alcanzable desde `En warp`; vuelve a `Disponible` en el siguiente salto.
- **Valores fijos como `enum`** (`TipoNave`, `TipoEvento`, `Cargo`, `Origen`, `ResultadoMision`, `ResultadoBatalla`): un valor inexistente no compila, y los `switch` trabajan directamente sobre ellos.
- **Ids de nave:** los asigna `NaveFactory` con un contador. `Universo` guarda las naves en un arreglo de capacidad fija (`CAPACIDAD = 10`).

## 9. Limitaciones conocidas

- Las contraseñas se guardan sin cifrar (el enunciado no lo exige).
- La ruta de la base H2 y la del esquema son relativas a la carpeta de ejecución, por eso se ejecuta desde la raíz del proyecto.
- El tablero de la cátedra sortea objetivos y distribución de elementos: dos ejecuciones no recorren la misma ruta.
- La interfaz se verifica manualmente (ver README); no hay pruebas automáticas.