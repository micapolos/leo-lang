package leo19.compiler

import leo.base.fold
import leo.base.reverse
import leo13.Stack
import leo13.onlyOrNull
import leo13.push
import leo13.stack
import leo13.toList
import leo14.FieldScriptLine
import leo14.LiteralScriptLine
import leo14.Script
import leo14.ScriptField
import leo14.ScriptLine
import leo14.lineSeq
import leo19.type.Arrow
import leo19.type.indexedOrNull
import leo19.type.native
import leo19.type.structOrNull
import leo19.typed.Typed
import leo19.typed.TypedField
import leo19.typed.typed
import leo19.value.ListGetValue

data class Compiler(
	val context: Stack<Arrow>,
	val typedFieldStack: Stack<TypedField>
)

val emptyCompiler = Compiler(stack(), stack())

fun Compiler.plus(script: Script): Compiler =
	fold(script.lineSeq.reverse) { plus(it) }

fun Compiler.plus(scriptLine: ScriptLine) =
	when (scriptLine) {
		is LiteralScriptLine -> TODO()
		is FieldScriptLine -> plus(scriptLine.field)
	}

fun Compiler.plus(scriptField: ScriptField) =
	plus(
		TypedField(
			scriptField.string,
			Compiler(context, stack()).plus(scriptField.rhs).typed))

fun Compiler.plus(typedField: TypedField): Compiler =
	copy(typedFieldStack = typedFieldStack.push(typedField))

fun Compiler.plus(name: String): Compiler =
	null
		?: maybePlusGet(name)
		?: plusMake(name)

fun Compiler.maybePlusGet(name: String): Compiler? =
	typedFieldStack.onlyOrNull?.let { typedField ->
		typedField.value.let { typed ->
			typed.type.structOrNull?.let { struct ->
				struct.indexedOrNull(name)?.let { indexed ->
					set(
						TypedField(
							typedField.name,
							Typed(
								ListGetValue(typed.value, indexed.index),
								native(indexed.value))))
				}
			}
		}
	}

fun Compiler.plusMake(name: String): Compiler =
	set(TypedField(name, typed))

fun Compiler.set(typedField: TypedField) =
	copy(typedFieldStack = stack(typedField))

val Compiler.typed
	get() =
		typed(*typedFieldStack.toList().toTypedArray())