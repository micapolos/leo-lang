package leo16.reducers

import leo.base.charSeq
import leo.base.fold
import leo14.Reducer
import leo14.reduce

fun <T> Reducer<T, Char>.charReduce(string: String): Reducer<T, Char> =
	fold(string.charSeq) { reduce(it) }