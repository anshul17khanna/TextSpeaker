package com.github.anshul17khanna.textspeaker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.github.zagum.speechrecognitionview.RecognitionProgressView;
import com.github.zagum.speechrecognitionview.adapters.RecognitionListenerAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Context context = this;

    SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final RecognitionProgressView recognitionProgressView = (RecognitionProgressView) findViewById(R.id.recognition_view);
        recognitionProgressView.setSpeechRecognizer(speechRecognizer);
        recognitionProgressView.setRecognitionListener(new RecognitionListenerAdapter() {
            @Override
            public void onResults(Bundle results) {
                showResults(results);
            }
        });

        int[] colors = {
                ContextCompat.getColor(this, R.color.color1),
                ContextCompat.getColor(this, R.color.color2),
                ContextCompat.getColor(this, R.color.color3),
                ContextCompat.getColor(this, R.color.color4),
                ContextCompat.getColor(this, R.color.color5)
        };
        recognitionProgressView.setColors(colors);

        int[] heights = {60, 76, 58, 80, 55};
        recognitionProgressView.setBarMaxHeightsInDp(heights);

        recognitionProgressView.play();

        Button listen = (Button) findViewById(R.id.listen);
        Button reset = (Button) findViewById(R.id.reset);

        final EditText editText = (EditText) findViewById(R.id.editText);

        ToggleButton speechToggle = (ToggleButton) findViewById(R.id.speak);

        CompoundButton.OnCheckedChangeListener toggleListener;

        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecognition();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recognitionProgressView.stop();
                recognitionProgressView.play();
            }
        });

        final Speaker speaker = new Speaker(context);

        toggleListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if(isChecked){
                    recognitionProgressView.stop();
                    recognitionProgressView.play();
                    startRecognition();
                    speaker.allow(true);
                    speaker.speak(editText.getText().toString());
                }else{
                    recognitionProgressView.stop();
                    recognitionProgressView.play();
                    startRecognition();
                    speaker.speak(editText.getText().toString());
                    speaker.allow(false);
                }
            }
        };
        speechToggle.setOnCheckedChangeListener(toggleListener);
    }

    private void startRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizer.startListening(intent);
    }

    private void showResults(Bundle results) {
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setText(matches.get(0));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
