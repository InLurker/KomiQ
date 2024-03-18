package com.inlurker.komiq.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inlurker.komiq.model.data.datamodel.Comic
import com.inlurker.komiq.model.data.datamodel.Tag
import com.inlurker.komiq.model.data.kotatsu.util.InMemoryCookieJar
import com.inlurker.komiq.model.data.repository.ComicLanguageSetting
import com.inlurker.komiq.ui.screens.components.ComicCollectionComponent
import okhttp3.OkHttpClient
import org.koitharu.kotatsu.parsers.KotatsuMangaLoaderContext
import org.koitharu.kotatsu.parsers.model.Manga
import org.koitharu.kotatsu.parsers.model.MangaListFilter
import org.koitharu.kotatsu.parsers.model.MangaSource
import java.time.LocalDateTime

@Composable
fun SampleKotatsu() {
    val loaderContext = KotatsuMangaLoaderContext(
        OkHttpClient(),
        InMemoryCookieJar(),
        LocalContext.current
    )
    val parser = loaderContext.newParserInstance(MangaSource.RAWKUMA)
    var mangaList by remember { mutableStateOf(emptyList<Manga>()) }

    LaunchedEffect(true) {
        mangaList = parser.getList(0, MangaListFilter.Search(""))
    }

    val lazyGridScrollState = rememberLazyGridState()

    var printText by remember { mutableStateOf("") }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        state = lazyGridScrollState,
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        items(mangaList) { manga ->
            val comic = Comic(
                id = manga.id.toString(),
                title = manga.title,
                altTitle = manga.altTitle?:"No Alt title",
                description = manga.description?:"",
                languageSetting = ComicLanguageSetting.Japanese,
                originalLanguage = ComicLanguageSetting.Japanese.isoCode,
                publicationDemographic = if (manga.isNsfw) "nsfw" else "safe",
                status = manga.state?.name ?: "unknown",
                year = 0,
                contentRating = if (manga.isNsfw) "nsfw" else "safe",
                addedAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                authors = manga.author?.let { listOf(it) } ?: emptyList(),
                tags = manga.tags.map {
                    Tag(
                        name = it.title,
                        group = it.key
                    )
                },
                cover = manga.coverUrl,
                isInLibrary = false
            )
            ComicCollectionComponent(
                comic,
                onClick = {
                    val details = buildString {
                        append("Comic Details:\n")
                        append("ID: ${comic.id}\n")
                        append("Title: ${comic.title}\n")
                        append("AltTitle: ${comic.altTitle}\n")
                        append("Description: ${comic.description}\n")
                        append("LanguageSetting: ${comic.languageSetting}\n")
                        append("OriginalLanguage: ${comic.originalLanguage}\n")
                        append("PublicationDemographic: ${comic.publicationDemographic}\n")
                        append("Status: ${comic.status}\n")
                        append("Year: ${comic.year}\n")
                        append("ContentRating: ${comic.contentRating}\n")
                        append("AddedAt: ${comic.addedAt}\n")
                        append("UpdatedAt: ${comic.updatedAt}\n")
                        append("Authors: ${comic.authors.joinToString()}\n")
                        append("Tags: ${comic.tags.joinToString { tag -> "Name: ${tag.name}, Group: ${tag.group}" }}\n")
                        append("Cover: ${comic.cover}\n")
                        append("IsInLibrary: ${comic.isInLibrary}\n")
                    }

                    printText = details
                }
            )
        }
        item(span = { GridItemSpan(3)} ) {
            Text(printText)
        }
    }
}

@Preview
@Composable
fun PreviewSampleKotatsu() {
    SampleKotatsu()
}

