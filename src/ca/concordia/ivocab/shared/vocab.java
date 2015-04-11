package ca.concordia.ivocab.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class vocab implements IsSerializable{
		private String word;
	   ArrayList<String> synonyms = new ArrayList<String>();
	 
	  /**
	   * Zero arg ctor to work with GWT RPC serialization.
	   */
	  public vocab() {
	    this.word = "";
	    this.synonyms = null;
	  }

	  public vocab (String word, ArrayList<String> synonyms) {
	    this.word = word;
	    this.synonyms = synonyms;
	  }

	  public String word() {
	    return word;
	  }
	  
	  public void setWord(String word)
	  {
		this.word = word;  
	  }

	  public ArrayList<String> synonyms() {
	    return synonyms;
	  }
	  
	  public void SetSynonyms(ArrayList<String> synonyms)
	  {
		  this.synonyms = synonyms;
	  }

}
