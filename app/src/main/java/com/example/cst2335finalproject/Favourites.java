package com.example.cst2335finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import static android.database.DatabaseUtils.dumpCursorToString;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;

public class Favourites extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    ArrayList<Favourite> favourites = new ArrayList<>();
    MyListAdapter list_adapter;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        loadDataFromDatabase();
        list_adapter = new MyListAdapter();
        ListView list_view = findViewById(R.id.list_view);
        list_view.setAdapter(list_adapter);
        list_view.setOnItemLongClickListener((p, b, pos, id) -> {
            Favourite selectedFavourite = favourites.get(pos);

            View fav_view = getLayoutInflater().inflate(R.layout.fav_row, null);

            TextView row_article = fav_view.findViewById(R.id.row_article);
            TextView row_id = fav_view.findViewById(R.id.row_id);

            row_article.setText(selectedFavourite.getArticle());
            row_id.setText("id: " + selectedFavourite.getId());
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            alertDialogBuilder.setTitle("Do you want to delete this item from your favourites?")
                    .setMessage("The selected row is: " + pos)
                    .setPositiveButton("Yes", (click, arg) -> {
                        deleteFavourite(selectedFavourite);
                        favourites.remove(pos);
                        list_adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", (click, arg) -> {})
                    .create().show();
            return true;
        });

    }

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

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String message = null;
        int id = item.getItemId();
        if (id == R.id.help) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Help")
                    .setMessage("Click a saved article to view details. " +
                                "To delete a saved article, click and hold.")
                    .create().show();
            message = "You clicked the help button!";
        }

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        return true;
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    private void loadDataFromDatabase() {
        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase();
        list_adapter = new MyListAdapter();
        ListView list_view = findViewById(R.id.list_view);
        list_view.setAdapter(list_adapter);

        String[] columns = {MyOpener.COL_ID, MyOpener.COL_ARTICLE, MyOpener.COL_CATEGORY, MyOpener.COL_URL};

        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        int articleColumnIndex = results.getColumnIndex(MyOpener.COL_ARTICLE);
        int categoryColumnIndex = results.getColumnIndex(MyOpener.COL_CATEGORY);
        int urlColumnIndex = results.getColumnIndex(MyOpener.COL_URL);
        int idColumnIndex = results.getColumnIndex(MyOpener.COL_ID);

        while (results.moveToNext()) {
            String article = results.getString(articleColumnIndex);
            String category = results.getString(categoryColumnIndex);
            String url = results.getString(urlColumnIndex);
            long id = results.getLong(idColumnIndex);

            favourites.add(new Favourite(article, category, url, id));
            System.out.println("favourites: " + favourites);
            list_adapter.notifyDataSetChanged();

        }
        printCursor(results);
    }

    protected void deleteFavourite(Favourite f) {
        db.delete(MyOpener.TABLE_NAME, MyOpener.COL_ID + "= ?", new String[] {Long.toString(f.getId())});
        System.out.println("column id: " + f.getId());
    }

    protected void printCursor(Cursor c) {
        System.out.println("Database version number: " + db.getVersion());
        System.out.println("Number of columns in cursor: " + c.getColumnCount());
        System.out.println("Names of columns in cursor: " + Arrays.toString(c.getColumnNames()));
        System.out.println("Number of results in cursor: " + c.getCount());
        String cursor_contents = dumpCursorToString(c);
        System.out.println("Cursor contents: " + cursor_contents);
    }

    private class MyListAdapter extends BaseAdapter {
        public int getCount() {
            return favourites.size();
        }

        public Favourite getItem(int position) {
            return favourites.get(position);
        }

        public long getItemId(int position) {
            return getItem(position).getId();
        }

        public View getView(int position, View old, ViewGroup parent) {
            View newView = old;

            LayoutInflater inflater = getLayoutInflater();

            Favourite thisRow = getItem(position);

            if (newView == null) {
                newView = inflater.inflate(R.layout.row_layout, parent, false);
            }

            TextView row_task = newView.findViewById(R.id.text_here);
            row_task.setText(thisRow.getArticle());

            return newView;
        }
    }
}