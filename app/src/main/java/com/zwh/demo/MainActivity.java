package com.zwh.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zwh.actionsheet.ActionSheet;

public class MainActivity extends AppCompatActivity implements ActionSheet.ActionSheetListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.textBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionSheet.createBuilder(MainActivity.this)
                        .setActionItemTitles(new String[]{"照相"})
                        .setCancelableOnTouchOutside(true)
                        .setCancelButtonTitle("取消")
                        .setListener(MainActivity.this)
                        .show();
                }
        });
    }

    @Override
    public void onDismiss( boolean isCancel) {

    }

    @Override
    public void onActionButtonClick(ActionSheet actionSheet, int index) {
        Toast.makeText(MainActivity.this,index+"",Toast.LENGTH_SHORT).show();
    }
}
