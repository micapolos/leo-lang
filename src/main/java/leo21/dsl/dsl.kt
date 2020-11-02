package leo21.dsl

import leo14.isEmpty
import leo14.lineTo
import leo15.dsl.*
import leo21.compiled.evaluate

fun run_(f: F) = script_(f).evaluate.run {
	if (!isEmpty) {
		error("unexpected" lineTo this)
	}
}
