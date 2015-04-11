package ca.concordia.ivocab.client;

import java.util.ArrayList;
import java.util.List;

import ca.concordia.ivocab.client.ControlBar.Controls;
import ca.concordia.ivocab.client.iVocab.Controller;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;

public class Synonyms extends Page{
	private final Synonyms.Resources resources;
	 private final Controller controller;
	 private final List<SynonymRow> wordSynonyms = new ArrayList<SynonymRow>();
	 private final DivElement synonym;
	 
	 public class SynonymRow extends Widget {
		    private final DivElement checkMark;
		    private final String synonymWord;
		    private boolean toRecord = true;
		    
		    private final DivElement titleElem;

		    public SynonymRow(Element parentElem, String data) {
		      super(parentElem);
		      this.synonymWord = data;
		      Element myElem = getElement();
		      Synonyms.Css css = resources.synonymsCss();
		      myElem.setClassName(css.synonymRow());
		      titleElem = com.google.gwt.dom.client.Document.get().createDivElement();
		      titleElem.setClassName(css.title());
		      DivElement rightMask = com.google.gwt.dom.client.Document.get().createDivElement();
		      rightMask.setClassName(css.checkBoxContainer());
		      checkMark = com.google.gwt.dom.client.Document.get().createDivElement();
		      rightMask.appendChild(checkMark);

		      myElem.appendChild(titleElem);
		      myElem.appendChild(rightMask);

		      renderSynonym();

		      hookEventListeners();
		    }

		    public String getSynonymWord() {
		      return synonymWord;
		    }
		    public void renderSynonym() {
		      titleElem.setInnerText(synonymWord);
		      if(toRecord)
		        checkMark.setClassName(resources.synonymsCss().checked());
		      else
		    	  checkMark.setClassName(resources.synonymsCss().unChecked());
		    }
		    public void reset()
		    {
		    	toRecord = true;
		    	checkMark.setClassName(resources.synonymsCss().checked());
		    }
		    private void hookEventListeners() {

		      DomUtils.addEventListener("click", checkMark, new EventListener() {
		        public void onBrowserEvent(Event event) {
		        	toRecord = !toRecord;
		          renderSynonym();
		        }
		      });
		    }
		    public boolean isToRecord()
		    {
		    	return toRecord == true;
		    }
		  }

	 public interface Resources extends ControlBar.Resources {
		 @Source("resources/checkBox.png")
		    ImageResource checkBox();
		    @Source("resources/check.png")
		    ImageResource checkMark();
		    @Source("resources/Save.png")
		    ImageResource save();
		    @Source("resources/Signout.png")
		    ImageResource signout();
		    @Source("resources/Cancel.png")
		    ImageResource cancel();
	    @Source("resources/synonyms.css")
	    Synonyms.Css synonymsCss();
//	    
	    
	  }
  public interface Css extends CssResource {
	  String signout();
	  String save();
	  String cancel();
	  
	  String checkBoxContainer();

	    String checked();

	    String title();

	    String unChecked();

	  String synonymRow();
   	  }
	protected Synonyms(PageTransitionPanel parent, Controls controls,
			Controller controller, Resources resources) {
		super(parent, controls, resources);
		this.controller = controller;
		this.resources = resources;
		synonym = com.google.gwt.dom.client.Document.get().createDivElement();
		Element container = getContentContainer();
	    container.appendChild(synonym);
		
	}
	
	 public void ShowError()
	 {
		 
	 }
	 public void ShowSynonyms(ArrayList<String> synonyms)
	 {
		 wordSynonyms.clear();
		 for(int i=0;i<synonyms.size();i++)
		 {
			 wordSynonyms.add(addTaskToUi(synonyms.get(i)));
		 }
	 }
	public static Controls createControls(final Controller controller,
		      Synonyms.Resources resources) {
		    Synonyms.Css css = resources.synonymsCss();

		    Controls controls = new Controls(resources);
		    controls.addControl(css.signout(), new EventListener() {

			      public void onBrowserEvent(Event event) {
			        controller.signout();
			      }

			    });
		    controls.addControl(css.save(), new EventListener() {

			      public void onBrowserEvent(Event event) {
			        controller.SaveSynonymsForWord();
			      }

			    });
		    controls.addControl(css.cancel(), new EventListener() {

			      public void onBrowserEvent(Event event) {
			        controller.CancelWordSaving();
			      }

			    });
		    return controls;
		
		  }
	public ArrayList<String> getSynonymsToSave()
	{
		ArrayList<String>  synonyms = new ArrayList<String>();
    	for(int i=0; i<wordSynonyms.size(); i++)
    	{
    		if(wordSynonyms.get(i).isToRecord())
    			synonyms.add(wordSynonyms.get(i).getSynonymWord());
    	}
    	return synonyms;
	}
	public SynonymRow addTaskToUi(String word) {
	    SynonymRow row = new SynonymRow(synonym, word);
	    return row;
	  }
	
	public void feedWord(String word)
	{
		controller.ShowSynonyms(word);
	}
	public void reset()
	{
		for(int i=0; i<wordSynonyms.size(); i++)
    	{
			wordSynonyms.get(i).getElement().removeFromParent();
    	}
		wordSynonyms.clear();
	}

}
