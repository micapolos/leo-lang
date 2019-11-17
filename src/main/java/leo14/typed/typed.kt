package leo14.typed

import leo.base.failIfOr
import leo.base.fold
import leo.base.notNullIf
import leo.base.notNullOrError
import leo13.*
import leo14.Literal
import leo14.any
import leo14.lambda.*

data class Typed<out T>(val term: Term<T>, val type: Type)
data class TypedLine<T>(val term: Term<T>, val line: Line)
data class TypedChoice<T>(val term: Term<T>, val choice: Choice)
data class TypedField<T>(val term: Term<T>, val field: Field)
data class TypedOption<T>(val term: Term<T>, val option: Option)
data class TypedArrow<T>(val term: Term<T>, val arrow: Arrow)

infix fun <T> Term<T>.of(type: Type) = Typed(this, type)
infix fun <T> Term<T>.of(line: Line) = TypedLine(this, line)
infix fun <T> Term<T>.of(choice: Choice) = TypedChoice(this, choice)
infix fun <T> Term<T>.of(field: Field) = TypedField(this, field)
infix fun <T> Term<T>.of(option: Option) = TypedOption(this, option)
infix fun <T> Term<T>.of(arrow: Arrow) = TypedArrow(this, arrow)

fun <T> choice(typed: TypedOption<T>): TypedChoice<T> = typed.term of choice(typed.option)
fun <T> line(typed: TypedChoice<T>): TypedLine<T> = typed.term of line(typed.choice)

fun <T> typed() = id<T>() of emptyType
val <T> Typed<T>.isEmpty get() = this == typed<T>()

fun <T> typed(line: TypedLine<T>, vararg lines: TypedLine<T>) =
	typed(line).fold(lines) { plus(it) }

fun <T> typed(field: TypedField<T>, vararg fields: TypedField<T>) =
	typed(line(field)).fold(fields) { plus(line(it)) }

fun <T> typed(string: String, vararg strings: String): Typed<T> =
	typed(string fieldTo typed<T>()).fold(strings) { plus(it fieldTo typed()) }

fun anyTyped(literal: Literal): Typed<Any> =
	term(literal.any) of nativeType

fun <T> nativeTyped(native: T): Typed<T> =
	term(native) of nativeType

fun <T> Typed<T>.plus(typed: TypedLine<T>): Typed<T> =
	plusTerm(typed) of type.plus(typed.line)

fun <T> Typed<T>.plus(action: Action<T>): Typed<T> =
	plus(fn(action.body.term) of line(action.param arrowTo action.body.type))

fun <T> Typed<T>.plus(typed: TypedField<T>): Typed<T> =
	plus(typed.term of line(typed.field))

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

val <T> Typed<T>.resolve: Typed<T>?
	get() =
		resolveLinkOrNull?.run {
			head.resolveFieldOrNull?.let { field ->
				tail.resolve(field)
			}
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

val <T> Typed<T>.lineLink: Link<Typed<T>, TypedLine<T>>
	get() =
		resolveLinkOrNull.notNullOrError("$type as link")

val <T> Typed<T>.lastTypedLine: TypedLine<T>
	get() =
		lineLink.head

val <T> TypedLine<T>.typedField: TypedField<T>
	get() =
		resolveFieldOrNull.notNullOrError("$line not a field")

val <T> Typed<T>.previousTyped: Typed<T>
	get() =
		lineLink.tail

val <T> Typed<T>.onlyLine
	get() =
		lineLink.run { failIfOr(!tail.isEmpty) { head } }

val <T> TypedLine<T>.choice: TypedChoice<T>
	get() =
		resolveChoiceOrNull.notNullOrError("$line as choice")

val <T> TypedLine<T>.arrow: TypedArrow<T>
	get() =
		(line as? ArrowLine)?.arrow?.let { term of it }.notNullOrError("$line as arrow")

val <T> TypedField<T>.rhs: Typed<T>
	get() =
		resolveRhs

val <T> TypedLine<T>.resolveFieldOrNull: TypedField<T>?
	get() =
		(line as? FieldLine)?.field?.let { term of it }

val <T> TypedLine<T>.resolveChoiceOrNull: TypedChoice<T>?
	get() =
		(line as? ChoiceLine)?.choice?.let { term of it }

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
		is FieldLine -> (term of line.field).resolveGet(string)
		is ChoiceLine -> notNullIf(string == "choice") { term of type(line) }
		is ArrowLine -> notNullIf(string == "function") { term of type(line) }
	}

fun <T> TypedField<T>.resolveGet(string: String): Typed<T>? =
	notNullIf(field.string == string) {
		term of type(field)
	}

fun <T> Typed<T>.eval(rhs: TypedField<T>): Typed<T> =
	resolve(rhs) ?: plus(term of line(rhs.field))

fun <T> Typed<T>.resolveWrap(string: String) =
	term of type(string fieldTo type)

fun <T> Typed<T>.plusNative(rhs: Term<T>): Typed<T> =
	if (type.isStatic) rhs of type.plus(nativeLine)
	else pair(term, rhs) of type.plus(nativeLine)

infix fun <T> String.fieldTo(typed: Typed<T>): TypedField<T> =
	typed.term of (this fieldTo typed.type)

infix fun <T> String.lineTo(typed: Typed<T>): TypedLine<T> =
	line(this fieldTo typed)

fun <T> line(typed: TypedField<T>): TypedLine<T> =
	typed.term of line(typed.field)

fun <T> typed(line: TypedLine<T>): Typed<T> =
	typed<T>().plus(line)

fun <T> typed(string: String): Typed<T> =
	typed(line(string fieldTo typed()))

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

// === switch

fun <T> Typed<T>.switchOf(type: Type, fnStack: Stack<Term<T>>): Typed<T> =
	term.matchTerm(*fnStack.array) of type

val <T> Typed<T>.action: Action<T>
	get() =
		onlyLine.arrow.action

val <T> TypedArrow<T>.action: Action<T>
	get() =
		arrow.lhs does (term of arrow.rhs)

fun <T> Typed<T>.ret(action: Action<T>): Typed<T> =
	fn(term).invoke(action.body.term) of type

fun <T> typed(action: Action<T>): Typed<T> =
	fn(action.body.term) of type(line(action.param arrowTo action.body.type))

fun <T> typed(index: Index, type: Type) =
	arg<T>(index) of type

fun <T> typed(term: Term<T>, type: Type) =
	term of type
