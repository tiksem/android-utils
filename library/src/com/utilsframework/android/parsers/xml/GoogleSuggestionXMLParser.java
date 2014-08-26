package com.utilsframework.android.parsers.xml;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class GoogleSuggestionXMLParser extends XMLParser<ArrayList<String> > {
    @Override
    protected ArrayList<String> getElements() {
        Document doc = getDoc();

        ArrayList<String> array = new ArrayList<String>();

        NodeList nodeList = doc.getElementsByTagName("CompleteSuggestion");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node element = nodeList.item(i);

            NodeList innerFields = element.getChildNodes();
            Node suggestionChild = innerFields.item(0);

            NamedNodeMap attributes = suggestionChild.getAttributes();
            Node suggestion = attributes.getNamedItem("data");

            String suggesction = suggestion.getNodeValue();
            array.add(suggesction);
        }
        return array;
    }
}
