package leo20.dsl

import leo14.isEmpty
import leo14.lineTo
import leo15.dsl.*
import leo20.eval

fun run_(f: F) = script_(f).eval.run {
	if (!isEmpty) {
		error("unexpected" lineTo this)
	}
}
