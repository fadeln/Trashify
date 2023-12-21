package com.example.trashify.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.trashify.R

class EditTextPassword : AppCompatEditText, View.OnTouchListener {

    private lateinit var visibleButton: Drawable

    private var isHidden = false

    private fun init() {
        visibleButton = ContextCompat.getDrawable(context, R.drawable.visibility_password) as Drawable
        setOnTouchListener(this)
        transformationMethod = PasswordTransformationMethod.getInstance()

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) showButton() else hideButton()
            }
            override fun afterTextChanged(s: Editable) {
                if (s.length < 8){
                    error = "Password must be more than 8 characters"
                    Handler().postDelayed({error = null
                    }, 1500)
                }else{
                    error = null
                }
            }
        })
    }

    private fun showButton() {
        setButtonDrawables(endOfTheText = visibleButton)
    }

    private fun hideButton() {
        setButtonDrawables()
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ){
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
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
        hint = "Password"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null){
            var isButtonClicked = false
            val visibleButtonStrt: Float = (width - paddingEnd - visibleButton.intrinsicWidth).toFloat()

            when{
                event.x > visibleButtonStrt -> isButtonClicked = true
            }

            when(event.action){
                MotionEvent.ACTION_UP -> {
                    if (isButtonClicked){
                        if (isHidden){
                            showButton()
                            transformationMethod = HideReturnsTransformationMethod.getInstance()
                        }else{
                            showButton()
                            transformationMethod = PasswordTransformationMethod.getInstance()
                        }
                        isHidden = !isHidden
                    }else return false
                }
            }
        }
        return false
    }
}