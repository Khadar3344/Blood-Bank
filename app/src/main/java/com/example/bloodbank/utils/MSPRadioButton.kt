package com.example.bloodbank.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton

class MSPRadioButton (context: Context, attributeSet: AttributeSet): AppCompatRadioButton(context, attributeSet) {

    init {
        applyFont()
    }

    private fun applyFont() {
        // This is used to get the file from the assets folder and set it to the title textView.
        val typeface = Typeface.createFromAsset(context.assets, "Poppins-Bold.ttf")
        setTypeface(typeface)
    }
}