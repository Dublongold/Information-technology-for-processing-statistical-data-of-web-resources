package com.study.infotechforprocstatidataofwebres.ui.general

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.study.infotechforprocstatidataofwebres.MainActivity
import com.study.infotechforprocstatidataofwebres.R
import com.study.infotechforprocstatidataofwebres.models.SlideInMenuController
import com.study.infotechforprocstatidataofwebres.ui.theme.slideInMenuColor
import com.study.infotechforprocstatidataofwebres.ui.theme.slideInMenuRightSideColor
import com.study.infotechforprocstatidataofwebres.ui.theme.textOnSlideInMenuColor
import org.koin.java.KoinJavaComponent.inject

@Composable
fun SlideInMenu(
    slideInMenuIsOpen: Boolean,
    navigationController: NavHostController? = null,
) {
    val slideInMenuController: SlideInMenuController by inject(SlideInMenuController::class.java)
    val actionButtons: List<Pair<String, String>> =
        stringArrayResource(id = R.array.navigation_buttons).mapIndexed { index, string ->
            string to when (index) {
                0 -> MainActivity.Destinations.GENERAL_INFO
                1 -> MainActivity.Destinations.META_INFO
                2 -> MainActivity.Destinations.ALL_IMAGES
                3 -> MainActivity.Destinations.STYLES_INFO
                4 -> MainActivity.Destinations.SCRIPTS_INFO
                5 -> MainActivity.Destinations.QUANTITY_TAGS_INFO
                else -> throw IllegalArgumentException("To more buttons...")
            }
        }
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = slideInMenuIsOpen,
            enter = fadeIn(),
            exit = fadeOut()
            ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(slideInMenuRightSideColor)
                    .clickable(onClick = slideInMenuController.closeCallback)
            )
        }
        AnimatedVisibility(
            visible = slideInMenuIsOpen,
            enter = slideInHorizontally {
                -it - it
            },
            exit = slideOutHorizontally {
                -it
            }
            ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .animateContentSize()
                    .defaultMinSize(minWidth = 200.dp)
                    .fillMaxWidth(0.45f)
                    .background(slideInMenuColor)
                    .padding(10.dp)
            ) {
                Text(
                    text = stringResource(R.string.slide_in_menu_title),
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    color = textOnSlideInMenuColor,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                )
                Column(modifier = Modifier.align(Alignment.Center)) {
                    for (actionButton in actionButtons) {
                        val (name, route) = actionButton
                        DestinationButton(
                            name = name,
                            route = route,
                            closeCallback = slideInMenuController.closeCallback,
                            navigationController =  navigationController
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DestinationButton(
    name: String,
    route: String,
    closeCallback: () -> Unit,
    navigationController: NavHostController? = null
) {
    Column(modifier = Modifier.fillMaxWidth()){
        TextButton(
            onClick = {
                if (navigationController != null) {
                    closeCallback()
                    navigationController.navigate(route, NavOptions.Builder().setPopUpTo(
                        navigationController.currentDestination?.route, true).build()
                    )
                } else {
                    Log.i(
                        "Destination button",
                        "$name clicked, but navigationManager is null."
                    )
                }
            },
            contentPadding = PaddingValues(vertical = 5.dp),
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)) {
                Text(
                    text = name,
                    color = textOnSlideInMenuColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                )
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowRight,
                    tint = Color.Black,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }
        Box(
            modifier = Modifier
                .height(2.dp)
                .fillMaxWidth()
                .background(Color.Black)
        )
    }
}

@Preview
@Composable
private fun SlideInMenuPreview() {
    Surface {
        AppPage {

        }
    }
}