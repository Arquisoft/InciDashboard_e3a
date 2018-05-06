package uo.asw.steps;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import uo.asw.dbManagement.model.Usuario;

public class LoginSteps {

	@Given("^an user$")
	public void an_user() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		Usuario user = new Usuario();
		user.setIdentificador("Id1");
		user.setContrasena("123");
	}

	@When("^I login with the username \"([^\"]*)\" and password \"([^\"]*)\"$")
	public void i_login_with_the_username_and_password(String arg1, String arg2) throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		
	}

	@Then("^I can access to the aplication$")
	public void i_can_access_to_the_aplication() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		throw new PendingException();
	}

}
