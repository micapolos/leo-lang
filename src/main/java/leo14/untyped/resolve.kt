package leo14.untyped

import leo.base.ifOrNull
import leo.java.lang.exec
import leo13.array
import leo13.mapOrNull
import leo13.reverse
import leo13.thisName
import leo14.*

val autoMake = false

val Thunk.resolve: Thunk?
	get() =
		value.resolve

val Value.resolve: Thunk?
	get() =
		when (this) {
			EmptyValue -> null
			is SequenceValue -> sequence.resolve
		}

val Sequence.resolve: Thunk?
	get() =
		null
			?: resolveAccess
			?: resolveFunctionApplyAnything
			?: resolveAnythingCallFunction
			?: resolveAnythingAppendAnything
			?: resolveAnythingItAnything
			?: resolveAnythingQuoteAnything
			?: resolveMinusNumber
			?: resolveNumberPlusNumber
			?: resolveNumberMinusNumber
			?: resolveNumberTimesNumber
			?: resolveTextPlusText
			?: resolveNewlineText
			?: resolveHead
			?: resolveTail
			?: resolveLast
			?: resolvePrevious
			?: resolveName
			?: resolveMake
			?: resolveThis
			?: resolveGet
			?: resolvePrint
			?: resolvePrinted
			?: resolveLeonardo
			?: resolveAnythingEqualsAnything
			?: resolveNative
			?: resolveAutoMake
			?: resolveForce
			?: resolveScript
			?: resolveExec
			?: resolveSubject
			?: resolveObject
			?: resolveLink
			?: resolveScriptText

val Sequence.resolveFunctionApplyAnything: Thunk?
	get() =
		matchInfix(applyName) { lhs, rhs ->
			lhs.matchFunction { function ->
				function.apply(rhs)
			}
		}

val Sequence.resolveAnythingCallFunction: Thunk?
	get() =
		matchInfix(callName) { lhs, rhs ->
			rhs.matchFunction { function ->
				function.apply(lhs)
			}
		}

val Sequence.resolveAccess: Thunk?
	get() =
		lastValue.matchName { name ->
			previousThunk.value.get(name)
		}

val Sequence.resolveGet: Thunk?
	get() =
		matchInfix(getName) { lhs, rhs ->
			rhs.matchName { name ->
				lhs.get(name)
			}
		}

val Sequence.resolveAnythingAppendAnything: Thunk?
	get() =
		matchInfix(appendName) { lhs, rhs ->
			lhs.value.onlyFieldOrNull?.let { field ->
				rhs.value.onlyLineOrNull?.let { line ->
					thunk(value(field.name lineTo field.thunk.plus(line)))
				}
			}
		}

val Sequence.resolveAnythingItAnything: Thunk?
	get() =
		matchInfix(itName) { lhs, rhs ->
			rhs.value.onlyLineOrNull?.let { rhs ->
				lhs.plus(rhs)
			}
		}

val Sequence.resolveAnythingQuoteAnything: Thunk?
	get() =
		matchInfix(quoteName) { lhs, rhs ->
			lhs.plus(rhs)
		}

val Sequence.resolveMake: Thunk?
	get() =
		matchInfix(makeName) { lhs, rhs ->
			rhs.matchName { name ->
				lhs.make(name)
			}
		}

val Sequence.resolveThis: Thunk?
	get() =
		matchPostfix(thisName) { lhs ->
			lhs.this_
		}

val Sequence.resolveNumberPlusNumber: Thunk?
	get() =
		matchInfix(plusName) { lhs, rhs ->
			lhs.matchNumber { lhs ->
				rhs.matchNumber { rhs ->
					thunk(value(literal(lhs + rhs)))
				}
			}
		}

val Sequence.resolveMinusNumber: Thunk?
	get() =
		matchInfix(minusName) { lhs, rhs ->
			lhs.matchEmpty {
				rhs.matchNumber { rhs ->
					thunk(value(literal(-rhs)))
				}
			}
		}

val Sequence.resolveNumberMinusNumber: Thunk?
	get() =
		matchInfix(minusName) { lhs, rhs ->
			lhs.matchNumber { lhs ->
				rhs.matchNumber { rhs ->
					thunk(value(literal(lhs - rhs)))
				}
			}
		}

val Sequence.resolveNumberTimesNumber: Thunk?
	get() =
		matchInfix(timesName) { lhs, rhs ->
			lhs.matchNumber { lhs ->
				rhs.matchNumber { rhs ->
					thunk(value(literal(lhs * rhs)))
				}
			}
		}

val Sequence.resolveTextPlusText: Thunk?
	get() =
		matchInfix(plusName) { lhs, rhs ->
			lhs.matchText { lhs ->
				rhs.matchText { rhs ->
					thunk(value(literal(lhs + rhs)))
				}
			}
		}

val Sequence.resolveNewlineText: Thunk?
	get() =
		matchPostfix(textName) { lhs ->
			lhs.matchPostfix("newline") { lhs ->
				lhs.matchEmpty {
					thunk(value(literal("\n")))
				}
			}
		}

val Sequence.resolveHead: Thunk?
	get() =
		matchPostfix("head") { lhs ->
			lhs.value.headOrNull?.let { thunk(it) } // TODO
		}

val Sequence.resolveTail: Thunk?
	get() =
		matchPostfix("tail") { lhs ->
			lhs.value.tailOrNull?.let { thunk(it) } // TODO
		}

val Sequence.resolveLast: Thunk?
	get() =
		matchPostfix(lastName) { lhs ->
			lhs.value.lastOrNull?.let { thunk(it) } // TODO
		}

val Sequence.resolvePrevious: Thunk?
	get() =
		matchPostfix(previousName) { lhs ->
			lhs.value.previousOrNull?.let { thunk(it) } // TODO
		}

val Sequence.resolveName: Thunk?
	get() =
		matchPostfix(textName) { lhs ->
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
		matchInfix(equalsName) { lhs, rhs ->
			lhs.equals(rhs).thunk
		}

val Sequence.resolveAutoMake: Thunk?
	get() =
		ifOrNull(autoMake) {
			lastValue.matchName { name ->
				previousThunk.matchNotEmpty {
					previousThunk.make(name)
				}
			}
		}

val Sequence.resolvePrint: Thunk?
	get() =
		matchPostfix(printName) { lhs ->
			thunk(value()).also { println(lhs) }
		}

val Sequence.resolvePrinted: Thunk?
	get() =
		matchPostfix(printedName) { lhs ->
			lhs.also { println(lhs) }
		}

val Sequence.resolveForce: Thunk?
	get() =
		matchPostfix(forceName) { lhs ->
			lhs.force
		}

val Sequence.resolveScript: Thunk?
	get() =
		matchPostfix(scriptName) { lhs ->
			thunk(lhs.script.value)
		}

val Sequence.resolveExec: Thunk?
	get() =
		matchInfix(execName) { lhs, rhs ->
			lhs.matchEmpty {
				rhs
					.value
					.lineStack
					.reverse
					.mapOrNull { literalOrNull?.stringOrNull }
					?.let { args ->
						thunk(value(literal(exec(*args.array))))
					}
			}
		}

val Sequence.resolveLink: Thunk?
	get() =
		matchPostfix(linkName) { lhs ->
			lhs.matchSequence { sequence ->
				thunk(value(sequence.lastValue.selectName))
			}
		}

val Sequence.resolveSubject: Thunk?
	get() =
		matchPostfix(subjectName) { lhs ->
			lhs.matchSequence { sequence ->
				sequence.previousThunk
			}
		}

val Sequence.resolveObject: Thunk?
	get() =
		matchPostfix(objectName) { lhs ->
			lhs.matchSequence { sequence ->
				sequence.lastValue.fieldOrNull?.let { field ->
					field.thunk
				}
			}
		}

val Sequence.resolveScriptText: Thunk?
	get() =
		matchPostfix(textName) { lhs ->
			lhs.matchPostfix(leoName) { lhs ->
				thunk(value(literal(lhs.script.leoString)))
			}
		}
