package leo16.library

import leo15.dsl.*
import leo16.compile_

val html = compile_ {
	use { base }
	use { reflection }

	any.text.html.open
	does {
		"leo14.js.compiler.MainKt".text.name.class_
		method {
			name { "htmlOpen".text }
			parameter { list { item { "java.lang.String".text.name.class_ } } }
		}
		invoke { parameter { list { item { open.html.text.native } } } }
		clear
	}
}
