package leo14.untyped

import leo.base.ifOrNull
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
			?: resolveContents
			?: resolveMake
			?: resolveThis
			?: resolveAccess
			?: resolveGet
			?: resolveLeonardo
			?: resolveAnythingEqualsAnything
			?: resolveIfThenElse
			?: resolveTextJavaClass
			?: resolveAnyInvoke
			?: resolveAutoMake

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

val Sequence.resolveTextJavaClass
	get() =
		matchInfix("java", "class") { lhs, rhs ->
			rhs.matchEmpty {
				lhs.matchText { text ->
					try {
						program(anyValue(javaClass.classLoader.loadClass(text)))
					} catch (x: ClassNotFoundException) {
						null
					}
				}
			}
		}

val Sequence.resolveAnyInvoke
	get() =
		matchInfix("invoke") { lhs, rhs ->
			lhs.matchAny { any ->
				rhs.matchText { text ->
					try {
						program(anyValue(any.javaClass.getMethod(text).invoke(any)))
					} catch (x: RuntimeException) {
						null
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
