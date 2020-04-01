package leo14.untyped

import leo.base.notNullIf
import leo.base.string
import leo.java.lang.exec
import leo13.array
import leo13.mapOrNull
import leo13.reverse
import leo14.*
import java.math.BigDecimal

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
			?: resolveNothing
			?: resolveAnythingUseFunction
			?: resolveAnythingAppendAnything
			?: resolveAnythingItAnything
			?: resolveAnythingQuoteAnything
			?: resolveMinusNumber
			?: resolveNumberPlusNumber
			?: resolveNumberMinusNumber
			?: resolveNumberTimesNumber
			?: resolveNumberText
			?: resolveTextPlusText
			?: resolveTextNumber
			?: resolveLast
			?: resolvePrevious
			?: resolveName
			?: resolveGet
			?: resolvePrint
			?: resolvePrinted
			?: resolveLeonardo
			?: resolveAnythingEqualsAnything
			?: resolveNative
			?: resolveForce
			?: resolveExec
			?: resolveLink
			?: resolveTextWord
			?: resolveWordText
			?: resolveLeoSubject
			?: resolveLeoHead
			?: resolveLeoObject
			?: resolveLeoWord
			?: resolveSubject // TODO: remove?
			?: resolveObject // TODO: remove?
			?: resolveHead // TODO: remove?
			?: resolveTail // TODO: remove?

val Sequence.resolveNothing: Thunk?
	get() =
		matchName(nothingName) {
			emptyThunk
		}

val Sequence.resolveAnythingUseFunction: Thunk?
	get() =
		matchInfix(useName) { lhs, rhs ->
			rhs.matchDoing { doing ->
				doing.with(lhs)
			}
		}

val Sequence.resolveAccess: Thunk?
	get() =
		lastLine.matchName { name ->
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
		matchInfixOrPrefix(itName) { lhs, rhs ->
			rhs.value.onlyLineOrNull?.let { rhs ->
				lhs.plus(rhs)
			}
		}

val Sequence.resolveAnythingQuoteAnything: Thunk?
	get() =
		matchInfix(quoteName) { lhs, rhs ->
			lhs.plus(rhs)
		}

val Sequence.resolveNumberText: Thunk?
	get() =
		matchPrefix(textName) { rhs ->
			rhs.matchNumber { number ->
				thunk(value(literal(number.string)))
			}
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
		matchPrefix(minusName) { rhs ->
			rhs.matchNumber { number ->
				thunk(value(literal(-number)))
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

val Sequence.resolveTextNumber: Thunk?
	get() =
		matchPrefix(numberName) { rhs ->
			rhs.matchText { text ->
				try {
					thunk(value(literal(number(BigDecimal(text)))))
				} catch (e: NumberFormatException) {
					null
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

val Sequence.resolveHead: Thunk?
	get() =
		matchPrefix("head") { rhs ->
			rhs.value.headOrNull?.let { thunk(it) } // TODO
		}

val Sequence.resolveTail: Thunk?
	get() =
		matchPrefix("tail") { rhs ->
			rhs.value.tailOrNull?.let { thunk(it) } // TODO
		}

val Sequence.resolveLast: Thunk?
	get() =
		matchPrefix(lastName) { rhs ->
			rhs.value.lastOrNull?.let { thunk(it) } // TODO
		}

val Sequence.resolvePrevious: Thunk?
	get() =
		matchPrefix(previousName) { rhs ->
			rhs.value.previousOrNull?.let { thunk(it) } // TODO
		}

val Sequence.resolveName: Thunk?
	get() =
		matchPrefix(textName) { rhs ->
			rhs.matchPrefix(nameName) { rhs ->
				rhs.matchField { field ->
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

val Sequence.resolvePrint: Thunk?
	get() =
		matchPrefix(printName) { rhs ->
			thunk(value()).also { println(rhs) }
		}

val Sequence.resolvePrinted: Thunk?
	get() =
		matchPrefix(printedName) { rhs ->
			rhs.also { println(rhs) }
		}

val Sequence.resolveForce: Thunk?
	get() =
		matchPrefix(forceName) { rhs ->
			rhs.force
		}

val Sequence.resolveExec: Thunk?
	get() =
		matchPrefix(execName) { rhs ->
			rhs
				.value
				.lineStack
				.reverse
				.mapOrNull { literalOrNull?.stringOrNull }
				?.let { args ->
					thunk(value(literal(exec(*args.array))))
				}
		}

val Sequence.resolveLink: Thunk?
	get() =
		matchPrefix(linkName) { rhs ->
			rhs.matchSequence { sequence ->
				thunk(value(sequence.lastLine.selectName))
			}
		}

val Sequence.resolveSubject: Thunk?
	get() =
		matchPrefix(subjectName) { rhs ->
			rhs.matchSequence { sequence ->
				sequence.previousThunk
			}
		}

val Sequence.resolveObject: Thunk?
	get() =
		matchPrefix(objectName) { rhs ->
			rhs.matchSequence { sequence ->
				sequence.lastLine.fieldOrNull?.let { field ->
					field.thunk
				}
			}
		}

val Sequence.resolveTextWord: Thunk?
	get() =
		matchPrefix(wordName) { rhs ->
			rhs.matchText { text ->
				notNullIf(text.isWord) {
					thunk(value(text))
				}
			}
		}

val Sequence.resolveWordText: Thunk?
	get() =
		matchPrefix(textName) { rhs ->
			rhs.matchPrefix(wordName) { rhs ->
				rhs.matchName { word ->
					thunk(value(literal(word)))
				}
			}
		}

val Sequence.resolveNormalize: Sequence?
	get() =
		lastLine.matchName { name ->
			emptyThunk.sequenceTo(name lineTo previousThunk)
		}

val Sequence.normalize: Sequence
	get() =
		resolveNormalize ?: this

val Sequence.resolveLeoSubject: Thunk?
	get() =
		matchPrefix(subjectName) { rhs ->
			rhs.matchPrefix(leoName) { rhs ->
				rhs.value.sequenceOrNull?.previousThunk
			}
		}

val Sequence.resolveLeoHead: Thunk?
	get() =
		matchPrefix(headName) { rhs ->
			rhs.matchPrefix(leoName) { rhs ->
				rhs.value.sequenceOrNull?.lastLine?.let { thunk(value(it)) }
			}
		}

val Sequence.resolveLeoObject: Thunk?
	get() =
		matchPrefix(objectName) { rhs ->
			rhs.matchPrefix(leoName) { rhs ->
				rhs.value.sequenceOrNull?.lastLine?.fieldOrNull?.thunk
			}
		}

val Sequence.resolveLeoWord: Thunk?
	get() =
		matchPrefix(wordName) { rhs ->
			rhs.matchPrefix(leoName) { rhs ->
				rhs.value.sequenceOrNull?.lastLine?.fieldOrNull?.name?.let { name ->
					thunk(value(name))
				}
			}
		}
