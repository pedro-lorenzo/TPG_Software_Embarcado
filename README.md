# Software embarcado de una nave interestelar

Trabajo Práctico Grupal 2026 — Programación C — FI-UNMdP.
Entrega 1: núcleo orientado a objetos (universo, naves, asistente de comando, motor warp, bitácora, misiones y liquidación de haberes).

## Requisitos

- JDK 17 o superior
- Maven 3.6 o superior (sirve el que trae IntelliJ IDEA)

No requiere configuración local adicional.

## Compilar y ejecutar

```bash
mvn compile exec:exec
```

Ejecuta `Main` con las aserciones habilitadas (`-ea`). Los contratos (precondiciones, postcondiciones e invariantes) están escritos con `assert`, así que solo se verifican con `-ea`.

Alternativa con jar ejecutable:

```bash
mvn package
java -ea -jar target/tpg-software-embarcado-1.0-SNAPSHOT.jar
```

Desde IntelliJ: ejecutar `Main` agregando `-ea` en *VM options*.

## Verificación

`Main` simula al usuario y reproduce los escenarios de la Ficha de Inicio E1. Cada escenario imprime los recursos y la bitácora del asistente.

| Escenario | Qué muestra | Resultado esperado |
|---|---|---|
| Alta de naves | La fábrica crea una nave de cada tipo y el universo las registra | Exploradora 60/80, Carguero 100/60, Combate 80/100 (combustible/energía), desgaste 0, motor Disponible |
| A — Ejecución correcta | M-01, M-02 y M-03 completas sobre la misma nave | Las tres EXITOSA; combustible 60→48, energía 80→70, desgaste 0→12; un informe por misión en la bitácora |
| B — Recursos insuficientes y mantenimiento | Misión sin combustible suficiente; misión con mantenimiento pendiente; mantenimiento | RECHAZADA sin cambios en la nave; RECHAZADA con desgaste 80; mantenimiento 80→0; la misión siguiente EXITOSA |
| C — Motor warp | Recorrido válido y una transición inválida | Disponible → Preparando salto → En warp → Enfriamiento → Disponible; `saltar` desde Disponible se rechaza y queda registrado |
| D — Contrato inválido | Carga de combustible que excede la capacidad | Se rechaza y los recursos no cambian |

## Estructura

```
src/main/java/unmdp/fi/programacionc/
├── Main.java        programa de demostración (único que imprime por consola)
├── universo/        centro de control: registra y devuelve naves
├── nave/            Nave, sus tipos y NaveFactory
├── comando/         Asistente y AsistenteComando: toda orden a la nave pasa por él
├── recursos/        Tanque: combustible, energía y desgaste
├── motor/           MotorWarp y sus estados (State)
├── mision/          Mision (Template Method), M-01, M-02 y M-03
├── bitacora/        Bitacora y eventos
├── tripulacion/     Tripulante, cargos y orígenes
├── haberes/         liquidación de haberes (Decorator)
└── excepciones/
```

## Documentación

- [Diseño: responsabilidades, patrones, SOLID y contratos](docs/diseno.md)
- [Diagrama de clases](docs/uml/diagrama-clases.png)
- [Registro de uso de IA](docs/uso-ia.md)

## Entrega

La Entrega 1 corresponde a la etiqueta `entrega-e1`.
