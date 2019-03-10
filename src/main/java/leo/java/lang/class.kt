package leo.java.lang

import leo.base.EnumBit
import leo.base.Stream

fun <R> Class<*>.useResourceBitStreamOrNull(siblingName: String, fn: Stream<EnumBit>?.() -> R): R =
	classLoader.useResourceBitStreamOrNull(`package`.name.replace('.', '/') + '/' + siblingName, fn)