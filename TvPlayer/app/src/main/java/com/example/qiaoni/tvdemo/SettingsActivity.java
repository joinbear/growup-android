package com.example.qiaoni.tvdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by qiaoni on 16/11/21.
 *
 */
public class SettingsActivity  extends Activity{
    Button setButton = null;
    EditText channelTxt = null;
    String channelId  = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setButton = (Button)findViewById(R.id.button);
        channelTxt = (EditText)findViewById(R.id.editText);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            channelId = channelTxt.getText().toString();
            FileIOHelper.putString("channelId",channelId);
            Bundle bundle = new Bundle();
            bundle.putString("channelId", channelId);
            Intent data = getIntent();
            data.putExtras(bundle);
            SettingsActivity.this.setResult(RESULT_OK, data);
            SettingsActivity.this.finish();
            }
        });
    }
}
