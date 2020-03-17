package leo14.untyped

import leo14.*

val Program.resolve
	get() =
		when (this) {
			EmptyProgram -> null
			is SequenceProgram -> sequence.resolve
		}

val Sequence.resolve: Program?
	get() =
		null
			?: resolveFunctionApplyAnything
			?: resolveAnythingDoFunction
			?: resolveAnythingAppendAnything
			?: resolveAnythingChangeToAnything
			?: resolveAnythingDelete
			?: resolveMinusNumber
			?: resolveNumberPlusNumber
			?: resolveNumberMinusNumber
			?: resolveNumberTimesNumber
			?: resolveTextPlusText
			?: resolveHead
			?: resolveTail
			?: resolveContents
			?: resolveMake
			?: resolveAccess
			?: resolveLeonardo
			?: resolveAnythingEqualsAnything
			?: resolveIfThenElse

val Sequence.resolveFunctionApplyAnything: Program?
	get() =
		matchInfix("apply") { lhs, rhs ->
			lhs.functionOrNull?.apply(rhs)
		}

val Sequence.resolveAnythingDoFunction: Program?
	get() =
		matchInfix("do") { lhs, rhs ->
			rhs.functionOrNull?.apply(lhs)
		}

val Sequence.resolveAccess: Program?
	get() =
		head.matchName { name ->
			tail.get(name)
		}

val Sequence.resolveAnythingAppendAnything
	get() =
		matchInfix("append") { lhs, rhs ->
			lhs.plus(rhs)
		}

val Sequence.resolveAnythingChangeToAnything
	get() =
		matchInfix("change", "to") { _, rhs ->
			rhs
		}

val Sequence.resolveAnythingDelete
	get() =
		matchPostfix("delete") { program() }

val Sequence.resolveMake
	get() =
		matchInfix("make") { lhs, rhs ->
			rhs.matchName { name ->
				lhs.make(name)
			}
		}

val Sequence.resolveNumberPlusNumber
	get() =
		matchInfix("plus") { lhs, rhs ->
			lhs.matchNumber { lhs ->
				rhs.matchNumber { rhs ->
					program(literal(lhs + rhs))
				}
			}
		}

val Sequence.resolveMinusNumber
	get() =
		matchInfix("minus") { lhs, rhs ->
			lhs.matchEmpty {
				rhs.matchNumber { rhs ->
					program(literal(-rhs))
				}
			}
		}

val Sequence.resolveNumberMinusNumber
	get() =
		matchInfix("minus") { lhs, rhs ->
			lhs.matchNumber { lhs ->
				rhs.matchNumber { rhs ->
					program(literal(lhs - rhs))
				}
			}
		}

val Sequence.resolveNumberTimesNumber
	get() =
		matchInfix("times") { lhs, rhs ->
			lhs.matchNumber { lhs ->
				rhs.matchNumber { rhs ->
					program(literal(lhs * rhs))
				}
			}
		}

val Sequence.resolveTextPlusText
	get() =
		matchInfix("plus") { lhs, rhs ->
			lhs.matchText { lhs ->
				rhs.matchText { rhs ->
					program(literal(lhs + rhs))
				}
			}
		}

val Sequence.resolveHead
	get() =
		matchPostfix("head", Program::headOrNull)

val Sequence.resolveTail
	get() =
		matchPostfix("tail", Program::tailOrNull)

val Sequence.resolveContents
	get() =
		matchPostfix("contents", Program::contentsOrNull)

val Sequence.resolveLeonardo
	get() =
		matchSimple("leonardo") {
			leonardoScript.program
		}

val Sequence.resolveAnythingEqualsAnything
	get() =
		matchInfix("equals") { lhs, rhs ->
			program(if (lhs == rhs) "yes" else "no")
		}

val Sequence.resolveIfThenElse
	get() =
		matchInfix("else") { lhs, alternate ->
			lhs.matchInfix("then") { lhs, consequent ->
				lhs.matchPrefix("if") { condition ->
					condition.matchName { name ->
						when (name) {
							"yes" -> consequent
							"no" -> alternate
							else -> null
						}
					}
				}
			}
		}
