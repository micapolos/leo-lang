package leo14.lambda

import leo.base.byte0
import leo.base.byte1
import leo.base.fold
import leo.base.int
import leo.base.map
import leo.base.short
import leo.base.short0
import leo.base.short1
import leo.binary.Bit
import leo.binary.bit
import leo.binary.isOne
import leo.binary.utf8ByteSeq
import leo.binary.utf8String
import leo13.Index
import leo13.Stack
import leo13.fold
import leo13.push
import leo13.reverse
import leo13.seq
import leo13.stack
import leo14.Int2
import leo14.Int4
import leo14.byte
import leo14.hi
import leo14.int2
import leo14.int4
import leo14.lo

fun <T> fn(body: Term<T>) = term(abstraction(body))
fun <T> fn2(body: Term<T>) = fn(fn(body))
fun <T> fn3(body: Term<T>) = fn(fn2(body))

operator fun <T> Term<T>.invoke(term: Term<T>) = term(application(this, term))

fun <T> arg(index: Index): Term<T> = term(variable(index))
fun <T> arg0(): Term<T> = arg(0)
fun <T> arg1(): Term<T> = arg(1)
fun <T> arg2(): Term<T> = arg(2)
fun <T> arg3(): Term<T> = arg(3)

fun <T> id(): Term<T> = fn(arg0())

fun <T> first(): Term<T> = fn2(arg1())
fun <T> second(): Term<T> = fn2(arg0())
fun <T> Term<T>.switch(firstFn: Term<T>, secondFn: Term<T>) = this(firstFn)(secondFn)

val <T> Term<T>.first get() = this(first())
val <T> Term<T>.second get() = this(second())

fun <T> empty() = pair<T>(first(), id())
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

// === switch

val <T> Term<T>.switchesFirst: Boolean
	get() =
		abstraction { body ->
			body.abstraction { body ->
				body.variable { index ->
					when (index) {
						0 -> false
						1 -> true
						else -> null!!
					}
				}
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
	empty<T>().fold(stack.reverse) { append(it) }

tailrec fun <T> Stack<Term<T>>.plus(term: Term<T>): Stack<Term<T>> =
	if (term.isEmpty) this
	else push(term.link.head).plus(term.link.tail)

fun <T> Term<T>.termStack(): Stack<Term<T>> = stack<Term<T>>().plus(this).reverse

// === string

fun <T> stringTerm(string: String): Term<T> =
	term(stack<Term<T>>().fold(string.utf8ByteSeq.map { term<T>(this) }) { push(it) })

fun <T> Term<T>.string() =
	termStack().reverse.seq.map { byte() }.utf8String

// === tuples

fun <T> tupleTerm(vararg terms: Term<T>): Term<T> =
	terms.size.let { index ->
		arg0<T>().invokeArgs(index).fn(index.inc()).invoke(*terms)
	}

fun <T> Term<T>.invoke(vararg terms: Term<T>): Term<T> =
	fold(terms) { invoke(it) }

tailrec fun <T> Term<T>.invokeArgs(index: Index): Term<T> =
	if (index == 0) this
	else invoke(arg(index)).invokeArgs(index.dec())

tailrec fun <T> Term<T>.fn(index: Index): Term<T> =
	if (index == 0) this
	else fn(this).fn(index.dec())

// === either

val <T> Term<T>.eitherFirst get() = fn(fn(arg<T>(1).invoke(this)))
val <T> Term<T>.eitherSecond get() = fn(fn(arg<T>(0).invoke(this)))

fun <T> Term<T>.eitherSwitch(firstFn: Term<T>, secondFn: Term<T>): Term<T> =
	invoke(firstFn).invoke(secondFn)

fun <T, R> Term<T>.either(firstFn: (Term<T>) -> R, secondFn: (Term<T>) -> R): R =
	abstraction { body ->
		body.abstraction { body ->
			body.application { selector, term ->
				selector.variable { index ->
					when (index) {
						0 -> secondFn(term)
						1 -> firstFn(term)
						else -> null!!
					}
				}
			}
		}
	}

// === one of

fun <T> choiceTerm(index: Index, count: Index, term: Term<T>): Term<T> =
	arg<T>(index).invoke(term).fn(count)

fun <T> Term<T>.matchTerm(vararg fns: Term<T>): Term<T> =
	invoke(*fns)

fun <T> fix(): Term<T> =
	fn(fn(arg<T>(1).invoke(arg<T>(0).invoke(arg(0))))
		.invoke(fn(arg<T>(1).invoke(arg<T>(0).invoke(arg(0))))))