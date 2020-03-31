package leo14.untyped.dsl2.library

import leo14.untyped.dsl2.*

val list = library_ {
	folded { anything }
	fold { list { anything } }
	step { function }
	does {
		fold.list
		equals_ { list }
		match {
			true_ { folded }
			false_ {
				folded
				it { fold.list.last }
				use { step.function }
				fold { fold.list.previous.list }
				step { step.function }
				repeat
			}
		}
	}

	assert {
		folded
		fold { list }
		step {
			function {
				folded
				append { last.number }
			}
		}
		gives { folded }
	}

	assert {
		folded
		fold {
			list {
				number(0)
				number(1)
				number(2)
			}
		}
		step {
			function {
				folded
				append { last.number }
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

	list { anything }
	reverse
	does {
		folded
		fold { reverse.list }
		step {
			function {
				folded
				append { last.object_ }
			}
		}
		object_.list
	}

	assert {
		list
		reverse
		gives { list }
	}

	assert {
		list {
			number(0)
			number(1)
			number(2)
		}
		reverse
		gives {
			list {
				number(2)
				number(1)
				number(0)
			}
		}
	}

	list { anything }
	map { function }
	does {
		map.function.as_ { f }
		folded
		fold { list }
		step {
			function {
				folded
				append {
					last.object_
					use { f }
				}
			}
		}
		object_.list.reverse
	}

	assert {
		list {
			number(1)
			number(2)
			number(3)
		}
		map {
			function {
				number.text
			}
		}
		gives {
			list {
				text("1")
				text("2")
				text("3")
			}
		}
	}
}

fun main() = run_ { list() }