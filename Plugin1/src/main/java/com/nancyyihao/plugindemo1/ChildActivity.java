package com.nancyyihao.plugindemo1;


import android.os.Bundle;

public class ChildActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_activity);
    }
}
