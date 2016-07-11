package com.github.anshul17khanna.textspeaker;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import android.widget.ImageButton;
import android.widget.Toast;

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

        final ImageButton listen = (ImageButton) findViewById(R.id.listen);
        final ImageButton reset = (ImageButton) findViewById(R.id.reset);
        final ImageButton speechToggle = (ImageButton) findViewById(R.id.speechToggle);
        final ImageButton copy = (ImageButton) findViewById(R.id.copy);

        final EditText editText = (EditText) findViewById(R.id.editText);

        final Speaker speaker = new Speaker(context);

        int imageMicResource1 = getResources().getIdentifier("drawable/mic", null, getPackageName());
        final Drawable imageMic1 = getResources().getDrawable(imageMicResource1);
        listen.setBackground(imageMic1);

        int imageMicResource2 = getResources().getIdentifier("drawable/mic1", null, getPackageName());
        final Drawable imageMic2 = getResources().getDrawable(imageMicResource2);

        int imageSpeakerResource1 = getResources().getIdentifier("drawable/speaker", null, getPackageName());
        final Drawable imageSpeaker1 = getResources().getDrawable(imageSpeakerResource1);
        speechToggle.setBackground(imageSpeaker1);

        int imageSpeakerResource2 = getResources().getIdentifier("drawable/speaker1", null, getPackageName());
        final Drawable imageSpeaker2 = getResources().getDrawable(imageSpeakerResource2);

        int imageResetResource = getResources().getIdentifier("drawable/refresh", null, getPackageName());
        Drawable imageReset = getResources().getDrawable(imageResetResource);
        reset.setBackground(imageReset);

        int imageCopyResource = getResources().getIdentifier("drawable/copy", null, getPackageName());
        final Drawable imageCopy = getResources().getDrawable(imageCopyResource);
        copy.setBackground(imageCopy);

        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Recording..", Toast.LENGTH_SHORT).show();
                listen.setBackground(imageMic2);
                startRecognition();
            }
        });

        speechToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString()!=null) {
                    speechToggle.setBackground(imageSpeaker2);
                    speechRecognizer.stopListening();
                    speaker.allow(true);
                    speaker.speak(editText.getText().toString());
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                speechRecognizer.stopListening();
                recognitionProgressView.stop();
                recognitionProgressView.play();
                Toast.makeText(context, "Refreshed", Toast.LENGTH_SHORT).show();
                listen.setBackground(imageMic1);
                speechToggle.setBackground(imageSpeaker1);
            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("copiedText", editText.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Text Copied", Toast.LENGTH_SHORT).show();
            }
        });
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
