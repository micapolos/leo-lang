package leo13.base

import leo13.base.type.Type
import leo13.base.type.toString

val stringType: Type<String> =
	Type(
		textType.name,
		{ textType.bodyScript(text(this)) },
		{ textType.unsafeBodyValue(this).string })

data class StringLeo(val string: String) {
	override fun toString() = stringType.toString(string)
}

fun leo(string: String) = StringLeo(string)
