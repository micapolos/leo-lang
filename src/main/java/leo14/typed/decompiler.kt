package leo14.typed

import leo13.get
import leo14.*
import leo14.lambda.*

fun <T> Typed<T>.decompile(fn: T.() -> ScriptLine): Script =
	decompileLinkOrNull
		?.run { tail.decompile(fn).plus(head.decompileLine(fn)) }
		?: script()

fun <T> TypedLine<T>.decompileLine(fn: T.() -> ScriptLine): ScriptLine =
	when (line) {
		is NativeLine -> term.decompileNative(fn)
		is FieldLine -> (term of line.field).decompileLine(fn)
		is ChoiceLine -> (term of line.choice).decompileLine(fn)
		is ArrowLine -> "function" lineTo script(
			"from" lineTo line.arrow.lhs.script,
			"to" lineTo line.arrow.rhs.script)
	}

fun <T> Term<T>.decompileNative(fn: T.() -> ScriptLine) =
	if (this is NativeTerm) native.fn()
	else error("$this as NativeTerm")

fun <T> TypedChoice<T>.decompileLine(fn: T.() -> ScriptLine): ScriptLine =
	term.abstraction(choice.countIndex) { body ->
		body.application { argTerm, fnTerm ->
			argTerm.variable { index ->
				choice.optionStack.get(index)!!.let { option ->
					(fnTerm of (option.string fieldTo option.rhs)).decompileLine(fn)
				}
			}
		}
	}

fun <T> TypedField<T>.decompileLine(fn: T.() -> ScriptLine): ScriptLine =
	line(field.string fieldTo resolveRhs.decompile(fn))
