package com.hm.arbitrament.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hm.arbitrament.business.apply.view.FiveAdvantageActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_five_advantage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FiveAdvantageActivity.class);
                intent.putExtra(FiveAdvantageActivity.EXTRA_KEY_IOU_ID,"123456");
                startActivity(intent);
            }
        });
    }
}
