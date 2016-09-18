package com.nancyyihao.plugindemo1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        findViewById(R.id.button1).setOnClickListener(mOnClickListener);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.button1:
                {
                    Intent intent = new Intent(MainActivity.this, ChildActivity.class);
                    startActivity(intent);
                }
                break;
            }
        }
    };
}
