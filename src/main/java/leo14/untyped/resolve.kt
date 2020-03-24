package leo14.untyped

import leo.base.ifOrNull
import leo.java.lang.exec
import leo13.fold
import leo13.thisName
import leo14.*

val autoMake = false

val Value.resolve: Thunk?
	get() =
		when (this) {
			EmptyValue -> null
			is SequenceValue -> sequence.resolve
		}

val Thunk.resolve: Thunk?
	get() =
		value.resolve

val Sequence.resolve: Thunk?
	get() =
		null
			?: resolveFunctionApplyAnything
			?: resolveAnythingCallFunction
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
			?: resolveNative
			?: resolveAutoMake
			?: resolveFold
			?: resolveForce
			?: resolveScript
			?: resolveSay

val Sequence.resolveFunctionApplyAnything: Thunk?
	get() =
		matchInfixThunk(applyName) { lhs, rhs ->
			lhs.matchFunction { function ->
				function.apply(rhs)
			}
		}

val Sequence.resolveAnythingCallFunction: Thunk?
	get() =
		matchInfixThunk(callName) { lhs, rhs ->
			rhs.matchFunction { function ->
				function.apply(lhs)
			}
		}

val Sequence.resolveAccess: Thunk?
	get() =
		head.matchName { name ->
			tail.value.get(name)
		}

val Sequence.resolveGet: Thunk?
	get() =
		matchInfixThunk(getName) { lhs, rhs ->
			rhs.matchName { name ->
				lhs.get(name)
			}
		}

val Sequence.resolveAnythingAppendAnything: Thunk?
	get() =
		matchInfixThunk(appendName) { lhs, rhs ->
			lhs.value.onlyFieldOrNull?.let { field ->
				rhs.value.onlyLineOrNull?.let { line ->
					thunk(value(field.name lineTo field.thunk.plus(line)))
				}
			}
		}

val Sequence.resolveAnythingItAnything: Thunk?
	get() =
		matchInfixThunk(itName) { lhs, rhs ->
			rhs.value.onlyLineOrNull?.let { rhs ->
				lhs.plus(rhs)
			}
		}

val Sequence.resolveAnythingQuoteAnything: Thunk?
	get() =
		matchInfixThunk(quoteName) { lhs, rhs ->
			lhs.plus(rhs)
		}

val Sequence.resolveAnythingReplaceAnything: Thunk?
	get() =
		matchInfixThunk(replaceName) { _, rhs ->
			rhs
		}

val Sequence.resolveAnythingDelete: Thunk?
	get() =
		matchPostfixThunk(deleteName) { thunk(value()) }

val Sequence.resolveMake: Thunk?
	get() =
		matchInfixThunk(makeName) { lhs, rhs ->
			rhs.matchName { name ->
				lhs.make(name)
			}
		}

val Sequence.resolveThis: Thunk?
	get() =
		matchPostfixThunk(thisName) { lhs ->
			lhs.this_
		}

val Sequence.resolveNumberPlusNumber: Thunk?
	get() =
		matchInfixThunk(plusName) { lhs, rhs ->
			lhs.matchNumber { lhs ->
				rhs.matchNumber { rhs ->
					thunk(value(literal(lhs + rhs)))
				}
			}
		}

val Sequence.resolveMinusNumber: Thunk?
	get() =
		matchInfixThunk(minusName) { lhs, rhs ->
			lhs.matchEmpty {
				rhs.matchNumber { rhs ->
					thunk(value(literal(-rhs)))
				}
			}
		}

val Sequence.resolveNumberMinusNumber: Thunk?
	get() =
		matchInfixThunk(minusName) { lhs, rhs ->
			lhs.matchNumber { lhs ->
				rhs.matchNumber { rhs ->
					thunk(value(literal(lhs - rhs)))
				}
			}
		}

val Sequence.resolveNumberTimesNumber: Thunk?
	get() =
		matchInfixThunk(timesName) { lhs, rhs ->
			lhs.matchNumber { lhs ->
				rhs.matchNumber { rhs ->
					thunk(value(literal(lhs * rhs)))
				}
			}
		}

val Sequence.resolveTextPlusText: Thunk?
	get() =
		matchInfixThunk(plusName) { lhs, rhs ->
			lhs.matchText { lhs ->
				rhs.matchText { rhs ->
					thunk(value(literal(lhs + rhs)))
				}
			}
		}

val Sequence.resolveHead: Thunk?
	get() =
		matchPostfixThunk("head") { lhs ->
			lhs.value.headOrNull?.let { thunk(it) } // TODO
		}

val Sequence.resolveTail: Thunk?
	get() =
		matchPostfixThunk("tail") { lhs ->
			lhs.value.tailOrNull?.let { thunk(it) } // TODO
		}

val Sequence.resolveLast: Thunk?
	get() =
		matchPostfixThunk(lastName) { lhs ->
			lhs.value.lastOrNull?.let { thunk(it) } // TODO
		}

val Sequence.resolvePrevious: Thunk?
	get() =
		matchPostfixThunk(previousName) { lhs ->
			lhs.value.previousOrNull?.let { thunk(it) } // TODO
		}

val Sequence.resolveContent: Thunk?
	get() =
		matchPostfixThunk(contentName) { lhs ->
			lhs.value.contentsOrNull
		}

val Sequence.resolveName: Thunk?
	get() =
		matchPostfixThunk(textName) { lhs ->
			lhs.matchPostfix(nameName) { lhs ->
				lhs.matchField { field ->
					thunk(value(literal(field.name)))
				}
			}
		}

val Sequence.resolveLeonardo: Thunk?
	get() =
		matchSimple("leonardo") {
			thunk(leonardoScript.value)
		}

val Sequence.resolveAnythingEqualsAnything: Thunk?
	get() =
		matchInfixThunk(equalsName) { lhs, rhs ->
			lhs.equals(rhs).thunk
		}

val Sequence.resolveAutoMake: Thunk?
	get() =
		ifOrNull(autoMake) {
			head.matchName { name ->
				tail.matchNotEmpty {
					tail.make(name)
				}
			}
		}

val Sequence.resolvePrint: Thunk?
	get() =
		matchPostfixThunk(printName) { lhs ->
			thunk(value()).also { println(lhs) }
		}

val Sequence.resolvePrinted: Thunk?
	get() =
		matchPostfixThunk(printedName) { lhs ->
			lhs.also { println(lhs) }
		}

val Sequence.resolveFold: Thunk?
	get() =
		matchInfixThunk(doingName) { lhs, rhs ->
			rhs.matchFunction { function ->
				lhs.value.sequenceOrNull?.matchInfixThunk(foldName) { folded, items ->
					items.value.contentsOrNull?.value?.let { contents ->
						folded.fold(contents.lineStack) { line ->
							function
								.copy(context = function.context.push(
									rule(
										pattern(thunk(value(foldedName))),
										body(thunk(value(foldedName lineTo value(line)))))))
								.apply(this)
						}
					}
				}
			}
		}

val Sequence.resolveForce: Thunk?
	get() =
		matchPostfixThunk(forceName) { lhs ->
			lhs.force
		}

val Sequence.resolveScript: Thunk?
	get() =
		matchPostfixThunk(scriptName) { lhs ->
			thunk(lhs.script.value)
		}

val Sequence.resolveSay: Thunk?
	get() =
		matchPostfixThunk("say") { lhs ->
			thunk(value()).also {
				exec("say", "\"${lhs.script.sayString}\"")
			}
		}