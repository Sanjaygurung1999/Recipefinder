package com.example.recipefinder.frags;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.recipefinder.R;
import com.google.android.material.slider.RangeSlider;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DialogFilter#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DialogFilter extends DialogFragment implements View.OnClickListener {

    int mNum;

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    static DialogFilter newInstance(int num) {
        DialogFilter f = new DialogFilter();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);
        return f;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments().getInt("num");
        // Pick a style based on the num.
        int style = DialogFragment.STYLE_NO_TITLE;
        setStyle(style, 0);
    }
    ImageButton closeDialog;
    RangeSlider rangeSlider;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_dialog_filter, container, false);
        closeDialog=root.findViewById(R.id.imageButton);
        rangeSlider=root.findViewById(R.id.rangeslider);
        closeDialog.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageButton:
                dismiss();
        }
    }
}