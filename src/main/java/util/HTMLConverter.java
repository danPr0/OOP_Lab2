package util;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URL;

public class HTMLConverter {
    public static void convertXmlToHtml(String xmlFilename, String xslFilename, String htmlFilename) throws IOException, TransformerException, ParserConfigurationException, SAXException {
        Document xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new FileInputStream(xmlFilename));
        Source styleSource = new StreamSource(new FileInputStream(xslFilename));
        Transformer transformer = TransformerFactory.newInstance().newTransformer(styleSource);

        StringWriter stringWriter = new StringWriter();
        transformer.transform(new DOMSource(xmlDocument), new StreamResult(stringWriter));

        File file = new File(htmlFilename);
        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(stringWriter.toString());
        bw.close();
    }
}
