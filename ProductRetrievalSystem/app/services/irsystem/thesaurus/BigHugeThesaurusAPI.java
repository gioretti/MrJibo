package services.irsystem.thesaurus;


import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;

public class BigHugeThesaurusAPI {

    private final String version = "2";
    private final String format = "json";
    private final String link = "http://words.bighugelabs.com/api/"+ version;
    private String apiKey = "";

    public BigHugeThesaurusAPI() {
        importKey();
    }

    public String[] getSynonyms(String word) {

        String result = getRequest(link +  "/" + apiKey + "/" + word + "/" + format);
        return parser(result);
    }

    private String[] parser(String jsonAsString) {

        JsonNode jsonObject = Json.parse(jsonAsString);
        JsonNode nodeNoun = jsonObject.get("noun");
        if (nodeNoun == null){
            return null;
        }
        JsonNode nodeSyn = nodeNoun.get("syn");
        if(nodeSyn == null){
            return null;
        }

        String[] synonyms = new String[nodeSyn.size()];
        int i = 0;
        for(JsonNode node : nodeSyn){
            synonyms[i] = node.toString();
            i++;
        }
        return synonyms;
    }

    private String getRequest(String address){
        String result = "";

        try {
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode() + conn.getResponseMessage());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                result += output;
            }
            conn.disconnect();

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void importKey(){
        try {
            InputStream fis = new FileInputStream("bighugetesaurus_api_key");
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(isr);
            apiKey = br.readLine();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Add a valid key in the file 'bighugetesaurus_api_key'", e);
        } catch (IOException e) {
            throw new RuntimeException("Error reading the bighugetesaurus api key", e);
        }
    }


}
