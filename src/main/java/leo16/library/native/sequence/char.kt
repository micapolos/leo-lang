package leo16.library.native.sequence

import leo.base.print
import leo15.dsl.*
import leo16.compile_

fun main() {
	char.value.print
}

val char = compile_ {
	use { reflection }

	char.sequence.class_
	is_ { "java.lang.CharSequence".text.name.class_ }
}