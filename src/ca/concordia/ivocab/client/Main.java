package ca.concordia.ivocab.client;

import java.util.ArrayList;
import java.util.List;

import ca.concordia.ivocab.client.ControlBar.Controls;
import ca.concordia.ivocab.client.iVocab.Controller;
import ca.concordia.ivocab.shared.vocab;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;

public class Main extends Page {
	private final Main.Resources resources;
	private ArrayList<vocab> vocabs;
	private int current;
	private final DivElement vocabElement;
	// private final DivElement buttonElement;
	private final Controller controller;
	private final DivElement left;
	private final DivElement right;
	private final vocabDisplay vocabdisplay;
	
		public class vocabDisplay extends Widget {
		private vocab data;
		private final DivElement wordElem;
		private final ArrayList<DivElement> synonyms = new ArrayList<DivElement>();
		private final int numberofsynonyms = 20;

		public vocabDisplay(Element parentElem) {
			super(parentElem);			
			Element myElem = getElement();
			Main.Css css = resources.mainCss();
			myElem.setClassName(css.vocabRow());
			wordElem = com.google.gwt.dom.client.Document.get()
					.createDivElement();
			wordElem.setClassName(css.wordRow());
			myElem.appendChild(wordElem);
			wordElem.getStyle().setProperty("display", "none");
			for (int i = 0; i < numberofsynonyms; i++) 
			{
				DivElement newSynonym = com.google.gwt.dom.client.Document
						.get().createDivElement();
				newSynonym.setClassName(css.synRow());
				synonyms.add(newSynonym);
				myElem.appendChild(newSynonym);
				newSynonym.getStyle().setProperty("display", "none");
			}
		}
		public void init() {
			Element myElem = getElement();
			Main.Css css = resources.mainCss();
			DivElement welcome = com.google.gwt.dom.client.Document.get()
					.createDivElement();
			welcome.setClassName(css.welcome());
			welcome.setInnerText("Your pool is empty :( Start Learning new words ");
			welcome.setId("welcome");
			myElem.appendChild(welcome);
		}
		public void display(vocab data)
		{
			//Document.get().getElementById("welcome").getStyle().setProperty("display", "none");
			wordElem.setInnerText(data.word());
			wordElem.getStyle().setProperty("display", "");
			ArrayList<String> listOfSynonyms = data.synonyms();
			int newnumberofsynonyms = listOfSynonyms.size();
			
			for (int i = 0; i < numberofsynonyms; i++) {
				if(i<newnumberofsynonyms)
				{
					synonyms.get(i).getStyle().setProperty("display", "");
					synonyms.get(i).setInnerText(listOfSynonyms.get(i));
				}
				else
				{
					synonyms.get(i).getStyle().setProperty("display", "none");
				}
					
			}
			synonyms.get(0).setId("first");
		}

		public void render() {
			wordElem.setInnerText(data.word());
			ArrayList<String> listOfSynonyms = data.synonyms();

			for (int i = 0; i < synonyms.size(); i++) {
				synonyms.get(i).setInnerText(listOfSynonyms.get(i));
			}
		}
		public void reset()
		{
			wordElem.getStyle().setProperty("display", "none");
			for (int i = 0; i < numberofsynonyms; i++) 
			{
				synonyms.get(i).getStyle().setProperty("display", "none");
			}
		}
	}

	public interface Resources extends ControlBar.Resources {
		@Source("resources/SearchButton.png")
		ImageResource SearchButton();

		@Source("resources/leftButton.png")
		ImageResource leftButton();

		@Source("resources/RightButton.png")
		ImageResource RightButton();
		
		@Source("resources/Signout.png")
		ImageResource SignoutButton();

		@Source("resources/main.css")
		Main.Css mainCss();
	}

	public interface Css extends CssResource {

		// String next();
		// String previous();
		String fieldGroup();

		String field();

		String label();

		String search();

		String SearchButton();

		String RightButton();

		String leftButton();

		String vocabRow();

		String wordRow();

		String synRow();

		String title();
		
		String SignoutButton();
		String welcome();
		
		
	}

	public Main(PageTransitionPanel parent, Controls controls,
			Controller controller, Resources resources) {
		super(parent, controls, resources);
		this.resources = resources;
		this.controller = controller;
		Element container = getContentContainer();
		Main.Css css = resources.mainCss();
		
		left = Document.get().createDivElement();
		left.setClassName(css.leftButton());
		right = Document.get().createDivElement();
		right.setClassName(css.RightButton());
		container.appendChild(left);
		container.appendChild(right);
		vocabElement = com.google.gwt.dom.client.Document.get()
				.createDivElement();
		container.appendChild(vocabElement);
		
		vocabdisplay = new vocabDisplay(vocabElement);
		//vocabdisplay.init();
	}

	public void addControlsInPage(final Controller controller) {
		DomUtils.addEventListener("click", right, new EventListener() {

			public void onBrowserEvent(Event event) {
				controller.nextvocab();
			}
		});
		
		DomUtils.addEventListener("click", left, new EventListener() {

			public void onBrowserEvent(Event event) {
				controller.previousvocab();
			}
		});
	}

	public static Controls createControls(final Controller controller,
			Main.Resources resources) {
		Main.Css css = resources.mainCss();

		Controls controls = new Controls(resources);
		controls.addSearchField(css.search(), new EventListener() {

			public void onBrowserEvent(Event event) {
			}
		});
		controls.addControl(css.SearchButton(), new EventListener() {

			public void onBrowserEvent(Event event) {
				
				InputElement searchElement =(InputElement) Document.get().getElementById("searchword");
				String word = searchElement.getValue();
				controller.GoToSynonyms(word);
			}

		});

		controls.addControl(css.SignoutButton(), new EventListener() {

			public void onBrowserEvent(Event event) {
					controller.signout();
				}

		});

		
		return controls;
	}

	public void showVocab(vocab currentVocab)
	{
		vocabdisplay.display(currentVocab);
	}
	public void resetSearch()
	{
		InputElement searchElement =(InputElement) Document.get().getElementById("searchword");
		searchElement.setValue("");
	}
	public void reset()
	{
		InputElement searchElement =(InputElement) Document.get().getElementById("searchword");
		searchElement.setValue("");
		vocabdisplay.reset();
		//vocabdisplay.init();
	}


}