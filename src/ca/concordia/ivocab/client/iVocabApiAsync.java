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
package ca.concordia.ivocab.client;

import java.util.ArrayList;

import ca.concordia.ivocab.shared.userprofile;
import ca.concordia.ivocab.shared.vocab;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Asynchronous version of iVocab API.
 */
public interface iVocabApiAsync { 
 void storeProfile(userprofile user, AsyncCallback<Boolean> callBack);
   void GetSynonymsFromWordnet(String word, AsyncCallback<ArrayList<String>> callBack);

void GetXMLSynonymsFromWordnet(String word, AsyncCallback<String> callback);

void verifyprofile(userprofile user, AsyncCallback<ArrayList<vocab>> callback);

void storeWords(userprofile user, vocab word, AsyncCallback<Void> callback);
}
