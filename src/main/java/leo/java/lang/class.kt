package leo.java.lang

import leo.base.Bit
import leo.base.Stream

fun <R> Class<*>.useResourceBitStream(siblingName: String, fn: Stream<Bit>?.() -> R): R =
	classLoader.useResourceBitStream(`package`.name.replace('.', '/') + '/' + siblingName, fn)