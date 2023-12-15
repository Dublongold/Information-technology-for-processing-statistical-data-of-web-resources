package com.study.infotechforprocstatidataofwebres.ui.general

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.study.infotechforprocstatidataofwebres.R
import com.study.infotechforprocstatidataofwebres.models.DataReceivingState
import com.study.infotechforprocstatidataofwebres.ui.theme.backgroundColor
import com.study.infotechforprocstatidataofwebres.ui.theme.textOnBackgroundColor

@Composable
fun<T> UserUrlField(
    url: String,
    changeUrl: (String) -> Unit,
    dataReceivingState: DataReceivingState<T>,
    setDataReceivingState: (DataReceivingState<T>) -> Unit,
    labelColor: Color = textOnBackgroundColor
) {
    Column(Modifier.fillMaxWidth()) {
        Text (
            text = stringResource(id = R.string.your_url_label),
            color = labelColor,
            fontSize = 15.sp,
            modifier = Modifier.padding(vertical = 5.dp)
        )
        AppTextField (
            value = url,
            onValueChange = {
                if (dataReceivingState is DataReceivingState.Finish) {
                    setDataReceivingState(DataReceivingState.Changed(dataReceivingState.content))
                }
                changeUrl(it)
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun UserUrlFieldPreview() {
    var url by remember {
        mutableStateOf(
            "https://www.youtube.com/watch?v=G2N1J73OnwI&t=67s&ab_channel=MagicMusicMix"
        )
    }
    var dataReceivingState: DataReceivingState<String> by remember {
        mutableStateOf(DataReceivingState.None())
    }
    Box(
        modifier = Modifier
            .background(backgroundColor)
            .padding(30.dp)
    ) {
        UserUrlField(
            url = url,
            changeUrl = {
                url = it
            },
            dataReceivingState = dataReceivingState,
            setDataReceivingState = { dataReceivingState = it }
        )
    }
}