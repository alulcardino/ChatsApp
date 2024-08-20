package com.vidial.chatsapp.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
fun CoilImage(
    imageUrl: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    defaultImageResId: Int
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build()
    )
    Box(modifier = modifier) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop
        )

        if (painter.state is AsyncImagePainter.State.Loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.Center)
            )
        }

        if (painter.state is AsyncImagePainter.State.Error) {
            Image(
                painter = painterResource(id = defaultImageResId),
                contentDescription = contentDescription,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}
