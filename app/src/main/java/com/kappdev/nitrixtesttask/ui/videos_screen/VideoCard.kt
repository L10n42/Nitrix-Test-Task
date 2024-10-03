package com.kappdev.nitrixtesttask.ui.videos_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.kappdev.nitrixtesttask.R
import com.kappdev.nitrixtesttask.data.model.Video

@Composable
fun VideoCard(
    video: Video,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier
    ) {
        VideoThumbnailWithDuration(
            imageUrl = video.thumbnailUrl,
            duration = video.duration,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
        )
        VideoInformation(
            title = video.title,
            description = video.description,
            uploadTime = video.uploadTime,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun VideoInformation(
    title: String,
    description: String,
    uploadTime: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = description,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = uploadTime,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Composable
private fun VideoThumbnailWithDuration(
    imageUrl: String,
    duration: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        VideoThumbnail(imageUrl = imageUrl, Modifier.matchParentSize())
        Text(
            text = duration,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
                .background(Color.Black.copy(0.32f), RoundedCornerShape(8.dp))
                .padding(6.dp)
        )
    }
}

@Composable
private fun VideoThumbnail(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    SubcomposeAsyncImage(
        model = imageUrl,
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant),
        contentScale = ContentScale.Crop,
        contentDescription = "Video thumbnail",
        error = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.thumbnail_error))
            }
        },
        loading = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    strokeCap = StrokeCap.Round
                )
            }
        }
    )
}