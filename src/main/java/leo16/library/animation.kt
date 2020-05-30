package leo16.library

import leo15.dsl.*
import leo16.compile_

fun main() {
	animation
}

val animation = compile_ {
	anything.animation.show
	does {
		body {
			use { dsl.javascript }
			show.animation.thing.evaluate
		}
		do_ {
			use { javascript }
			empty.javascript
			this_ { body.thing }
			evaluate
			animated
			run
			do_ { show.thing }
		}
	}
}