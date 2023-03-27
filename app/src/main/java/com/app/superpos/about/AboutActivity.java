package com.app.superpos.about;

import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.app.superpos.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
//        getSupportActionBar().setTitle(R.string.about_us);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.about_us) + "</font>"));
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradiant_flotting_btn));

    }

    //for back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
