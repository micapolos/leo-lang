package leo16.library.demo

import leo15.dsl.*
import leo16.compile_

fun main() {
	animation
}

val animation = compile_ {
	use { base }

	fill {
		text {
			"Frame: ".text
			plus { animation.frame.number }
		}
		x { 10.number }
		y { 30.number }
	}

	fill {
		circle {
			radius {
				animation.second.number
				times { 5.number }
				sinus.absolute
				times { 30.number }
			}
			x { mouse.x.number }
			y { mouse.y.number }
		}
	}
	animation
	show
}