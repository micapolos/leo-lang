package leo14.untyped.dsl2.library

import leo14.untyped.dsl2.*

val list = library_ {
	folded { anything }
	fold { list { anything } }
	join { function }
	does {
		fold.list
		equals_ { list }
		match {
			true_ { folded }
			false_ {
				folded
				join { fold.list.last.object_ }
				use { join.function }
				fold { fold.list.previous.list }
				join { join.function }
				repeat
			}
		}
	}

	assert {
		folded
		fold { list }
		join {
			function {
				folded
				append { join.number }
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
		join {
			function {
				folded
				append { join.number }
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
		join {
			function {
				folded
				append { join.object_ }
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
		join {
			function {
				folded
				append {
					join.object_
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