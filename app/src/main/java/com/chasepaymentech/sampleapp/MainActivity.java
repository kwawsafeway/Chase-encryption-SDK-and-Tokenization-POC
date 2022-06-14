package com.chasepaymentech.sampleapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.voltage.securedatamobile.sdw.IVPCallback;
import com.voltage.securedatamobile.sdw.VPException;
import com.voltage.securedatamobile.sdw.VPProtect;
import com.voltage.securedatamobile.sdw.VPProtectedCardEmbeddedResult;
import com.voltage.securedatamobile.sdw.VPProtectedCardExternalResult;
import com.voltage.securedatamobile.sdw.VPProtectedStringResult;

import java.util.Properties;



public class MainActivity extends AppCompatActivity {


    public static final String EMPTY = "";
    private static final String TAG = "MainActivity";
    private IVPCallback cb = new VPCallBack();

    private VPProtect api;

    public MainActivity() {


    }

    private VPProtect getApi() {
        if (api == null) {

            synchronized (this) {

                if (api == null) {
                    try {
                        Properties properties = new Properties();
                        properties.load(this.getAssets().open("application.properties"));


                        api = new VPProtect(properties.getProperty("safetech_page_encryption_url"), properties.getProperty("format_id_subscriber_id"));
                        return api;

                    } catch (Exception e) {
                        Log.e(TAG, "Error while reading the properties", e);
                        throw new RuntimeException(e);
                    }
                }

            }

        }

        return api;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        final Button encCardBtn = findViewById(R.id.encCardBtn);
        final Button encCardNdCvvBtn = findViewById(R.id.encCardNdCvvBtn);

        encCardBtn.setOnClickListener(v -> handleClick("btn clicked:encCardCvvBtn", v, false));

        encCardNdCvvBtn.setOnClickListener(v -> handleClick("btn clicked:encCardNdCvvBtn", v));
    }

    private void handleClick(String buttonMessage, View v) {

        handleClick(buttonMessage, v, true);
    }

    private void handleClick(String buttonMessage, View v, boolean captureCvv) {
        Log.i(TAG, buttonMessage);
        CardData cardData = captureValuesAndLog(captureCvv);
        hideKeybaord(v);
        encrypt(cardData);
    }

    private CardData captureValuesAndLog(boolean captureCvv) {

        final EditText panInput = findViewById(R.id.panInput);
        final EditText cvvInput = findViewById(R.id.cvvInput);
        final EditText expiryInput = findViewById(R.id.expiryInput);

        final TextView panOutput = findViewById(R.id.panOutput);
        final TextView cvvOutput = findViewById(R.id.cvvOutput);
        final TextView expiryOutput = findViewById(R.id.expiryOutput);
        final TextView integrityOutput = findViewById(R.id.integrityOutput);
        final TextView ketIdOutput = findViewById(R.id.keyIdOutput);
        final TextView phaseOutput = findViewById(R.id.phaseOutput);


        logValues(panInput, cvvInput, expiryInput);

        String pan = panInput.getText().toString();
        String cvv = captureCvv ? cvvInput.getText().toString() : EMPTY;

        panOutput.setText(EMPTY);
        cvvOutput.setText(EMPTY);
        expiryOutput.setText(EMPTY);
        integrityOutput.setText(EMPTY);
        ketIdOutput.setText(EMPTY);
        phaseOutput.setText(EMPTY);

        return new CardData(pan, cvv);
    }

    private void logValues(EditText panInput, EditText cvvInput, EditText expiryInput) {
        Log.i(TAG, "btn encCardBtn clicked");
        Log.i(TAG, "PAN: " + panInput.getText());
        Log.i(TAG, "CVV: " + cvvInput.getText());
        Log.i(TAG, "Expiry: " + expiryInput.getText());
    }


    private void encrypt(CardData cardData) {

        VPProtect api = getApi();
        api.discardSettings();
        final Switch emb = findViewById(R.id.encrTypeSwitch);
        if (emb.isChecked()){
            Log.i(TAG, "Ecryption type: embedded");
            api.protectEmbeddedModeWithPANandCVV(cardData.getPan(), cardData.getCvv(), cb);
        }else {
            Log.i(TAG, "Ecryption type: external");
            api.protectExternalModeWithPANandCVV(cardData.getPan(), cardData.getCvv(), cb);
        }


    }

    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }


    private void updateResults(VPProtectedCardEmbeddedResult result) {
        final TextView panOutput = findViewById(R.id.panOutput);
        final TextView cvvOutput = findViewById(R.id.cvvOutput);
        final TextView expiryOutput = findViewById(R.id.expiryOutput);
        final TextView integrityOutput = findViewById(R.id.integrityOutput);

        panOutput.setText(result.getPAN());
        cvvOutput.setText(result.getCVV() == null || result.getCVV().length() == 0 ?
                ((EditText) findViewById(R.id.cvvInput)).getText().toString() : result.getCVV());
        expiryOutput.setText(((EditText) findViewById(R.id.expiryInput)).getText().toString());
        integrityOutput.setText(result.getIntegrity());

        Log.i(TAG, result.toString());
    }


    private void updateResults(VPProtectedCardExternalResult result) {

        updateResults((VPProtectedCardEmbeddedResult) result);
        final TextView ketIdOutput = findViewById(R.id.keyIdOutput);
        final TextView phaseOutput = findViewById(R.id.phaseOutput);

        ketIdOutput.setText(String.valueOf(result.getKeyID()));
        phaseOutput.setText(String.valueOf(result.getPhase()));
    }


    private void updateResults(VPProtectedStringResult result) {
        // we are not calling this, string encryption is used for the other data
    }

    private void updateResults(VPException e) {
        Log.i(TAG, "Protection Failed : " + e.toString());
        makeToast(e.toString());
    }

    private void makeToast(CharSequence msg) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, msg, duration);
        toast.show();
    }


    private final class VPCallBack implements IVPCallback {
        @Override
        public void protectCardUsingEmbeddedModeSucceeded(VPProtectedCardEmbeddedResult vpProtectedCardEmbeddedResult, int i) {

            updateResults(vpProtectedCardEmbeddedResult);
        }

        @Override
        public void protectCardUsingExternalModeSucceeded(VPProtectedCardExternalResult vpProtectedCardExternalResult, int i) {
            updateResults(vpProtectedCardExternalResult);
        }

        @Override
        public void protectStringSucceeded(VPProtectedStringResult vpProtectedStringResult, int i) {
            updateResults(vpProtectedStringResult);
        }

        @Override
        public void protectFailed(VPException e, int i) {

            updateResults(e);
        }
    }

}