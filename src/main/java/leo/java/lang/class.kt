package leo.java.lang

import leo.base.Bit
import leo.base.Stream

fun <R> Class<*>.useResourceBitStreamOrNull(siblingName: String, fn: Stream<Bit>?.() -> R): R =
	classLoader.useResourceBitStreamOrNull(`package`.name.replace('.', '/') + '/' + siblingName, fn)