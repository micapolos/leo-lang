package leo13.decompiler

import leo13.ObjectScripting
import leo13.Processor
import leo13.decompilerName
import leo13.interpreter.ValueTyped
import leo13.script.Script
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script
import leo13.value.scriptOrNull

data class Decompiler(val processor: Processor<Script>) : ObjectScripting(), Processor<ValueTyped> {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = decompilerName lineTo script(processor.scriptingLine)

	override fun process(typed: ValueTyped) =
		Decompiler(processor.process(typed.value.scriptOrNull!!))
}

fun Processor<Script>.decompiler() = Decompiler(this)