package leo14.typed

import leo.base.notNullIf
import leo13.EmptyStack
import leo13.LinkStack
import leo13.onlyOrNull
import leo14.lambda.*

data class Typed<out T>(val term: Term<T>, val type: Type)

infix fun <T> Term<T>.of(type: Type) = Typed(this, type)

fun <T> Typed<T>.plus(string: String, rhs: Typed<T>): Typed<T> =
	term.typedPlus(rhs.term) of type.plus(string fieldTo rhs.type)

fun <T> emptyTyped() = id<T>() of emptyType

val <T> Typed<T>.isEmpty get() = this == emptyTyped<T>()

fun <T> Term<T>.typedPlus(rhs: Term<T>) =
	if (this == id<T>()) rhs
	else pair(this, rhs)

val <T> Term<T>.typedHead get() = second
val <T> Term<T>.typedTail get() = first

fun <T> Typed<T>.resolve(string: String, rhs: Typed<T>): Typed<T>? =
	if (rhs.isEmpty) resolve(string)
	else null

fun <T> Typed<T>.resolve(string: String): Typed<T>? =
	when (string) {
		else -> resolveAccess(string)
	}

fun <T> Typed<T>.resolveAccess(string: String): Typed<T>? =
	term
		.resolveRhs(type)
		?.resolveGet(string)
		?: wrap(string)

fun <T> Term<T>.resolveRhs(type: Type): Typed<T>? =
	when (type.lineStack) {
		is EmptyStack -> null
		is LinkStack -> type.lineStack.link.let { link ->
			when (link.stack) {
				is EmptyStack -> resolveRhs(link.value)
				is LinkStack -> second.resolveRhs(link.value)
			}
		}
	}

fun <T> Term<T>.resolveRhs(line: Line): Typed<T>? =
	(line as? ChoiceLine)?.choice?.let { choice ->
		resolveRhs(choice)
	}

fun <T> Term<T>.resolveRhs(choice: Choice): Typed<T>? =
	choice.fieldStack.onlyOrNull?.let { field ->
		resolveRhs(field)
	}

fun <T> Term<T>.resolveRhs(field: Field): Typed<T> =
	this of field.rhs

fun <T> Typed<T>.resolveGet(string: String): Typed<T>? =
	term.resolveGet(type, string)

fun <T> Term<T>.resolveGet(type: Type, string: String): Typed<T>? =
	when (type.lineStack) {
		is EmptyStack -> null
		is LinkStack -> type.lineStack.link.let { link ->
			when (link.stack) {
				is EmptyStack -> resolveGet(link.value, string)
				is LinkStack -> second.resolveGet(link.value, string)
					?: first.resolveGet(Type(link.stack), string)
			}
		}
	}

fun <T> Term<T>.resolveGet(line: Line, string: String): Typed<T>? =
	when (line) {
		is NativeLine -> notNullIf(string == "native") { this of type(line) }
		is ChoiceLine -> resolveGet(line.choice, string)
		is ArrowLine -> notNullIf(string == "function") { this of type(line) }
	}

fun <T> Term<T>.resolveGet(choice: Choice, string: String): Typed<T>? =
	choice.fieldStack.onlyOrNull?.let { field ->
		resolveGet(field, string)
	}

fun <T> Term<T>.resolveGet(field: Field, string: String): Typed<T>? =
	notNullIf(field.string == string) {
		this of type(field)
	}

fun <T> Typed<T>.eval(string: String, rhs: Typed<T>): Typed<T> =
	resolve(string, rhs) ?: plus(string, rhs)

fun <T> Typed<T>.wrap(string: String) =
	term of type(string fieldTo type)
