package com.jimenuzca.UdemyJUnit.models;

import com.jimenuzca.UdemyJUnit.exceptions.DineroInsuficienteException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Si falla un solo Assert dentro del Test falla el metodo completo
 * Cada test debe ser completamente independiente de otro; uno no debe pender de otro. por eso no importa el orden
 * Dentro del assertTrue / assertFalse tiene que haber uan condicion que se evalue de esa manera true/false.
 * TDD primero hacemos la prueba, despues implementamos el código
 * assertAll, se utiliza cuando tenemos muchos assert dentro una misma pruebas unitarias. AssertAll nos permite rastrear
 * cual de los assertions de un test ha fallado, los agrupa. Solamente despliega informacion de cada falla
 * En los assertions podemos agregarle como ultimo parametro un mensaje propio mas descriptivo, solo se consume si ocurre
 * el error (ocurra o no ocurra el error se crea una instancia de este string, mensaje) por eso es mejor crearla como
 * una expresion lambda
 * fail() nos permite forzar el error
 * ANNOTATIONS:
 * @DisplayName nos permite ponerle un nombre mas descriptivo al test.Al ejecutarlo vemos que el nombre que toma es este
 * (soporta caracteres especiales, emojis)
 * @Disable nos permite deshabilitar el test, de esta manera al correr la prueba completa no lo evalua. fijarse que lo
 * marca con el simbolo de prohibido al ejecutar y tambien indica el total de las que paso y las que ignoró.
 * No es lo mismo que comentarla ya que si lo comentas no lo marca ni lo contabiliza en el total
 * CICLO DE VIDA: proceso en el cual se crea una instancia, en nuestro caso CuentaTest, al final del proceso se destruye
 * el motor de JUnit es quien maneja esta instancia, crea cuando se ejecuta el test.
 * HOOKS o eventos, nos permite ejecutar ciertos metodos en algun momento del ciclo de vida
 * NO CREAR DEPENDENCIAS entre TEST, solo ordenarlo para mejorar la visualizacion
 * ANOTACIONES DEL CICLO DE VIDA
 * @BeforeAll antes de ejecutar TODOS los test
 * @BeforeEach antes de ejecutar x test. Cada ejecucion de test tiene un beforeeach propio, limpio que no tiene que ver
 * con otras instancias, aqui podemos inicializar variables
 * @AfterEach luego de ejecutar dicho test
 * @AfterAll luego de ejecutar TODOS los test
 */

class CuentaTest {

    @Test
    @DisplayName("Probando nombre de la cuenta")
    //indicarle al runner que lo tiene que ejecutar
    void testNombreCuenta() {
        Cuenta cuenta = new Cuenta();
        cuenta.setPersona("Andres");
        String esperado = "Andres";
        String real = cuenta.getPersona();
        // si el mensaje lo declaramos en una funcion lambda no se carga en memoria ni se crea una instancia en caso
        // de no arrojarse el error.
        assertEquals(esperado, real, () -> "Se esperaba " + esperado + " pero nos llega " + real);
    }

    @Test
    void testNombreCuentaII() {
        Cuenta cuenta = new Cuenta("Cecilia", new BigDecimal("100.12345"));
        String esperado = "Cecilia";
        String real = cuenta.getPersona();
        assertEquals(esperado, real);
        //es igual que el assertEquals de arriba y es lo que el IDE te aconseja
        assertTrue(real.equals("Cecilia"), "El nombre de la cuenta no es el esperado");
    }

    @Test
    void testSaldoCuenta() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
        //validamos que el saldo no sea nulo
        // le agreamos un mensaje propio mas descriptivo
        assertNotNull(cuenta.getSaldo(), "La cuenta no puede ser nula");

        //convertimos el BigDecimal a double
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue(), "Esperado deber ser igual al real/actual");

        // para asegurarnos que el saldo nunca es negativo
        // No se puede comparar con > < directo porque no es un tipo numerico primitivo sino un bigdecimal
        // para esto usamos el compareTo que devuelve -1, 0 o 1, de las siguientes maneras:
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertFalse(cuenta.getSaldo().compareTo(new BigDecimal("0")) < 0);

        //con true, y dandole la vuelta la logica
        assertTrue(cuenta.getSaldo().compareTo(new BigDecimal("0")) > 0);
    }

    //lo deshabilitamos porque fue pensado antes de sobreescribir el equals
    @Disabled
    @Test
    void testReferenciaCuenta() {
        Cuenta cuentaGraciela = new Cuenta("Graciela", new BigDecimal("999"));
        Cuenta cuentaGraciela2 = new Cuenta("Graciela", new BigDecimal("999"));
        // Es correcto porque son dos instancias/ objetos diferentes aunque tengan los mismo valores
        assertNotEquals(cuentaGraciela2, cuentaGraciela);
        // Al imprimir te indica que son dos instancias diferentes
        System.out.println(cuentaGraciela);
        System.out.println(cuentaGraciela2);
        //esta funciona
    }

    @Test
    @DisplayName("Testeando que dos instancias de cuenta sean iguales, por valor 🤣")
    void testPorValorCuenta() {
        Cuenta cuentaGraciela = new Cuenta("Graciela", new BigDecimal("999"));
        Cuenta cuentaGraciela2 = new Cuenta("Graciela", new BigDecimal("999"));
        // si cambia la regla de negocio y queremos que compare los valores y no por la referencia en memoria
        // vamos a sobreescribir el metodo equals en la clase Cuenta
        assertEquals(cuentaGraciela2, cuentaGraciela);
        System.out.println(cuentaGraciela);
        System.out.println(cuentaGraciela2);
    }

    @Test
    void testDebitoCuenta() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
        cuenta.debito(new BigDecimal("100"));

        //valido que no sea nulo
        assertNotNull(cuenta.getSaldo());
        //intvalue nos devuelve solo la parte entera y a eso lo comparamos con 900,
        assertEquals(900, cuenta.getSaldo().intValue());

        // el toPlainString
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testCreditoCuenta() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
        cuenta.credito(new BigDecimal("112"));

        //valido que no sea nulo
        assertNotNull(cuenta.getSaldo());

        //intvalue nos devuelve solo la parte entera y a eso lo comparamos con 900,
        assertEquals(1112, cuenta.getSaldo().intValue());

        // el toPlainString
        assertEquals("1112.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testDineroInsuficiente() {
        // REPRODUCIMOS EL ERROR
        //1. evaluamos el modelo de excpecion y lo capturamos
        Cuenta cuentaCeci = new Cuenta("Cecilia", new BigDecimal("1000.12345"));
        // para capturar la excepcion que arroja la funcion debito cuando intento debitar mas de lo que tengo en la cuenta
        // assertThrows para el manejo de excpeciones
        // el primer parametro es la excepcion que se arroja, el segundo es una funcion lambda donde invocamos
        // el metodo que supuestamente lanza la excepcion en cierto escenario
        // devuelve el objeto excepcion que envia
        Exception exc = assertThrows(DineroInsuficienteException.class, () -> {
            cuentaCeci.debito(new BigDecimal("1500"));
        });
        //2. Comparamos el mensaje de la excepcion con el esperado
        String actual = exc.getMessage();
        String esperado = "Dinero insuficiente";
        assertEquals(esperado, actual);
    }

    @Test
    void testTransferirDineroCuentas() {
        Cuenta cuentaGraciela = new Cuenta("Graciela", new BigDecimal("2500"));
        Cuenta cuentaCecilia = new Cuenta("Cecilia", new BigDecimal("1250"));
        Banco credicoop = new Banco();
        credicoop.setNombre("Banco Credicoop");
        credicoop.transferir(cuentaGraciela, cuentaCecilia, new BigDecimal("925"));
        //recordar que para comparar bigdecimal podemos pasarlo
        assertEquals("1575", cuentaGraciela.getSaldo().toPlainString());
        assertEquals("2175", cuentaCecilia.getSaldo().toPlainString());
    }

    @Test
    void testRelacionBancoCuentas() {
        Cuenta cuentaGraciela = new Cuenta("Graciela", new BigDecimal("2500"));
        Cuenta cuentaCecilia = new Cuenta("Cecilia", new BigDecimal("1250"));

        Banco credicoop = new Banco();
        credicoop.addCuenta(cuentaCecilia);
        credicoop.addCuenta(cuentaGraciela);
        credicoop.setNombre("Banco Credicoop");

        //testea la relacion del banco con las cuentas
        credicoop.transferir(cuentaGraciela, cuentaCecilia, new BigDecimal("925"));

        assertEquals("1575", cuentaGraciela.getSaldo().toPlainString());
        assertEquals("2175", cuentaCecilia.getSaldo().toPlainString());
        assertEquals(2, credicoop.getCuentas().size());

        //testeamos al relacion de la cuenta con el banco
        assertEquals("Banco Credicoop", cuentaCecilia.getBanco().getNombre());
        // buscamos la cuenta desde las cuentas del bando
        assertEquals("Cecilia", credicoop.getCuentas().stream()
                .filter(cta -> cta.getPersona().equals("Cecilia"))
                .findFirst()
                .get().getPersona());
    }

    @Test
    void testRelacionBancoCuentasII() {
        Cuenta cuentaGraciela = new Cuenta("Graciela", new BigDecimal("2500"));
        Cuenta cuentaCecilia = new Cuenta("Cecilia", new BigDecimal("1250"));

        Banco credicoop = new Banco();
        credicoop.addCuenta(cuentaCecilia);
        credicoop.addCuenta(cuentaGraciela);
        credicoop.setNombre("Banco Credicoop");
        // probamos el banco con una cuenta en cuestion
        assertTrue(credicoop.getCuentas().stream()
                .filter(cta -> cta.getPersona().equals("Cecilia"))
                .findFirst()
                .isPresent());

        assertTrue(credicoop.getCuentas().stream()
                .anyMatch(cta -> cta.getPersona().equals("Graciela")));

        assertFalse(credicoop.getCuentas().stream()
                .anyMatch(cta -> cta.getPersona().equals("John Doe")));
    }

    @Test
    void testRastreoTodas() {
        Cuenta cuentaGraciela = new Cuenta("Graciela", new BigDecimal("2500"));
        Cuenta cuentaCecilia = new Cuenta("Cecilia", new BigDecimal("1250"));

        Banco credicoop = new Banco();
        credicoop.addCuenta(cuentaCecilia);
        credicoop.addCuenta(cuentaGraciela);
        credicoop.setNombre("Banco Credicoop");

        credicoop.transferir(cuentaGraciela, cuentaCecilia, new BigDecimal("925"));

        //usamos el assertAll para poder evaluar y rastrear TODOS los assert de este test
        //en cada lambda tenes que tener una assertions, si tiene varias lineas usar {};
        //el tercer parámetro que se le pasa es el mensaje de error personalizado
        assertAll(
                () -> assertEquals("1575", cuentaGraciela.getSaldo().toPlainString(), () -> "ERROR"),
                () -> assertEquals("2175", cuentaCecilia.getSaldo().toPlainString()),
                () -> assertEquals(2, credicoop.getCuentas().size(), () -> "Espera 2 cuentas"),
                () -> assertEquals("Banco Credicoop", cuentaCecilia.getBanco().getNombre()),
                () -> {
                    assertEquals("Cecilia", credicoop.getCuentas().stream()
                            .filter(cta -> cta.getPersona().equals("Cecilia"))
                            .findFirst()
                            .get().getPersona());
                },
                () -> assertTrue(credicoop.getCuentas().stream()
                            .anyMatch(cta -> cta.getPersona().equals("Cecilia"))));
    }
}