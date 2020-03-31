package leo14.untyped.dsl2.library

import leo14.untyped.dsl2.*

val fold = library_ {
	folded { anything }
	fold { anything }
	doing { function }
	gives {
		given.fold.equals_ { fold }.match {
			true_ { given.folded }
			false_ {
				given.folded
				it { given.fold.last }
				call { given.doing.function }
				it { given.fold.previous.fold }
				doing { given.doing.function }
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
		doing {
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