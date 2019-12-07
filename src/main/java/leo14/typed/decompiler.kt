package leo14.typed

import leo13.get
import leo14.*
import leo14.lambda.abstraction
import leo14.lambda.application
import leo14.lambda.scriptLine
import leo14.lambda.variable

typealias DecompileLiteral<T> = TypedLine<T>.() -> Literal?

fun <T> Typed<T>.decompile(fn: DecompileLiteral<T>): Script =
	decompileLinkOrNull
		?.run { tail.decompile(fn).plus(head.decompileLine(fn)) }
		?: script()

fun <T> TypedLine<T>.decompileLine(decompileLiteralFn: DecompileLiteral<T>): ScriptLine =
	decompileLiteralFn()
		?.let { line(it) }
		?: when (line) {
			is NativeLine -> Keyword.NATIVE.string lineTo script(term.scriptLine) // TODO: Decompile literal
			is FieldLine -> (term of line.field).decompileLine(decompileLiteralFn)
			is ChoiceLine -> (term of line.choice).decompileLine(decompileLiteralFn)
			is ArrowLine -> Keyword.FUNCTION.string lineTo line.arrow.lhs.script.plus(Keyword.GIVES.string lineTo line.arrow.rhs.script)
			is AnyLine -> "anything" lineTo script()
		} ?: error("$this.decompileLine")

fun <T> TypedChoice<T>.decompileLine(decompileLiteralFn: DecompileLiteral<T>): ScriptLine =
	term.abstraction(choice.countIndex) { body ->
		body.application { argTerm, fnTerm ->
			argTerm.variable { index ->
				choice.optionStack.get(index)!!.let { option ->
					(fnTerm of (option.string fieldTo option.rhs)).decompileLine(decompileLiteralFn)
				}
			}
		}
	}

fun <T> TypedField<T>.decompileLine(fn: DecompileLiteral<T>): ScriptLine =
	line(field.string fieldTo resolveRhs.decompile(fn))
