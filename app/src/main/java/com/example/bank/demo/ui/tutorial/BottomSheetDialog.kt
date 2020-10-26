package com.example.bank.demo.ui.tutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.bank.demo.R
import com.example.bank.demo.ui.tutorial.callback.ICallBackSheet
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetDialog(
    private val callback: ICallBackSheet
) : BottomSheetDialogFragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(
            R.layout.bottom_sheet_layout,
            container, false
        )
        val algo_button = v.findViewById<Button>(R.id.algo_button)
        val course_button = v.findViewById<Button>(R.id.course_button)
        algo_button.setOnClickListener {
            dismiss()
            callback. startGuide()
        }
        course_button.setOnClickListener {
            Toast.makeText(
                getActivity(),
                "ok, Please shake the Phone and I will appear again :)", Toast.LENGTH_SHORT
            )
                .show()
            dismiss()
        }
        return v
    }


}
