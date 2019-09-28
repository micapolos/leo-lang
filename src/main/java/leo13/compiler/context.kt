package leo13.compiler

import leo13.ObjectScripting
import leo13.contextName
import leo13.givenName
import leo13.matchingName
import leo13.script.lineTo
import leo13.script.script
import leo13.type.Type
import leo13.type.TypeLine
import leo13.type.lineTo
import leo13.type.type

data class Context(
	val typeDefinitions: TypeDefinitions,
	val typeLines: TypeLines,
	val functions: Functions,
	val givenType: Type,
	val matchingType: Type) : ObjectScripting() {
	override val scriptingLine
		get() =
			contextName lineTo script(
				typeDefinitions.scriptingLine,
				typeLines.scriptingLine,
				functions.scriptingLine,
				givenName lineTo script(givenType.scriptingLine),
				matchingName lineTo script(matchingType.scriptingLine))
}

fun context() = Context(typeDefinitions(), typeLines(), functions(), type(), type())

fun Context.plus(definition: TypeDefinition) =
	copy(typeDefinitions = typeDefinitions.plus(definition))

fun Context.plus(line: TypeLine) =
	copy(typeLines = typeLines.plus(line))

fun Context.plus(typedFunction: FunctionTyped) =
	copy(functions = functions.plus(typedFunction))

fun Context.give(type: Type) =
	copy(givenType = givenType.plus(givenName lineTo type))

fun Context.plusMatch(type: Type) =
	copy(matchingType = matchingType.plus(matchingName lineTo type))
