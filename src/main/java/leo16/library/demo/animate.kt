package leo16.library.demo

import leo15.dsl.*
import leo16.compile_

fun main() {
	animate
}

val animate = compile_ {
	use { animate.library }

	set { font { "30px Arial".text } }

	fill {
		text {
			"Time: ".text
			plus { time }
		}
		x { 10.number }
		y { 40.number }
	}

	fill {
		circle {
			radius {
				time
				times { 0.005.number }
				sinus.absolute
				times { 5.number }
				plus { 25.number }
			}
			x { mouse.x }
			y { mouse.y }
		}
	}

	fill {
		circle {
			radius { 5.number }
			x {
				mouse.x
				plus {
					time
					times { 0.005.number }
					sinus
					times { 40.number }
				}
			}
			y {
				mouse.y
				plus {
					time
					times { 0.005.number }
					cosinus
					times { 40.number }
				}
			}
		}
	}

	animate
}