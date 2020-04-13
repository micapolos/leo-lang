package leo14.untyped.typed.lambda

import leo13.thisName
import leo13.typeName

val Typed.coreApply: Typed?
	get() =
		null
			?: applyThis
			?: applyType
			?: applyGet // must be last

val Typed.applyGet: Typed?
	get() =
		matchPrefix { rhs ->
			rhs.get(this)
		}

val Typed.applyThis: Typed?
	get() =
		matchPrefix(thisName) { this }

val Typed.applyType: Typed?
	get() =
		matchPrefix(typeName) {
			type.script.typed
		}