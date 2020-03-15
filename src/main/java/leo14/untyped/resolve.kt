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
			lhs.matchString { lhs ->
				rhs.matchString { rhs ->
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

val Sequence.resolveBody
	get() =
		matchPostfix("body", Program::bodyOrNull)
