package com.baytag.daniel.proyectof.apoyo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Daniel on 20/08/2017.
 */

public class Utilidades {

    public static ArrayList<String> stringToCities(String data) {
        ArrayList<String> cities = new ArrayList<>();
        try {
            if (!data.equalsIgnoreCase("")) {
                JSONArray objectArray = new JSONArray(data);
                for (int i = 0; i < objectArray.length(); i++) {
                    JSONObject object = objectArray.getJSONObject(i);
                    cities.add((String) object.get("name"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return cities;
    }

    public static HashMap<String, HashMap<String, String>> datosJson(String data) {

        HashMap<String, HashMap<String, String>> datos = new HashMap<>();
        JSONObject object = null;
        String keyPasada = "";

        try {
            if (!data.equalsIgnoreCase("")) {

                object = new JSONObject(data);

                Iterator<?> keys = object.keys();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    JSONArray actual = object.getJSONArray(key);
                    //HashMap<String, String> jsonObject = new HashMap<>();
                    for (int i = 0; i < actual.length(); i++) {
                        HashMap<String, String> jsonObject = new HashMap<>();
                        JSONObject obj = actual.getJSONObject(i);
                        Iterator<?> objKeys = obj.keys();
                        while (objKeys.hasNext()) {

                            String objkey = (String) objKeys.next();
                            if (obj.get(objkey) instanceof String) {
                                String valorAct = (String) obj.get(objkey);
                                jsonObject.put(objkey, valorAct);
                            } else {
                                Boolean boolAct = (Boolean) obj.get(objkey);
                                jsonObject.put(objkey, boolAct.toString());
                            }

                        }
                        if (!key.equals(keyPasada)) {
                            keyPasada = key;
                        } else {
                            key = key + i;
                            keyPasada = key;
                        }


                        datos.put(key, jsonObject);
                        jsonObject = null;
                    }
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datos;
    }

}
