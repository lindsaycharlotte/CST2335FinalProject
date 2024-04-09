package com.example.cst2335finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailsFragment extends Fragment {

    private Bundle dataFromActivity;
    private long id;
    private AppCompatActivity parentActivity;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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


        return result;

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        parentActivity = (AppCompatActivity) context;
    }

}