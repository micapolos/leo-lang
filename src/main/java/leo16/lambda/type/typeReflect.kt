package leo16.lambda.type

import leo14.Script
import leo14.ScriptLine
import leo14.emptyScript
import leo14.invoke
import leo14.plus
import leo14.script
import leo16.names.*
import leo16.nativeScript

val Type.reflect: ScriptLine
	get() =
		_type(reflectScript)

val Type.reflectScript: Script
	get() =
		(if (isStatic) _static else _dynamic)(body.reflect).script

val TypeBody.reflect: ScriptLine
	get() =
		_case(reflectScript)

val TypeBody.reflectScript: Script
	get() =
		when (this) {
			EmptyTypeBody -> emptyScript
			is LinkTypeBody -> link.previousType.reflectScript.plus(link.lastSentence.reflect)
			is AlternativeTypeBody -> alternative.reflectScript
			is FunctionTypeBody -> function.reflectScript
			is NativeTypeBody -> native.nativeScript
			is LazyTypeBody -> lazy.reflectScript
			is RepeatingTypeBody -> repeating.reflectScript
			RepeatTypeBody -> _repeat().script
		}

val TypeSentence.reflect: ScriptLine
	get() =
		word(type.reflect)

val TypeFunction.reflectScript: Script
	get() =
		parameterType.reflectScript.plus(_giving(resultType.reflectScript))

val TypeAlternative.reflectScript: Script
	get() =
		firstType.reflectScript.plus(_or(secondType.reflectScript))

val TypeLazy.reflectScript: Script
	get() =
		_lazy(resultType.reflectScript).script

val TypeRepeating.reflectScript: Script
	get() =
		_repeating(type.reflectScript).script
