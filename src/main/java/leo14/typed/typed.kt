package leo14.typed

import leo.base.failIfOr
import leo.base.fold
import leo.base.notNullIf
import leo.base.notNullOrError
import leo13.*
import leo14.lambda.*
import leo14.line
import leo14.literal

data class Typed<out T>(val term: Term<T>, val type: Type) {
	override fun toString() = reflectScriptLine { line(literal(toString())) }.toString()
}
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

fun <T> typed(line: TypedLine<T>, vararg lines: TypedLine<T>): Typed<T> =
	typed(line).fold(lines) { plus(it) }

fun <T> typed(field: TypedField<T>, vararg fields: TypedField<T>) =
	typed(line(field)).fold(fields) { plus(line(it)) }

fun <T> typed(string: String, vararg strings: String): Typed<T> =
	typed(string fieldTo typed<T>()).fold(strings) { plus(it fieldTo typed()) }

fun <T> Typed<T>.plus(typed: TypedLine<T>): Typed<T> =
	plusTerm(typed) of type.plus(typed.line)

fun <T> Typed<T>.plus(function: Function<T>): Typed<T> =
	plus(function.does.term of line(function.takes arrowTo function.does.type))

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
		else -> resolveAccess(string)// ?: resolveWrap(string)
	}

val <T> Typed<T>.resolve: Typed<T>?
	get() =
		resolveLinkOrNull?.run {
			head.fieldOrNull?.let { field ->
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

fun <T> Typed<T>.resolveLink(apply: (Link<Typed<T>, TypedLine<T>>) -> Typed<T>?): Typed<T>? =
	type.lineLinkOrNull?.let { link ->
		if (link.tail.isStatic)
			if (link.head.isStatic) apply((id<T>() of link.tail) linkTo (id<T>() of link.head))
			else apply((id<T>() of link.tail) linkTo (term of link.head))
		else
			if (link.head.isStatic) apply((term of link.tail) linkTo (id<T>() of link.head))
			else apply((arg0<T>().first of link.tail) linkTo (arg0<T>().second of link.head))
				?.let { applied ->
					fn(applied.term).invoke(term).of(applied.type)
				}
	}

val <T> Typed<T>.lineLink: Link<Typed<T>, TypedLine<T>>
	get() =
		resolveLinkOrNull.notNullOrError("$type as link")

val <T> Typed<T>.resolveFieldOrNull: TypedField<T>?
	get() =
		type.onlyLineOrNull?.fieldOrNull?.let { term of it }

val <T> Typed<T>.lastTypedLine: TypedLine<T>
	get() =
		lineLink.head

val <T> TypedLine<T>.typedField: TypedField<T>
	get() =
		fieldOrNull.notNullOrError("$line not a field")

val <T> Typed<T>.previousTyped: Typed<T>
	get() =
		lineLink.tail

val <T> Typed<T>.onlyLine
	get() =
		lineLink.run { failIfOr(!tail.isEmpty) { head } }

val <T> Typed<T>.onlyLineOrNull
	get() =
		resolveLinkOrNull?.run { notNullIf(tail.isEmpty) { head } }

val <T> TypedLine<T>.choice: TypedChoice<T>
	get() =
		choiceOrNull.notNullOrError("$line as choice")

val <T> TypedLine<T>.arrow: TypedArrow<T>
	get() =
		arrowOrNull.notNullOrError("$line as arrow")

val <T> TypedLine<T>.arrowOrNull: TypedArrow<T>?
	get() =
		(line as? ArrowLine)?.arrow?.let { term of it }

val <T> TypedField<T>.rhs: Typed<T>
	get() =
		resolveRhs

val <T> TypedLine<T>.fieldOrNull: TypedField<T>?
	get() =
		(line as? FieldLine)?.field?.let { term of it }

val <T> TypedLine<T>.choiceOrNull: TypedChoice<T>?
	get() =
		(line as? ChoiceLine)?.choice?.let { term of it }

val <T> TypedField<T>.resolveRhs: Typed<T>
	get() =
		term of field.rhs

fun <T> Typed<T>.resolveAccess(string: String): Typed<T>? =
	onlyLineOrNull?.fieldOrNull?.resolveRhs?.resolveGet(string)

fun <T> Typed<T>.resolveGet(string: String): Typed<T>? =
	resolveLinkOrNull?.let { link ->
		link.head.rhs(string) ?: link.tail.resolveGet(string)
	}

fun <T> TypedLine<T>.rhs(string: String): Typed<T>? =
	when (line) {
		is NativeLine -> notNullIf(string == line.native.name) { term of type(line) }
		is FieldLine -> (term of line.field).rhs(string)
		is ChoiceLine -> notNullIf(string == "choice") { term of type(line) }
		is ArrowLine -> notNullIf(string == "function") { term of type(line) }
		is AnyLine -> null
	}

fun <T> TypedField<T>.rhs(string: String): Typed<T>? =
	notNullIf(field.string == string) {
		term of type(field)
	}

fun <T> Typed<T>.eval(rhs: TypedField<T>): Typed<T> =
	resolve(rhs) ?: plus(term of line(rhs.field))

fun <T> Typed<T>.resolveWrap(string: String) =
	term of type(string fieldTo type)

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

fun <T> Stack<TypedLine<T>>.pushReverseDecompileLines(typed: Typed<T>): Stack<TypedLine<T>>? {
	val linkOrNull = typed.decompileLinkOrNull
	return if (linkOrNull == null) notNullIf(typed == typed<T>()) { this }
	else push(linkOrNull.head).pushReverseDecompileLines(linkOrNull.tail)
}

val <T> Typed<T>.decompileLineStack: Stack<TypedLine<T>>?
	get() =
		stack<TypedLine<T>>().pushReverseDecompileLines(this)?.reverse

val <T> Typed<T>.decompileOnlyLineOrNull: TypedLine<T>?
	get() =
		decompileLinkOrNull?.let { link ->
			notNullIf(link.tail.isEmpty) {
				link.head
			}
		}

fun <T> Typed<T>.get(string: String): Typed<T>? =
	decompileOnlyLineOrNull?.fieldOrNull?.rhs?.rhs(string)

fun <T> Typed<T>.rhs(string: String): Typed<T>? =
	decompileLinkOrNull?.let { link ->
		link.head.rhs(string) ?: link.tail.rhs(string)
	}

// === switch

fun <T> Typed<T>.switchOf(type: Type, fnStack: Stack<Term<T>>): Typed<T> =
	term.matchTerm(*fnStack.array) of type

val <T> Typed<T>.function: Function<T>
	get() =
		functionOrNull!!

val <T> Typed<T>.functionOrNull: Function<T>?
	get() =
		onlyLineOrNull?.arrowOrNull?.function

val <T> TypedArrow<T>.function: Function<T>
	get() =
		arrow.lhs does (term of arrow.rhs)

fun <T> Typed<T>.ret(function: Function<T>): Typed<T> =
	fn(term).invoke(function.does.term) of type

fun <T> typed(function: Function<T>): Typed<T> =
	fn(function.does.term) of type(line(function.takes arrowTo function.does.type))

fun <T> typed(index: Index, type: Type) =
	arg<T>(index) of type

fun <T> typed(term: Term<T>, type: Type) =
	term of type

fun <T> Typed<T>.eval(evaluator: Evaluator<T>) =
	term.eval(evaluator) of type
