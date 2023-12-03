# UdemyJUnit
Curso Udemy - Guía Completa JUnit y Mockito incluye Spring Boot Test 2022

## Pruebas unitarias
Pasos para crear un desarrollo:
1. Diseño
2. Escribir nuestro código
3. Probar nuestro código

INPUT (datos de entrada) -> PIEZA DE CODIGO (algoritmo) -> OUTPUT (resultado esperado)
Uan prueba unitaria siempre tiene un valor esperado.
Multiples escenarios
Sistemas de reporteria para los test que no corren.
Pruebas automaticas y continuas para que el codigo siga funcionando a lo largo del tiempo, habiendo realizado cambios.

### Que son las pruebas unitarias?
Son un proceso de examen para verificar que una pieza de código cumple con ciertas reglas de negocio y afirmar el resultado esperado

## Introduccion a JUnit5
La version anterior, la 4, salió en 2006 y ya tenia mucho tiempo en la industria y no abaracaba la programacion funcional, las lambdas, etc.
En la version 5 se incorporan muchas actualizaciones que se corresponde con versiones mas nuevas de java, donde se incorporan varios conceptos fundamentales

### Que es JUnit5?
JUnit es un framework para escribir prueba unitarias de nuestro código y ejecutarlas en la JVM. Utiliza programacion funcional y lambda e incluye varios estilos diferentes de pruebas, configuraciones, anotaciones, ciclo de vida, etc.

### Arquitectura de JUnit 5
Junit es monolitica, tiene un solo jar.
En cambio, Junit 5 esta formada por distintas partes, distintos jar

JUnit Platform (Es el core, es donde esta el motor y donde se lanzan los tests, interactua con los distintos IDE)
Junit Jupiter (Es donde escribimos los test los programadores)
JUnit Vintage (permite la articulacion con versiones anteriores de Junit)

### JUnit Jupiter
- API para escribir nuestros tests
- Agrega un modelo y caracteristicas en Junit5
- Nuevas anotaciones y estilos de testings
- Permite escribir extensiones

### Anotaciones JUnit Jupiter
```Java:
@Test
@DisplayName
@Nested
@Tag
@ExtendWith
@BeforeEach
@AfterEach
@BeforeAll
@AfterAll
@Disable
```

## Configuracion el proyecto maven con JUnit 5
Dependencia a agregar
```XML:
 <dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.6.3</version>
</dependency>
```
- Comúnmente para trabajar con finanzas utilizamos el tipo **BigDecimal**. Ojo es un valor inmutable!
- Como buen práctica se crea la clase test, en un paquete con la misma ruta de la clase a probar pero en el apartado test. El nombre deberia ser el mimso y terminar con Test. Ej: Cuenta -> CuentaTest
- Los test debe ser de ambito **privado** en la clase, por defecto sino se le indica el modificador de acceso es privado.
- Podemos importar la libreria assertion como estatica para no tener que colocarlo siempre que querramos usar sus metodos `import static org.junit.jupiter.api.Assertions.*;`
- Los tests tienen que ser **metodos independientes**, STATELESS. No pueden ni deben depender unos de otros. Ya que se ejecutan instancias distintas para cada test.

### TDD Test Driven Development
Desarrollo guiado por pruebas
1. Creamos el metodo de test(va a fallar, porque no hay codigo)
2. Implementamos el codigo, la logica de negocio, el algoritmo.
3. Probamos el test.


