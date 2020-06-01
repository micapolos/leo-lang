package leo16.library.demo

import leo15.dsl.*
import leo16.print_

fun main() {
	print_(animation)
}

val animation = dsl_ {
	use { base }

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