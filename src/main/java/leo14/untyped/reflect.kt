package leo14.untyped

import leo14.*

fun Scope.reflect(thunk: Thunk): Script =
	resolver(thunk)
		.apply(reflectName lineTo emptyThunk)
		.thunk
		.let { applied ->
			if (applied == thunk(value(reflectName lineTo thunk))) reflectRaw(thunk)
			else reflectRaw(applied)
		}

fun Scope.reflectRaw(thunk: Thunk): Script =
	when (thunk) {
		is ValueThunk -> reflect(thunk.value)
		is LazyThunk -> reflect(thunk.lazy)
	}

fun Scope.reflect(value: Value): Script =
	when (value) {
		EmptyValue -> script()
		is SequenceValue -> reflect(value.sequence)
	}

fun Scope.reflect(lazy: Lazy): Script =
	script(lazyName lineTo lazy.script)

fun Scope.reflect(sequence: Sequence): Script =
	script(reflect(sequence.previousThunk) linkTo reflect(sequence.lastLine))

fun Scope.reflect(line: Line): ScriptLine =
	when (line) {
		is LiteralLine -> reflectLine(line.literal)
		is FieldLine -> reflectLine(line.field)
		is DoingLine -> reflectLine(line.action)
		is NativeLine -> reflectLine(line.native)
	}

fun Scope.reflectLine(literal: Literal): ScriptLine =
	scriptLine(literal)

fun Scope.reflectLine(field: Field): ScriptLine =
	field.name lineTo reflect(field.thunk)

fun Scope.reflectLine(action: Action): ScriptLine =
	doingName lineTo action.script

fun Scope.reflectLine(native: Native): ScriptLine =
	nativeName lineTo script(literal(native.toString()))
