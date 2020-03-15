package leo14.untyped

import leo14.*

val ScriptLink.resolve: Script?
	get() =
		null
			?: resolveAnythingAppendAnything
			?: resolveMinusNumber
			?: resolveNumberPlusNumber
			?: resolveNumberMinusNumber
			?: resolveNumberTimesNumber
			?: resolveTextPlusText
			?: resolveHead
			?: resolveTail
			?: resolveBody
			?: resolveMake
			?: resolveAccess

val ScriptLink.resolveAccess
	get() =
		line.matchField { field ->
			field.matchName { string ->
				lhs.get(string)
			}
		}

val ScriptLink.resolveAnythingAppendAnything
	get() =
		match("append") { lhs, rhs ->
			lhs.plus(rhs)
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

val ScriptLink.resolveNumberPlusNumber
	get() =
		match("plus") { lhs, rhs ->
			lhs.matchNumber { lhs ->
				rhs.matchNumber { rhs ->
					script(literal(lhs + rhs))
				}
			}
		}

val ScriptLink.resolveMinusNumber
	get() =
		match("minus") { lhs, rhs ->
			lhs.matchEmpty {
				rhs.matchNumber { rhs ->
					script(literal(-rhs))
				}
			}
		}

val ScriptLink.resolveNumberMinusNumber
	get() =
		match("minus") { lhs, rhs ->
			lhs.matchNumber { lhs ->
				rhs.matchNumber { rhs ->
					script(literal(lhs - rhs))
				}
			}
		}

val ScriptLink.resolveNumberTimesNumber
	get() =
		match("times") { lhs, rhs ->
			lhs.matchNumber { lhs ->
				rhs.matchNumber { rhs ->
					script(literal(lhs * rhs))
				}
			}
		}

val ScriptLink.resolveTextPlusText
	get() =
		match("plus") { lhs, rhs ->
			lhs.matchString { lhs ->
				rhs.matchString { rhs ->
					script(literal(lhs + rhs))
				}
			}
		}

val ScriptLink.resolveHead
	get() =
		matchSimple("head") { lhs ->
			lhs.matchLink { link ->
				script(link.line)
			}
		}

val ScriptLink.resolveTail
	get() =
		matchSimple("tail") { lhs ->
			lhs.matchLink { link ->
				link.lhs
			}
		}

val ScriptLink.resolveBody
	get() =
		matchSimple("body") { lhs ->
			lhs.matchBody { it }
		}
