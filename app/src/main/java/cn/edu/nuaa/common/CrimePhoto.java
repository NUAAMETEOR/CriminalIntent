package cn.edu.nuaa.common;

import com.google.gson.JsonObject;

/**
 * Created by Meteor on 2018/2/27.
 */

public class CrimePhoto {
    private String filePath;
    public static final String PHOTO_PATH = "photo_path";

    public CrimePhoto(String filePath) {
        this.filePath = filePath;
    }

    public CrimePhoto(JsonObject jsonObj) {
        filePath = jsonObj.get(PHOTO_PATH).getAsString();
    }

    public JsonObject toJSON() {
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty(PHOTO_PATH, filePath);
        return jsonObject;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
