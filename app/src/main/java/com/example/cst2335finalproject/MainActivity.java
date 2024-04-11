package com.example.cst2335finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String item_selected = "item";
    public static final String item_position = "position";
    public static final String item_id = "id";
    public static final String article_title = "title";
    public static final String category = "category";
    public static final String web_url = "web_url";
    public static final String activity = "activity";
    ArrayList<String> articles;
    ListView list;
    EditText search;
    MyListAdapter list_adapter;
    JSONObject json;
    JSONArray article_data;
    URL url;
    Guardian guardian;

    SharedPreferences prefs = null;

    ProgressBar prog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("MainActivity V1.0");
        prog = findViewById(R.id.prog);
        prog.setVisibility(View.GONE);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

//        guardian = new Guardian();
        list = findViewById(R.id.list_view);
        articles = new ArrayList<>();
        list_adapter = new MyListAdapter();
        list.setAdapter(list_adapter);
        Button btn = findViewById(R.id.searchbtn);
        prefs = getSharedPreferences("FileName", Context.MODE_PRIVATE);
        String savedString = prefs.getString("typed_txt", "");
        search = findViewById(R.id.search);

        // shared preferences will auto-populate the last search the user entered into the search bar
        if (!savedString.equals("")) {
            search.setText(savedString);
        }
        // when the user clicks the search button after entering a query, the search string is appended to the api query to provide custom search results
        btn.setOnClickListener((click) -> {
            String query = String.valueOf(search.getText());
            System.out.println("query: " + query);
            guardian = new Guardian();

            guardian.execute("https://content.guardianapis.com/search?api-key=4f732a4a-b27e-4ac7-9350-e9d0b11dd949&q=" + query);
            prog.setVisibility(View.VISIBLE);
            articles.clear();

        });
        // when the user selects an article from the search results, they will be redirected to the details page
        // details page contains article name, category, and web url
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
                        dataToPass.putString(activity, "main");
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

    // methods to save shared preferences (search string entered)
    protected void onPause() {
        super.onPause();
        search = findViewById(R.id.search);
        saveSharedPrefs(search.getText().toString());
    }

    private void saveSharedPrefs(String stringToSave) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("typed_txt", stringToSave);
        editor.commit();
    }

    // logic for navigation drawer
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            Intent home = new Intent(this, MainActivity.class);
            startActivity(home);
        }
        else if (id == R.id.favourites) {
            Intent favourites = new Intent(this, Favourites.class);
            startActivity(favourites);
        }
        else if (id == R.id.exit) {
            finishAffinity();
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        return false;
    }

    // logic for help menu icon
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String message = null;
        int id = item.getItemId();
        if (id == R.id.help) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Help")
                .setMessage("Enter a search term into the search bar to find articles! " +
                            "Your most recent search will appear in the search bar, thanks to SavedPreferences.")
                .create().show();
            message = "You clicked the help button!";
            Snackbar.make(list, message, Snackbar.LENGTH_LONG).show();
        }

        return true;
    }

    //inflate options menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
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

            for (int i = 0; i < 100; i++) {
                try {
                    publishProgress(i);
                    Thread.sleep(30);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        public void onProgressUpdate(Integer... args) {
            prog.setProgress(args[0]);
            prog.setVisibility(View.GONE);
            System.out.println("args: " + args[0]);
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

