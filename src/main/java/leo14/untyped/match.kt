package leo14.untyped

import leo.base.notNullIf
import leo14.*
import leo14.Number

fun <R> Value.matchEmpty(fn: () -> R): R? =
	(this as? EmptyValue)?.let { fn() }

fun <R> Thunk.matchEmpty(fn: () -> R): R? =
	value.matchEmpty(fn)

fun <R> Value.matchNotEmpty(fn: () -> R): R? =
	if (isEmpty) null else fn()

fun <R> Thunk.matchNotEmpty(fn: () -> R): R? =
	value.matchNotEmpty(fn)

fun <R> Value.matchSequence(fn: (Sequence) -> R): R? =
	(this as? SequenceValue)?.sequence?.let(fn)

fun <R> Value.matchName(fn: (String) -> R): R? =
	onlyNameOrNull?.let(fn)

fun <R> Thunk.matchName(fn: (String) -> R): R? =
	value.matchName(fn)

fun <R> Thunk.matchField(fn: (Field) -> R): R? =
	value.onlyFieldOrNull?.let(fn)

fun <R> Value.matchNumber(fn: (Number) -> R): R? =
	numberOrNull?.let(fn)

fun <R> Thunk.matchNumber(fn: (Number) -> R): R? =
	value.matchNumber(fn)

fun <R> Thunk.matchFunction(fn: (Function) -> R): R? =
	value.functionOrNull?.let(fn)

fun <R> Value.matchText(fn: (String) -> R): R? =
	textOrNull?.let(fn)

fun <R> Thunk.matchText(fn: (String) -> R): R? =
	value.matchText(fn)

fun <R> Value.matchNative(fn: (Native) -> R): R? =
	nativeOrNull?.let(fn)

fun <R> Thunk.matchNative(fn: (Native) -> R): R? =
	value.matchNative(fn)

fun <R> Thunk.matchInfix(name: String, fn: (Thunk, Thunk) -> R) =
	value.sequenceOrNull?.matchInfixThunk(name, fn)

fun <R> Thunk.matchPrefix(name: String, fn: (Thunk) -> R) =
	matchInfix(name) { lhs, rhs ->
		lhs.matchEmpty {
			fn(rhs)
		}
	}

fun <R> Thunk.matchPostfix(name: String, fn: (Thunk) -> R) =
	matchInfix(name) { lhs, rhs ->
		rhs.matchEmpty {
			fn(lhs)
		}
	}

fun <R> Sequence.matchInfixThunk(name: String, fn: (Thunk, Thunk) -> R) =
	head.matchThunk(name) { rhs ->
		fn(tail, rhs)
	}

fun <R> Sequence.matchPostfixThunk(name: String, fn: (Thunk) -> R) =
	matchInfixThunk(name) { lhs, rhs ->
		rhs.matchEmpty {
			fn(lhs)
		}
	}

fun <R> Sequence.matchSimple(name: String, fn: () -> R) =
	matchInfixThunk(name) { lhs, rhs ->
		rhs.matchEmpty {
			lhs.matchEmpty {
				fn()
			}
		}
	}

fun <R> Line.matchField(fn: (Field) -> R): R? =
	(this as? FieldLine)?.field?.let(fn)

fun <R> Line.matchThunk(string: String, fn: (Thunk) -> R): R? =
	(this as? FieldLine)?.field?.let { field ->
		field.matchThunk(string, fn)
	}

fun <R> Field.matchThunk(name: String, fn: (Thunk) -> R) =
	if (this.name == name) fn(thunk)
	else null

fun <R> Field.matchName(fn: (String) -> R) =
	thunk.matchEmpty {
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
