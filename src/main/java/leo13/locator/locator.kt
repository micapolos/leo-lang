package leo13.locator

import leo13.*
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

data class Locator(
	val processor: Processor<Char>,
	val location: Location) : ScriptingObject() {
	override fun toString() = super.toString()
	override val scriptingLine: ScriptLine
		get() =
			"locator" lineTo script(processor.scriptingLine, location.scriptingLine)
}

fun Processor<Char>.locator(location: Location = location()): Locator =
	Locator(this, location)

fun Locator.plus(char: Char): Locator =
	trace {
		location.scriptingLine
	}.traced {
		processor.process(char).run { locator(location.plus(char)) }
	}

val Locator.processor: Processor<Char>
	get() =
		processor { plus(it) }
