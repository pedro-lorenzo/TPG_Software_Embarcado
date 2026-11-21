# Registro de uso de IA

Registro de los usos de inteligencia artificial que influyeron en el diseño, el código, las pruebas o la documentación entregada (Reglas Operativas del TPG, §7 y §8). No se registran autocompletados triviales.

En todas las entradas la herramienta es **Claude (Anthropic)**, usada desde claude.ai. El equipo es responsable de todo lo incorporado al repositorio.

## Entrega 1

### E1-1. Conceptos, diseño y cierre de la Entrega 1

| Campo | Detalle |
|---|---|
| Herramienta utilizada | Claude (Anthropic) |
| Propósito de la consulta | Comprender conceptos de la materia, investigar propuestas de código, obtener criterios y comparar decisiones de diseño. También se consultó sobre el reparto de tareas del grupo, el uso de Git y GitHub, y el armado del diagrama de clases. |
| Componente o documento afectado | Código en general. Diagrama de clases (`docs/uml`). `README.md`, `pom.xml` y `docs/diseno.md`. Ajustes de cierre de la Entrega 1: rechazo de misiones con mantenimiento pendiente, requisitos de combustible definidos por cada misión, demostración de mantenimiento en `Main` y revisión de comentarios y contratos. |
| Resultado aprovechado | Explicaciones y criterios para elegir las decisiones de diseño; propuestas de código para los ajustes de cierre listados arriba; estructura del diagrama de clases y de la documentación. |
| Revisión o modificación realizada por el equipo | Toda propuesta fue revisada entre los integrantes que la plantearon, según los criterios del grupo y con apoyo en la bibliografía de la cátedra. |
| Forma en que se verificó el resultado | Compilación y ejecución de `Main` con aserciones habilitadas (`-ea`), comprobando los escenarios A a D, y revisión contra la aclaración de la cátedra. |

## Entrega 2

### E2-1. Revisión del repositorio contra la consigna

| Campo | Detalle |
|---|---|
| Herramienta utilizada | Claude (Anthropic) |
| Propósito de la consulta | Revisar el repositorio completo contra el enunciado de la Parte 2, el anexo de misiones y motor warp, la aclaración de la cátedra y el manual de requerimientos, para detectar faltantes y requisitos no cumplidos. |
| Componente o documento afectado | Todo el repositorio (código, `README.md`, `docs/`). |
| Resultado aprovechado | Una lista de faltantes: etiquetas de entrega, campos de este registro, diagrama desactualizado, links rotos en el README, tamaño del tablero inconsistente en la documentación. También tres bugs (entradas E2-2 a E2-4) y desvíos del enunciado para preparar la defensa (el informe se arma después de `cerrar()`, el motor queda en "En warp", el canje de gemas). |
| Revisión o modificación realizada por el equipo | El equipo decidió qué puntos corregir. Los desvíos de diseño se mantuvieron y se documentaron en `docs/diseno.md` (sección 8). |
| Forma en que se verificó el resultado | La IA compiló el proyecto, ejecutó `Main` con `-ea` y probó los bugs con programas de prueba sobre el modelo. El equipo contrastó los puntos con la consigna. |

### E2-2. Bug: la preparación permitía misiones que destruían la nave

| Campo | Detalle |
|---|---|
| Herramienta utilizada | Claude (Anthropic) |
| Propósito de la consulta | Corregir `Mision.preparar()`, que aceptaba misiones con los recursos justos: el despegue o el último salto dejaban combustible o energía en 0 y destruían la nave. Con desgaste 97, M-03 lanzaba una excepción que no se capturaba. |
| Componente o documento afectado | `mision/Mision.java` (método `preparar()`). |
| Resultado aprovechado | Las comparaciones pasaron de `<` a `<=` (tiene que sobrar recurso) y se agregó un control de desgaste: el desgaste más el del despegue tiene que quedar por debajo de 100. |
| Revisión o modificación realizada por el equipo | El equipo aplicó el cambio a mano en IntelliJ y repuso un evento de bitácora ("preparación correcta") que se había perdido al copiar. |
| Forma en que se verificó el resultado | Casos de prueba: energía 10 con M-02 y desgaste 96 y 97 con M-03 pasaron a dar RECHAZADA sin modificar la nave (antes, la nave destruida o la excepción). `Main` con `-ea` dio los mismos resultados que antes. |

### E2-3. Bug: el desgaste del enfriamiento no destruía la nave

| Campo | Detalle |
|---|---|
| Herramienta utilizada | Claude (Anthropic) |
| Propósito de la consulta | Corregir el enfriamiento del motor warp: si el desgaste superaba 100, `Tanque` rechazaba la operación en lugar de destruir la nave, y el motor quedaba trabado en Enfriamiento. |
| Componente o documento afectado | `recursos/Tanque.java` (nuevo método `sumarDesgasteHastaTope`) y `motor/MotorWarp.java`. |
| Resultado aprovechado | El criterio de diseño: las **órdenes** se rechazan sin modificar nada, y los **efectos** (daño de un asteroide, desgaste del enfriamiento) se topean en el límite del recurso. Se aplicó con el método nuevo. |
| Revisión o modificación realizada por el equipo | El equipo aplicó el cambio y documentó el criterio en `docs/diseno.md`. |
| Forma en que se verificó el resultado | Caso de prueba: desgaste 98 más un enfriamiento deja desgaste 100 y la nave destruida (antes, "desgaste fuera de rango"). El escenario C de `Main` dio la misma secuencia que antes. |

### E2-4. Bug: el informe mostraba consumos negativos

| Campo | Detalle |
|---|---|
| Herramienta utilizada | Claude (Anthropic) |
| Propósito de la consulta | Cumplir el "balance detallado de los recursos ganados, consumidos o perdidos" de la Fase 3: el informe calculaba la diferencia neta, que daba negativa cuando la nave pasaba por estaciones de carga. |
| Componente o documento afectado | Nueva clase `mision/BalanceRecursos.java`; `mision/Travesia.java`, `mision/Mision.java` e `mision/InformeMision.java`. |
| Resultado aprovechado | La clase `BalanceRecursos`, que separa lo consumido (despegue y saltos), lo ganado (estaciones) y lo perdido (asteroides), y la integración en la travesía, la misión y el informe. |
| Revisión o modificación realizada por el equipo | El equipo aplicó los cambios a mano en IntelliJ, completando los campos, getters y el constructor de `InformeMision`. |
| Forma en que se verificó el resultado | Caso de prueba con una estación y un asteroide en la ruta: el informe pasó de "consumido -40" a valores separados por causa (consumido 10/5, ganado 25/25, perdido 0/20), coincidentes con los eventos de la bitácora. `Main` con `-ea` sin errores. |

### E2-5. Reproducibilidad de la demostración por consola

| Campo | Detalle |
|---|---|
| Herramienta utilizada | Claude (Anthropic) |
| Propósito de la consulta | El escenario B de `Main` no era reproducible: la última misión daba EXITOSA o FALLIDA según el pedido de auxilio sorteado, aunque el README prometía resultados fijos. |
| Componente o documento afectado | `Main.java` (escenario B). |
| Resultado aprovechado | La última misión del escenario B usa un `Random` con semilla fija, como ya lo hacía el escenario A. |
| Revisión o modificación realizada por el equipo | El equipo aplicó el cambio y quitó la llamada anterior duplicada. |
| Forma en que se verificó el resultado | `Main` ejecutado 8 veces antes de integrar y 3 veces sobre la versión del repositorio, con resultados idénticos en todas. |

### E2-6. Documentación: README, diseño y diagrama de clases

| Campo | Detalle |
|---|---|
| Herramienta utilizada | Claude (Anthropic) |
| Propósito de la consulta | Dejar la documentación de acuerdo con las Reglas Operativas (§5) y con el código: README completo, decisiones de diseño actualizadas y diagrama de clases de la Entrega 2. |
| Componente o documento afectado | `README.md`, `docs/diseno.md` (sección 8), `docs/uml/diagrama-clases.md` (antes `diagrama2.md`) y los PNG de `docs/uml/`. |
| Resultado aprovechado | README reescrito, con los resultados esperados obtenidos de ejecuciones reales, casos manuales de la interfaz y valores del universo. Sección 8 de `diseno.md` actualizada. Diagrama corregido: `ejecutarCiclo()` figuraba como estático, `Tripulante` como inmutable, y faltaban `BalanceRecursos`, las excepciones y varias relaciones. PNG generados desde el Mermaid. |
| Revisión o modificación realizada por el equipo | El equipo revisó y aplicó los textos, renombró el diagrama y reemplazó el PNG de la Entrega 1. |
| Forma en que se verificó el resultado | Los resultados del README se contrastaron con ejecuciones de `Main` y del universo paralelo. Se comprobaron todos los links de la documentación. Los diagramas se dibujaron sin errores con mermaid-cli. |

### E2-7. Interfaz gráfica (Swing, MVC y Observer)

| Campo | Detalle |
|---|---|
| Herramienta utilizada | Claude (Anthropic) |
| Propósito de la consulta | Diseñar y programar la interfaz gráfica: el aspecto visual (colores, tipografía, fondo, barras de recursos y dibujo del tablero), la organización en ventanas, pestañas, paneles y botones, y la lógica que conecta la interfaz con el modelo según MVC y Observer (controladores, sesión del operador, actualización automática y ejecución de misiones sin congelar la ventana). |
| Componente o documento afectado | `vista/` (ventanas, paneles, interfaces `IVista*`, `Tema`, `FondoEspacial`, `BarraRecurso` y `PanelTablero`), `controlador/` (los ocho controladores), `aplicacion/` (`Sesion` y `ObservadorSesion`) y `AppGrafica.java`. |
| Resultado aprovechado | El estilo visual y los componentes propios (`Tema`, `FondoEspacial`, `BarraRecurso`, `PanelTablero`), la distribución de las secciones y sus botones, y la estructura MVC: vistas que solo exponen textos y un `ActionListener`, controladores que llaman al modelo, `Sesion` como observable y el despachador con `SwingUtilities.invokeLater` para los avisos que llegan desde el hilo de la misión. |
| Revisión o modificación realizada por el equipo | Se cambiaron ajustes visuales menores para evitar que la interfaz sea demasiado cargada. |
| Forma en que se verificó el resultado | Ejecución de `AppGrafica` y prueba manual de cada sección: acceso y registro, creación de nave y tripulación, tripulantes y salarios, control de la nave, misión con el tablero en tiempo real y visor. |