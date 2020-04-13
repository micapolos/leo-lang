package leo14.untyped.typed.lambda

import leo14.untyped.setName

val Typed.coreApply: Typed?
	get() =
		null
			?: applyThis
			?: applyType
			?: applySet
			?: applyGet // must be last

val Typed.applyThis: Typed?
	get() =
		matchPrefix(leo14.untyped.thisName) { this }

val Typed.applyType: Typed?
	get() =
		matchPrefix(leo14.untyped.typeName) {
			type.script.typed
		}

val Typed.applySet: Typed?
	get() =
		matchInfix(setName) { rhs ->
			let { target ->
				rhs.matchPrefix { rhs ->
					let { name ->
						target.setOrNull(name, rhs)
					}
				}
			}
		}

val Typed.applyGet: Typed?
	get() =
		matchPrefix { rhs ->
			rhs.get(this)
		}
