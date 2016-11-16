package services.irsystem.importer;

import services.irsystem.model.InformationElement;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class FileNameAsIndexImporter extends Importer {

    public Collection<InformationElement> importQueries() {
        return importElements(DEFAULT_QUERIES_DIRECTORY);
    }

    public Collection<InformationElement> importDocuments() {
        return importElements(DEFAULT_DOCUMENTS_DIRECTORY);
    }

    private Collection<InformationElement> importElements(String folderName){

        File folder = new File(folderName);
        File[] listOfFiles = folder.listFiles();
        Collection<InformationElement> elements = new ArrayList<InformationElement>();

        if(listOfFiles == null){
            throw new RuntimeException("Wrong folder");
        }

        for(File file : listOfFiles){
            if(file.isFile()){
                String filename = file.getName();
                InformationElement element = new InformationElement(filename, readFile(folderName + "\\" + filename));
                elements.add(element);
            }
        }

        return  elements;
    }
}
