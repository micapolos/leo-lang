package leo16.lambda.type

import leo14.Script
import leo14.emptyScript
import leo14.invoke
import leo14.plus
import leo14.script
import leo16.names.*
import leo16.nativeScript

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
			is LazyTypeBody -> lazy.script
			is RepeatingTypeBody -> repeating.script
			RepeatTypeBody -> _repeat().script
		}

val TypeLink.script
	get() =
		previousType.script.plus(lastSentence.scriptLine)

val TypeAlternative.script
	get() =
		firstType.script.plus(_or(secondType.script))

val TypeSentence.scriptLine
	get() =
		word(type.script)

val TypeFunction.script
	get() =
		parameterType.script.plus(_giving(resultType.script))

val TypeLazy.script
	get() =
		_lazy(resultType.script).script

val TypeRepeating.script
	get() =
		_repeating(type.script).script
