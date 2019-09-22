package leo.java.lang

import leo.base.EnumBit
import leo.base.Seq
import leo.base.Stream
import leo.java.io.bitStreamOrNull
import leo.java.io.charSeq
import java.io.InputStream

fun <R> ClassLoader.useResourceInputStream(name: String, fn: InputStream.() -> R): R =
	getResourceAsStream(name).let { inputStreamOrNull ->
		if (inputStreamOrNull == null) throw error("Resource not found: $name")
		else inputStreamOrNull.use { fn(it) }
	}

fun <R> ClassLoader.useResourceCharSeq(name: String, fn: Seq<Char>.() -> R): R =
	useResourceInputStream(name) { reader().charSeq.fn() }

fun <R> ClassLoader.useResourceBitStreamOrNull(name: String, fn: Stream<EnumBit>?.() -> R): R =
	getResourceAsStream(name).let { inputStream ->
		if (inputStream == null) throw error("Resource not found: $name")
		else inputStream.use { fn(it.bitStreamOrNull) }
	}
