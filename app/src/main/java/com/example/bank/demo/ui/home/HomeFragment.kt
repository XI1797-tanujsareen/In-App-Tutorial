package com.example.bank.demo.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.bank.demo.R
import com.example.bank.demo.databinding.HomeFragmentBinding

class HomeFragment : Fragment() {

    private var contextAttach: Context ? =null

    private var homeFragmentBinding : HomeFragmentBinding ? =null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.contextAttach= context

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeFragmentBinding = DataBindingUtil.inflate(inflater,
        R.layout.home_fragment,container,false)
        init()
        return homeFragmentBinding?.root
    }

    private fun init() {

    }

}