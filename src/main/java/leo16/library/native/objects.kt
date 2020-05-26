package leo16.library.native

import leo.base.print
import leo15.dsl.*
import leo16.compile_

fun main() {
	objects.value.print
}

val objects = compile_ {
	use { reflection }

	object_.class_.is_ { "java.lang.Object".text.name.class_ }
}