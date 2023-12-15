package com.study.infotechforprocstatidataofwebres.ui.general

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.study.infotechforprocstatidataofwebres.R
import com.study.infotechforprocstatidataofwebres.ui.theme.textOnBackgroundColor

@Composable
fun ContentWithButton(link: String, content: String) {
    val contentLimit = integerResource(id = R.integer.content_limit)
    var showMore by remember {
        mutableStateOf(false)
    }
    val textSize by remember {
        derivedStateOf {
            content.length
        }
    }
    Text(stringResource(id = R.string.style_link, link))
    if (textSize <= contentLimit / 4) {
        Text(text = stringResource(id = R.string.style_content, content))
    }
    else {
        Column(Modifier.fillMaxWidth()) {
            Text(text = stringResource(
                id = R.string.style_content,
                if (showMore)
                    content
                else
                    content.substring(0, 100)
            ),
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
                    .clickable {
                        showMore = !showMore
                    }
            )
            TextButton(
                onClick = {
                    showMore = !showMore
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = stringResource(
                        if (showMore)
                            R.string.less
                        else
                            R.string.more
                    ),
                    color = textOnBackgroundColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}