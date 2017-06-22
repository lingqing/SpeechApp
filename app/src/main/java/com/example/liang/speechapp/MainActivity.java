package com.example.liang.speechapp;

import android.app.Activity;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liang.ui.DynamicWave;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.liang.speechapp.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //
    private static String TAG = MainActivity.class.getSimpleName();
    private static String SPE = "SPEECH";
    private Toast mToast;
    private boolean isNewTxt = false;
    private boolean isUserEnd = true;
    // Result Txt UI
    private List<ResultTxt> mResultTxtList = new ArrayList<>();

    private Button btnControl;
    private RecyclerView resultRecyclerView;
    private ResultTxtAdapter adapter;
    private DynamicWave wave;
    private TextView txtState;

    // Speech
    private SpeechRecognizer recognizer;
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_RIGHT_ICON);

        setContentView(R.layout.active_main);

        initLayout();
        SpeechConfig();
    }

    @Override
    protected void onDestroy() {
        isUserEnd = true;
        if(recognizer.isListening()) recognizer.stopListening();
        super.onDestroy();
    }

    private void initLayout(){
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        btnControl = (Button)findViewById(R.id.btn_control);
        resultRecyclerView = (RecyclerView)findViewById(R.id.result_bg_id);
        wave = (DynamicWave)findViewById(R.id.wave);
        txtState = (TextView)findViewById(R.id.txt_state);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        resultRecyclerView.setLayoutManager(manager);
//        mResultTxtList.add(new ResultTxt("你好"));
        adapter = new ResultTxtAdapter(mResultTxtList);
        resultRecyclerView.setAdapter(adapter);
        btnControl.setOnClickListener(this);
    }

    private void SpeechConfig(){
        SpeechUtility.createUtility(MainActivity.this, SpeechConstant.APPID +"=59310ed0");

        recognizer = SpeechRecognizer.createRecognizer(MainActivity.this, initListener);
        recognizer.setParameter(SpeechConstant.DOMAIN, "iat");
        recognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        recognizer.setParameter(SpeechConstant.ACCENT, "mandarin");
        recognizer.setParameter(SpeechConstant.RESULT_TYPE,"json");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_control:
                if(isUserEnd){
                    showTip("开始监听");
                    recognizer.startListening(recognizerListener);
                    btnControl.setText("停止");
                    btnControl.setBackgroundResource(R.drawable.shape_stop);
                    txtState.setText("听读中");
                }
                else {
                    showTip("停止监听");
                    if(recognizer.isListening()) recognizer.stopListening();
                    btnControl.setText("开始");
                    btnControl.setBackgroundResource(R.drawable.shape);
                    txtState.setText("待机");
                }
                isUserEnd = !isUserEnd;
//                mResultTxtList.add(new ResultTxt("你好"));
//                adapter.notifyItemInserted(mResultTxtList.size() - 1);
//                resultRecyclerView.scrollToPosition(mResultTxtList.size() - 1);
//                timer.schedule(task, Calendar.getInstance().getTime(),100);
                break;
            default:
                break;
        }

    }

    private void showTip(final String string){
        mToast.setText(string);
        mToast.show();
    }
    private InitListener initListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(SPE, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }
        }
    };
    private RecognizerListener recognizerListener = new RecognizerListener() {
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {
//            Log.d(SPE, "VolumeChanged");
            wave.setFactor(i);
        }

        @Override
        public void onBeginOfSpeech() {
//            showTip("开始讲话");
            wave.setFactor(0);
            isNewTxt = true;
            mIatResults.clear();
            if(mResultTxtList.size()>=5){
                mResultTxtList.remove(0);
            }
//            mResultTxtList.add(new ResultTxt(""));
//            adapter.notifyItemInserted(mResultTxtList.size()-1);
//            resultRecyclerView.scrollToPosition(mResultTxtList.size()-1);
        }

        @Override
        public void onEndOfSpeech() {
//            showTip("结束讲话");
            wave.setFactor(0);
            if(!isUserEnd){
                recognizer.startListening(recognizerListener);
            }
        }

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            if(isNewTxt){
                mResultTxtList.add(new ResultTxt(""));
                adapter.notifyItemInserted(mResultTxtList.size()-1);
                resultRecyclerView.smoothScrollToPosition(mResultTxtList.size()-1);
                isNewTxt = false;
            }
            printResult(recognizerResult);
        }

        @Override
        public void onError(SpeechError speechError) {
//            showTip(speechError.getPlainDescription(true));
//            mResultTxtList.remove(mResultTxtList.size()-1);
//            adapter.notifyDataSetChanged();
            wave.setFactor(0);
            if(!isUserEnd){
                recognizer.startListening(recognizerListener);
            }
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        mResultTxtList.set(mResultTxtList.size()-1, new ResultTxt(resultBuffer.toString()));
        adapter.notifyDataSetChanged();
        //        resultRecyclerView.scrollToPosition(mResultTxtList.size()-1);
//        mResultText.setText(resultBuffer.toString());
//        mResultText.setSelection(mResultText.length());
//        Log.i(SPE, resultBuffer.toString());
    }
}
