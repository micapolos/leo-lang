package leo19.decompiler

import leo13.Empty
import leo13.Stack
import leo13.empty
import leo13.fold
import leo13.get
import leo13.push
import leo13.reverse
import leo13.stack
import leo13.toList
import leo14.Script
import leo14.ScriptLine
import leo14.lineTo
import leo14.plus
import leo14.script
import leo19.term.ArrayTerm
import leo19.type.ArrowType
import leo19.type.ChoiceType
import leo19.type.RecurseType
import leo19.type.RecursiveType
import leo19.type.StructType
import leo19.type.Type
import leo19.type.field
import leo19.type.isComplex
import leo19.type.isStatic
import leo19.type.reflectScript
import leo19.type.type
import leo19.typed.Typed
import leo19.typed.TypedChoice
import leo19.typed.TypedField
import leo19.typed.TypedStruct
import leo19.typed.index
import leo19.typed.of
import leo19.typed.yesTerm

data class Decompiler(val recursiveTypeStack: Stack<Type>)

val Empty.decompiler get() = Decompiler(stack())
fun Decompiler.push(type: Type) = Decompiler(recursiveTypeStack.push(type))
fun Decompiler.get(depth: Int) = recursiveTypeStack.get(depth)!!

val Typed.typedScript: Script
	get() =
		script.plus("of" lineTo type.reflectScript)

val Typed.script: Script
	get() = empty.decompiler.script(this)

fun Decompiler.script(typed: Typed): Script =
	when (typed.type) {
		is StructType -> script(typed.term.of(typed.type.struct))
		is ChoiceType -> script(typed.term.of(typed.type.choice))
		is ArrowType -> TODO()
		is RecursiveType -> push(typed.type.type).script(typed.term of typed.type.type)
		is RecurseType -> script(typed.term of get(typed.type.depth))
	}

fun Decompiler.scriptLine(typedField: TypedField): ScriptLine =
	typedField.name lineTo script(typedField.typed)

fun Decompiler.script(typedStruct: TypedStruct): Script =
	if (typedStruct.struct.isStatic) StructDecompiler(this, script(), typedStruct.struct.fieldStack.reverse)
		.dropStatic
		.decompile
	else if (!typedStruct.struct.isComplex) StructDecompiler(this, script(), typedStruct.struct.fieldStack.reverse)
		.plus(typedStruct.term)
		.decompile
	else StructDecompiler(this, script(), typedStruct.struct.fieldStack.reverse)
		.fold((typedStruct.term as ArrayTerm).stack.reverse) { plus(it) }
		.decompile

fun Decompiler.script(typedChoice: TypedChoice): Script =
	script(typedChoice.yesTerm
		.of(type(typedChoice.choice.caseStack.toList().get(typedChoice.index).field)))

