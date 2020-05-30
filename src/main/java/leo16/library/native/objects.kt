package leo16.library.native

import leo15.dsl.*
import leo16.library_

fun main() {
	library_(objects)
}

val objects = dsl_ {
	use { reflection }

	object_.class_.is_ { "java.lang.Object".text.name.class_ }
}