package com.study.infotechforprocstatidataofwebres.ui.general

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.study.infotechforprocstatidataofwebres.R
import com.study.infotechforprocstatidataofwebres.ui.theme.headerColor
import com.study.infotechforprocstatidataofwebres.ui.theme.onHeaderColor

@Composable
fun NavigationMenuButton(
    navigationController: NavHostController?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = R.drawable.navigation_menu),
            contentDescription = stringResource(id = R.string.navigation),
            tint = onHeaderColor,
            modifier = modifier
                .width(48.dp)
                .height(43.6.dp)
                .padding(5.dp)
        )
    }
}

@Preview
@Composable
private fun NavigationMenuButtonPreview() {
    Surface(color = headerColor) {
        NavigationMenuButton(navigationController = null) {

        }
    }
}