package leo14.untyped.dsl2.example

import leo14.untyped.dsl2.*

fun main() {
	_run {
		my.circle
		gives {
			circle {
				radius { number(10) }
				center {
					point {
						x { number(13) }
						y { number(15) }
					}
				}
			}
		}

		my.circle.print
		my.circle.radius.print
		my.circle.center.print
		my.circle.center.point.print
		my.circle.center.point.x.print
		my.circle.center.point.y.print
	}
}