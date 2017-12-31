package com.example.abhishekyadav.sumapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    public fun onButtonClick(view:View)
    {
        val e1 = editText
        val e2 = editText2
       val num1 = Integer.parseInt(e1.text.toString())
       //val num1 = e1.text.toString()
        val num2 = Integer.parseInt(e2.text.toString())
        val sum = num1 + num2
        //val sum = 4
        textView.setText(Integer.toString(sum))

    }
}
