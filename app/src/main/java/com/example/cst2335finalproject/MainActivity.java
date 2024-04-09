package com.example.cst2335finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {
    public static final String item_selected = "item";
    public static final String item_position = "position";
    public static final String item_id = "id";
    public static final String article_title = "title";
    public static final String category = "category";
    public static final String web_url = "web_url";
    ArrayList<String> articles;
    ListView list;
    EditText search;
    MyListAdapter list_adapter;
    JSONObject json;
    JSONArray article_data;
    URL url;
    Guardian guardian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        guardian = new Guardian();
        list = findViewById(R.id.list_view);
        articles = new ArrayList<>();
        list_adapter = new MyListAdapter();
        list.setAdapter(list_adapter);
        Button btn = findViewById(R.id.searchbtn);
        search = findViewById(R.id.search);
        btn.setOnClickListener((click) -> {
            String query = String.valueOf(search.getText());
            System.out.println("query: " + query);
            guardian = new Guardian();
            guardian.execute("https://content.guardianapis.com/search?api-key=4f732a4a-b27e-4ac7-9350-e9d0b11dd949&q=" + query);
            articles.clear();
            Toast.makeText(MainActivity.this, getResources().getString(R.string.toast_msg), Toast.LENGTH_LONG).show();
        });
        list.setOnItemClickListener((list, item, position, id) -> {
            try {
                System.out.println("does this work...?");
                Bundle dataToPass = new Bundle();
                dataToPass.putString(item_selected, articles.get(position));
                JSONObject json = null;
                for (int i = 0; i < article_data.length(); i++) {
                    JSONObject thing = (JSONObject) article_data.get(i);
                    if (thing.get("webTitle") == articles.get(position)) {
                        dataToPass.putString(article_title, thing.get("webTitle").toString());
                        dataToPass.putString(category, thing.get("sectionName").toString());
                        dataToPass.putString(web_url, thing.get("webUrl").toString());
                    }
                }
                System.out.println("data to pass: " + dataToPass);

                Intent nextActivity = new Intent(MainActivity.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass);
                startActivity(nextActivity);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });


    }

    public class Guardian extends AsyncTask<String, Integer, String> {
        public String doInBackground(String... args) {
            String result = null;


            try {

                url = new URL(args[0]);
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

                article_data = new JSONObject(result).getJSONObject("response").getJSONArray("results");

                System.out.println("temp: " + article_data);

                String id = article_data.getString(0);
                System.out.println("id: " + id);

                System.out.println("response code: " + responseCode);

                for (int i = 0; i < 10; i++) {
                    JSONObject article = article_data.getJSONObject(i);
                    System.out.println("json object: " + article);
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
            if (newView == null) {
                newView = inflater.inflate(R.layout.row_layout, parent, false);
            }

            TextView tView = newView.findViewById(R.id.text_here);
            tView.setText(getItem(position).toString());

            return newView;
        }
    }
}

