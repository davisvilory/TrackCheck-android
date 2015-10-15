package beteam.viloco.trackcheck.util;

import android.location.Address;
import android.os.AsyncTask;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class GeoCoding extends AsyncTask<Void, Void, Address> {
    JSONObject jsonObj;
    private double latitude, longitude;
    HttpURLConnection connection;
    BufferedReader br;
    StringBuilder sb;
    Address mAddress;

    public GeoCoding(double lat, double lng) {
        this.latitude = lat;
        this.longitude = lng;
    }

    public Address getAddress() {
        Address address;

        try {
            String Status = jsonObj.getString("status");
            if (Status.equalsIgnoreCase("OK")) {
                address = new Address(new Locale("es", "MX"));
                JSONArray Results = jsonObj.getJSONArray("results");
                JSONObject zero = Results.getJSONObject(0);
                JSONArray address_components = zero.getJSONArray("address_components");

                for (int i = 0; i < address_components.length(); i++) {
                    JSONObject zero2 = address_components.getJSONObject(i);
                    String long_name = zero2.getString("long_name");
                    JSONArray mtypes = zero2.getJSONArray("types");
                    String Type = mtypes.getString(0);

                    if (!TextUtils.isEmpty(long_name) || !long_name.equals(null) || long_name.length() > 0 || !long_name.equals("")) {
                        if (Type.equalsIgnoreCase("street_number")) {
                            address.setFeatureName(long_name);
                        } else if (Type.equalsIgnoreCase("route")) {
                            address.setAddressLine(0, long_name);
                        } else if (Type.equalsIgnoreCase("sublocality") || Type.equalsIgnoreCase("sublocality_level_1")) {
                            address.setSubLocality(long_name);
                        } else if (Type.equalsIgnoreCase("locality")) {
                            address.setLocality(long_name);
                        } else if (Type.equalsIgnoreCase("administrative_area_level_2") || Type.equalsIgnoreCase("administrative_area_level_3")) {
                            address.setSubAdminArea(long_name);
                        } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
                            address.setAdminArea(long_name);
                        } else if (Type.equalsIgnoreCase("country")) {
                            address.setCountryName(long_name);
                        } else if (Type.equalsIgnoreCase("postal_code")) {
                            address.setPostalCode(long_name);
                        } else if (Type.equalsIgnoreCase("neighborhood")) {
                            address.setCountryCode(long_name);
                        }
                    }
                }
            } else
                address = null;
        } catch (Exception e) {
            address = null;
        }

        return address;
    }

    public void getGeoPoint() {
        try {
            longitude = ((JSONArray) jsonObj.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");
            latitude = ((JSONArray) jsonObj.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected Address doInBackground(Void... params) {
        try {
            String URL = String.format(new Locale("es", "MX"), "http://maps.googleapis.com/maps/api/geocode/json?latlng=%1$f,%2$f&sensor=true&language=" + new Locale("es", "MX").getCountry(), latitude, longitude);

            URL url = new URL(URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb = sb.append(line).append("\n");
            }

            jsonObj = new JSONObject(sb.toString());

            mAddress = getAddress();
            return mAddress;
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    protected void onPostExecute(Address aVoid) {
        super.onPostExecute(mAddress);
    }
}