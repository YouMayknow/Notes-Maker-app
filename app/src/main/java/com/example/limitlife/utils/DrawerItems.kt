package com.example.limitlife.utils

import androidx.annotation.StringRes
import androidx.compose.ui.res.stringResource
import com.example.limitlife.R
import kotlinx.serialization.Serializable

enum class DrawerItems(@StringRes val titleResId: Int) {
    Recent(R.string.drawer_item_recent),
    Starred(R.string.drawer_item_starred),
    Offline(R.string.drawer_item_offline),
    Bin(R.string.drawer_item_bin),
    Settings(R.string.drawer_item_settings),
    HelpAndFeedback(R.string.drawer_item_help_and_feedback)
}