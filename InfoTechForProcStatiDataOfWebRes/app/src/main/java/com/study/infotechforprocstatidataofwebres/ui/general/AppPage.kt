package com.study.infotechforprocstatidataofwebres.ui.general

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.study.infotechforprocstatidataofwebres.models.SlideInMenuController
import com.study.infotechforprocstatidataofwebres.ui.theme.backgroundColor
import com.study.infotechforprocstatidataofwebres.ui.theme.headerColor
import com.study.infotechforprocstatidataofwebres.ui.theme.onHeaderColor
import org.koin.java.KoinJavaComponent.inject
@Composable
fun AppPage(
    navigationController: NavHostController? = null,
    headerText: String? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    val openSlideInMenu: SlideInMenuController by inject(SlideInMenuController::class.java)
    Column {
        Box(
            Modifier
                .background(headerColor)
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            NavigationMenuButton(
                navigationController = navigationController,
                modifier = Modifier.align(Alignment.CenterStart),
                onClick = openSlideInMenu.openCallback
            )
            headerText?.let {
                Text(
                    text = it,
                    color = onHeaderColor,
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }
        Box(
            Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(20.dp)
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun AppPagePreview() {
    Surface {
        AppPage(headerText = "Welcome!") {

        }
    }
}