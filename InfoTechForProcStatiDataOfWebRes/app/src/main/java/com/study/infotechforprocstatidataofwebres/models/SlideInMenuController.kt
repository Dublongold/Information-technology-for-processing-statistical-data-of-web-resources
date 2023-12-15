package com.study.infotechforprocstatidataofwebres.models

import android.util.Log

class SlideInMenuController(
    var openCallback: () -> Unit,
    var closeCallback: () -> Unit,
) {
    companion object {
        val DEFAULT: SlideInMenuController
            get() = SlideInMenuController({
                    Log.w("Slide in menu controller", "Default open!")
                }, {
                    Log.w("Slide in menu controller", "Default close!")
                }
            )
    }
}