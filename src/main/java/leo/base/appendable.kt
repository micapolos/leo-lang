package leo.base

import java.io.File

fun Appendable.tryAppend(tryFn: Appendable.() -> Appendable?): Appendable? =
	StringBuilder().let { stringBuilder ->
		stringBuilder.tryFn().let { triedAppendable ->
			if (triedAppendable == null) null
			else append(stringBuilder.toString())
		}
	}


fun File.write(fn: Appendable.() -> Unit) {
	bufferedWriter().use(fn)
}

fun string(fn: Appendable.() -> Unit) {
	StringBuilder().apply { fn() }.toString()
}
