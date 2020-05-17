package leo16.library

import leo15.dsl.*
import leo16.compile_

fun main() {
	animation
}

val animation = compile_ {
	any.animation.show
	does {
		body {
			use { dsl.javascript.library }
			show.animation.content.evaluate
		}
		do_ {
			use { javascript.library }
			empty.javascript
			this_ { body.content }
			evaluate
			animated
			run
			do_ { show.content }
		}
	}
}