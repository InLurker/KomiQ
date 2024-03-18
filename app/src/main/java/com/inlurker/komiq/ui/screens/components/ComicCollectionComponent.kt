package com.inlurker.komiq.ui.screens.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.inlurker.komiq.model.data.datamodel.Comic

@Composable
fun ComicCollectionComponent(
    comic: Comic,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {
        val isAuthorNotEmpty = comic.authors.isNotEmpty()
        AsyncImage(
            model = comic.cover,
            contentDescription = "cover",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.outline,
                    RoundedCornerShape(10.dp)
                )
                .clip(RoundedCornerShape(10.dp))
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = comic.title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = if (isAuthorNotEmpty) 1 else 2,
            lineHeight = 12.sp,
            overflow = TextOverflow.Ellipsis
        )
        if (isAuthorNotEmpty) {
            Text(
                text = comic.authors.joinToString(separator = ", "),
                fontSize = 10.sp,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                lineHeight = 10.sp,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}