package com.study.infotechforprocstatidataofwebres.ui.general

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.study.infotechforprocstatidataofwebres.ui.theme.backgroundColor
import com.study.infotechforprocstatidataofwebres.ui.theme.buttonColor

@Composable
fun ReceiveDataButton(onClick: () -> Unit, text: String, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor
        ),
        modifier = modifier
    ) {
        Text(
            text = text,
            color = Color.White
        )
    }
}

@Preview
@Composable
private fun ReceiveDataButtonPreview() {
    Surface(color = backgroundColor) {
        Column(Modifier.padding(20.dp)) {
            ReceiveDataButton({}, "Some text")
            ReceiveDataButton({}, "Some text")
        }
    }
}