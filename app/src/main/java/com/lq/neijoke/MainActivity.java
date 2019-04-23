

package com.lq.neijoke;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lq.baselibrary.ioc.CheckNet;
import com.lq.baselibrary.ioc.OnClick;
import com.lq.baselibrary.ioc.ViewById;
import com.lq.baselibrary.ioc.ViewUtils;


public class MainActivity extends AppCompatActivity {

    @ViewById(R.id.text_tv)
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewUtils.inject(this);


        mTextView.setText("6666666");

    }

    @CheckNet
    @OnClick(R.id.text_iv)
    public void onClick(View v) {
        Toast.makeText(this, "66666666", Toast.LENGTH_SHORT).show();
    }
}
