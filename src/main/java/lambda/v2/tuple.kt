package lambda.v2

import leo.base.iterate

fun tuple(size: Int) = fn(size + 1) {
	var index = 1
	arg(size + 1).iterate(size) { apply(arg(index++)) }
}

fun nthOf(n: Int, size: Int) = fn(size) { arg(n) }
