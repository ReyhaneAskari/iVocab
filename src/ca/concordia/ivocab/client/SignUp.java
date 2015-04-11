package ca.concordia.ivocab.client;
 
import ca.concordia.ivocab.client.ControlBar.Controls;
import ca.concordia.ivocab.client.ControlBar.Resources;
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
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Label;

 
public class SignUp extends Page{
     
     private final SignUp.Resources resources;
     private final ButtonElement save;
     private final InputElement nameField;
     private final  InputElement emailField;
     private final DivElement emailFieldError;
     private final InputElement passField;
     private final InputElement confirmpassField;
     private final DivElement passwordFieldError;
      private final Controller controller;
      private final LabelElement fieldError;
      
      final Label errorLabel = new Label();
    public interface Resources extends ControlBar.Resources {
        @Source("resources/Cancel.png")
        ImageResource cancel();
        @Source("resources/signup.css")
        SignUp.Css signupCss();
      }
  public interface Css extends CssResource {
 
      String title();
        String field();
 
        String fieldGroup();
 
        String label();
 
        String cancel();
        
        String errorlabel();
 
      }
    public SignUp(PageTransitionPanel parent, Controls controls,
            Controller controller,Resources resources) {
        super(parent, controls, resources);     
        this.controller = controller;
        this.resources = resources;
        Element container = getContentContainer();
    
        fieldError = Document.get().createLabelElement();
        nameField = Document.get().createElement("input").cast();
        container.appendChild(createLabelledFieldGroup("Name:", nameField, null));
        
        emailField = Document.get().createElement("input").cast();
        emailFieldError = Document.get().createDivElement();
        container.appendChild(createLabelledFieldGroup("Email:", emailField, emailFieldError));
         
        passField= Document.get().createPasswordInputElement().cast();
        container.appendChild(createLabelledFieldGroup("Password:", passField, null));
         
        confirmpassField= Document.get().createPasswordInputElement().cast();
        passwordFieldError = Document.get().createDivElement();
        container.appendChild(createLabelledFieldGroup("Confirm:", confirmpassField, passwordFieldError));
         
        save = Document.get().createPushButtonElement();
        save.getStyle().setPropertyPx("marginLeft", 75);
        save.setInnerText("Save");
        save.setId("save");
         
        container.appendChild(save);
        container.appendChild(fieldError);
             
    }
     
    private DivElement createLabelledFieldGroup(String labelText, Element field, Element errorField) {
		  
        
	    DivElement fieldGroup = Document.get().createDivElement();
	    fieldGroup.setClassName(resources.signupCss().fieldGroup());

	    DivElement label = Document.get().createDivElement();
	    label.setInnerText(labelText);
	    
	    label.setClassName(resources.signupCss().label());
	    field.setClassName(resources.signupCss().field());
	    if(errorField != null)
	    	errorField.setClassName(resources.signupCss().errorlabel());

	    fieldGroup.appendChild(label);
	    fieldGroup.appendChild(field);
	    if(errorField != null)
	    	fieldGroup.appendChild(errorField);

	    return fieldGroup;
	  }
     public BodyElement getBodyElement()
  	{
  		return Document.get().getBody();
  	}
     
     
      public void addControlsInPage(final Controller controller)
      {
    	  DomUtils.addEventListener("DOMFocusOut", emailField, new EventListener() {

		      public void onBrowserEvent(Event event) {
		    	 if( !FieldVerifier.isValidLogin(emailField.getValue()))
		    	 {
		    		 emailFieldError.getStyle().setProperty("display", "");
		    		 emailFieldError.setInnerHTML("email is in a wrong format");
		    	 }
		    	 else
		    		 emailFieldError.getStyle().setProperty("display", "none");
		   	      }
		      });

    	  DomUtils.addEventListener("DOMFocusOut", confirmpassField, new EventListener() {

		      public void onBrowserEvent(Event event) {
		    	 if( !FieldVerifier.isIdenticalPassword(passField.getValue(), confirmpassField.getValue()))
		    	 {
		    		 passwordFieldError.getStyle().setProperty("display", "");
		    		 passwordFieldError.setInnerHTML("the confirmed password is different from the first password");
		    	 }
		    	 else
		    		 passwordFieldError.getStyle().setProperty("display", "none");
	    		 
		   	      }
		      });

    	  
          DomUtils.addEventListener("click", save, new EventListener() {
 
              public void onBrowserEvent(Event event) {
            	  if( !FieldVerifier.isValidLogin(emailField.getValue()))
     		    	 {
     		    		 fieldError.setInnerHTML("email is in a wrong format");
     		    	 }
     		    	 else
     		    	 {
     		    		controller.signup(nameField.getValue(), emailField.getValue() ,passField.getValue());
     		    	 }
            	
              }
              });
      }
     
    public static Controls createControls(final Controller controller,
              SignUp.Resources resources) {
    	    	
            SignUp.Css css = resources.signupCss();
 
            Controls controls = new Controls(resources);
             
            controls.addControl(css.cancel(), new EventListener() {
            	
            	      	      public void onBrowserEvent(Event event) {
            	      	        controller.GoToLogin();
            	      	      }
            	
            	      	    });
             
            return controls;
    	//return null;
          }
    
    public void reset()
    {
    	nameField.setValue("");
    	passField.setValue("");
    	emailField.setValue("");
    	confirmpassField.setValue("");
    	emailFieldError.getStyle().setProperty("display", "none");
    	passwordFieldError.getStyle().setProperty("display", "none");
    	fieldError.getStyle().setProperty("display", "none");
    	
    }
    
}