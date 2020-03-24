package leo14.untyped

import leo.base.ifOrNull
import leo13.fold
import leo13.thisName
import leo14.*

val autoMake = false

val Value.resolve
	get() =
		when (this) {
			EmptyValue -> null
			is SequenceValue -> sequence.resolve
		}

val Sequence.resolve: Value?
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

val Sequence.resolveFunctionApplyAnything: Value?
	get() =
		matchInfixThunk(applyName) { lhs, rhs ->
			lhs.value.functionOrNull?.apply(rhs)?.value
		}

val Sequence.resolveAnythingDoFunction: Value?
	get() =
		matchInfixThunk(doName) { lhs, rhs ->
			rhs.value.functionOrNull?.apply(lhs)?.value
		}

val Sequence.resolveAccess: Value?
	get() =
		head.matchName { name ->
			tail.value.get(name)
		}

val Sequence.resolveGet: Value?
	get() =
		matchInfix(getName) { lhs, rhs ->
			rhs.matchName { name ->
				lhs.get(name)
			}
		}

val Sequence.resolveAnythingAppendAnything: Value?
	get() =
		matchInfix(appendName) { lhs, rhs ->
			lhs.onlyFieldOrNull?.let { field ->
				rhs.onlyLineOrNull?.let { line ->
					value(field.name lineTo field.rhs.plus(line))
				}
			}
		}

val Sequence.resolveAnythingItAnything: Value?
	get() =
		matchInfix(itName) { lhs, rhs ->
			rhs.onlyLineOrNull?.let { rhs ->
				lhs.plus(rhs)
			}
		}

val Sequence.resolveAnythingQuoteAnything: Value?
	get() =
		matchInfix(quoteName) { lhs, rhs ->
			lhs.plus(rhs)
		}

val Sequence.resolveAnythingReplaceAnything: Value?
	get() =
		matchInfix(replaceName) { _, rhs ->
			rhs
		}

val Sequence.resolveAnythingDelete: Value?
	get() =
		matchPostfix(deleteName) { value() }

val Sequence.resolveMake
	get() =
		matchInfix(makeName) { lhs, rhs ->
			rhs.matchName { name ->
				lhs.make(name)
			}
		}

val Sequence.resolveThis: Value?
	get() =
		matchPostfix(thisName) { lhs ->
			lhs._this
		}

val Sequence.resolveNumberPlusNumber: Value?
	get() =
		matchInfix(plusName) { lhs, rhs ->
			lhs.matchNumber { lhs ->
				rhs.matchNumber { rhs ->
					value(literal(lhs + rhs))
				}
			}
		}

val Sequence.resolveMinusNumber: Value?
	get() =
		matchInfix(minusName) { lhs, rhs ->
			lhs.matchEmpty {
				rhs.matchNumber { rhs ->
					value(literal(-rhs))
				}
			}
		}

val Sequence.resolveNumberMinusNumber: Value?
	get() =
		matchInfix(minusName) { lhs, rhs ->
			lhs.matchNumber { lhs ->
				rhs.matchNumber { rhs ->
					value(literal(lhs - rhs))
				}
			}
		}

val Sequence.resolveNumberTimesNumber: Value?
	get() =
		matchInfix(timesName) { lhs, rhs ->
			lhs.matchNumber { lhs ->
				rhs.matchNumber { rhs ->
					value(literal(lhs * rhs))
				}
			}
		}

val Sequence.resolveTextPlusText: Value?
	get() =
		matchInfix(plusName) { lhs, rhs ->
			lhs.matchText { lhs ->
				rhs.matchText { rhs ->
					value(literal(lhs + rhs))
				}
			}
		}

val Sequence.resolveHead: Value?
	get() =
		matchPostfix("head", Value::headOrNull)

val Sequence.resolveTail: Value?
	get() =
		matchPostfix("tail", Value::tailOrNull)

val Sequence.resolveLast: Value?
	get() =
		matchPostfix(lastName, Value::lastOrNull)

val Sequence.resolvePrevious: Value?
	get() =
		matchPostfix(previousName, Value::previousOrNull)

val Sequence.resolveContent: Value?
	get() =
		matchPostfix(contentName, Value::contentsOrNull)

val Sequence.resolveName: Value?
	get() =
		matchPostfix(textName) { lhs ->
			lhs.matchPostfix(nameName) { lhs ->
				lhs.nameOrNull?.let { name ->
					value(literal(name))
				}
			}
		}

val Sequence.resolveLeonardo: Value?
	get() =
		matchSimple("leonardo") {
			leonardoScript.value
		}

val Sequence.resolveAnythingEqualsAnything: Value?
	get() =
		matchInfix(equalsName) { lhs, rhs ->
			value(if (lhs == rhs) "yes" else "no")
		}

val Sequence.resolveIfThenElse: Value?
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

val Sequence.resolveAutoMake: Value?
	get() =
		ifOrNull(autoMake) {
			head.matchName { name ->
				tail.value.matchNotEmpty {
					tail.value.make(name)
				}
			}
		}

val Sequence.resolvePrint: Value?
	get() =
		matchPostfix(printName) { lhs ->
			value().also { println(lhs) }
		}

val Sequence.resolvePrinted: Value?
	get() =
		matchPostfix(printedName) { lhs ->
			lhs.also { println(lhs) }
		}

val Sequence.resolveFold: Value?
	get() =
		matchInfix(doingName) { lhs, rhs ->
			rhs.functionOrNull?.let { function ->
				lhs.matchInfixThunk(foldName) { folded, items ->
					items.value.contentsOrNull?.let { contents ->
						folded.fold(contents.lineStack) { line ->
							function
								.copy(context = function.context.push(
									rule(
										pattern(value(foldedName)),
										body(thunk(value(foldedName lineTo value(line)))))))
								.apply(this)
						}.value // TODO: To thunk
					}
				}
			}
		}

val Sequence.resolveForce: Value?
	get() =
		matchInfixThunk(forceName) { lhs, rhs ->
			rhs.value.matchEmpty {
				lhs.value
			}
		}