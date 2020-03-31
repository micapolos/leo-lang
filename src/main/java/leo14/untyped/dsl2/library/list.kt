package leo14.untyped.dsl2.library

import leo14.untyped.dsl2.*

val list = library_ {
	anything
	fold { list { anything } }
	join { doing }
	expands {
		given.fold.list
		equals_ { list }
		match {
			true_ { given.object_.subject.subject }
			false_ {
				given.object_.subject.subject
				join { given.fold.list.last.object_ }
				use { given.join.doing }
				fold { given.fold.list.previous.list }
				join { given.join.doing }
				repeat
			}
		}
	}

	assert {
		list
		fold { list }
		join {
			doing {
				folded
				append { join.number }
			}
		}
		gives { list }
	}

	assert {
		list
		fold {
			list {
				number(0)
				number(1)
				number(2)
			}
		}
		join {
			doing {
				list
				append { join.number }
			}
		}
		gives {
			list {
				number(2)
				number(1)
				number(0)
			}
		}
	}

	list { anything }
	reverse
	does {
		list
		fold { reverse.list }
		join {
			doing {
				list
				append { join.object_ }
			}
		}
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
	map { doing }
	does {
		quote { list }
		fold { list }
		join {
			doing {
				list
				append {
					join.object_
					use { map.doing }
				}
			}
		}
		reverse
	}

	assert {
		list {
			number(1)
			number(2)
			number(3)
		}
		map {
			doing {
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