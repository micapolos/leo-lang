package lambda.v2

import leo.base.fold
import leo.base.iterate

fun tuple(size: Int) = fn(size + 1) {
	var index = 1
	arg(size + 1).iterate(size) { apply(arg(index++)) }
}

fun nthOf(n: Int, size: Int) = fn(size) { arg(n) }

val Term.tupleAt get() = this
fun tuple(vararg terms: Term) = tuple(terms.size).fold(terms) { invoke(it) }


