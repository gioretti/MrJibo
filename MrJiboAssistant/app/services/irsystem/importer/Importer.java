package services.irsystem.importer;

import services.irsystem.model.InformationElement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

public abstract class Importer {

    protected static final String DEFAULT_QUERIES_DIRECTORY = "data/queries";
    protected static final String DEFAULT_DOCUMENTS_DIRECTORY = "data/documents";

    public abstract Collection<InformationElement> importQueries();

    public abstract Collection<InformationElement> importDocuments();


    protected String readFile(String filename){
        BufferedReader inFile;
        String line = "";
        String returnString = "";
        try{
            inFile = new BufferedReader(new FileReader(filename));
            while ( ( line=inFile.readLine() ) != null){
                returnString = returnString + " " + line;
            }
        }
        catch (IOException e){
            System.err.println("ERROR Reading the file: " + e.toString());
        }
        return returnString;
    }


}
