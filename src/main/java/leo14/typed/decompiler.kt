package leo14.typed

import leo13.EmptyStack
import leo13.LinkStack
import leo14.*
import leo14.lambda.NativeTerm
import leo14.lambda.Term
import leo14.lambda.pair

fun <T> Typed<T>.decompile(fn: T.() -> ScriptLine): Script =
	term.decompile(type, fn)

fun <T> Term<T>.decompile(type: Type, fn: T.() -> ScriptLine): Script =
	when (type.lineStack) {
		is EmptyStack -> script()
		is LinkStack -> type.lineStack.link.let { link ->
			when (link.stack) {
				is EmptyStack -> script(decompileLine(link.value, fn))
				is LinkStack -> pair()
					.run {
						first.decompile(Type(link.stack), fn)
							.plus(second.decompileLine(link.value, fn))
					}
			}
		}
	}

fun <T> Term<T>.decompileNative(fn: T.() -> ScriptLine) =
	if (this is NativeTerm) native.fn()
	else error("$this as NativeTerm")

fun <T> Term<T>.decompileLine(line: Line, fn: T.() -> ScriptLine): ScriptLine =
	when (line) {
		is NativeLine -> decompileNative(fn)
		is ChoiceLine -> decompileLine(line.choice, fn)
		is ArrowLine -> decompileLine(line.arrow, fn)
	}

fun <T> Term<T>.decompileLine(field: Field, fn: T.() -> ScriptLine): ScriptLine =
	line(decompileField(field, fn))

fun <T> Term<T>.decompileLine(choice: Choice, fn: T.() -> ScriptLine): ScriptLine =
	when (choice.fieldStack) {
		is EmptyStack -> error("empty choice")
		is LinkStack -> choice.fieldStack.link.let { link ->
			when (link.stack) {
				is EmptyStack -> decompileLine(link.value, fn)
				is LinkStack -> TODO()
			}
		}
	}

fun <T> Term<T>.decompileLine(arrow: Arrow, fn: T.() -> ScriptLine): ScriptLine =
	TODO()

fun <T> Term<T>.decompileField(field: Field, fn: T.() -> ScriptLine): ScriptField =
	field.string fieldTo decompile(field.rhs, fn)
