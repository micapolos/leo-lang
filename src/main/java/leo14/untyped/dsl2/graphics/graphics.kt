package leo14.untyped.dsl2.graphics

import leo14.untyped.dsl2.*

fun main() {
	_run {
		native
		draw { circle }
		does {
			given.native
			invoke {
				text("fillArc")
				it { number(100).native.int }
				it { number(100).native.int }
				it { number(50).native.int }
				it { number(50).native.int }
				it { number(0).native.int }
				it { number(360).native.int }
			}
		}

		native
		draw { rectangle }
		does {
			given.native
			invoke {
				text("fillRect")
				it { number(50).native.int }
				it { number(50).native.int }
				it { number(100).native.int }
				it { number(100).native.int }
			}
		}
	}
}