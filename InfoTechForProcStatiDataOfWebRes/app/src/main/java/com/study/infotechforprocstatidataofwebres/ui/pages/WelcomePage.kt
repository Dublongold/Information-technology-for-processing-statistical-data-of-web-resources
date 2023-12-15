package com.study.infotechforprocstatidataofwebres.ui.pages

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.study.infotechforprocstatidataofwebres.R
import com.study.infotechforprocstatidataofwebres.ui.general.AppPage
import com.study.infotechforprocstatidataofwebres.ui.theme.backgroundColor
import com.study.infotechforprocstatidataofwebres.ui.theme.textOnBackgroundColor

@Composable
fun WelcomePage(navigationController: NavHostController? = null) {
    AppPage(navigationController = navigationController) {
        Text(
            text = stringResource(id = R.string.welcome_text),
            color = textOnBackgroundColor,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.75f),
        )
    }
}

@Preview
@Composable
private fun WelcomePagePreview() {
    Surface(color = backgroundColor) {
        WelcomePage()
    }
}