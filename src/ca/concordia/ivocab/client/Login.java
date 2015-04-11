package ca.concordia.ivocab.client;

import ca.concordia.ivocab.client.ControlBar.Controls;
import ca.concordia.ivocab.client.iVocab.Controller;
import ca.concordia.ivocab.shared.FieldVerifier;

import com.google.gwt.dom.client.BodyElement;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;

public class Login extends Page {
	  private final Login.Resources resources;
	  private final ButtonElement signInButton;
	  private final InputElement loginField;
	  private final DivElement loginFieldError;
	  private final InputElement passwordField;
	  private final DivElement passwordFieldError;
	  private final Controller controller;
	  private final LabelElement fieldError;
	  /**
	   * Styles for this Widget. CssResource styles are compiled, minified and
	   * injected into the compiled output for this application. Fewer round trips
	   * since everything is included in the JavaScript :)!
	   */
	  
	  public interface Resources extends ControlBar.Resources {
		  @Source("resources/signup.png")
		    ImageResource signup();
		  @Source("resources/login.css")
		    Login.Css loginCss();
		  }
	  public interface Css extends CssResource {
	    String title();
	    String field();
	    String signup();
	    String fieldGroup();
	    String label();
	    String errorlabel();
	  }
	  
	protected Login(PageTransitionPanel parent, Controls controls,
			Controller controller, Resources resources) {
			super(parent, controls, resources);
			this.controller = controller;
			this.resources = resources;
			Element container = getContentContainer();
			
			fieldError = Document.get().createLabelElement();
			
			loginField = Document.get().createElement("input").cast();
			loginFieldError = Document.get().createDivElement();
		    container.appendChild(createLabelledFieldGroup("Login", loginField, loginFieldError));
			
		    passwordField= Document.get().createPasswordInputElement().cast();
		    passwordFieldError = Document.get().createDivElement();
		    container.appendChild(createLabelledFieldGroup("Password", passwordField, passwordFieldError)); 
		    
		    signInButton = Document.get().createPushButtonElement();
		  //  signInButton.getStyle().setPropertyPx("marginLeft", 75);
		    signInButton.setInnerText("SignIn");
		    signInButton.setId("signin");
		    
		    container.appendChild(signInButton);
		    container.appendChild(fieldError);
		    
	}
	public BodyElement getBodyElement()
	{
		return Document.get().getBody();
	}
	private DivElement createLabelledFieldGroup(String labelText,
			Element field, Element errorField) {

		DivElement fieldGroup = Document.get().createDivElement();
		fieldGroup.setClassName(resources.loginCss().fieldGroup());
		fieldGroup.setId(labelText+"One");
		
		

		DivElement label = Document.get().createDivElement();
		label.setInnerText(labelText);
		
		field.setId(labelText);

		label.setClassName(resources.loginCss().label());
		field.setClassName(resources.loginCss().field());
		errorField.setClassName(resources.loginCss().errorlabel());

		fieldGroup.appendChild(label);
		fieldGroup.appendChild(field);
		fieldGroup.appendChild(errorField);

		return fieldGroup;
	}
	  public void addControlsInPage(final Controller controller)
	  {
		  Login.Css css = resources.loginCss();

		  DomUtils.addEventListener("DOMFocusOut", loginField, new EventListener() {

		      public void onBrowserEvent(Event event) {
		    	 // fieldError.setInnerHTML("focus is out");
		    	 if( !FieldVerifier.isValidLogin(loginField.getValue()))
		    	 {
		    		 loginFieldError.getStyle().setProperty("display", "");
		    		 loginFieldError.setInnerHTML("login is in a wrong format");
		    	 }
		    	 else
		    		 loginFieldError.getStyle().setProperty("display", "none");
		   	      }
		      });

		 // signInButton.setClassName(css.garbage());
		  DomUtils.addEventListener("click", signInButton, new EventListener() {

		      public void onBrowserEvent(Event event) {
		    	 if( !FieldVerifier.isValidLogin(loginField.getValue()))
		    	 {
		    		 fieldError.setInnerHTML("login is in a wrong format");
		    	 }
		    	 else
		    	 {
		    		 controller.login(loginField.getValue(), passwordField.getValue());
		    	 }
		      }
		      });
	  }
	  /**
	   * Creates the controls to be added to a login.
	   */
	  public static Controls createControls(final Controller controller,
	      Login.Resources resources) {
	    Login.Css css = resources.loginCss();
	    Controls controls = new Controls(resources);
	    controls.addControl(css.signup(), new EventListener() {
	      public void onBrowserEvent(Event event) {
	        controller.GoToSignUp();	 
	      }
	    });
	    return controls;
	  }
	  
	  public void ShowLoginError(String error)
	  {
		  passwordFieldError.getStyle().setProperty("display", "");
		  passwordFieldError.setInnerHTML(error);
	  }
	  
	  public void reset()
	  {
		  loginField.setValue("");
		  passwordField.setValue("");
		  passwordFieldError.getStyle().setProperty("display", "none");
		  loginFieldError.getStyle().setProperty("display", "none");
		  
	  }
}
