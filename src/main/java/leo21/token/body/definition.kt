package leo21.token.body

import leo14.ScriptLine
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.lambda.value.Value
import leo14.lineTo
import leo21.compiled.ArrowCompiled
import leo21.compiled.Compiled
import leo21.compiled.of
import leo21.prim.Prim
import leo21.prim.runtime.value
import leo21.type.script

sealed class Definition
data class ArrowCompiledDefinition(val arrowCompiled: ArrowCompiled) : Definition()

val ArrowCompiled.asDefinition: Definition get() = ArrowCompiledDefinition(this)

fun Compiled.wrap(definition: Definition): Compiled =
	when (definition) {
		is ArrowCompiledDefinition -> fn(term).invoke(definition.arrowCompiled.term).of(type)
	}

val Definition.binding: Binding
	get() =
		when (this) {
			is ArrowCompiledDefinition -> arrowCompiled.arrow.binding
		}

val Definition.printScriptLine: ScriptLine
	get() =
		when (this) {
			is ArrowCompiledDefinition -> "function" lineTo arrowCompiled.arrow.script
		}

val Definition.value: Value<Prim>
	get() =
		when (this) {
			is ArrowCompiledDefinition -> arrowCompiled.term.value
		}