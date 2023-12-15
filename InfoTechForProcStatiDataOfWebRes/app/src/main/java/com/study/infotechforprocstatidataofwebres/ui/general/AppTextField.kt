package com.study.infotechforprocstatidataofwebres.ui.general

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.study.infotechforprocstatidataofwebres.R
import com.study.infotechforprocstatidataofwebres.ui.theme.backgroundColor
import com.study.infotechforprocstatidataofwebres.ui.theme.buttonColor
import com.study.infotechforprocstatidataofwebres.ui.theme.headerColor
import com.study.infotechforprocstatidataofwebres.ui.theme.onTextFieldBackgroundColor
import com.study.infotechforprocstatidataofwebres.ui.theme.placeholderColor
import com.study.infotechforprocstatidataofwebres.ui.theme.textFieldBackground
import com.study.infotechforprocstatidataofwebres.ui.theme.textFieldSize

private val decorationBoxDefault: @Composable (innerTextField: @Composable () -> Unit) -> Unit =
    @Composable { innerTextField -> innerTextField() }

@Suppress("UNUSED_PARAMETER")
@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = TextStyle.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    textSelectionColors: TextSelectionColors? = null,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorBrush: Brush = SolidColor(Color.Black),
    @StringRes placeholderStringId: Int = R.string.your_url_placeholder,
    decorationBox: @Composable (innerTextField: @Composable () -> Unit) -> Unit =
        decorationBoxDefault
) {
    val decorationBoxMain = @Composable { it: @Composable () -> Unit ->
        Box(Modifier.padding(15.dp)) {
            it()
            if(value.isEmpty()) {
                Text(
                    text = stringResource(id = placeholderStringId),
                    color = placeholderColor,
                    fontSize = textFieldSize
                )
            }
        }
    }
    CompositionLocalProvider(
        LocalTextSelectionColors.provides(textSelectionColors ?: TextSelectionColors (
            handleColor = buttonColor,
            backgroundColor = onTextFieldBackgroundColor
        ))
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = if (textStyle != TextStyle.Default) textStyle else {
                TextStyle(fontSize = textFieldSize)
            },
            modifier = Modifier
                .background(textFieldBackground)
                .then(modifier),
            decorationBox = if (decorationBoxDefault == decorationBox)
                decorationBoxMain
            else
                decorationBox
        )
    }
}


@Preview
@Composable
private fun AppTextFieldPreview() {
    var url by remember {
        mutableStateOf(
            "https://www.youtube.com/watch?v=G2N1J73OnwI&t=67s&ab_channel=MagicMusicMix"
        )
    }
    Box(
        modifier = Modifier
            .background(backgroundColor)
            .padding(30.dp)
    ) {
        AppTextField(
            value = url,
            onValueChange = {
                url = it
            }
        )
    }
}