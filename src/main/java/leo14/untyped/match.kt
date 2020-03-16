package leo14.untyped

import leo14.Number

fun <R> Program.matchEmpty(fn: () -> R): R? =
	(this as? EmptyProgram)?.let { fn() }

fun <R> Program.matchSequence(fn: (Sequence) -> R): R? =
	(this as? SequenceProgram)?.sequence?.let(fn)

fun <R> Program.matchName(fn: (String) -> R): R? =
	onlyNameOrNull?.let(fn)

fun <R> Program.matchNumber(fn: (Number) -> R): R? =
	numberOrNull?.let(fn)

fun <R> Program.matchText(fn: (String) -> R): R? =
	textOrNull?.let(fn)

fun <R> Program.matchFunction(fn: (Function) -> R): R? =
	functionOrNull?.let(fn)

fun <R> Program.matchBody(fn: (Program) -> R): R? =
	matchSequence { sequence ->
		sequence.tail.matchEmpty {
			sequence.head.matchField { field ->
				fn(field.rhs)
			}
		}
	}

fun <R> Program.matchInfix(name: String, fn: (Program, Program) -> R) =
	sequenceOrNull?.matchInfix(name, fn)

fun <R> Program.matchPrefix(name: String, fn: (Program) -> R) =
	sequenceOrNull?.matchPrefix(name, fn)

fun <R> Program.matchPostfix(name: String, fn: (Program) -> R) =
	sequenceOrNull?.matchPostfix(name, fn)

fun <R> Program.matchName(name: String, fn: () -> R) =
	matchSequence { sequence ->
		sequence.matchInfix(name) { lhs, rhs ->
			lhs.matchEmpty {
				rhs.matchEmpty {
					fn()
				}
			}
		}
	}

fun <R> Sequence.matchInfix(name: String, fn: (Program, Program) -> R) =
	head.match(name) { rhs ->
		fn(tail, rhs)
	}

fun <R> Sequence.matchPrefix(name: String, fn: (Program) -> R) =
	matchInfix(name) { lhs, rhs ->
		lhs.matchEmpty {
			fn(rhs)
		}
	}

fun <R> Sequence.matchPostfix(name: String, fn: (Program) -> R) =
	matchInfix(name) { lhs, rhs ->
		rhs.matchEmpty {
			fn(lhs)
		}
	}

fun <R> Sequence.matchSimple(name: String, fn: () -> R) =
	matchInfix(name) { lhs, rhs ->
		rhs.matchEmpty {
			lhs.matchEmpty {
				fn()
			}
		}
	}

fun <R> Value.matchField(fn: (Field) -> R): R? =
	(this as? FieldValue)?.field?.let(fn)

fun <R> Value.match(string: String, fn: (Program) -> R): R? =
	(this as? FieldValue)?.field?.let { field ->
		field.match(string, fn)
	}

fun <R> Field.match(name: String, fn: (Program) -> R) =
	if (this.name == name) fn(rhs)
	else null

fun <R> Field.matchName(fn: (String) -> R) =
	rhs.matchEmpty {
		fn(name)
	}

fun <R> Value.matchName(fn: (String) -> R) =
	matchField { field ->
		field.matchName(fn)
	}
