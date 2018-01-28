package cn.edu.nuaa.model;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by Meteor on 2018/1/27.
 */

public class CriminalIntentJSONSerializer {
    private String  fileName;
    private Context context;
    private Gson       gson       = new Gson();
    private JsonParser jsonParser = new JsonParser();

    public CriminalIntentJSONSerializer(Context context, String fileName) {
        this.fileName = fileName;
        this.context = context;
    }

    public ArrayList<Crime> loadCrimeFromFile() {
        ArrayList<Crime> list = new ArrayList<Crime>();
        BufferedReader   br   = null;
        StringBuilder    sb   = new StringBuilder();
        try {
            FileInputStream fis = context.openFileInput(fileName);
            br = new BufferedReader(new InputStreamReader(fis));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (sb.length() != 0) {
            JsonArray jsonArray = jsonParser.parse(sb.toString()).getAsJsonArray();
            for (JsonElement element :
                    jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();
                list.add(gson.fromJson(jsonObject, Crime.class));
            }
        }
        return list;
//        JSONArray jsonArray = JSONArray.fromObject(sb.toString());
//        return (ArrayList<Crime>)JSONArray.toList(jsonArray,Crime.class);
    }

    public void saveCrimes(ArrayList<Crime> crimes) {

        OutputStreamWriter writer = null;
        try {
            OutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(outputStream);
            String jsonString = gson.toJson(crimes);
            writer.write(jsonString);
            Log.d("store crime", jsonString);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
