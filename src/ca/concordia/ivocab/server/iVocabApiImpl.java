/*
 * Copyright 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package ca.concordia.ivocab.server;

import ca.concordia.ivocab.client.iVocabApi;
import ca.concordia.ivocab.shared.userprofile;
import ca.concordia.ivocab.shared.vocab;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Implementation of iVocab API.
 */
@SuppressWarnings("serial")



public class iVocabApiImpl extends RemoteServiceServlet implements iVocabApi {
	
	public static DatastoreService ds = DatastoreServiceFactory.getDatastoreService(); 
	
  static String WordNetAddress = "http://www.stands4.com/services/v2/syno.php?uid=3815&tokenid=P2QreomlQhjhAy6f&word=";
	
  /*
   * Hanieh added 4 methods to server side
   */   
  public boolean storeProfile(userprofile user)
  {	 
	  String name = user.getUsername();
	  String password = user.getPassword();
	  String email = user.getEmail();      
	  email = email.toLowerCase();
	  Entity User = new Entity("userprofile", email); 
      User.setProperty("Name", name);
      User.setProperty("Email", email);
      User.setProperty("Password", password);
      User.setProperty("numberofwords",user.getNumberofwords());
      ds.put(User);               
      return true;
  }
 
  public void retriveProfile (String email)
  {
	  email = email.toLowerCase();
	  Entity e = null;  
	     try {
	    	 Key key = KeyFactory.createKey("userprofile", email);
			e = ds.get(key);
		} catch (EntityNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    String name=(String) e.getProperty("Name");
	    String em=(String)  e.getProperty("Email");
	    String pass=(String)  e.getProperty("Password");
	    System.out.println("retrieving profile with email address "+em+" the password is " +pass);     
  }
  
  public void storeWords (userprofile user,vocab word)
  {
    String email = user.getEmail();    
    email = email.toLowerCase();
    String wordTest= word.word(); 
    Entity userEntity = null;
    try {
   	 Key key = KeyFactory.createKey("userprofile", email);
   	userEntity = ds.get(key);
	} catch (EntityNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
    long numberOfWords = (Long) userEntity.getProperty("numberofwords");
    Entity Word= new Entity("Words");
    Word.setProperty(wordTest, word.synonyms());    
    EmbeddedEntity embeddedsyno = new EmbeddedEntity();
    Key infoKey;
    infoKey = Word.getKey();
    embeddedsyno.setKey(infoKey);
    embeddedsyno.setPropertiesFrom(Word);
    numberOfWords++;
    String propertyId = "Word"+numberOfWords;
    userEntity.setProperty(propertyId, embeddedsyno);   
    System.out.println("adding the word  "+wordTest+ " to the profile with ID "+ user.getEmail());
    user.setNumberofwords(numberOfWords);
    userEntity.setProperty("numberofwords", numberOfWords);
    ds.put(userEntity);
    
    
  }
  
  private ArrayList<vocab> retrieveVocabs(Entity user)
  {
	  ArrayList<vocab> vocabs = new ArrayList<vocab>();
	 Map<String,Object> mapofvocabs = user.getProperties();
	 Iterator<Map.Entry<String, Object>> it = mapofvocabs.entrySet().iterator();
	 Map.Entry<String, Object> entry = null;
     while (it.hasNext()) {
         entry = it.next();
         String propertyWord = entry.getKey();
         if(propertyWord.contains("Word"))
         {
        	 EmbeddedEntity wordEmentity = (EmbeddedEntity) entry.getValue();
        	 Key wordkey = wordEmentity.getKey();
        	 Entity wordEntity = new Entity(wordkey);
        	 wordEntity.setPropertiesFrom(wordEmentity);
        	 Map<String,Object> allvocabs = wordEntity.getProperties();
        	 Iterator<Map.Entry<String, Object>> it2 = allvocabs.entrySet().iterator();
        	 Map.Entry<String, Object> entry2 = null;
        	 while (it2.hasNext()) {
                 entry2 = it2.next();
                 String word = entry2.getKey();
	        	 ArrayList<String> synonyms = (ArrayList<String>) entry2.getValue();
		         vocab newWord = new vocab(word, synonyms);
		         vocabs.add(newWord);
        	 }
         }
     }
	  return vocabs;
  }
  
  @Override
  public ArrayList<vocab> verifyprofile(userprofile user) {
	  String email = user.getEmail();
	  System.out.println("Logging in the user with email "+ user.getEmail() +" and password "+user.getPassword());
	  email = email.toLowerCase();
	  Entity e = null;  
	     try {
	    	 Key key = KeyFactory.createKey("userprofile", email);
			e = ds.get(key);
		} catch (EntityNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	     String pass=(String)  e.getProperty("Password");
	     
	     if(pass.equals(user.getPassword()))
	    {
	    	ArrayList<vocab> listofvocabs = retrieveVocabs(e);
	    	System.out.println("the password is correct");
	    	return listofvocabs;
	    
	    }
	    else
	    {
	    	ArrayList<vocab> listofvocabs = new ArrayList<vocab>();
	    	vocab error = new vocab("the password is wrong", null);
	    	listofvocabs.add(error);
	    	 return listofvocabs;
	    }
	    
	  
  }
  public String GetXMLSynonymsFromWordnet(String word)
  {
	  System.out.println("received request to find the synomyms of "+word);
	  //send a request to wordnet
	  	String URLBasis = WordNetAddress+word;
	  	URL url = null;
		try {
			url = new URL(URLBasis);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) url.openConnection();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		connection.setDoOutput(true);
		try {
			connection.setRequestMethod("GET");
		} catch (ProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String termReturn = null;
		try {	
			BufferedReader reader;
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while((line = reader.readLine()) != null)
			{
				termReturn = line;	
			}
			reader.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) 
			{
				System.out.println(connection.getResponseCode());
			} 
			else 
			{
				// Server returned HTTP error code.
				System.out.println(connection.getResponseCode());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connection.disconnect();
	  
	  //parse result
	  return termReturn;
  }
  public ArrayList<String> GetSynonymsFromWordnet(String word)
  {
	  System.out.println("received request to find the synomyms of "+word);
	  ArrayList<String> synonyms = new ArrayList<String>();
	  //send a request to wordnet
	  	String URLBasis = WordNetAddress+word;
	  	URL url = null;
		try {
			url = new URL(URLBasis);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) url.openConnection();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		connection.setDoOutput(true);
		try {
			connection.setRequestMethod("GET");
		} catch (ProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String termReturn = null;
		try {	
			BufferedReader reader;
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while((line = reader.readLine()) != null)
			{
				termReturn = line;	
			}
			reader.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		synonyms = parseSearchTermResult(termReturn);
		try {
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) 
			{
				System.out.println(connection.getResponseCode());
			} 
			else 
			{
				// Server returned HTTP error code.
				System.out.println(connection.getResponseCode());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connection.disconnect();
	  
	  //parse result
	  return synonyms;
  }
  public ArrayList<String> parseSearchTermResult(String termResult) 
	{
	  ArrayList<String> synonyms = new ArrayList<String>();
	  
		if(termResult == null)
			synonyms.add("the result for the search was null!!!");
		else
		{
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = null;
			try {
				docBuilder = docBuilderFactory.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(termResult));
			Document doc = null;
			try {
				doc = docBuilder.parse(is);
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
          
			NodeList listResults = doc.getElementsByTagName("result");
	  		Node firstResult = listResults.item(0);
	  		NodeList firstResultNodes = firstResult.getChildNodes();
	  		for(int j=0; j < firstResultNodes.getLength(); j++)
	  		{
	  			if(firstResultNodes.item(j).getNodeName().equals("synonyms"))
	  			{
	  				String firstSynonyms = firstResultNodes.item(j).getTextContent();
	  				System.out.println("the synonym is "+firstSynonyms);
		  			String[] synonymList = firstSynonyms.split(",");
		  			for(int i=0; i<synonymList.length;i++)
		  				
		  				synonyms.add(synonymList[i]);
	  			}
	  		}
		}
		return synonyms;
		
	}
  


}
