package com.example.bloodbank.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class MSPButton (context: Context, attrs: AttributeSet) : AppCompatButton(context, attrs){
    init {
        // Call the function to apply the font to the components.
        applyFont()
    }

    private fun applyFont() {
        val typeface = Typeface.createFromAsset(context.assets, "Poppins-Bold.ttf")
        setTypeface(typeface)
    }
}