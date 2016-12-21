package services.irsystem.importer;

import services.irsystem.model.InformationElement;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class TrecFilesImporter extends Importer {

    public Collection<InformationElement> importQueries() {

        return importQueries("data/irg_queries.trec");
    }

    public Collection<InformationElement> importQueries(String filename) {

        return importElements(filename);
    }

    public Collection<InformationElement> importDocuments() {
        return importDocuments("data/irg_collection.trec");
    }

    public Collection<InformationElement> importDocuments(String filename) {
        return importElements(filename);
    }

    private Collection<InformationElement> importElements(String filename){

        Collection<InformationElement> elements = new ArrayList<InformationElement>();

        try {

            File file = new File(filename);
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            if (doc.hasChildNodes()) {
                NodeList nodeList = doc.getElementsByTagName("DOC");
                for(int i = 0; i < nodeList.getLength(); i++){
                    Node node = nodeList.item(i);
                    InformationElement e = importSingleElement(node);
                    if ( e != null ) {
                        elements.add(e);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return elements;
    }

    private InformationElement importSingleElement(Node node){

        InformationElement element = null;
        if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName() == "DOC" && node.hasChildNodes()) {
            NodeList properties = node.getChildNodes();

            String id = properties.item(1).getFirstChild().getNodeValue();
            String text = properties.item(3).getLastChild().getNodeValue();

            element = new InformationElement(id, text);
        }

        return element;
    }


}
