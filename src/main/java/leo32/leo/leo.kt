package leo32.leo

import leo.base.empty
import leo.base.string
import leo32.base.size
import leo32.runtime.*

data class Builder(var _term: Term)

typealias T = Builder
typealias Leo = Builder.() -> Unit

val Term.builder
	get() =
		Builder(this)

fun Builder.plus(string: String, fn: Builder.() -> Unit): Builder {
	_term = _term.plus(string) {
		val termBuilder = builder
		termBuilder.fn()
		termBuilder._term
	}
	return this
}

fun Builder.plus(string: String): Builder =
	plus(string) { Unit }

fun _term(leo: Leo): Term {
	val termBuilder = empty.term.builder
	termBuilder.leo()
	return termBuilder._term.descope
}

fun T.int(int: Int): T = plus("int") { plus(int.toString()) }
fun T.char(char: Char): T = plus("char") { plus("$char'") } // TODO: Escape
fun T.string(string: String): T = plus("string") { plus(string) } // TODO: Escape

val T.doIt get() = _do
fun T.doIt(leo: Leo) = _do(leo)

fun T.comment(string: String) = comment { string(string) }

fun T._import(leo: Leo) = leo()

fun _test(leo: Leo) {
	_term(leo).script.run {
		when {
			lineList.size.int == 0 -> println("ok")
			else -> throw AssertionError(code.string)
		}
	}
}
