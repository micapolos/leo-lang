package leo16.reducers

import leo.base.iterate
import leo14.Reducer
import leo14.reduce
import leo14.reducer

fun <T> Reducer<T, Char>.indented(indent: Int): Reducer<T, Char> =
	reduced
		.reducer { char ->
			this
				.reducer(reduceFn)
				.run {
					if (char == '\n') reduce('\n').iterate(indent) { reduce(' ').reduce(' ') }
					else reduce(char)
				}
				.indented(indent)
		}
