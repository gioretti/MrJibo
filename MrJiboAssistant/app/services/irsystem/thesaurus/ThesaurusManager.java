package services.irsystem.thesaurus;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.util.concurrent.ConcurrentMap;

public class ThesaurusManager {

    private DB db;
    private ConcurrentMap<String, String[]> savedThesaurus;
    private BigHugeThesaurusAPI api;

    public ThesaurusManager() {
        api = new BigHugeThesaurusAPI();
    }

    public String[] getSynonyms(String word) {
        String[] synonyms =  findSavedSynonyms(word);
        if(synonyms == null){
            synonyms = api.getSynonyms(word);
            saveSynonyms(word, synonyms);
        }
        return synonyms;
    }

    private void open(){
        db = DBMaker.fileDB("zhaw.irsystem.thesaurus.db").make();
        savedThesaurus = (ConcurrentMap<String, String[]>) db.hashMap("services/irsystem/thesaurus").createOrOpen();
    }

    public void fetchSynonyms(String word) {
        String[] synonyms = api.getSynonyms(word);
        saveSynonyms(word, synonyms);
    }

    private void saveSynonyms(String key, String[] value){
        open();
        savedThesaurus.put(key, value);
        close();
    }

    private String[] findSavedSynonyms(String key){
        open();
        String[] synonyms = savedThesaurus.get(key);
        close();
        return synonyms;
    }

    private void close(){
        db.close();
    }
}
