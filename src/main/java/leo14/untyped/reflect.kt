package leo14.untyped

import leo14.*

fun Context.reflect(thunk: Thunk): Script =
	resolver(thunk)
		.apply(reflectName lineTo emptyThunk)
		.thunk
		.let { applied ->
			if (applied == thunk.plus(reflectName lineTo emptyThunk)) reflectRaw(thunk)
			else reflectRaw(applied)
		}

fun Context.reflectRaw(thunk: Thunk): Script =
	when (thunk) {
		is ValueThunk -> reflect(thunk.value)
		is LazyThunk -> reflect(thunk.lazy)
	}

fun Context.reflect(value: Value): Script =
	when (value) {
		EmptyValue -> script()
		is SequenceValue -> reflect(value.sequence)
	}

fun Context.reflect(lazy: Lazy): Script =
	script(lazyName lineTo lazy.script)

fun Context.reflect(sequence: Sequence): Script =
	script(reflect(sequence.previousThunk) linkTo reflect(sequence.lastLine))

fun Context.reflect(line: Line): ScriptLine =
	when (line) {
		is LiteralLine -> reflectLine(line.literal)
		is FieldLine -> reflectLine(line.field)
		is FunctionLine -> reflectLine(line.function)
		is NativeLine -> reflectLine(line.native)
	}

fun Context.reflectLine(literal: Literal): ScriptLine =
	scriptLine(literal)

fun Context.reflectLine(field: Field): ScriptLine =
	field.name lineTo reflect(field.thunk)

fun Context.reflectLine(function: Function): ScriptLine =
	functionName lineTo function.script

fun Context.reflectLine(native: Native): ScriptLine =
	nativeName lineTo script(literal(native.toString()))
