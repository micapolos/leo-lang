package leo13.untyped

import leo13.script.*

data class Option(val name: String, val rhs: Pattern) {
	override fun toString() = optionWriter.string(this)
}

infix fun String.optionTo(rhs: Pattern) = Option(this, rhs)

fun Option.matches(scriptLine: ScriptLine): Boolean =
	name == scriptLine.name && rhs.matches(scriptLine.rhs)

val optionName = "option"

val optionReader: Reader<Option> =
	reader(optionName) {
		unsafeOnlyLine.run {
			name optionTo patternReader.unsafeBodyValue(rhs)
		}
	}

val optionWriter: Writer<Option> =
	writer(optionName) {
		script(name lineTo patternWriter.bodyScript(rhs))
	}
