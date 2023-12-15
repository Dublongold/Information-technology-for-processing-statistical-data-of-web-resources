package com.study.infotechforprocstatidataofwebres.ui.pages

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.study.infotechforprocstatidataofwebres.R
import com.study.infotechforprocstatidataofwebres.models.DataReceivingState
import com.study.infotechforprocstatidataofwebres.models.ImageContainer
import com.study.infotechforprocstatidataofwebres.network.SaveReceivingClient
import com.study.infotechforprocstatidataofwebres.ui.general.AppPage
import com.study.infotechforprocstatidataofwebres.ui.general.DependOnDataReceivingState
import com.study.infotechforprocstatidataofwebres.ui.general.UserUrlField
import com.study.infotechforprocstatidataofwebres.ui.theme.textOnBackgroundColor
import org.koin.java.KoinJavaComponent.inject

private const val PAGE_NUMBER = 2

@Composable
fun AllImages(
    navigationController: NavHostController?,
    url: String,
    changeUrl: (String) -> Unit
) {
    val pageTitle = stringArrayResource(id = R.array.navigation_buttons)[PAGE_NUMBER]
    val buttonSubName = stringArrayResource(id = R.array.receive_button_sub_names)[PAGE_NUMBER]
    val decodedUrl = Uri.decode(url)
    val client: SaveReceivingClient by inject(SaveReceivingClient::class.java)
    var dataReceivingState by remember {
        mutableStateOf<DataReceivingState<List<ImageContainer>>>(DataReceivingState.None())
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
                buttonSubName = pageTitle,
                response = { client.getAllImages(url) },
                onSuccessful = { it }
            ) { WhenAllOkContent(content = it) }
        }
    }
}

@Composable
private fun WhenAllOkContent(content: List<ImageContainer>) {
    LazyColumn(Modifier.fillMaxWidth()) {
        items(content) {
            val (link, description) = it
            if (link != null && description != null) {
                Column(modifier = Modifier.padding(vertical = 10.dp)) {
                    UrlImage(link = link, description = description)
                }
            }
        }
    }
}

@Composable
private fun UrlImage(link: String, description: String) {
    Text(
        text = description.ifEmpty { stringResource(id = R.string.no_description) },
        modifier = Modifier.padding(bottom = 5.dp)
    )
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(link)
            .setHeader("User-Agent", stringResource(id = R.string.user_agent))
            .build(),
        contentDescription = description,
        modifier = Modifier
            .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
    ) {
        Log.i("Image", "Link: $link, description: $description")
        when (val painterState = painter.state) {
            is AsyncImagePainter.State.Loading -> {
                Log.i("Image", "Loading.")
                CircularProgressIndicator()
            }

            is AsyncImagePainter.State.Error, is AsyncImagePainter.State.Empty -> {
                Log.i(
                    "Image",
                    "${painter.state::class.java.simpleName}."
                )
                if (painterState is AsyncImagePainter.State.Error) {
                    val errorMessage = painterState.result.throwable.localizedMessage
                        ?: stringResource(id = R.string.unknown_reason)
                    Text(
                        stringResource(id = R.string.failed_to_load_image, errorMessage),
                        color = textOnBackgroundColor,
                        fontStyle = FontStyle.Italic
                    )
                }
            }

            is AsyncImagePainter.State.Success -> {
                Log.i("Image", "Success.")
                SubcomposeAsyncImageContent(modifier = Modifier
                    .padding(2.dp)
                    .border(1.dp, Color.White.copy(alpha = 0.25f))
                )
            }
        }
    }

}

@Preview
@Composable
fun AllImagesPreview() {
    var url by remember {
        mutableStateOf(
            "https://www.youtube.com/watch?v=G2N1J73OnwI&t=67s&ab_channel=MagicMusicMix"
        )
    }
    Surface {
        AllImages(
            navigationController = null,
            url = url,
            changeUrl = { url = it }
        )
    }
}
