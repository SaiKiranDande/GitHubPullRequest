package com.example.mygithubrequest

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.text.SimpleDateFormat
import java.util.*

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.hideKeyboard() {
    val input = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    input.hideSoftInputFromWindow(this.applicationWindowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun RecyclerView.hideFabOnScroll(fab: View) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            recyclerView?.run { if (!canScrollVertically(-1)) fab.visible() }
            super.onScrollStateChanged(recyclerView, newState)
        }

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy > 10) fab.gone()
            else if (dy < -75) fab.visible()
        }
    })
}

fun Date.formatDate(): String {
    val dateFormate = SimpleDateFormat("EEE, " + "dd MMM yyyy", Locale.ENGLISH)
    return dateFormate.format(this)
}