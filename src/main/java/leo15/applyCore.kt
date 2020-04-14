package leo15

import leo.base.notNullIf
import leo14.lambda2.valueApply

val Typed.coreApply: Typed?
	get() =
		null
			?: applyEquals
			?: applyThis
			?: applyType
			?: applySet
			?: applyGet // must be last

val Typed.applyEquals: Typed?
	get() =
		matchInfix(equalsName) { rhs ->
			notNullIf(type == rhs.type) {
				booleanType.typed(
					term.valueApply(rhs.term) { rhs ->
						(this == rhs).value
					})
			}
		}

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
