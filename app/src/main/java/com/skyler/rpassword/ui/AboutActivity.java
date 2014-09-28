package com.skyler.rpassword.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.skyler.rpassword.R;

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView textView = (TextView) findViewById(R.id.team_member);
        String member = "<span>小组成员</span><br></br>"
                + "<span>Skyler<span><br></br>"
                + "<span>KarmenTse</span><br></br>"
                + "<span>Louiseltt</span><br></br>"
                + "<span>William</span><br></br>"
                + "<span>Dee</span>";
        textView.setText(Html.fromHtml(member));
    }
}
