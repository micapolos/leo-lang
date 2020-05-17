package leo16.library.demo

import leo15.dsl.*
import leo16.compile_

fun main() {
	animation
}

val animation = compile_ {
	use { animation.library }

	set { font { "20px Menlo".text } }

	fill {
		text {
			"Time: ".text
			plus { animation.second.number }
			plus { "s".text }
		}
		x { 10.number }
		y { 40.number }
	}

	fill {
		text { "Frame: ".text.plus { animation.frame.number } }
		x { 10.number }
		y { 70.number }
	}

	fill {
		circle {
			radius {
				animation.second.number
				sinus.absolute
				times { 15.number }
				plus { 10.number }
			}
			x { mouse.x.number }
			y { mouse.y.number }
		}
	}

	fill {
		circle {
			radius { 10.number }
			x {
				mouse.x.number
				plus {
					animation.second.number
					sinus
					times { 40.number }
				}
			}
			y {
				mouse.y.number
				plus {
					animation.second.number
					cosinus
					times { 40.number }
				}
			}
		}
	}

	show
}