package leo16.library.demo

import leo15.dsl.*
import leo16.compile_

val animate = compile_ {
	use { animate.library }

	set { font { "30px Arial".text } }
	fill {
		text {
			"Time: ".text
			plus { time }
		}
		x { mouse.x }
		y { mouse.y }
	}
	animate
}