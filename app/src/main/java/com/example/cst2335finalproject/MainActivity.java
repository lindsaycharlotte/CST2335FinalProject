package com.example.cst2335finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> articles;
    ListView list;
    MyListAdapter list_adapter;
    JSONObject json;
    JSONArray article_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.list_view);
        articles = new ArrayList<>();
        list_adapter = new MyListAdapter();
        list.setAdapter(list_adapter);
        Guardian guardian = new Guardian();
        guardian.execute("https://content.guardianapis.com/search?api-key=4f732a4a-b27e-4ac7-9350-e9d0b11dd949");


    }

    public class Guardian extends AsyncTask<String, Integer, String> {
        public String doInBackground(String... args) {
            String result = null;

            try {
                URL url = new URL(args[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();

                InputStream resp = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(resp, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
//                    System.out.println("line: " + line);
                }
                result = sb.toString();

                JSONArray article_data = new JSONObject(result).getJSONObject("response").getJSONArray("results");

                System.out.println("temp: " + article_data);

                System.out.println("response code: " + responseCode);

                for (int i = 0; i < 10; i++) {
                    JSONObject article = article_data.getJSONObject(i);
                    articles.add(article.getString("webTitle"));

                }
                System.out.println("results: " + articles);
            }

            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public void onProgressUpdate(Integer... args) {

        }

        public void onPostExecute(String fromDoInBackground) {
            list_adapter.notifyDataSetChanged();
        }
    }

    private class MyListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return articles.size();
        }

        @Override
        public Object getItem(int position) {
            return articles.get(position);
        }

        @Override
        public long getItemId(int position) {
            return (long) position;
        }

        @Override
        public View getView(int position, View old, ViewGroup parent) {
            View newView = old;

            LayoutInflater inflater = getLayoutInflater();
            System.out.println("got here");
            if (newView == null) {
                newView = inflater.inflate(R.layout.row_layout, parent, false);
            }

            TextView tView = newView.findViewById(R.id.text_here);
            tView.setText(getItem(position).toString());

            return newView;
        }
    }
}

