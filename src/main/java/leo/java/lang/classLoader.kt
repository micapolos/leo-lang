package leo.java.lang

import leo.base.Bit
import leo.base.Stream
import leo.java.io.bitStreamOrNull

fun <R> ClassLoader.useResourceBitStream(name: String, fn: Stream<Bit>?.() -> R): R =
	getResourceAsStream(name).let { inputStream ->
		if (inputStream == null) throw error("Resource not found: $name")
		else inputStream.use { fn(it.bitStreamOrNull) }
	}