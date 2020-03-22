package leo14.untyped

import leo.base.ifOrNull
import leo13.fold
import leo13.reverse
import leo13.thisName
import leo14.*

val autoMake = false

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
			?: resolveAnythingItAnything
			?: resolveAnythingQuotedAnything
			?: resolveAnythingChangeToAnything
			?: resolveAnythingDelete
			?: resolveMinusNumber
			?: resolveNumberPlusNumber
			?: resolveNumberMinusNumber
			?: resolveNumberTimesNumber
			?: resolveTextPlusText
			?: resolveHead
			?: resolveTail
			?: resolveLast
			?: resolvePrevious
			?: resolveContents
			?: resolveMake
			?: resolveThis
			?: resolveAccess
			?: resolveGet
			?: resolvePrint
			?: resolveLeonardo
			?: resolveAnythingEqualsAnything
			?: resolveIfThenElse
			?: resolveNative
			?: resolveAutoMake
			?: resolveFold

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

val Sequence.resolveGet: Program?
	get() =
		matchInfix("get") { lhs, rhs ->
			rhs.matchName { name ->
				lhs.get(name)
			}
		}

val Sequence.resolveAnythingAppendAnything
	get() =
		matchInfix("append") { lhs, rhs ->
			lhs.plus(rhs)
		}

val Sequence.resolveAnythingItAnything
	get() =
		matchInfix("it") { lhs, rhs ->
			rhs.onlyValueOrNull?.let { rhs ->
				lhs.plus(rhs)
			}
		}

val Sequence.resolveAnythingQuotedAnything
	get() =
		matchInfix("quoted") { lhs, rhs ->
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

val Sequence.resolveThis
	get() =
		matchPostfix(thisName) { lhs ->
			lhs._this
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

val Sequence.resolveLast
	get() =
		matchPostfix("last", Program::lastOrNull)

val Sequence.resolvePrevious
	get() =
		matchPostfix("previous", Program::previousOrNull)

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

val Sequence.resolveAutoMake: Program?
	get() =
		ifOrNull(autoMake) {
			head.matchName { name ->
				tail.matchNotEmpty {
					tail.make(name)
				}
			}
		}

val Sequence.resolvePrint: Program?
	get() =
		matchPostfix("print") { lhs ->
			program().also { println(lhs) }
		}

val Sequence.resolveFold: Program?
	get() =
		matchInfix("doing") { lhs, rhs ->
			rhs.functionOrNull?.let { function ->
				lhs.matchInfix("fold") { folded, items ->
					items.contentsOrNull?.let { contents ->
						folded.fold(contents.valueStack.reverse) { value ->
							function.apply(
								program(
									"folded" valueTo this,
									"next" valueTo program(value)))
						}
					}
				}
			}
		}