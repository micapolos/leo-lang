package leo16.library

import leo15.dsl.*

val html = dsl_ {
	use { base }
	use { reflection }

	html.any.is_ { text.any.html }

	html.any.open
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
