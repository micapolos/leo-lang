package leo16.lambda.type

import leo14.Script
import leo14.ScriptLine
import leo14.emptyScript
import leo14.invoke
import leo14.plus
import leo16.names.*
import leo16.nativeScript
import leo16.nativeScriptLine

val Type.script: Script
	get() =
		body.script

val TypeBody.script: Script
	get() =
		when (this) {
			EmptyTypeBody -> emptyScript
			is LinkTypeBody -> link.script
			is AlternativeTypeBody -> alternative.script
			is FunctionTypeBody -> function.script
			is NativeTypeBody -> native.nativeScript
		}

val TypeLink.script
	get() =
		previousType.script.plus(lastField.scriptLine)

val TypeAlternative.script
	get() =
		firstType.script.plus(_or(secondType.script))

val TypeField.scriptLine: ScriptLine
	get() =
		when (this) {
			is SentenceTypeField -> sentence.scriptLine
			is NativeTypeField -> native.nativeScriptLine
		}

val TypeSentence.scriptLine
	get() =
		word(type.script)

val TypeFunction.script
	get() =
		input.script.plus(_giving(output.script))
