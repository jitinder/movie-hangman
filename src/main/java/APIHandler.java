import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class APIHandler {
    private final String END_POINT_URL = "https://api.themoviedb.org/3/discover/movie";
    private final String API_KEY = "YOUR API KEY HERE";

    public static final int RESPONSE_ID_HOLLYWOOD = 1;
    public static final int RESPONSE_ID_BOLLYWOOD = 2;

    public String getResponse(int responseID) throws IOException{
        URL url;
        if(responseID == RESPONSE_ID_HOLLYWOOD){
            url = new URL(END_POINT_URL + "?api_key=" +API_KEY+ "&region=" + "US");
        } else if(responseID == RESPONSE_ID_BOLLYWOOD){
            url = new URL(END_POINT_URL + "?api_key=" +API_KEY+ "&region=" + "IN" + "&with_original_language=" +"hi");
        } else {
            return null;
        }

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            return null;
        }
    }

    public ArrayList<String> getParsedMovieList(String response){
        if(response == null){
            return null;
        }
        ArrayList<String> toReturn = new ArrayList<String>();
        JSONObject jsonObject = new JSONObject(response);
        JSONArray resultsArray = jsonObject.getJSONArray("results");
        for(int i = 0; i < resultsArray.length(); i++){
            JSONObject movieJson = resultsArray.getJSONObject(i);
            String title = movieJson.getString("title");
            toReturn.add(title);
        }
        return toReturn;
    }
}
