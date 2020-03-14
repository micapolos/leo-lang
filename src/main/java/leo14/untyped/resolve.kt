package leo14.untyped

import leo14.*

val ScriptLink.resolve: Script?
	get() =
		null
			?: resolveNumberAddNumber
			?: resolveNumberSubtractNumber
			?: resolveNumberMultiplyByNumber
			?: resolveStringAppendString
			?: resolveMake
			?: resolveAccess

val ScriptLink.resolveAccess
	get() =
		line.matchField { field ->
			field.matchName { string ->
				lhs.get(string)
			}
		}

// TODO: Maybe support multiple names at once?
val ScriptLink.resolveMake
	get() =
		line.matchField { field ->
			field.match("make") { rhs ->
				rhs.matchName { name ->
					lhs.make(name)
				}
			}
		}

val ScriptLink.resolveNumberAddNumber
	get() =
		match("add") { lhs, rhs ->
			lhs.matchNumber { lhs ->
				rhs.matchNumber { rhs ->
					script(literal(lhs + rhs))
				}
			}
		}

val ScriptLink.resolveNumberSubtractNumber
	get() =
		match("subtract") { lhs, rhs ->
			lhs.matchNumber { lhs ->
				rhs.matchNumber { rhs ->
					script(literal(lhs - rhs))
				}
			}
		}

val ScriptLink.resolveNumberMultiplyByNumber
	get() =
		match("multiply", "by") { lhs, rhs ->
			lhs.matchNumber { lhs ->
				rhs.matchNumber { rhs ->
					script(literal(lhs * rhs))
				}
			}
		}

val ScriptLink.resolveStringAppendString
	get() =
		match("append") { lhs, rhs ->
			lhs.matchString { lhs ->
				rhs.matchString { rhs ->
					script(literal(lhs + rhs))
				}
			}
		}
