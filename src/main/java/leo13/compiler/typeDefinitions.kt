package leo13.compiler

import leo13.*
import leo13.type.TypeLine
import leo13.script.emptyIfEmpty
import leo13.script.lineTo

data class TypeDefinitions(val stack: Stack<TypeDefinition>) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			definitionsName lineTo stack.scripting.script.emptyIfEmpty

	fun plus(definition: TypeDefinition) =
		TypeDefinitions(stack.push(definition))

	fun resolve(line: TypeLine): TypeLine =
		stack.mapFirst { resolveOrNull(line) } ?: line
}

fun typeDefinitions() = TypeDefinitions(stack())
