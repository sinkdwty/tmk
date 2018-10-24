package com.hjzddata.core.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlToolUtil {


    public static Document buildDoc(String strXml) {
        SAXReader reader = null;
        InputSource is = null;
        try {
            reader = new SAXReader();
            StringReader sr = new StringReader(strXml);
            is = new InputSource(sr);
            return reader.read(is);
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is = null;
            }
        }
        return null;
    }

    public static List isExistsNode(String strXml, String xmlPath) {
        Document doc = buildDoc(strXml);
        if (doc != null) {
            return doc.selectNodes(xmlPath);
        } else
            return null;
    }


    public static Map getElementAsMap(Element ele) {
        Map subMap = new HashMap();
        List list1 = ele.selectNodes("./*");
        for (int j = 0; j < list1.size(); j++) {
            Element e1 = (Element) list1.get(j);
            subMap.put(e1.getName(), null == e1.getText() ? "" : e1.getText());
        }
        return subMap;
    }
}
