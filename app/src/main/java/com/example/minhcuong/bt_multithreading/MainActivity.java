package com.example.minhcuong.bt_multithreading;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btnDoItAgain;
    EditText txtNumberInput;
    ProgressBar progressBar;
    TextView txtPercent;

    int percentage;
    boolean isRunning;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String returnValue = (String) msg.obj;
            txtPercent.setText(returnValue + "%");
            progressBar.incrementProgressBy(1);
            if (progressBar.getProgress() == progressBar.getMax()) {
                Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public synchronized int getPercentage() {
        return percentage;
    }

    public synchronized void setPercentage(int num) {
        percentage = num;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnDoItAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyDoItAgainClick();
            }
        });
    }

    private void xuLyDoItAgainClick() {
        percentage = 0; //Khoi tao 0%
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int numberInput = Integer.parseInt(txtNumberInput.getText().toString());
                    progressBar.setMax(numberInput);
                    progressBar.setProgress(0);
                    for (int i = 0 ; i<=numberInput; i++){
                        setPercentage((int)Math.round((i*100/numberInput)));
                        Message message = handler.obtainMessage(1, (String)(getPercentage()+""));
                        if (isRunning){
                            handler.sendMessage(message);
                        }
                    }
                }catch (Throwable throwable){
                    isRunning=false;
                }
            }
        });
        isRunning=true;
        thread.start();
    }

    private void addControls() {
        btnDoItAgain = (Button) findViewById(R.id.btnDoItAgain);
        txtNumberInput = (EditText) findViewById(R.id.txtNumberInput);
        txtPercent = (TextView) findViewById(R.id.txtPercent);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }
}
