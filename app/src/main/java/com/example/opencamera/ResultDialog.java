package com.example.opencamera;

import android.app.Notification;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ResultDialog extends DialogFragment {

    Button ok;
    EditText result;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alert_dialog, container, false);
        String resultText = "";
        ok = view.findViewById(R.id.ok);
        result = view.findViewById(R.id.result);
        Bundle bundle = getArguments();
        resultText = bundle.getString(LOCFaceDetection.result_text);
        result.setText(resultText);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return  view;
    }
}
