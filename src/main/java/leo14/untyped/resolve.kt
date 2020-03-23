package leo14.untyped

import leo.base.ifOrNull
import leo13.fold
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
			?: resolveAnythingQuoteAnything
			?: resolveAnythingReplaceAnything
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
			?: resolveContent
			?: resolveName
			?: resolveMake
			?: resolveThis
			?: resolveAccess
			?: resolveGet
			?: resolvePrint
			?: resolvePrinted
			?: resolveLeonardo
			?: resolveAnythingEqualsAnything
			?: resolveIfThenElse
			?: resolveNative
			?: resolveAutoMake
			?: resolveFold
			?: resolveForce

val Sequence.resolveFunctionApplyAnything: Program?
	get() =
		matchInfix(applyName) { lhs, rhs ->
			lhs.functionOrNull?.apply(rhs)
		}

val Sequence.resolveAnythingDoFunction: Program?
	get() =
		matchInfix(doName) { lhs, rhs ->
			rhs.functionOrNull?.apply(lhs)
		}

val Sequence.resolveAccess: Program?
	get() =
		head.matchName { name ->
			tail.program.get(name)
		}

val Sequence.resolveGet: Program?
	get() =
		matchInfix(getName) { lhs, rhs ->
			rhs.matchName { name ->
				lhs.get(name)
			}
		}

val Sequence.resolveAnythingAppendAnything: Program?
	get() =
		matchInfix(appendName) { lhs, rhs ->
			lhs.onlyFieldOrNull?.let { field ->
				rhs.onlyValueOrNull?.let { value ->
					program(field.name valueTo field.rhs.plus(value))
				}
			}
		}

val Sequence.resolveAnythingItAnything: Program?
	get() =
		matchInfix(itName) { lhs, rhs ->
			rhs.onlyValueOrNull?.let { rhs ->
				lhs.plus(rhs)
			}
		}

val Sequence.resolveAnythingQuoteAnything: Program?
	get() =
		matchInfix(quoteName) { lhs, rhs ->
			lhs.plus(rhs)
		}

val Sequence.resolveAnythingReplaceAnything: Program?
	get() =
		matchInfix(replaceName) { _, rhs ->
			rhs
		}

val Sequence.resolveAnythingDelete: Program?
	get() =
		matchPostfix(deleteName) { program() }

val Sequence.resolveMake
	get() =
		matchInfix(makeName) { lhs, rhs ->
			rhs.matchName { name ->
				lhs.make(name)
			}
		}

val Sequence.resolveThis: Program?
	get() =
		matchPostfix(thisName) { lhs ->
			lhs._this
		}

val Sequence.resolveNumberPlusNumber: Program?
	get() =
		matchInfix(plusName) { lhs, rhs ->
			lhs.matchNumber { lhs ->
				rhs.matchNumber { rhs ->
					program(literal(lhs + rhs))
				}
			}
		}

val Sequence.resolveMinusNumber: Program?
	get() =
		matchInfix(minusName) { lhs, rhs ->
			lhs.matchEmpty {
				rhs.matchNumber { rhs ->
					program(literal(-rhs))
				}
			}
		}

val Sequence.resolveNumberMinusNumber: Program?
	get() =
		matchInfix(minusName) { lhs, rhs ->
			lhs.matchNumber { lhs ->
				rhs.matchNumber { rhs ->
					program(literal(lhs - rhs))
				}
			}
		}

val Sequence.resolveNumberTimesNumber: Program?
	get() =
		matchInfix(timesName) { lhs, rhs ->
			lhs.matchNumber { lhs ->
				rhs.matchNumber { rhs ->
					program(literal(lhs * rhs))
				}
			}
		}

val Sequence.resolveTextPlusText: Program?
	get() =
		matchInfix(plusName) { lhs, rhs ->
			lhs.matchText { lhs ->
				rhs.matchText { rhs ->
					program(literal(lhs + rhs))
				}
			}
		}

val Sequence.resolveHead: Program?
	get() =
		matchPostfix("head", Program::headOrNull)

val Sequence.resolveTail: Program?
	get() =
		matchPostfix("tail", Program::tailOrNull)

val Sequence.resolveLast: Program?
	get() =
		matchPostfix(lastName, Program::lastOrNull)

val Sequence.resolvePrevious: Program?
	get() =
		matchPostfix(previousName, Program::previousOrNull)

val Sequence.resolveContent: Program?
	get() =
		matchPostfix(contentName, Program::contentsOrNull)

val Sequence.resolveName: Program?
	get() =
		matchPostfix(textName) { lhs ->
			lhs.matchPostfix(nameName) { lhs ->
				lhs.nameOrNull?.let { name ->
					program(literal(name))
				}
			}
		}

val Sequence.resolveLeonardo: Program?
	get() =
		matchSimple("leonardo") {
			leonardoScript.program
		}

val Sequence.resolveAnythingEqualsAnything: Program?
	get() =
		matchInfix(equalsName) { lhs, rhs ->
			program(if (lhs == rhs) "yes" else "no")
		}

val Sequence.resolveIfThenElse: Program?
	get() =
		matchInfix(elseName) { lhs, alternate ->
			lhs.matchInfix(thenName) { lhs, consequent ->
				lhs.matchPrefix(ifName) { condition ->
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
				tail.program.matchNotEmpty {
					tail.program.make(name)
				}
			}
		}

val Sequence.resolvePrint: Program?
	get() =
		matchPostfix(printName) { lhs ->
			program().also { println(lhs) }
		}

val Sequence.resolvePrinted: Program?
	get() =
		matchPostfix(printedName) { lhs ->
			lhs.also { println(lhs) }
		}

val Sequence.resolveFold: Program?
	get() =
		matchInfix(doingName) { lhs, rhs ->
			rhs.functionOrNull?.let { function ->
				lhs.matchInfix(foldName) { folded, items ->
					items.contentsOrNull?.let { contents ->
						folded.fold(contents.valueStack) { value ->
							function
								.copy(context = function.context.push(
									rule(
										pattern(program(foldedName)),
										body(program(foldedName valueTo program(value))))))
								.apply(this)
						}
					}
				}
			}
		}

val Sequence.resolveForce: Program?
	get() =
		matchInfixThunk(forceName) { lhs, rhs ->
			rhs.program.matchEmpty {
				lhs.program
			}
		}