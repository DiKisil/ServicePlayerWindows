package ua.example.json;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
 
import org.json.JSONException;
import org.json.JSONObject;

 
import android.util.Log;
 
public class JSONParser {
 
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    
    // constructor
    public JSONParser() {
 
    }
 
    public JSONObject getJSONFromUrl(String file) {
 
       
    			try
    			{
    			    // Open an input stream
    			    is = new FileInputStream (file);	

    			}
    			// Catches any error conditions
    			catch (IOException e)
    			{
    				Log.e("JSON Parser open file", "Error file open :" + e.toString());
    			}
 
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } 
        catch (Exception e)
        {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
 
        // try parse the string to a JSON object
     
        try {
			jObj = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1));
		} 
        catch (JSONException e) {
			e.printStackTrace();
		}
        // return JSON String
       return jObj;
     
    }
}