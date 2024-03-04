package org.koitharu.kotatsu.parsers

import android.annotation.SuppressLint
import android.content.Context
import android.util.Base64
import android.webkit.WebView
import androidx.core.os.LocaleListCompat
import com.inlurker.komiq.model.data.kotatsu.util.MangaHttpClient
import com.inlurker.komiq.model.data.kotatsu.util.MutableCookieJar
import com.inlurker.komiq.model.data.kotatsu.util.SourceConfigMock
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import org.koitharu.kotatsu.parsers.config.MangaSourceConfig
import org.koitharu.kotatsu.parsers.model.MangaSource
import java.lang.ref.WeakReference
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class KotatsuMangaLoaderContext @Inject constructor(
	@MangaHttpClient override val httpClient: OkHttpClient,
	override val cookieJar: MutableCookieJar,
	@ApplicationContext private val androidContext: Context,
) : MangaLoaderContext() {

	private var webViewCached: WeakReference<WebView>? = null

	@SuppressLint("SetJavaScriptEnabled")
	override suspend fun evaluateJs(script: String): String? = withContext(Dispatchers.Main) {
		val webView = webViewCached?.get() ?: WebView(androidContext).also {
			it.settings.javaScriptEnabled = true
			webViewCached = WeakReference(it)
		}
		suspendCoroutine { cont ->
			webView.evaluateJavascript(script) { result ->
				cont.resume(result?.takeUnless { it == "null" })
			}
		}
	}

	override fun getConfig(source: MangaSource): MangaSourceConfig {
		return SourceConfigMock()
	}

	override fun encodeBase64(data: ByteArray): String {
		return Base64.encodeToString(data, Base64.NO_WRAP)
	}

	override fun decodeBase64(data: String): ByteArray {
		return Base64.decode(data, Base64.DEFAULT)
	}

	override fun getPreferredLocales(): List<Locale> {
		return LocaleListCompat.getAdjustedDefault().toList()
	}
}

fun LocaleListCompat.toList(): List<Locale> = List(size()) { i -> getOrThrow(i) }

fun LocaleListCompat.getOrThrow(index: Int) = get(index) ?: throw NoSuchElementException()

