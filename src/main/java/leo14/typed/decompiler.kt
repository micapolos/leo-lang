package leo14.typed

import leo13.get
import leo14.*
import leo14.lambda.abstraction
import leo14.lambda.application
import leo14.lambda.variable

typealias DecompileLine<T> = TypedLine<T>.() -> Literal?

fun <T> Typed<T>.decompile(fn: DecompileLine<T>): Script =
	decompileLinkOrNull
		?.run { tail.decompile(fn).plus(head.decompileLine(fn)) }
		?: script()

fun <T> TypedLine<T>.decompileLine(fn: DecompileLine<T>): ScriptLine =
	fn()
		?.let { line(it) }
		?: when (line) {
			is NativeLine -> null
			is FieldLine -> (term of line.field).decompileLine(fn)
			is ChoiceLine -> (term of line.choice).decompileLine(fn)
			is ArrowLine -> "action" lineTo script("doing" lineTo line.arrow.lhs.script)
			is AnyLine -> null
		} ?: error("$this.decompileLine")

fun <T> TypedChoice<T>.decompileLine(fn: DecompileLine<T>): ScriptLine =
	term.abstraction(choice.countIndex) { body ->
		body.application { argTerm, fnTerm ->
			argTerm.variable { index ->
				choice.optionStack.get(index)!!.let { option ->
					(fnTerm of (option.string fieldTo option.rhs)).decompileLine(fn)
				}
			}
		}
	}

fun <T> TypedField<T>.decompileLine(fn: DecompileLine<T>): ScriptLine =
	line(field.string fieldTo resolveRhs.decompile(fn))
