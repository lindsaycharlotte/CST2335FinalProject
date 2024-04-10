package com.example.cst2335finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class DetailsFragment extends Fragment {

    private Bundle dataFromActivity;
    private long id;
    private AppCompatActivity parentActivity;
    SQLiteDatabase db;

    ArrayList<String> favourites = new ArrayList<>();

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MyOpener dbOpener = new MyOpener(getActivity());

        MyListAdapter list_adapter = new MyListAdapter();

        db = dbOpener.getWritableDatabase();
        dataFromActivity = getArguments();
        System.out.println("data from activity: " + dataFromActivity);
        id = dataFromActivity.getLong(MainActivity.item_id);

        View result = inflater.inflate(R.layout.fragment_details, container, false);

        TextView title = (TextView) result.findViewById(R.id.name_fill);
        title.setText(dataFromActivity.getString(MainActivity.article_title));

        TextView category = (TextView) result.findViewById(R.id.category_fill);
        category.setText(dataFromActivity.getString(MainActivity.category));

        TextView url = (TextView) result.findViewById(R.id.url_fill);
        url.setText(dataFromActivity.getString(MainActivity.web_url));

        Button btn = (Button) result.findViewById(R.id.save);

        btn.setOnClickListener( (click) -> {
            String db_title = title.getText().toString();
            String db_category = category.getText().toString();
            String db_url = url.getText().toString();

            ContentValues newRowValues = new ContentValues();

            newRowValues.put(MyOpener.COL_ARTICLE, db_title);
            System.out.println("db title: " + db_title);
            newRowValues.put(MyOpener.COL_CATEGORY, db_category);
            newRowValues.put(MyOpener.COL_URL, db_url);
            long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);

            Favourite newFav = new Favourite(db_title, db_category, db_url, newId);
            String fav_title = newFav.getArticle();
            favourites.add(fav_title);
            System.out.println("favourites: " + favourites);
            list_adapter.notifyDataSetChanged();



        });


        return result;

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        parentActivity = (AppCompatActivity) context;
    }

    private class MyListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return favourites.size();
        }

        @Override
        public Object getItem(int position) {
            return favourites.get(position);
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