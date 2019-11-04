package leo14.lambda

import leo.base.*
import leo.binary.*
import leo13.*
import leo13.Stack
import leo14.*
import leo14.Int2
import leo14.Int4

fun <T> fn(body: Term<T>) = term(abstraction(body))
fun <T> fn2(body: Term<T>) = fn(fn(body))
fun <T> fn3(body: Term<T>) = fn(fn2(body))

operator fun <T> Term<T>.invoke(term: Term<T>) = term(application(this, term))

fun <T> arg(index: Index): Term<T> = term(variable(index))
fun <T> arg0(): Term<T> = arg(index(0))
fun <T> arg1(): Term<T> = arg(index(1))
fun <T> arg2(): Term<T> = arg(index(2))

fun <T> id(): Term<T> = fn(arg0())

fun <T> first(): Term<T> = fn2(arg1())
fun <T> second(): Term<T> = fn2(arg0())
fun <T> Term<T>.switch(firstFn: Term<T>, secondFn: Term<T>) = this(firstFn)(secondFn)

val <T> Term<T>.first get() = this(first())
val <T> Term<T>.second get() = this(second())

fun <T> list() = pair<T>(first(), id())
fun <T> Term<T>.append(head: Term<T>) = pair(second(), pair(this, head))
val <T> Term<T>.isEmpty: Boolean get() = pair().first == first<T>()
val <T> Term<T>.link: Term<T> get() = pair().second
val <T> Term<T>.tail: Term<T> get() = pair().first
val <T> Term<T>.head: Term<T> get() = pair().second

// === boolean

fun <T> term(boolean: Boolean): Term<T> =
	if (boolean) second()
	else first()

fun <T> Term<T>.boolean(): Boolean =
	when (this) {
		first<T>() -> false
		second<T>() -> true
		else -> error("$this is not a boolean")
	}

// === pair

fun <T> pair(lhs: Term<T>, rhs: Term<T>) = fn3(arg0<T>()(arg2())(arg1()))(lhs)(rhs)
fun <T> Term<T>.pair(): Pair<Term<T>, Term<T>> =
	application { lhs, second ->
		lhs.application { fn, first ->
			if (fn == fn3(arg0<T>()(arg2())(arg1()))) first to second
			else error("$this is not a pair")
		}
	}

// === bit

fun <T> term(bit: Bit): Term<T> = term(bit.isOne)
fun <T> Term<T>.bit(): Bit = boolean().bit

// === ints

fun <T> term(int2: Int2): Term<T> = pair(term(int2.hi), term(int2.lo))
fun <T> term(int4: Int4): Term<T> = pair(term(int4.hi), term(int4.lo))
fun <T> term(byte: Byte): Term<T> = pair(term(byte.hi), term(byte.lo))
fun <T> term(short: Short): Term<T> = pair(term(short.byte1), term(short.byte0))
fun <T> term(int: Int): Term<T> = pair(term(int.short1), term(int.short0))

fun <T> Term<T>.int2() = pair().run { int2(first.bit(), second.bit()) }
fun <T> Term<T>.int4() = pair().run { int4(first.int2(), second.int2()) }
fun <T> Term<T>.byte() = pair().run { byte(first.int4(), second.int4()) }
fun <T> Term<T>.short() = pair().run { short(first.byte(), second.byte()) }
fun <T> Term<T>.int() = pair().run { int(first.short(), second.short()) }

// === stack

fun <T> term(stack: Stack<Term<T>>): Term<T> =
	list<T>().fold(stack.reverse) { append(it) }

tailrec fun <T> Stack<Term<T>>.plus(term: Term<T>): Stack<Term<T>> =
	if (term.isEmpty) this
	else push(term.link.head).plus(term.link.tail)

fun <T> Term<T>.termStack(): Stack<Term<T>> = stack<Term<T>>().plus(this).reverse

// === string

fun <T> stringTerm(string: String): Term<T> =
	term(stack<Term<T>>().fold(string.utf8ByteSeq.map { term<T>(this) }) { push(it) })

fun <T> Term<T>.string() =
	termStack().reverse.seq.map { byte() }.utf8String
