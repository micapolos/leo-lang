package leo14.untyped.typed.lambda

import leo13.typeName

val Typed.coreApply: Typed?
	get() =
		null
			?: applyType
			?: applyGet // must be last

val Typed.applyGet: Typed?
	get() =
		matchPrefix { rhs ->
			rhs.get(this)
		}

val Typed.applyType: Typed?
	get() =
		matchPrefix(typeName) {
			type.script.typed
		}