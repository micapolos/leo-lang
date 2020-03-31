package leo14.untyped.dsl2.library

import leo14.untyped.dsl2.*

val fold = library_ {
	folded { anything }
	fold { anything }
	using { function }
	gives {
		given.fold.equals_ { fold }.match {
			true_ { given.folded }
			false_ {
				given.folded
				it { given.fold.last }
				use { given.using.function }
				it { given.fold.previous.fold }
				using { given.using.function }
				repeat
			}
		}
	}

	assert {
		folded
		fold {
			number(0)
			number(1)
			number(2)
		}
		using {
			function {
				given.folded
				append { given.last.number }
			}
		}
		gives {
			folded {
				number(2)
				number(1)
				number(0)
			}
		}
	}
}

fun main() = run_(fold)