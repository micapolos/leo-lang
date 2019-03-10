package leo.java.io

import leo.base.EnumBit
import leo.base.Stream
import java.io.File
import java.nio.file.Path

val Path.file
	get() =
		toFile()

fun <R> File.useBitStream(fn: Stream<EnumBit>?.() -> R): R {
	inputStream().use { inputStream ->
		return fn(inputStream.bitStreamOrNull)
	}
}
