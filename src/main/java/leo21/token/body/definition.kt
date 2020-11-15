package leo21.token.body

import leo14.ScriptLine
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.lineTo
import leo14.script
import leo21.compiled.ArrowCompiled
import leo21.compiled.Compiled
import leo21.compiled.LineCompiled
import leo21.compiled.castOrNull
import leo21.compiled.of
import leo21.type.Line
import leo21.type.script
import leo21.type.scriptLine

sealed class Definition
data class ArrowCompiledDefinition(val arrowCompiled: ArrowCompiled) : Definition()
data class LineDefinition(val line: Line) : Definition()

val ArrowCompiled.asDefinition: Definition get() = ArrowCompiledDefinition(this)
val Line.asDefinition: Definition get() = LineDefinition(this)

fun Compiled.wrap(definition: Definition): Compiled =
	when (definition) {
		is ArrowCompiledDefinition -> fn(term).invoke(definition.arrowCompiled.term).of(type)
		is LineDefinition -> this
	}

val Definition.bindingOrNull: Binding?
	get() =
		when (this) {
			is ArrowCompiledDefinition -> arrowCompiled.arrow.binding
			is LineDefinition -> null
		}

val Definition.printScriptLine: ScriptLine
	get() =
		when (this) {
			is ArrowCompiledDefinition -> "function" lineTo arrowCompiled.arrow.script
			is LineDefinition -> "type" lineTo script(line.scriptLine)
		}

fun Definition.castOrNull(lineCompiled: LineCompiled): LineCompiled? =
	when (this) {
		is ArrowCompiledDefinition -> null
		is LineDefinition -> lineCompiled.castOrNull(line)?.t
	}