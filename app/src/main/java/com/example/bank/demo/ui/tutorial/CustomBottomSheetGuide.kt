package com.example.bank.demo.ui.tutorial

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.bank.demo.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*


class CustomBottomSheetGuide : BottomSheetDialogFragment(){

    private var textToSpeech: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(
                R.layout.tutorial_sheet,
                container, false
        )

        v.findViewById<TextView>(R.id.txtHeader).text=arguments?.getString("header")
       val description = v.findViewById<TextView>(R.id.textDescription)
        description.text=arguments?.getString("description")

        textToSpeech = TextToSpeech(context?.applicationContext) { status ->
            if (status != TextToSpeech.ERROR) {
                textToSpeech?.language = Locale.ENGLISH
            }
        }

        v.findViewById<FloatingActionButton>(R.id.floatingActionButton)?.setOnClickListener {
            textToSpeech?.speak(description?.text.toString(), TextToSpeech.QUEUE_FLUSH, null);
        }
        return v
    }


    override fun onPause() {
        if(textToSpeech !=null){
            textToSpeech?.stop()
            textToSpeech?.shutdown()
        }
        super.onPause()

    }

}
