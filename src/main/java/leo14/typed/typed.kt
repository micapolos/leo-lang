package leo14.typed

import leo.base.notNullIf
import leo13.Link
import leo13.linkTo
import leo14.lambda.*

data class Typed<out T>(val term: Term<T>, val type: Type)
data class TypedLine<T>(val term: Term<T>, val line: Line)
data class TypedChoice<T>(val term: Term<T>, val choice: Choice)
data class TypedField<T>(val term: Term<T>, val field: Field)

infix fun <T> Term<T>.of(type: Type) = Typed(this, type)
infix fun <T> Term<T>.of(line: Line) = TypedLine(this, line)
infix fun <T> Term<T>.of(choice: Choice) = TypedChoice(this, choice)
infix fun <T> Term<T>.of(field: Field) = TypedField(this, field)

fun <T> choice(typed: TypedField<T>): TypedChoice<T> = typed.term of choice(typed.field)
fun <T> line(typed: TypedChoice<T>): TypedLine<T> = typed.term of line(typed.choice)

fun <T> emptyTyped() = id<T>() of emptyType
val <T> Typed<T>.isEmpty get() = this == emptyTyped<T>()

fun <T> Typed<T>.plus(typed: TypedLine<T>): Typed<T> =
	plusTerm(typed) of type.plus(typed.line)

fun <T> Typed<T>.plus(typed: TypedField<T>): Typed<T> =
	plus(typed.term of line(choice(typed.field)))

fun <T> Typed<T>.plusTerm(rhs: TypedLine<T>): Term<T> =
	if (type.isStatic)
		if (rhs.line.isStatic) id()
		else rhs.term
	else
		if (rhs.line.isStatic) term
		else term.plus(rhs.term)

fun <T> Term<T>.plus(rhs: Term<T>) =
	pair(this, rhs)

val <T> Term<T>.typedHead get() = second
val <T> Term<T>.typedTail get() = first

fun <T> Typed<T>.resolve(rhs: TypedField<T>): Typed<T>? =
	if (rhs.field.rhs.isEmpty) resolve(rhs.field.string)
	else null

fun <T> Typed<T>.resolve(string: String): Typed<T>? =
	when (string) {
		else -> resolveAccess(string) ?: resolveWrap(string)
	}

val <T> Typed<T>.resolveLinkOrNull: Link<Typed<T>, TypedLine<T>>?
	get() =
		type.lineLinkOrNull?.let { link ->
			if (link.tail.isStatic)
				if (link.head.isStatic) (id<T>() of link.tail) linkTo (id<T>() of link.head)
				else (id<T>() of link.tail) linkTo (term of link.head)
			else
				if (link.head.isStatic) (term of link.tail) linkTo (id<T>() of link.head)
				else (term.first of link.tail) linkTo (term.second of link.head)
		}

val <T> TypedLine<T>.resolveFieldOrNull: TypedField<T>?
	get() =
		(line as? ChoiceLine)?.choice?.let { term of it }?.resolveFieldOrNull

val <T> TypedChoice<T>.resolveFieldOrNull: TypedField<T>?
	get() =
		choice.onlyFieldOrNull?.let { term of it }

val <T> TypedField<T>.resolveRhs: Typed<T>
	get() =
		term of field.rhs

fun <T> Typed<T>.resolveAccess(string: String): Typed<T>? =
	resolveLinkOrNull?.head?.resolveFieldOrNull?.resolveRhs?.resolveGet(string)

fun <T> Typed<T>.resolveGet(string: String): Typed<T>? =
	resolveLinkOrNull?.let { link ->
		link.head.resolveGet(string) ?: link.tail.resolveGet(string)
	}

fun <T> TypedLine<T>.resolveGet(string: String): Typed<T>? =
	when (line) {
		is NativeLine -> notNullIf(string == "native") { term of type(line) }
		is ChoiceLine -> (term of line.choice).resolveGet(string)
		is ArrowLine -> notNullIf(string == "function") { term of type(line) }
	}

fun <T> TypedChoice<T>.resolveGet(string: String): Typed<T>? =
	resolveFieldOrNull?.resolveGet(string)

fun <T> TypedField<T>.resolveGet(string: String): Typed<T>? =
	notNullIf(field.string == string) {
		term of type(field)
	}

fun <T> Typed<T>.eval(rhs: TypedField<T>): Typed<T> =
	resolve(rhs) ?: plus(term of line(choice(rhs.field)))

fun <T> Typed<T>.resolveWrap(string: String) =
	term of type(string fieldTo type)

fun <T> Typed<T>.plusNative(rhs: Term<T>): Typed<T> =
	if (type.isStatic) rhs of type.plus(nativeLine)
	else pair(term, rhs) of type.plus(nativeLine)

// === deconstruction

val <T> Typed<T>.decompileLinkOrNull: Link<Typed<T>, TypedLine<T>>?
	get() =
		type.lineLinkOrNull?.let { link ->
			if (link.tail.isStatic)
				if (link.head.isStatic) (id<T>() of link.tail) linkTo (id<T>() of link.head)
				else (id<T>() of link.tail) linkTo (term of link.head)
			else
				if (link.head.isStatic) (term of link.tail) linkTo (id<T>() of link.head)
				else term.pair().run { (first of link.tail) linkTo (second of link.head) }
		}
