package leo16.lambda.typed

import leo14.Script
import leo14.ScriptLine
import leo14.emptyScript
import leo14.invoke
import leo14.plus
import leo14.script
import leo15.lambda.choiceTerm
import leo15.lambda.unsafeUnchoice
import leo15.lambda.value
import leo16.lambda.type.Type
import leo16.lambda.type.or
import leo16.lambda.type.script
import leo16.names.*
import leo16.nativeScript
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
			{ it.script },
			{ it.term.value.nativeScript },
			{ it.scriptLine.script })

val LinkTyped.script: Script
	get() =
		previousTyped.script.plus(lastSentenceTyped.scriptLine)

val AlternativeTyped.script: Script
	get() =
		term.unsafeUnchoice(2).run {
			if (index == 1) (value of alternative.firstType).script
			else (value of alternative.secondType).script
		}

val SentenceTyped.scriptLine: ScriptLine
	get() =
		sentence.word.invoke(rhsTyped.script)

val FunctionTyped.scriptLine: ScriptLine
	get() =
		_taking(function.parameterType.script.plus(_giving(function.resultType.script)))

val FunctionTyped.script: Script
	get() =
		function.parameterType.script.plus(_giving(function.resultType.script))

val NativeTyped.scriptLine: ScriptLine
	get() =
		term.value!!.nativeScriptLine

val LazyTyped.scriptLine: ScriptLine
	get() =
		_lazy(lazy.script)

fun Typed.or(type: Type): Typed =
	choiceTerm(2, 1, term) of (this.type or type)

fun Type.or(typed: Typed): Typed =
	choiceTerm(2, 0, typed.term) of (this or typed.type)