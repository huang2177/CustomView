package com.huang.customview.lyrics

import android.content.res.AssetManager
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.regex.Pattern

/**
 * Des:
 * Created by huang on 2018/10/12 0012 14:28
 */

fun readLrc(asset: AssetManager?): List<LyricsItem> {
    val result = mutableListOf<LyricsItem>()
    try {
        val lyricInput = BufferedReader(InputStreamReader(asset?.open("thatgirl.lrc")))
        var line = lyricInput.readLine()
        while (line != null) {
            val lyricItem = parse(line)
            if (result.size == 6) {
                for (i in 0 until 5) {
                    result[i].start = i * lyricItem.start / 5
                    result[i].duration = lyricItem.start / 5
                }
            } else if (result.size > 6) {
                result[result.size - 1].duration = lyricItem.start - result[result.size - 1].start
            }
            result.add(lyricItem)
            line = lyricInput.readLine()
        }
        lyricInput.close()
    } catch (e: Exception) {
    }
    return result
}

private fun parse(line: String): LyricsItem {

    val lyricsItem = LyricsItem()

    val pattern = Pattern.compile("^(\\[(.*?)\\])(.*?)$")

    val matcher = pattern.matcher(line)

    if (matcher.find()) {
        val front = matcher.group(2)

        when {
            front.contains("ti") -> lyricsItem.ti = front.split(":")[1]
            front.contains("ar") -> lyricsItem.ar = front.split(":")[1]
            front.contains("al") -> lyricsItem.al = front.split(":")[1]
            front.contains("by") -> lyricsItem.by = front.split(":")[1]
            front.contains("offset") -> lyricsItem.offset = front.split(":")[1].toLong()
            else -> {
                val timeArray = front.split(":")
                val secondTimeArray = timeArray[1].split(".")
                val second = secondTimeArray[0].toLong()
                val micSecond = secondTimeArray[1].toLong()
                lyricsItem.start = (timeArray[0].toLong() * 60 + second) * 1000 + micSecond
                lyricsItem.lyrics = matcher.group(3)
            }
        }

    }

    return lyricsItem
}

fun getLyricContent(lyricsItem: LyricsItem): String {
    return when {
        lyricsItem.ti.isNotEmpty() -> lyricsItem.ti
        lyricsItem.ar.isNotEmpty() -> "歌手: ${lyricsItem.ar}"
        lyricsItem.al.isNotEmpty() -> "专辑: ${lyricsItem.al}"
        lyricsItem.by.isNotEmpty() -> "作曲: ${lyricsItem.by}"
        else -> lyricsItem.lyrics
    }
}

data class LyricsItem(var ti: String = ""
                      , var ar: String = ""
                      , var al: String = ""
                      , var by: String = ""
                      , var offset: Long = 0
                      , var start: Long = 0
                      , var duration: Long = 0
                      , var lyrics: String = "")

