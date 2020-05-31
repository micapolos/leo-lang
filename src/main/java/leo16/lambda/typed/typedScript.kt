package leo16.lambda.typed

import leo14.Script
import leo14.ScriptLine
import leo14.emptyScript
import leo14.invoke
import leo14.plus
import leo15.lambda.choiceTerm
import leo15.lambda.unsafeUnchoice
import leo15.lambda.value
import leo16.lambda.type.Type
import leo16.lambda.type.or
import leo16.lambda.type.script
import leo16.names.*
import leo16.nativeScriptLine

val Typed.script: Script
	get() =
		bodyTyped.script

val BodyTyped.script: Script
	get() =
		match(
			{ emptyScript },
			{ it.script },
			{ it.script },
			{ it.script })

val LinkTyped.script: Script
	get() =
		previousTyped.script.plus(lastFieldTyped.scriptLine)

val AlternativeTyped.script: Script
	get() =
		term.unsafeUnchoice(2).run {
			if (index == 1) (value of alternative.firstType).script
			else (value of alternative.secondType).script
		}

val FieldTyped.scriptLine: ScriptLine
	get() =
		match(
			{ it.scriptLine },
			{ it.scriptLine })

val SentenceTyped.scriptLine: ScriptLine
	get() =
		sentence.word.invoke(rhsTyped.script)

val FunctionTyped.scriptLine: ScriptLine
	get() =
		_taking(function.input.script.plus(_giving(function.output.script)))

val FunctionTyped.script: Script
	get() =
		function.input.script.plus(_giving(function.output.script))

val NativeTyped.scriptLine: ScriptLine
	get() =
		term.value!!.nativeScriptLine

fun Typed.or(type: Type): Typed =
	choiceTerm(2, 1, term) of (this.type or type)

fun Type.or(typed: Typed): Typed =
	choiceTerm(2, 0, typed.term) of (this or typed.type)