package com.mpvillafranca.quotesapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;

public class TypeTextFragment extends Fragment {

    private static EditText quoteTextInput;
    private static EditText authorTextInput;

    TypeTextListener activityCommander;

    public interface TypeTextListener{
        void createQuote(String quote, String author);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if (context instanceof Activity){
            Activity activity = (Activity) context;

            try{
                activityCommander = (TypeTextListener) activity;
            }catch(ClassCastException e){
                throw new ClassCastException(activity.toString());
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.type_text_fragment, container, false);

        quoteTextInput = (EditText) view.findViewById(R.id.quoteTextInput);
        authorTextInput = (EditText) view.findViewById(R.id.authorTextInput);

        quoteTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        authorTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return view;
    }

    public void textChanged(){
        activityCommander.createQuote(quoteTextInput.getText().toString(), authorTextInput.getText().toString());
    }
}
