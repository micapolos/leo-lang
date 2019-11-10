package leo14.typed

import leo14.*
import leo14.lambda.NativeTerm
import leo14.lambda.Term

fun <T> Typed<T>.decompile(fn: T.() -> ScriptLine): Script =
	decompileLinkOrNull
		?.run { tail.decompile(fn).plus(head.decompileLine(fn)) }
		?: script()

fun <T> TypedLine<T>.decompileLine(fn: T.() -> ScriptLine): ScriptLine =
	when (line) {
		is NativeLine -> term.decompileNative(fn)
		is FieldLine -> (term of line.field).decompileLine(fn)
		is ChoiceLine -> (term of line.choice).decompileLine(fn)
		is ArrowLine -> TODO()
	}

fun <T> Term<T>.decompileNative(fn: T.() -> ScriptLine) =
	if (this is NativeTerm) native.fn()
	else error("$this as NativeTerm")

fun <T> TypedChoice<T>.decompileLine(fn: T.() -> ScriptLine): ScriptLine =
	TODO()

fun <T> TypedField<T>.decompileLine(fn: T.() -> ScriptLine): ScriptLine =
	line(field.string fieldTo resolveRhs.decompile(fn))
