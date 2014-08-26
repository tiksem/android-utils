package com.utilsframework.android.parsers.xml;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

public abstract class XMLParser<T> {

    private Document doc;

    private T parseResult;

    final protected Document getDoc() {
        return doc;
    }

    private Document initDomElement(String xml){
        Document doc = null;
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

            InputSource inputSource = new InputSource();
            inputSource.setCharacterStream(new StringReader(xml));
            doc = docBuilder.parse(inputSource);

        } catch (ParserConfigurationException e) {

        } catch (SAXException e) {

        } catch (IOException e) {

        }
        return doc;
    }

    protected abstract T getElements();

    final public T parse(String xml){
        doc = initDomElement(xml);
        parseResult = getElements();
        return parseResult;
    }
}
