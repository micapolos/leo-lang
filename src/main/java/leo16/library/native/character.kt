package leo16.library.native

import leo.base.print
import leo15.dsl.*
import leo16.compile_

fun main() {
	character.value.print
}

val character = compile_ {
	character.class_
	is_ { "java.lang.Character".text.name.class_ }
}