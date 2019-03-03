package leo.java.lang

import leo.base.Stream
import leo.binary.Bit

fun <R> Class<*>.useResourceBitStreamOrNull(siblingName: String, fn: Stream<Bit>?.() -> R): R =
	classLoader.useResourceBitStreamOrNull(`package`.name.replace('.', '/') + '/' + siblingName, fn)