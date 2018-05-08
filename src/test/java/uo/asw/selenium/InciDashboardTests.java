package uo.asw.selenium;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import inciDashboard.uo.asw.InciDashboardApplication;
import uo.asw.selenium.pageobjects.PO_AdminView;
import uo.asw.selenium.pageobjects.PO_LoginView;
import uo.asw.selenium.pageobjects.PO_NavView;

//Ordenamos las pruebas por el nombre del método
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
		InciDashboardApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
public class InciDashboardTests {
//	static String PathFirefox = "E:\\USER\\Desktop\\Firefox46.win\\FirefoxPortable.exe";
	private static final Logger LOGGER = Logger.getLogger(InciDashboardTests.class);
	
	@Value("${local.server.port:8090}")
	private int port;

//	static WebDriver driver = getDriver(PathFirefox);
	static WebDriver driver = new HtmlUnitDriver();
	static String URL = "http://localhost:8081";
	
	// Antes de cada prueba se navega al URL home de la aplicaciónn
	@Before
	public void setUp() {
		URL = "http://localhost:" + port;
		LOGGER.debug(URL);
		driver.navigate().to(URL);
	}
	
	public static WebDriver getDriver(String PathFirefox) {
		// Firefox (Versión 46.0) sin geckodriver para Selenium 2.x.
		System.setProperty("webdriver.firefox.bin", PathFirefox);
		WebDriver driver = new FirefoxDriver();
		return driver;
	}

	// Después de cada prueba se borran las cookies del navegador y se cierra la
	// sesión
	@After
	public void tearDown() {
		PO_NavView.logout(driver);
		driver.manage().deleteAllCookies();
	}

	// Antes de la primera prueba
	@BeforeClass
	static public void begin() {
	}

	// Al finalizar la última prueba
	@AfterClass
	static public void end() {
		// Cerramos el navegador al finalizar las pruebas
		driver.quit();
	}

	/**
	 * Comprobamos que al iniciar la aplicación entramos al login
	 */
	@Test
	public void P01_Inicio() {
		PO_LoginView.checkElement(driver, "id", "username");
	}

	/**
	 * Comprobamos que al introducir datos incorrectos no se realiza el login
	 */
	@Test
	public void P02_IncorrectLogin() {
		// Comprobamos que estamos en el Login
		PO_LoginView.checkElement(driver, "id", "username");
		// Usuario incorrecto, contraseña correcta
		PO_LoginView.fillForm(driver, "Paco", "123456");
		// Comprobamos que permanecemos en el Login
		PO_LoginView.checkElement(driver, "id", "username");
		// Usuario correcto, contraseña incorrecta
		PO_LoginView.fillForm(driver, "Id5", "1234567");
		// Comprobamos que permanecemos en el Login
		PO_LoginView.checkElement(driver, "id", "username");
	}
	
	/**
	 * Comprobamos que el administrador puede acceder de manera correcta
	 * al cuadro de mando
	 */
	@Test
	public void P03_CorrectLoginAdmin() {
		// Comprobamos que estamos en el Login
		PO_LoginView.checkElement(driver, "id", "username");
		// Usuario correcto, contraseña correcta
		PO_LoginView.fillForm(driver, "Id4", "123456");
		//Comprobamos que accedemos a la vista de administrador: panelUmbrales
		PO_LoginView.checkElement(driver, "id", "panelUmbrales");
	}
	
	/**
	 * Comprobamos que el administrador puede acceder de manera correcta
	 * al cuadro de mando
	 */
	@Test
	public void P04_ChangePropertiesAdmin() {
		// Comprobamos que estamos en el Login
		PO_LoginView.checkElement(driver, "id", "username");
		// Usuario correcto, contraseña correcta
		PO_LoginView.fillForm(driver, "Id4", "123456");
		//Comprobamos que accedemos a la vista de administrador: panelUmbrales
		PO_LoginView.checkElement(driver, "id", "panelUmbrales");
		//Realizamos los cambios del formulario.
		PO_AdminView.fillForm(driver, "100", "5", "HUMEDAD");
		//Comprobamos que los valores son correctos
		Map<String, Double> valores = PO_AdminView.getValues(driver, "HUMEDAD");
		assertEquals(100, valores.get("maximo"), 0.1);
		assertEquals(5, valores.get("minimo"), 0.1);
	}
	
	/**
	 * Comprobamos que el administrador puede acceder de manera correcta
	 * al cuadro de mando
	 */
	@Test
	public void P05_CorrectLoginOperario() {
		// Comprobamos que estamos en el Login
		PO_LoginView.checkElement(driver, "id", "username");
		// Usuario correcto, contraseña correcta
		PO_LoginView.fillForm(driver, "Id1", "123456");
		//Comprobamos que accedemos a la vista de administrador: panelUmbrales
		PO_LoginView.checkElement(driver, "id", "panelLista");
	}

	/**
	 * Comprobamos que al introducir datos correctos se realiza el login y se
	 * redirige a la página adecuada.
	 */
	@Test
	public void P06_CorrectLoginAnalista() {
		// Comprobamos que estamos en el Login
		PO_LoginView.checkElement(driver, "id", "username");
		// Usuario correcto, contraseña correcta
		PO_LoginView.fillForm(driver, "Id5", "123456");
//		// Aumentamos el tiempo de espera tolerable
//		PO_LoginView.setTimeout(30);
//		// Comprobamos que accedemos a la página home
//		PO_LoginView.checkElement(driver, "id", "page-top");
//		// Disminuimos el tiempo de espera tolerable
//		PO_LoginView.setTimeout(2);
	}

}
