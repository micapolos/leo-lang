package leo.java.lang

import leo.base.EnumBit
import leo.base.Stream

fun <R> Class<*>.useResourceBitStreamOrNull(siblingName: String, fn: Stream<EnumBit>?.() -> R): R =
	classLoader.useResourceBitStreamOrNull(`package`.name.replace('.', '/') + '/' + siblingName, fn)

val String.typeClassOrNull: Class<*>?
	get() =
		when (this) {
			"boolean" -> java.lang.Boolean.TYPE
			"byte" -> java.lang.Byte.TYPE
			"short" -> java.lang.Short.TYPE
			"int" -> java.lang.Integer.TYPE
			"long" -> java.lang.Long.TYPE
			"float" -> java.lang.Float.TYPE
			"double" -> java.lang.Double.TYPE
			else -> null
		}
