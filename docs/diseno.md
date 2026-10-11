# Diseño — Entrega 1

Documentación de responsabilidades, patrones, principios SOLID y contratos del núcleo orientado a objetos.
Diagrama de clases: [`uml/diagrama-clases.png`](uml/diagrama-clases.png) (fuente editable: `uml/diagrama-clases.dot`).

## 1. Alcance

Según la aclaración de la cátedra (R1–R6):

- Un **universo** (centro de control) registra naves listas para operar y las devuelve cuando se le piden.
- Las naves se crean con una **fábrica**. Toda consulta u orden a una nave pasa por su **asistente de comando**, y cada asistente opera una sola nave.
- El **motor warp** tiene cuatro estados con transiciones fijas. Como todavía no se modela el paso del tiempo, una nave en warp vuelve a Disponible al terminar el salto.
- Las **misiones** siguen siempre preparar → ejecutar → evaluar → cerrar. M-01, M-02 y M-03 son versiones simuladas; su contenido real se define en E2.
- Cada asistente lleva una **bitácora** inmutable que funciona como informe de las misiones.
- Un programa principal (`Main`) simula al usuario.

Además se incluyen tripulación y liquidación de haberes (E1-04 y E1-08 del enunciado).

## 2. Responsabilidades

| Clase | Responsabilidad |
|---|---|
| `Universo` | Registrar naves y devolverlas por id. No las crea ni las opera. |
| `NaveFactory` | Único lugar que conoce las clases concretas de nave y de asistente. Arma tanque, motor y asistente, y los conecta con la nave. Asigna ids únicos. |
| `Nave` | Identidad, tipo, componentes y tripulación. No expone ni reenvía órdenes a sus componentes. |
| `Asistente` | Interfaz con todo lo que se le puede consultar u ordenar a una nave. Extiende `OperadorMision`. |
| `AsistenteComando` | Implementa `Asistente`: recibe órdenes, las delega en el componente que corresponde y registra en su bitácora el cambio (antes → después) y los errores. |
| `Tanque` | Combustible, energía y desgaste, con sus límites. |
| `MotorWarp` + estados | Transiciones del motor. Cada estado decide qué acciones admite. |
| `Mision` | Ciclo común de toda misión: verificación previa, consumo, salto e informe. |
| `MisionIntercepcion`, `MisionRecoleccion`, `MisionRetornoSeguro` | Requisitos propios, condición de éxito y cierre de cada misión. |
| `OperadorMision` | Lo que una misión puede consultar y ordenar a la nave. |
| `Bitacora`, `EventoBitacora` | Registro temporal e inmutable de eventos. |
| `Tripulante` | Datos de un tripulante (inmutable). |
| `ComponenteHaber` y decoradores, `LiquidadorHaberes` | Cálculo del haber mensual, concepto por concepto. |
| `Main` | Demostración. Única clase que imprime por consola. |

## 3. Patrones

### State — motor warp
- **Problema:** las acciones válidas del motor dependen de su estado, y una acción no permitida no debe tener efecto (R3).
- **Aplicación:** `MotorWarp` es el contexto y delega cada acción en su `EstadoMotor` actual. `Disponible`, `PreparandoSalto`, `Warp` y `Enfriamiento` sobreescriben solo las acciones que admiten. Las demás las rechazan los métodos `default` de la interfaz con `OperacionInvalidaException`, sin cambiar de estado. Solo los estados pueden cambiar el estado del motor (`setEstado` es package-private).
- **Consecuencia:** agregar un estado es agregar una clase. Ni el motor ni la nave tienen `switch` o `if` sobre el estado.
- **Evidencia:** escenario C.

### Factory — creación de naves
- **Problema:** el cliente debe pedir un tipo de nave sin conocer las clases concretas, y toda nave debe nacer en un estado válido.
- **Aplicación:** `NaveFactory.crear(tipo)` devuelve una `Nave` (tipo abstracto). Los constructores de las naves son package-private, así que fuera del paquete `nave` no se puede hacer `new NaveCarguero(...)`. La fábrica verifica al final que la nave tenga el motor Disponible y desgaste 0.
- **Consecuencia:** un tipo nuevo requiere un valor en el enum `TipoNave`, una subclase y un `case` en la fábrica. El resto del sistema no cambia.
- **Evidencia:** alta de naves en `Main`.

### Template Method — ciclo de la misión
- **Problema:** toda misión sigue las mismas cuatro etapas en el mismo orden (R4), pero cada una tiene sus propios requisitos y su condición de éxito.
- **Aplicación:** `Mision.ejecutarCiclo()` es `final` y fija el orden. `preparar()` y `ejecutar()` son pasos comunes privados. Cada misión concreta implementa los ganchos: `getCombustibleRequerido()`, `getEnergiaRequerida()`, `evaluar()` y `cerrar()`.
- **Consecuencia:** una misión nueva es una subclase nueva, sin modificar las existentes ni el ciclo.
- **Evidencia:** escenarios A y B.

### Decorator — liquidación de haberes
- **Problema:** el haber combina remuneración por cargo, antigüedad, subsidio por origen y consejos. Una subclase por cada combinación sería inmanejable.
- **Aplicación:** `HaberBase` (remuneración del cargo) implementa `ComponenteHaber`. `AdicionalAntiguedad`, `AdicionalConsejos` y `SubsidioOrigen` extienden `DecoratorHaber`, que envuelve otro componente y le suma su aporte. `LiquidadorHaberes` arma la cadena según el tripulante. El total es `getImporte()`, y recorriendo `getInterno()` se obtiene cada concepto por separado.
- **Consecuencia:** un concepto nuevo es un decorador nuevo. La antigüedad se calcula sobre la remuneración del cargo, no sobre el total acumulado.

## 4. Principios SOLID

| Principio | Dónde se aplica |
|---|---|
| Responsabilidad única | Cada clase de la tabla 2 tiene un solo motivo de cambio. Por ejemplo, los límites de recursos están solo en `Tanque` y las transiciones solo en los estados. |
| Abierto/Cerrado | Misiones, estados del motor y conceptos de haber se extienden agregando clases, sin modificar las existentes. |
| Sustitución de Liskov | `MotorWarp` funciona con cualquier `EstadoMotor`, `ejecutarCiclo()` con cualquier subclase de `Mision`, y la cadena de haberes con cualquier `ComponenteHaber`. |
| Segregación de interfaces | `OperadorMision` expone a la misión solo lo que usa. `Asistente` la extiende con lo que solo usa quien opera la nave: cargas, mantenimiento, enfriamiento y la bitácora. |
| Inversión de dependencias | `Mision` depende de `OperadorMision`, que vive en el paquete `mision`: es `comando` el que depende de `mision`. `Nave` y `Main` dependen de la interfaz `Asistente`, no de `AsistenteComando`. `Universo` depende de `Nave` abstracta. |

### Pedidos de diseño de la aclaración

| Pedido | Cómo se cumple |
|---|---|
| El centro de control no depende de cómo se construyen los objetos que guarda | `Universo` solo conoce `Nave` (abstracta). La construcción está en `NaveFactory`. |
| Otra variante de asistente se registra sin cambiar el centro de control | La variante implementa `Asistente`. `Universo`, `Nave`, sus subclases, las misiones y `Main` no cambian; solo cambia el `new` en `NaveFactory`. |
| Un nuevo tipo de misión no modifica las existentes | Template Method (sección 3). |
| Un nuevo estado no llena la nave de condicionales | State (sección 3). |
| Reemplazar `Main` por una pantalla no cambia el modelo | Ninguna clase del modelo usa `System.out`, `Scanner` ni Swing. Las consultas devuelven datos (`int`, `String`, `List<EventoBitacora>`). |

## 5. Contratos

### Criterio

Siguiendo *Proceso disciplinado – Aserciones* y *Clase 4 – Polimorfismo*:

- **Precondiciones, postcondiciones e invariantes** se escriben en el Javadoc (`pre ->`, `post ->`, `inv ->`) y se verifican con `assert`. Las invariantes se comprueban con un método privado, como `Tanque.verificarInvariante()`. Solo se verifican ejecutando con `-ea`.
- Una condición que el llamador **puede garantizar** (argumento no nulo, carga positiva) es precondición.
- Una condición que depende del **estado actual** y el llamador no controla (capacidad disponible, estado del motor) **no** es precondición: si no se cumple se lanza una excepción, sin modificar nada.

### Invariantes

| Clase | Invariante |
|---|---|
| `Tanque` | `0 <= combustible <= 100`, `0 <= energia <= 100`, `0 <= desgaste <= 100` |
| `MotorWarp` | `estado != null` (arranca en Disponible) |
| `Nave` | `id > 0`; tipo, tanque, motor y asistente no nulos |
| `Universo` | `0 <= cantidad <= CAPACIDAD`; naves registradas no nulas y sin ids repetidos |
| `AsistenteComando` | tanque, motor y bitácora no nulos |
| `Mision` | código, nombre y descripción no vacíos; `asistente != null` |
| `Bitacora` | eventos ordenados por número de secuencia creciente |
| `EventoBitacora` | inmutable |
| `Tripulante` | nombre, cargo y origen no nulos; `antiguedad >= 0` |
| `DecoratorHaber` | `componente != null`; aporte finito y `>= 0` |

### Métodos centrales

| Método | Pre | Post | Excepción (sin cambios en el objeto) |
|---|---|---|---|
| `NaveFactory.crear(tipo)` | `tipo != null` | Nave nueva con id único, motor Disponible, desgaste 0 y recursos según su tipo | — |
| `Universo.registrar(nave)` | `nave != null`; id no registrado | La nave queda registrada | Universo lleno |
| `Universo.obtener(id)` | — | Devuelve la nave con ese id | No existe una nave con ese id |
| `Tanque.cargarCombustible(c)` / `cargarEnergia(c)` | `c > 0` | Recurso = anterior + `c` | La carga excede la capacidad |
| `Tanque.consumir(c, e, d)` | `c, e, d >= 0` | Recursos descontados y desgaste incrementado | Algún recurso no alcanza o el desgaste quedaría fuera de rango |
| `Tanque.realizarMantenimiento()` | — | `desgaste == 0` | — |
| Acciones de `MotorWarp` | — | Transición según la tabla de R3 | Acción no válida en el estado actual |
| Órdenes de `AsistenteComando` | Las del componente al que delega | Cambio registrado en la bitácora (antes → después) | Registra el error y relanza la del componente |
| `Mision.ejecutarCiclo()` | La misión no fue ejecutada | Misión finalizada e informe en la bitácora | — (un rechazo es un resultado, no una excepción) |
| `Bitacora.registrar(tipo, d)` | `tipo != null`; `d` no vacía | Evento al final con el siguiente número | — |
| `LiquidadorHaberes.liquidar(t, consejos)` | `t != null`; `consejos >= 0` | Cadena de decoradores con total y desglose | — |

### Excepciones

| Excepción | Quién la lanza | Quién la maneja |
|---|---|---|
| `OperacionInvalidaException` | `Tanque`, estados del motor y `Universo` | `AsistenteComando` la registra en la bitácora y la relanza: quien dio la orden (`Main` o la futura interfaz) se entera y decide. |
| `MisionNoEjecutableException` | `Mision.preparar()`: motor no disponible, mantenimiento pendiente o recursos insuficientes | `Mision.ejecutarCiclo()` la captura y cierra la misión como RECHAZADA sin modificar la nave. Es un caso normal del dominio y no se propaga. |

## 6. Decisiones de diseño

- **`Tanque` separado de `Nave`:** las invariantes de recursos están en una sola clase.
- **La nave no reenvía órdenes:** toda consulta u orden pasa por el asistente (R2). La nave es dueña de sus componentes, pero no los expone.
- **El asistente registra y relanza:** la bitácora conserva cada rechazo y quien dio la orden no pierde el error.
- **El informe es un evento de la bitácora** (R5), con misión, resultado, recursos consumidos, estado del motor y observaciones. No hay una clase de informe aparte.
- **El mantenimiento pendiente bloquea la salida:** con desgaste ≥ 80 la misión se rechaza en la preparación. Una nave con mantenimiento pendiente no está operativa (el mismo criterio que usa M-03 para evaluar el retorno), así que no inicia ninguna misión.
- **Requisitos por misión:** cada misión declara su combustible y su energía (R4). El desgaste por misión (4) es común a todas.
- **Enfriamiento existe y es alcanzable** desde En warp, pero el ciclo de misión vuelve directo a Disponible hasta que se modele el tiempo (R3).
- **Ids:** los asigna `NaveFactory` con un contador, por lo que no se repiten.
- **Valores fijos como `enum`** (`TipoNave`, `TipoEvento`, `Cargo`, `Origen`, `ResultadoMision`): un valor inexistente (por ejemplo, un cargo "Comandante") no compila. Con constantes `String` solo lo detectaría un `assert`, y únicamente ejecutando con `-ea`. Además, los `switch` de la fábrica y de los haberes trabajan directamente sobre los valores del enum.
- **`Universo`** guarda las naves en un arreglo de capacidad fija (`CAPACIDAD = 10`).
