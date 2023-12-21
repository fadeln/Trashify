package com.example.trashify.components

import android.content.Context
import android.graphics.Canvas
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

class EditTextEmail : AppCompatEditText {

    private fun init() {

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
            override fun afterTextChanged(s: Editable) {
                val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+.[A-Za-z0-9.-]\$"
                if (s.matches(emailRegex.toRegex())){
                    error = "Fomat Email Salah"
                    Handler().postDelayed({error = null
                    }, 1500)
                }else{
                    error = null
                }
            }
        })
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "Email"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }
}