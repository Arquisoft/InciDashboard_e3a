package uo.asw.selenium.pageobjects;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class PO_AdminView extends PO_NavView {

	static public void fillForm(WebDriver driver, String maximop, String minimop, String propertiep) {
		
		Select dropdown = new Select(driver.findElement(By.id("propiedadUmbral")));
		dropdown.selectByValue(propertiep);
		
		WebElement maximo = driver.findElement(By.name("valorMaximo"));
		maximo.click();
		maximo.clear();
		maximo.sendKeys(maximop);
		
		WebElement minimo = driver.findElement(By.name("valorMinimo"));
		minimo.click();
		minimo.clear();
		minimo.sendKeys(minimop);
		
		//Pulsar el boton de Alta.
		By boton = By.id("cambiar");
		driver.findElement(boton).click();
	}

	public static Map<String, Double> getValues(WebDriver driver, String propertiep) {
		Map<String, Double> valores = new HashMap<String, Double>();
		
		Select dropdown = new Select(driver.findElement(By.id("propiedadUmbral")));
		dropdown.selectByValue(propertiep);
		
		WebElement maximo = driver.findElement(By.name("valorMaximo"));
		valores.put("maximo", Double.valueOf(maximo.getAttribute("value")));
		
		WebElement minimo = driver.findElement(By.name("valorMinimo"));
		valores.put("minimo", Double.valueOf(minimo.getAttribute("value")));
		
		return valores;
	}
}
