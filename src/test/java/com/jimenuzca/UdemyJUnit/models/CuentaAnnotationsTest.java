package com.jimenuzca.UdemyJUnit.models;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * @BeforeAll y @AfterAll
 * Se ejecutan una sola vez para todos los metodos
 * CONDICIONALES, te permite ejecutar los test de acuerdo a ciertas condiciones, a ciertos contextos. Por ej si queremos
 * ejecutar cierto test si se ejecuta en distintos SO, distintos ambientes, quizas distintas versiones de java
 * @EnabledOnOs({OS.MAC, OS.LINUX}), @DisabledOnOs(OS.WINDOWs) evalua el SO operativo utilizado
 * @EnabledOnJre(JRE.JAVA_8) evalua la version de java utilizada
 * para indicar mas de una opcion abrimos {}
 * @EnabledIfSystemProperty(named = "nombreVarible", matches = "valor/expresion") evalua las variables de sistema con un
 * valor posible
 * @EnabledIfEnvironmentVariable(named = "nombreVariable", matches = "valor/expresion") evalua las variables de sistema
 * Recordar que se pueden crear variables (sistema y/o entorno) propias desde la configuracion
 *
 * IMPORTANTE: EJECUTAR TODA LA CLASE
 * @Nested CLASES ANIDADAS - inner class, Nos permiten organizar de forma jerarquica los test (e ven como nodos, arbol)
 * Si falla un test dentro de una clase anidada, falla toda la clase; sube hacia arriba
 *
 * @RepeatedTest permite repetir un mismo test, se usa cuando hay algun valor que es aleatorio cobrando así sentido su
 * repeticion
 */

//@TestInstance(TestInstance.Lifecycle.PER_CLASS) //crea una instancia comun para todos los metodos, asi podemos manejar
//ESTADOS, podemos ordenar los METODOS y quitar los static. NO RECOMENDABLE
@TestInstance(TestInstance.Lifecycle.PER_METHOD)  //el que es por defecto, crea una referencia por cada test
// es lo mas recomendable ya que tienen que evaluarse de manera independiente, evitando dependencias y acomplamiento
public class CuentaAnnotationsTest {
    Cuenta cuenta;

    // indica que se ejecuta antes de cada metodo
    // pueden colocarse dentro de las clases anidadas y ser diferentes entre si
    @BeforeEach
    void initMetodoTest(){
        this.cuenta = new Cuenta("Graciela", new BigDecimal("1730"));
        System.out.println("iniciando metodo, con cuenta:" + cuenta);
    }

    // por cada metodo, una vez finalizado, ejecuta este
    @AfterEach
    void tearDown() {
        System.out.println("finalizando el metodo");
    }

    //antes que se cree la instancia
    // es estatico por lo cual no requiere de una instancia para inicializarla
    // si le quito el estatic tira un error porque no se ha creado todavia la instancia
    // estas dos como afectan a todas las clases van fuera de las clases anidadas
    @BeforeAll
    static void beforeAll() {
        System.out.println("Inicializando el test");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("Finalizando el test");
    }

    //podemos agrupar tambien por contextos
    @Nested
    @DisplayName("Probando atributos de la cuenta: nombre y saldo")
    class CuentaNombreSaldoTest{
        @Test
        @DisplayName("Probando nombre")
        //indicarle al runner que lo tiene que ejecutar
        public void testNombreCuenta() {
            cuenta.setPersona("Andres");
            String esperado = "Andres";
            String real = cuenta.getPersona();
            // si el mensaje lo declaramos en una funcion lambda no se carga en memoria ni se crea una instancia en caso
            // de no arrojarse el error.
            assertEquals(esperado, real, () -> "Se esperaba " + esperado + " pero nos llega " + real);
        }

        @Test
        @DisplayName("Probando nombre, version II")
        public void testNombreCuentaII() {
            String esperado = "Graciela";
            String real = cuenta.getPersona();
            assertEquals(esperado, real);
            //es igual que el assertEquals de arriba y es lo que el IDE te aconseja
            assertTrue(real.equals("Graciela"), "El nombre de la cuenta no es el esperado");
        }
    }

//    CONDICIONALES, se ejecutan si...

    //podemos agrupar tambien por
    @Nested
    class SistemaOperativoTest{
//    PARA EVALUAR SISTEMAS OPERATIVOS
        @Test
        @EnabledOnOs(OS.WINDOWS)
        void testSoloEnWindows() {
            System.out.println("Test desde Windows");
        }

        // figura como ignorado/ deshabilitado porque estamos en WINDOWS
        @Test
        @EnabledOnOs({OS.MAC, OS.LINUX})
        void testSoloMacLinux() {
            System.out.println("Test desde Mac");
        }

        // figura como ignorado/ deshabilitado porque estamos en WINDOWS y es Disable
        @Test
        @DisabledOnOs(OS.WINDOWS)
        void testNoWindows() {
            System.out.println("Test NO WINDOS");
        }
    }

    @Nested
    class JavaVersionTest{
    //    PARA EVALUAR VERSIONES DE JAVA
        @Test
        @EnabledOnJre(JRE.JAVA_8)
        void testJava() {
            System.out.println("Test en java 8");
        }

        @Test
        @DisabledOnJre(JRE.JAVA_13)
        void testJava13() {
            System.out.println("TEst en java 13");
        }

    }

    @Nested
    class SystemPropertiesTest{
    //    PARA EVALUAR PROPIEDADES DEL SISTEMA

        //para conocer las caracteristicas de sistema
        @Test
        void imprimirSystemProperties() {
            Properties properties = System.getProperties();
            properties.forEach((k,v)-> System.out.println(k + ":" + v));
        }

        @Test
        @EnabledIfSystemProperty(named = "java.version", matches = "1.8.0_251") //indicamos nombre de la variable y el valor con
        // el que deberia hacer match (puede ser una aproximacion y ahi usamos una expresion)
        void testJavaVersion() {
            System.out.println("Ejecuta si tengo la version exacta");
        }

        //para usar una expresion tenemos que conocer los valores comodin .* indica caracteteres desconocidos
        @Test
        @EnabledIfSystemProperty(named = "user.name", matches = ".*Jimena.*")
        void testUsername() {
            System.out.println("Test habilitado para el usuario que en su nombre contiene Jimena");
        }

        // deshabilita el metodo si es de 32 bits
        @Test
        @DisabledIfSystemProperty(named = "os.arch", matches = ".*32")
        void testSolo64() {
            System.out.println("TEST solo 64bits");
        }

        // En este caso la variable no existe, es propia creada por nosotros, por lo que en primera instancia al ejecutarla
        // quedaria deshabilitado el test, ignorado
        // Creamos al varibale desde la barra superior al lado del martillo donde figura CuentaAnnotationsTest (nombre de la
        //clase) -> Edit Configurations. Al lado del -ea, que indica que ejecuta la clase habilitando los assertions
        // colocamos -DnombreVariable=valor de la variable. El -D indica que le vamos a indicar una propiedad nueva
        //de sistema
        @Test
        @EnabledIfSystemProperty(named = "ENV", matches = "develop")
        void testDev() {
            System.out.println("Ejecuta solo si el ENTORNO es develop");
        }

    }

    @Nested
    class EnvironmentVariablesTest {
//    PARA EVALUAR VARIABLES DE AMBIENTE

    //    Para obtener las variables de entorno
        @Test
        void testVariableAmbiente() {
            Map<String, String> varEnvironment = System.getenv();
            varEnvironment.forEach((k,v) -> System.out.println(k + ":" + v));
        }

    //    Las / se modifican por doble \, \\
        @Test
        @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*jdk1.8.0_251")
        void testJavaHome() {
            System.out.println("Este metodo solo se ejecuta si la variable JAVA_HOME es C:\\Program Files\\Java\\jdk1.8.0_251");
        }

        //como yo tengo 8 no se ejecuta
        @Test
        @EnabledIfEnvironmentVariable(named = "NUMBER_OF_PROCESSORS", matches = "10")
        void testNumberProcessors() {
            System.out.println("Este metodo solo se ejecuta si el numero de procesadores es 10");
        }

    //
        // Evalua una variable de entorno propia, la creeamos en el mismo lugar, con la diferencia que se agrega en
        //Environment variables y sin la -D... ej: ENVIRONMENTE=DEVELOPER
        @Test
        @EnabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "DEVELOPER")
        void testEnv() {
            System.out.println("Se ejecuta si tiene la variable de entorno ENVIRONMENT con valor DEVELOPER creada en la config");
        }

        @Test
        @DisabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "PRODUCCION")
        void testProd() {
            System.out.println("No se ejecuta si tiene la variable de entorno ENVIRONMENT con valor PRODUCCION creada en la config");
        }
    }

    @Nested
    class CuentasOperacionesTest{

        // Se indica la cantidad de veces que se va a repetir, de mano izquierda vemos que aparece como un nodo
//        @RepeatedTest(3)
        @RepeatedTest(value = 3, name = "{displayName} - Repeticion numero {currentRepetition} de {totalRepetitions}")
        @DisplayName("Probando test debito repitiendo")
//        mediante inyeccion de dependencia podemos obtener la repticion mediante RepetitionInfo
        void testDebitoCuenta(RepetitionInfo info) {
            if(info.getCurrentRepetition() == 2)
                System.out.println(info.getCurrentRepetition());

            cuenta.debito(new BigDecimal("100"));

            //valido que no sea nulo
            assertNotNull(cuenta.getSaldo());
            //intvalue nos devuelve solo la parte entera y a eso lo comparamos con 900,
            assertEquals(1630, cuenta.getSaldo().intValue());

            // el toPlainString
            assertEquals("1630", cuenta.getSaldo().toPlainString());
        }

        @Test
        void testCreditoCuenta() {
            cuenta.credito(new BigDecimal("370"));

            //valido que no sea nulo
            assertNotNull(cuenta.getSaldo());

            //intvalue nos devuelve solo la parte entera y a eso lo comparamos con 900,
            assertEquals(2100, cuenta.getSaldo().intValue());

            // el toPlainString
            assertEquals("2100", cuenta.getSaldo().toPlainString());
        }
    }

    //HABILITAMOS O DESHABLILITAMOS (sin anotaciones) UN TEST SEGUN UNA CONDICION PERO DENTRO DEL CODIGO
    //quiero que solo se ejecute si el entorno es dev
    @Test
    void testSaldoCuentaDev() {
        System.out.println("develop".equals(System.getProperty("ENV")));
        boolean esDEV = "develop".equals(System.getProperty("ENV"));
        // todo lo que esta por debajo del assume se ejecuta o no de acuerdo a la respuesta de la condicion
        // si es true si, sino se ignora
        assumeTrue(esDEV);
        assertNotNull(cuenta.getSaldo());
        assertEquals(1730, cuenta.getSaldo().intValue());
        assertEquals("1730", cuenta.getSaldo().toPlainString());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void testSaldoCuentaDevII() {
        System.out.println("develop".equals(System.getProperty("ENV")));
        boolean esDEV = "hola".equals(System.getProperty("ENV"));
        // 1. parametro lo que se evalua, 2. funcion lambda que incluye todas las comprobaciones que se ejecutan
        // OJO! las que estan dentro del assuming se evaluan solo si esDEv es true
        // se lee asumiendo que
        assumingThat(esDEV, () -> {
            assertNotNull(cuenta.getSaldo());
            assertEquals(1730, cuenta.getSaldo().intValue());
            assertEquals("1730", cuenta.getSaldo().toPlainString());
        });
        //estan no se evaluan
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

}