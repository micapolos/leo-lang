package leo13.base

import leo13.scripter.Scripter
import leo13.scripter.toString

val stringScripter: Scripter<String> =
	Scripter(
		textScripter.name,
		{ textScripter.bodyScript(text(this)) },
		{ textScripter.unsafeBodyValue(this).string })

data class StringLeo(val string: String) {
	override fun toString() = stringScripter.toString(string)
}

fun leo(string: String) = StringLeo(string)
