package com.study.infotechforprocstatidataofwebres.ui.general

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.study.infotechforprocstatidataofwebres.R
import com.study.infotechforprocstatidataofwebres.models.DataReceivingState
import com.study.infotechforprocstatidataofwebres.models.ReceiveDataButtonCondition
import com.study.infotechforprocstatidataofwebres.ui.theme.textOnBackgroundColor
import com.study.infotechforprocstatidataofwebres.util.loadData
import retrofit2.Response

@Composable
fun<T, RT> DependOnDataReceivingState(
    url: String,
    dataReceivingState: DataReceivingState<T>,
    changeDataReceivingState: (DataReceivingState<T>) -> Unit,
    buttonSubName: String,
    receiveDataButtonCondition: ReceiveDataButtonCondition<RT>,
    onSuccessful: (RT) -> T,
    context: Context = LocalContext.current,
    whenAllOkContent: @Composable (T) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val badResponse = stringResource(id = R.string.bad_response)
    val invalidUrl = stringResource(id = R.string.invalid_url)
    val receiveDataButton = @Composable {
        ReceiveDataButton(
            onClick = {
                if (url.contains("http") && url.contains("/") &&
                    url.contains(":")) {
                    if (receiveDataButtonCondition.condition) {
                        coroutineScope.loadData(
                            setDataReceivingState = { changeDataReceivingState(it) },
                            badResponse = badResponse,
                            response = receiveDataButtonCondition.request,
                            onSuccessful = onSuccessful
                        )
                    } else {
                        receiveDataButtonCondition.onFalse()
                    }
                }
                else {
                    Toast.makeText(context, invalidUrl, Toast.LENGTH_SHORT).show()
                }
            },
            text = stringResource(id = R.string.get_data, buttonSubName.lowercase()),
            modifier = Modifier.fillMaxWidth()
        )
    }
    when (dataReceivingState) {
        is DataReceivingState.Loading -> LoadingPart()
        is DataReceivingState.None -> {
            receiveDataButton()
        }
        is DataReceivingState.Empty -> {
            receiveDataButton()
            Spacer(modifier = Modifier.height(10.dp))
            Text(stringResource(R.string.too_long_to_respond))
        }
        is DataReceivingState.Changed<T> -> {
            receiveDataButton()
            dataReceivingState.previousContent?.let {
                Spacer(modifier = Modifier.height(10.dp))
                whenAllOkContent(it)
            }
        }
        is DataReceivingState.Error -> {
            receiveDataButton()
            Text(
                text = dataReceivingState.errorMessage,
                color = textOnBackgroundColor,
            )
        }
        is DataReceivingState.Finish<T> -> {
            whenAllOkContent(dataReceivingState.content)
        }
    }
}

@Composable
fun<T, RT> DependOnDataReceivingState(
    url: String,
    dataReceivingState: DataReceivingState<T>,
    changeDataReceivingState: (DataReceivingState<T>) -> Unit,
    buttonSubName: String,
    response: suspend () -> Response<RT>?,
    onSuccessful: (RT) -> T,
    whenAllOkContent: @Composable (T) -> Unit
) {
    DependOnDataReceivingState(
        url = url,
        dataReceivingState = dataReceivingState,
        changeDataReceivingState = changeDataReceivingState,
        buttonSubName = buttonSubName,
        receiveDataButtonCondition = ReceiveDataButtonCondition(response, true) {},
        onSuccessful = onSuccessful,
        whenAllOkContent = whenAllOkContent
    )
}