package com.example.interviewtask.utils

interface UiActivity {
    fun showProgress(boolean: Boolean)
    fun isDarkTheme(): Boolean
    fun setDarkTheme(boolean: Boolean)
}