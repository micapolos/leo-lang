package leo13.base.typed

import leo13.base.Typed
import leo13.base.string
import leo13.base.text
import leo13.base.textType
import leo13.base.type.Type
import leo13.base.type.nativeType

val stringType: Type<String> =
	nativeType(
		textType,
		{ text(this) },
		{ string })

data class StringTyped(val string: String) : Typed<String>() {
	override fun toString() = super.toString()
	override val type = stringType
}

fun typed(string: String) = StringTyped(string)
