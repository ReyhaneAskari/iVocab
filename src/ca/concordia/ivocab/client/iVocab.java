package ca.concordia.ivocab.client;

import java.util.ArrayList;

import ca.concordia.ivocab.shared.userprofile;
import ca.concordia.ivocab.shared.vocab;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 *
 */
public class iVocab implements EntryPoint {

  public class Controller {
	  
	  private userprofile currentuser;
	  private ArrayList<vocab> uservocab = new ArrayList<vocab>();
	  private int currentSynonym = 0;
	  private int sizeofsynonyms;
	  private String wordToSearch;
	  
	  public void login(String useremail, String password)
	  {
		  //check if the user exists
		  final userprofile userToLogin = new userprofile("", password, useremail);
		  api.verifyprofile(userToLogin, new AsyncCallback<ArrayList<vocab>>()
				  {
			  	public void onFailure(Throwable caught) {
			  		loginPage.ShowLoginError("This account does not exist, please try to sign up first");
				}

				public void onSuccess(ArrayList<vocab> result) {
					
						if(result != null)
						{
							if(result.size() == 1)
							{
								// is it the error
								vocab firstElement = result.get(0);
								if(firstElement.word().equals("the password is wrong"))
								{
									loginPage.ShowLoginError("The password is wrong, please try again");
								}
								else
								{
									signin(userToLogin, result);
								}
							}
							else
							{
								signin(userToLogin, result);
							}
						}
						
					}
				  }
		);
		  
	  }
	  
	  public void signin(userprofile userToLogin, ArrayList<vocab> result)
	  {
		  uiPages.doPageTransition(mainPage.getPageIndex());
			loginPage.getControlBar().disableControls();
			mainPage.getControlBar().enableControls();
			currentuser = userToLogin;
			currentSynonym = 0;
			uservocab = result;
			sizeofsynonyms = uservocab.size();
			mainPage.showVocab(uservocab.get(currentSynonym));
		
	  }
	  
	  public void nextvocab()
	  {
		//  mainPage.reset();
		  currentSynonym++;
		  int newSynonymID = currentSynonym%sizeofsynonyms;
		  currentSynonym = newSynonymID;
		  mainPage.showVocab(uservocab.get(currentSynonym));
		  
	  }
	  public void previousvocab()
	  {
		  currentSynonym--;
		  int newSynonymID;
		  if(currentSynonym < 0)
			  newSynonymID = sizeofsynonyms+currentSynonym;
		  else
			  newSynonymID = currentSynonym;
		  currentSynonym = newSynonymID;
		  mainPage.showVocab(uservocab.get(currentSynonym));
	  }
	  public void ShowSynonyms(String word)
	  {
		  wordToSearch = word;
		  api.GetSynonymsFromWordnet(word,  new AsyncCallback<ArrayList<String>>() 
				  {
			  	public void onFailure(Throwable caught) {

				}

				public void onSuccess(ArrayList<String> result) {
					synonymsPage.ShowSynonyms(result);
				}
				  }
				  );
		  
	  }
	  public void SaveSynonymsForWord()
	  {
		  ArrayList synonyms = synonymsPage.getSynonymsToSave();
		  final vocab newWord = new vocab(wordToSearch, synonyms);
		  api.storeWords(currentuser, newWord, new AsyncCallback<Void>() 
				  {
			  	public void onFailure(Throwable caught) {
				}

				public void onSuccess(Void nothing) {
					uservocab.add(newWord);
					sizeofsynonyms = uservocab.size();
					  currentSynonym = sizeofsynonyms - 1;
					  uiPages.doPageTransition(mainPage.getPageIndex());
					  synonymsPage.getControlBar().disableControls();
					  mainPage.reset();
					  mainPage.getControlBar().enableControls();
					  mainPage.showVocab(uservocab.get(currentSynonym));
				}
				  }
				  );
		  
	  }
	  public void CancelWordSaving()
	  {
		  synonymsPage.reset();
		  mainPage.resetSearch();
		  uiPages.doPageTransition(mainPage.getPageIndex());
		  synonymsPage.getControlBar().disableControls();
		  mainPage.getControlBar().enableControls();
	  }
	  public void GoToSignUp()
	  {
		  //check if the user exists
		  signupPage.reset();
		  uiPages.doPageTransition(signupPage.getPageIndex());
		  loginPage.getControlBar().disableControls();
		  signupPage.getControlBar().enableControls();
	  }
	  public void GoToSynonyms(String word)
	  {
		  //check if the user exists
		  mainPage.getControlBar().disableControls();
		  synonymsPage.reset();
		  synonymsPage.feedWord(word);
		  uiPages.doPageTransition(synonymsPage.getPageIndex());
		  synonymsPage.getControlBar().enableControls();
	  }
	  public void signout()
	  {
		  //signout
		  currentuser = null;
		  currentSynonym = 0;
		  uservocab.clear();
		  sizeofsynonyms = 0;
		  loginPage.reset();
		  mainPage.reset();
		  synonymsPage.reset();
		  signupPage.getControlBar().disableControls();
		  synonymsPage.getControlBar().disableControls();
		  mainPage.getControlBar().disableControls();
		  uiPages.doPageTransition(loginPage.getPageIndex());
		  loginPage.getControlBar().enableControls();
	  }
	  public void GoToLogin()
	  {
		  //signout
		  loginPage.reset();
		  signupPage.getControlBar().disableControls();
		  uiPages.doPageTransition(loginPage.getPageIndex());
		  loginPage.getControlBar().enableControls();
	  }
//////////////////////////////////////////
/**
* Singup method is added by Hanieh for talking with server server side
*/

	  public void signup (String name, String email, String password)
	  {
		  userprofile user = new userprofile(name, password, email);
		  
		  api.storeProfile(user, new AsyncCallback<Boolean>() 
				  {
			  	public void onFailure(Throwable caught) {
				//taskList.movePendingTasksBackToCompleted();
				}

				public void onSuccess(Boolean result) {
					if (result == true) {
						loginPage.reset();
						signupPage.getControlBar().disableControls();
						  uiPages.doPageTransition(loginPage.getPageIndex());
						  loginPage.getControlBar().enableControls();
					}
				}

				  });
	  }
    
    /**
     * Transitions to the {@link TaskDetails} page.
     */
    public void goToMain() {
      uiPages.doPageTransition(synonymsPage.getPageIndex());
      loginPage.getControlBar().disableControls();
      synonymsPage.getControlBar().enableControls();
    }
    
  }

  /**
   * Our resources used in the sample.
   *
   * {@link ControlBar.Resources} is an {@link ImmutableResourceBundle} (IRB).
   * IRB allows us to have a programmatic interface with static resources used
   * in this sample, like CSS styles and Images. Images specified here (or in
   * the inheritance chain) are automatically combined into a single sprite,
   * with corresponding CSS automatically generated to display each individual
   * image piece through cropping.
   *
   */
  public interface Resources extends Login.Resources, Main.Resources, SignUp.Resources, Synonyms.Resources  {
  }

  private final iVocabApiAsync api = GWT.create(iVocabApi.class);;
  private final Resources resources = GWT.create(Resources.class);
  private Login loginPage;
  private Main mainPage;
  private SignUp signupPage;
  private Synonyms synonymsPage;
  private PageTransitionPanel uiPages;
 
  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    StyleInjector.injectAtEnd(
         resources.loginCss().getText()
         +resources.mainCss().getText()
         +resources.synonymsCss().getText()
        + resources.signupCss().getText()
        + resources.controlBarCss().getText()
        );

    uiPages = new PageTransitionPanel(Document.get().getBody());
    Controller controller = new Controller();
    
   ControlBar.Controls loginControls = Login.createControls(controller,
		   resources);
   loginPage = new Login(uiPages, loginControls, controller, resources);
   loginPage.addControlsInPage(controller);
   
   ControlBar.Controls signupControls = SignUp.createControls(controller,
	        resources);
  	signupPage = new SignUp(uiPages, signupControls, controller, resources);
  	signupPage.addControlsInPage(controller);	       	

   ControlBar.Controls synonymsControls = Synonyms.createControls(controller,
   		    resources);
   		  synonymsPage = new Synonyms(uiPages, synonymsControls, controller, resources);
   		
    ControlBar.Controls mainControls = Main.createControls(controller,
   	        resources);
      mainPage = new Main(uiPages, mainControls, controller, resources);
      mainPage.addControlsInPage(controller);

  	uiPages.doResize();

    DeferredCommand.defer(new DeferredCommand() {
      @Override
      public void onExecute() {
        uiPages.doResize();      
        loginPage.getControlBar().enableControls();
      signupPage.getControlBar().disableControls();
        mainPage.getControlBar().disableControls();
        synonymsPage.getControlBar().disableControls();

      }
    }, 50);
  }
}
