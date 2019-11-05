package leo14.typed

import leo14.*
import leo14.lambda.NativeTerm
import leo14.lambda.Term
import leo14.lambda.pair

fun <T> Typed<T>.decompile(fn: T.() -> Script): Script =
	term.decompile(type, fn)

fun <T> Term<T>.decompile(type: Type, fn: T.() -> Script): Script =
	when (type) {
		is EmptyType -> script()
		is NativeType -> decompileNative(fn)
		is LinkType -> script(decompileLink(type.link, fn))
		else -> TODO()
	}

fun <T> Term<T>.decompileNative(fn: T.() -> Script) =
	if (this is NativeTerm) native.fn()
	else error("$this as NativeTerm")

fun <T> Term<T>.decompileLink(link: Link, fn: T.() -> Script): ScriptLink =
	if (link.lhs == emptyType) script() linkTo decompileLine(link.field, fn)
	else pair().run { first.decompile(link.lhs, fn) linkTo second.decompileLine(link.field, fn) }

fun <T> Term<T>.decompileLine(field: Field, fn: T.() -> Script): ScriptLine =
	line(decompileField(field, fn))

fun <T> Term<T>.decompileField(field: Field, fn: T.() -> Script): ScriptField =
	field.string fieldTo decompile(field.rhs, fn)
