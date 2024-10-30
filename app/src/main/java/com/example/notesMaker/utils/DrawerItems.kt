package com.example.notesMaker.utils

import androidx.annotation.StringRes
import com.example.notesMaker.R

enum class DrawerItems(@StringRes val titleResId: Int) {
    Recent(R.string.drawer_item_recent),
    Starred(R.string.drawer_item_starred),
    Offline(R.string.drawer_item_offline),
    Bin(R.string.drawer_item_bin),
    Settings(R.string.drawer_item_settings),
    HelpAndFeedback(R.string.drawer_item_help_and_feedback)
}