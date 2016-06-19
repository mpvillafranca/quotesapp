package com.mpvillafranca.quotesapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class PictureFragment extends Fragment {

    private TextView quoteTextView;
    private TextView authorTextView;
    private ImageView picImageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.picture_fragment, container, false);

        quoteTextView = (TextView) view.findViewById(R.id.quoteTextView);
        authorTextView = (TextView) view.findViewById(R.id.authorTextView);
        picImageView = (ImageView) view.findViewById(R.id.picImageView);

        return view;
    }

    void setQuoteText(String quote, String author){
        String quoteformat;
        String authorformat;
        if(quote.equals(""))
            quoteformat = quote;
        else
            quoteformat = "\"" + quote + "\"";

        if(author.equals(""))
            authorformat = author;
        else
            authorformat = "~ " + author;

        quoteTextView.setText(quoteformat);
        authorTextView.setText(authorformat);
    }

    void setImage(Bitmap bitmap){
        picImageView.setImageBitmap(bitmap);
    }
}
