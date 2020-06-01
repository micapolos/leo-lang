package leo16.library.native.sequence

import leo15.dsl.*
import leo16.library_

fun main() {
	library_(char)
}

val char = dsl_ {
	use { native.reflection }

	char.sequence.class_
	is_ {
		"java.lang.CharSequence".text.name.class_
		matching { native.any.class_ }
	}
}