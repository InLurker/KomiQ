package com.inlurker.komiq.model.data.kotatsu.util

import androidx.annotation.WorkerThread
import androidx.core.util.Predicate
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import java.util.concurrent.ConcurrentHashMap

interface MutableCookieJar : CookieJar {

	@WorkerThread
	override fun loadForRequest(url: HttpUrl): List<Cookie>

	@WorkerThread
	override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>)

	@WorkerThread
	fun removeCookies(url: HttpUrl, predicate: Predicate<Cookie>?)

	suspend fun clear(): Boolean
}


class InMemoryCookieJar : MutableCookieJar {

	private val cookieStore: ConcurrentHashMap<String, MutableList<Cookie>> = ConcurrentHashMap()

	@WorkerThread
	override fun loadForRequest(url: HttpUrl): List<Cookie> {
		val cookies = mutableListOf<Cookie>()
		val urlCookies = cookieStore[url.host] ?: mutableListOf()
		cookies.addAll(urlCookies.filter { it.expiresAt >= System.currentTimeMillis() })
		return cookies
	}

	@WorkerThread
	override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
		val urlCookies = cookieStore.getOrPut(url.host) { mutableListOf() }
		cookies.forEach { newCookie ->
			urlCookies.removeAll { it.name == newCookie.name }
			if (newCookie.expiresAt >= System.currentTimeMillis()) {
				urlCookies.add(newCookie)
			}
		}
	}

	@WorkerThread
	override fun removeCookies(url: HttpUrl, predicate: Predicate<Cookie>?) {
		val urlCookies = cookieStore[url.host] ?: return
		if (predicate == null) {
			urlCookies.clear()
		} else {
			urlCookies.removeAll { predicate.test(it) }
		}
	}

	override suspend fun clear(): Boolean {
		try {
			cookieStore.clear()
		} catch (e: Exception) {
			return false
		}
		return true
	}
}
