package com.study.infotechforprocstatidataofwebres.ui.pages

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.study.infotechforprocstatidataofwebres.R
import com.study.infotechforprocstatidataofwebres.models.DataReceivingState
import com.study.infotechforprocstatidataofwebres.network.SaveReceivingClient
import com.study.infotechforprocstatidataofwebres.ui.general.AppPage
import com.study.infotechforprocstatidataofwebres.ui.general.DependOnDataReceivingState
import com.study.infotechforprocstatidataofwebres.ui.general.UserUrlField
import com.study.infotechforprocstatidataofwebres.ui.theme.textOnBackgroundColor
import org.koin.java.KoinJavaComponent.inject
private const val PAGE_NUMBER = 1

@Composable
fun MetaInfo(
    navigationController: NavHostController?,
    url: String,
    changeUrl: (String) -> Unit
) {
    val pageTitle = stringArrayResource(id = R.array.navigation_buttons)[PAGE_NUMBER]
    val buttonSubName = stringArrayResource(id = R.array.receive_button_sub_names)[PAGE_NUMBER]
    val decodedUrl = Uri.decode(url)
    val client: SaveReceivingClient by inject(SaveReceivingClient::class.java)
    var dataReceivingState by remember {
        mutableStateOf<DataReceivingState<String>>(DataReceivingState.None())
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
            Spacer(modifier = Modifier.height(20.dp))
            DependOnDataReceivingState(
                url = url,
                dataReceivingState = dataReceivingState,
                changeDataReceivingState = { dataReceivingState = it },
                buttonSubName = buttonSubName,
                response = { client.getMetaInfo(url) },
                onSuccessful = {
                    var index = 0
                    it.joinToString("\n") { metaAttrs ->
                        index++
                        "$index) ${metaAttrs.entries.joinToString("\n") {  attr ->
                            "\t${attr.key}: ${attr.value}"
                        }}"
                    }
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
fun MetaInfoPreview() {
    var url by remember {
        mutableStateOf(
            "https://www.youtube.com/watch?v=G2N1J73OnwI&t=67s&ab_channel=MagicMusicMix"
        )
    }
    Surface {
        MetaInfo(
            navigationController = null,
            url = url,
            changeUrl = { url = it }
        )
    }
}
