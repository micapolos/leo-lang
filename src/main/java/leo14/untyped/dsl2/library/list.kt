package leo14.untyped.dsl2.library

import leo14.untyped.dsl2.*

val list = library_ {
	list { anything }
	reverse
	gives {
		folded
		it { fold { given.reverse.list.object_ } }
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
		it { given.list.object_.fold }
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

fun main() = run_ { fold(); list() }