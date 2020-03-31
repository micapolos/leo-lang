package leo14.untyped.dsl2.library

import leo14.untyped.dsl2.*

val text = library_ {
	text.length
	gives {
		given.length.text.string.native
		invoke { text("length") }
		number
	}
	assert { text("foo").length.gives { number(3) } }

	text.lower.case
	gives {
		given.case.lower.text.string.native
		invoke { text("toLowerCase") }
		text
	}
	assert { text("FoO").lower.case.gives { text("foo") } }

	text.upper.case
	gives {
		given.case.upper.text.string.native
		invoke { text("toUpperCase") }
		text
	}
	assert { text("FoO").upper.case.gives { text("FOO") } }

	text.lines.list
	gives {
		given.list.lines.text.string.native
		invoke {
			it { text("split") }
			it { text("\n").string.native }
		}
		list
		map {
			doing {
				function {
					given.native.text
				}
			}
		}
	}

	assert {
		text("foo\nbar").lines.list
		gives {
			list {
				text("foo")
				text("bar")
			}
		}
	}
}

fun main() = run_ { fold(); list(); text() }