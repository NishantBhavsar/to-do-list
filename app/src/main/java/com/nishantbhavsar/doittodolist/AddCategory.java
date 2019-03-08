package com.nishantbhavsar.doittodolist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class AddCategory extends AppCompatActivity {

    public static final String MyCatPref = "catprefs" ;
    public static final String CATEGORIES = "categories";
    SharedPreferences sharedpreferences;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        getSupportActionBar().setTitle("New Category");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textView = (TextView) findViewById(R.id.category_text_view);

        sharedpreferences = getSharedPreferences(MyCatPref, Context.MODE_PRIVATE);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_category_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save_category) {

            String inputCategory = textView.getText().toString();
            inputCategory = inputCategory.trim();

            if(inputCategory.equals("")){
                Toast.makeText(this, "Enter category at first", Toast.LENGTH_SHORT).show();
            }
            else{
                SharedPreferences.Editor editor = sharedpreferences.edit();

                String categoriesText = sharedpreferences.getString(CATEGORIES, null);

                if(categoriesText != null){
                    List<String> categories = Arrays.asList(categoriesText.split(";"));
                    if(categories.contains(inputCategory)){
                        Toast.makeText(this, "This category already exists", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        categoriesText = categoriesText + ";" + inputCategory;
                        editor.putString(CATEGORIES,categoriesText);
                        editor.apply();
                        finish();
                    }
                }
                else {
                    categoriesText = inputCategory;
                    editor.putString(CATEGORIES,categoriesText);
                    editor.apply();
                    finish();
                }

            }
        }
        return super.onOptionsItemSelected(item);
    }

}
