package leo16.library

import leo15.dsl.*
import leo16.compile_

fun main() {
	animation
}

val animation = compile_ {
	any.animate
	does {
		body {
			use { dsl.javascript.library }
			animate.content.evaluate
		}
		do_ {
			use { javascript.library }
			empty.javascript
			this_ { body.content }
			evaluate
			animated
			run
		}
	}
}