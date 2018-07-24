package net.albertogarrido.githubreposearch.util

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.*
import android.widget.EditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


fun View?.visible() {
    this?.let {
        it.visibility = VISIBLE
    }
}

fun View?.invisible() {
    this?.let {
        it.visibility = INVISIBLE
    }
}

fun View?.gone() {
    this?.let {
        it.visibility = GONE
    }
}

// source: https://gist.github.com/alexfacciorusso/43010274970aa882c1e7
fun <T> Call<T>.enqueue(success: (response: Response<T>) -> Unit, failure: (t: Throwable) -> Unit) {
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>?, response: Response<T>) = success(response)
        override fun onFailure(call: Call<T>?, t: Throwable) = failure(t)
    })
}

fun EditText.onTextChanged(action: (String, Int, Int) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {

        override fun afterTextChanged(newString: Editable?) {
            /* ignore */
        }

        override fun beforeTextChanged(string: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(newString: CharSequence?, start: Int, before: Int, count: Int) {
            action(newString.toString(), before, count)
        }

    })
}