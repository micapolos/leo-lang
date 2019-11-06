package leo14.typed

import leo.base.notNullIf
import leo13.EmptyStack
import leo13.LinkStack
import leo13.onlyOrNull
import leo14.lambda.*

data class Typed<out T>(val term: Term<T>, val type: Type)
data class TypedLine<T>(val term: Term<T>, val line: Line)
data class TypedChoice<T>(val term: Term<T>, val choice: Choice)
data class TypedField<T>(val term: Term<T>, val field: Field)

infix fun <T> Term<T>.of(type: Type) = Typed(this, type)
infix fun <T> Term<T>.of(line: Line) = TypedLine(this, line)
infix fun <T> Term<T>.of(choice: Choice) = TypedChoice(this, choice)
infix fun <T> Term<T>.of(field: Field) = TypedField(this, field)

fun <T> Typed<T>.plus(string: String, rhs: Typed<T>): Typed<T> =
	term.pairTo(rhs.term) of type.plus(string fieldTo rhs.type)

fun <T> Typed<T>.plus(rhs: TypedLine<T>): Typed<T> =
	when (rhs.line) {
		is NativeLine -> plusNative(rhs.term)
		is ChoiceLine -> plus(rhs.term of rhs.line.choice)
		is ArrowLine -> TODO()
	}

fun <T> Typed<T>.plus(rhs: TypedChoice<T>): Typed<T> =
	rhs.choice.fieldStack.onlyOrNull
		?.let { field -> plus(term of field) }
		?:TODO()

fun <T> Typed<T>.plus(rhs: TypedField<T>): Typed<T> =
	term.pairTo(rhs.term) of type.plus(rhs.field.string fieldTo rhs.field.rhs)

fun <T> emptyTyped() = id<T>() of emptyType

val <T> Typed<T>.isEmpty get() = this == emptyTyped<T>()

infix fun <T> Term<T>.pairTo(rhs: Term<T>) =
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
		is LinkStack -> type.lineStack.link.let { lineLink ->
			when (lineLink.stack) {
				is EmptyStack -> resolveRhs(lineLink.value)
				is LinkStack -> second.resolveRhs(lineLink.value)
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
		is LinkStack -> type.lineStack.link.let { lineLink ->
			when (lineLink.stack) {
				is EmptyStack -> resolveGet(lineLink.value, string)
				is LinkStack -> second.resolveGet(lineLink.value, string)
					?: first.resolveGet(lineLink.stack.type, string)
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

fun <T> Typed<T>.plusNative(aterm: Term<T>): Typed<T> =
	if (type == emptyType) aterm of nativeType
	else term.pairTo(aterm) of type.plus(nativeLine)