package leo.java.lang

import leo.base.EnumBit
import leo.base.Stream
import leo.java.io.bitStreamOrNull

fun <R> ClassLoader.useResourceBitStreamOrNull(name: String, fn: Stream<EnumBit>?.() -> R): R =
	getResourceAsStream(name).let { inputStream ->
		if (inputStream == null) throw error("Resource not found: $name")
		else inputStream.use { fn(it.bitStreamOrNull) }
	}