package com.study.infotechforprocstatidataofwebres.ui.pages

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.study.infotechforprocstatidataofwebres.R
import com.study.infotechforprocstatidataofwebres.models.DataReceivingState
import com.study.infotechforprocstatidataofwebres.models.ScriptInfo
import com.study.infotechforprocstatidataofwebres.network.SaveReceivingClient
import com.study.infotechforprocstatidataofwebres.ui.general.AppPage
import com.study.infotechforprocstatidataofwebres.ui.general.DependOnDataReceivingState
import com.study.infotechforprocstatidataofwebres.ui.general.UserUrlField
import org.koin.java.KoinJavaComponent.inject

private const val PAGE_NUMBER = 4

@Composable
fun ScriptsInfo(
    navigationController: NavHostController?,
    url: String,
    changeUrl: (String) -> Unit
) {
    val contentLimit = integerResource(id = R.integer.content_limit)
    val pageTitle = stringArrayResource(id = R.array.navigation_buttons)[PAGE_NUMBER]
    val buttonSubName = stringArrayResource(id = R.array.receive_button_sub_names)[PAGE_NUMBER]
    val contentAndMore = stringResource(id = R.string.content_and_more)
    val decodedUrl = Uri.decode(url)
    val client: SaveReceivingClient by inject(SaveReceivingClient::class.java)
    var dataReceivingState by remember {
        mutableStateOf<DataReceivingState<List<ScriptInfo>>>(DataReceivingState.None())
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
                response = { client.getScriptsInfo(url) },
                onSuccessful = {
                    it.map { scriptInfo ->
                        if (scriptInfo.content?.let { content ->
                                content.length > contentLimit
                            } == true) {
                            ScriptInfo(
                                scriptInfo.link,
                                contentAndMore.format(
                                    scriptInfo.content.substring(0, contentLimit),
                                    scriptInfo.content.length - contentLimit
                                ))
                        }
                        else {
                            ScriptInfo(scriptInfo.link, scriptInfo.content)
                        }
                    }
                }
            ) { WhenAllOkContent(content = it) }
        }
    }
}



@Composable
private fun WhenAllOkContent(content: List<ScriptInfo>) {
    LazyColumn {
        items(content) { (link, content) ->
            if (link != null && content != null) {
                com.study.infotechforprocstatidataofwebres.ui.general.ContentWithButton(
                    link,
                    content
                )
            }
        }
    }
}

@Preview
@Composable
fun ScriptsInfoPreview() {
    var url by remember {
        mutableStateOf(
            "https://www.youtube.com/watch?v=G2N1J73OnwI&t=67s&ab_channel=MagicMusicMix"
        )
    }
    Surface {
        ScriptsInfo(
            navigationController = null,
            url = url,
            changeUrl = { url = it }
        )
    }
}
