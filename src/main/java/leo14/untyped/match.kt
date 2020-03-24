package leo14.untyped

import leo.base.notNullIf
import leo14.*
import leo14.Number

fun <R> Program.matchEmpty(fn: () -> R): R? =
	(this as? EmptyProgram)?.let { fn() }

fun <R> Program.matchNotEmpty(fn: () -> R): R? =
	if (isEmpty) null else fn()

fun <R> Program.matchSequence(fn: (Sequence) -> R): R? =
	(this as? SequenceProgram)?.sequence?.let(fn)

fun <R> Program.matchName(fn: (String) -> R): R? =
	onlyNameOrNull?.let(fn)

fun <R> Program.matchNumber(fn: (Number) -> R): R? =
	numberOrNull?.let(fn)

fun <R> Program.matchText(fn: (String) -> R): R? =
	textOrNull?.let(fn)

fun <R> Program.matchNative(fn: (Native) -> R): R? =
	nativeOrNull?.let(fn)

fun <R> Program.matchFunction(fn: (Function) -> R): R? =
	functionOrNull?.let(fn)

fun <R> Program.matchBody(fn: (Program) -> R): R? =
	matchSequence { sequence ->
		sequence.tail.program.matchEmpty {
			sequence.head.matchField { field ->
				fn(field.rhs)
			}
		}
	}

fun <R> Program.matchInfix(name: String, fn: (Program, Program) -> R) =
	sequenceOrNull?.matchInfix(name, fn)

fun <R> Program.matchInfixThunk(name: String, fn: (Thunk, Thunk) -> R) =
	sequenceOrNull?.matchInfixThunk(name, fn)

fun <R> Program.matchInfix(name1: String, name2: String, fn: (Program, Program) -> R) =
	sequenceOrNull?.matchInfix(name1, name2, fn)

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
		fn(tail.program, rhs)
	}

fun <R> Sequence.matchInfixThunk(name: String, fn: (Thunk, Thunk) -> R) =
	head.matchThunk(name) { rhs ->
		fn(tail, rhs)
	}

fun <R> Sequence.matchInfix(name1: String, name2: String, fn: (Program, Program) -> R) =
	matchInfix(name1) { lhs, rhs ->
		rhs.matchPrefix(name2) { rhs ->
			fn(lhs, rhs)
		}
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

fun <R> Line.matchField(fn: (Field) -> R): R? =
	(this as? FieldLine)?.field?.let(fn)

fun <R> Line.match(string: String, fn: (Program) -> R): R? =
	(this as? FieldLine)?.field?.let { field ->
		field.match(string, fn)
	}

fun <R> Line.matchThunk(string: String, fn: (Thunk) -> R): R? =
	(this as? FieldLine)?.field?.let { field ->
		field.matchThunk(string, fn)
	}

fun <R> Line.match(string1: String, string2: String, fn: (Program) -> R): R? =
	match(string1) { rhs ->
		rhs.matchPrefix(string2) { rhs ->
			fn(rhs)
		}
	}

fun <R> Field.match(name: String, fn: (Program) -> R) =
	if (this.name == name) fn(rhs)
	else null

fun <R> Field.matchThunk(name: String, fn: (Thunk) -> R) =
	if (this.name == name) fn(thunk)
	else null

fun <R> Field.matchName(fn: (String) -> R) =
	rhs.matchEmpty {
		fn(name)
	}

fun <R> Line.matchName(fn: (String) -> R) =
	matchField { field ->
		field.matchName(fn)
	}

fun <R : Any> Script.matchInfix(name: String, fn: (Script, Script) -> R): R? =
	when (this) {
		is UnitScript -> null
		is LinkScript -> link.matchInfix(name, fn)
	}

fun <R : Any> ScriptLink.matchInfix(name: String, fn: (Script, Script) -> R): R? =
	line.match(name) { rhs -> fn(lhs, rhs) }

fun <R : Any> ScriptLine.match(name: String, fn: (Script) -> R): R? =
	when (this) {
		is LiteralScriptLine -> null
		is FieldScriptLine -> field.match(name, fn)
	}

fun <R : Any> ScriptField.match(name: String, fn: (Script) -> R): R? =
	notNullIf(this.string == name) {
		fn(rhs)
	}
