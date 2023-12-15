package com.study.infotechforprocstatidataofwebres.ui.general

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.study.infotechforprocstatidataofwebres.R
import com.study.infotechforprocstatidataofwebres.ui.theme.textOnBackgroundColor

@Composable
fun LoadingPart() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(id = R.string.loading),
            color = textOnBackgroundColor,
        )
        CircularProgressIndicator(
            color = textOnBackgroundColor,
            modifier = Modifier.padding(top = 20.dp),

        )
    }
}