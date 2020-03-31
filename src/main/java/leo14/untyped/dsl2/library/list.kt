package leo14.untyped.dsl2.library

import leo14.untyped.dsl2.*

val list = library_ {
	folded { anything }
	fold { list { anything } }
	using { function }
	gives {
		given.fold.list
		equals_ { list }
		match {
			true_ { given.folded }
			false_ {
				given.folded
				it { given.fold.list.last }
				use { given.using.function }
				fold { given.fold.list.previous.list }
				using { given.using.function }
				repeat
			}
		}
	}

	assert {
		folded
		fold { list }
		using {
			function {
				given.folded
				append { given.last.number }
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

	list { anything }
	reverse
	gives {
		folded
		fold { given.reverse.list }
		using {
			function {
				given.folded
				append { given.last.object_ }
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
	map { using { function } }
	gives {
		given.map.using.function.as_ { f }
		folded
		fold { given.list }
		using {
			function {
				given.folded
				append {
					given.last.object_
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
			using {
				function {
					given.number.text
				}
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