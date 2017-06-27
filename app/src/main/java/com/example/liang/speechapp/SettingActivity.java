package com.example.liang.speechapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class SettingActivity extends Activity {

    private static String mEngineType = "cloud";
    private static int mFontSize = 20;

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        setFinishOnTouchOutside(false);

        Button okButton = (Button)findViewById(R.id.setting_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.setting_file), MODE_PRIVATE).edit();
                editor.putString("engine_type", mEngineType);
                editor.putInt("font_size", mFontSize);
                editor.apply();
                setResult(RESULT_OK);
                finish();
            }
        });
        Button cancelButton = (Button)findViewById(R.id.setting_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SharedPreferences pref = getSharedPreferences(getString(R.string.setting_file), MODE_PRIVATE);
        // Engine Type
        RadioGroup engineGroup = (RadioGroup)findViewById(R.id.engine_group);
        mEngineType = pref.getString("engine_type", mEngineType);
        switch (mEngineType){
            case "local":
                engineGroup.check(R.id.local);
                break;
            case "cloud":
                engineGroup.check(R.id.cloud);
                break;
            case "mixed":
                engineGroup.check(R.id.mixture);
                break;
            default:
                break;
        }
        engineGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.local:
                        mEngineType = "local";
                        break;
                    case R.id.cloud:
                        mEngineType = "cloud";
                        break;
                    case R.id.mixture:
                        mEngineType = "mixed";
                        break;
                    default:
                        break;
                }
            }
        });
        // font size
        RadioGroup fontSizeGroup = (RadioGroup)findViewById(R.id.fontsize_group);
        mFontSize = pref.getInt("font_size", mFontSize);
        switch (mFontSize){
            case 20:
                fontSizeGroup.check(R.id.small_font);
                break;
            case 30:
                fontSizeGroup.check(R.id.mid_font);
                break;
            case 40:
                fontSizeGroup.check(R.id.big_font);
                break;
            default:
                break;
        }
        fontSizeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.small_font:
                        mFontSize = 20;
                        break;

                    case R.id.mid_font:
                        mFontSize = 30;
                        break;
                    case R.id.big_font:
                        mFontSize = 40;
                        break;
                    default:
                        break;
                }
            }
        });
    }

}
