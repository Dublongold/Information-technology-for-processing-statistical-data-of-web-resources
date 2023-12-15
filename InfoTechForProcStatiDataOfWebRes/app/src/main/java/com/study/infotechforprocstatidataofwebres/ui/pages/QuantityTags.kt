package com.study.infotechforprocstatidataofwebres.ui.pages

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.study.infotechforprocstatidataofwebres.R
import com.study.infotechforprocstatidataofwebres.models.DataReceivingState
import com.study.infotechforprocstatidataofwebres.models.ReceiveDataButtonCondition
import com.study.infotechforprocstatidataofwebres.network.SaveReceivingClient
import com.study.infotechforprocstatidataofwebres.ui.general.AppPage
import com.study.infotechforprocstatidataofwebres.ui.general.AppTextField
import com.study.infotechforprocstatidataofwebres.ui.general.DependOnDataReceivingState
import com.study.infotechforprocstatidataofwebres.ui.general.UserUrlField
import com.study.infotechforprocstatidataofwebres.ui.theme.textOnBackgroundColor
import org.koin.java.KoinJavaComponent.inject

private const val PAGE_NUMBER = 5

@Composable
fun QuantityTags(
    navigationController: NavHostController?,
    url: String,
    changeUrl: (String) -> Unit
) {
    val context = LocalContext.current
    val pageTitle = stringArrayResource(id = R.array.navigation_buttons)[PAGE_NUMBER]
    val buttonSubName = stringArrayResource(id = R.array.receive_button_sub_names)[PAGE_NUMBER]
    val decodedUrl = Uri.decode(url)
    val client: SaveReceivingClient by inject(SaveReceivingClient::class.java)
    val tagQuantityString = stringResource(id = R.string.tag_quantity)
    val invalidTags = stringResource(id = R.string.invalid_tags)
    var dataReceivingState by remember {
        mutableStateOf<DataReceivingState<String>>(DataReceivingState.None())
    }
    var tags by remember {
        mutableStateOf("")
    }

    val convertedTags by remember {
        derivedStateOf {
            tags.split(",").map {
                it.trim()
            }
        }
    }

    AppPage(
        navigationController = navigationController,
        headerText = pageTitle
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            UserUrlField(
                url = decodedUrl,
                changeUrl = changeUrl,
                dataReceivingState = dataReceivingState,
                setDataReceivingState = { dataReceivingState = it }
            )
            Column(Modifier.fillMaxWidth()) {
                Text (
                    text = stringResource(R.string.tags_label),
                    color = textOnBackgroundColor,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(vertical = 5.dp)
                )
                AppTextField (
                    value = tags,
                    onValueChange = {
                        val localDataReceivingState = dataReceivingState
                        if (localDataReceivingState is DataReceivingState.Finish) {
                            dataReceivingState = DataReceivingState.Changed(
                                localDataReceivingState.content
                            )
                        }
                        tags = it
                    },
                    placeholderStringId = R.string.tags_placeholder,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            DependOnDataReceivingState(
                url = url,
                dataReceivingState = dataReceivingState,
                changeDataReceivingState = { dataReceivingState = it },
                buttonSubName = buttonSubName,
                receiveDataButtonCondition = ReceiveDataButtonCondition(
                    { client.getQuantityTags(url, convertedTags) },
                    convertedTags.all { tag ->
                        tag.all { tagCharacter ->
                            !tagCharacter.isWhitespace()
                        } && tag.isNotEmpty()
                    }, {
                        Toast.makeText(context, invalidTags, Toast.LENGTH_SHORT).show()
                    }
                ),
                onSuccessful = {
                    val resultString = mutableListOf<String>()
                    for (tagQuantity in it) {
                        resultString += tagQuantityString.format(
                            tagQuantity.name,
                            tagQuantity.quantity
                        )
                    }
                    resultString.joinToString("\n")
                }
            ) { WhenAllOkContent(content = it) }
        }
    }
}

@Composable
private fun WhenAllOkContent(content: String) {
    val scrollState = rememberScrollState()

    Text(
        text = content,
        color = textOnBackgroundColor,
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState, true)
    )
}

@Preview
@Composable
fun QuantityTagsPreview() {
    var url by remember {
        mutableStateOf(
            "https://www.youtube.com/watch?v=G2N1J73OnwI&t=67s&ab_channel=MagicMusicMix"
        )
    }
    Surface {
        QuantityTags(
            navigationController = null,
            url = url,
            changeUrl = { url = it }
        )
    }
}
