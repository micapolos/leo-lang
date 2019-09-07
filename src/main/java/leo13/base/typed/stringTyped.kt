package leo13.base.typed

import leo13.base.Typed
import leo13.base.string
import leo13.base.text
import leo13.base.textType
import leo13.base.type.Type

val stringType: Type<String> =
	Type(
		textType.name,
		{ textType.bodyScript(text(this)) },
		{ textType.unsafeBodyValue(this).string })

data class StringTyped(val string: String) : Typed<String>() {
	override fun toString() = super.toString()
	override val type = stringType
}

fun typed(string: String) = StringTyped(string)
