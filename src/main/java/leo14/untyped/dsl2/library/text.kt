package leo14.untyped.dsl2.library

import leo14.untyped.dsl2.*

val text = library_ {
	text.length
	does {
		length.text.string.native
		invoke { text("length") }
		number
	}
	assert { text("foo").length.equals_ { number(3) } }

	text.lower.case
	does {
		case.lower.text.string.native
		invoke { text("toLowerCase") }
		text
	}
	assert { text("FoO").lower.case.equals_ { text("foo") } }

	text.upper.case
	does {
		case.upper.text.string.native
		invoke { text("toUpperCase") }
		text
	}
	assert { text("FoO").upper.case.equals_ { text("FOO") } }

	text.lines.list
	does {
		list.lines.text.string.native
		invoke {
			it { text("split") }
			it { text("\n").string.native }
		}
		list
		map {
			doing {
				native.text
			}
		}
	}

	assert {
		text("foo\nbar").lines.list
		equals_ {
			list {
				text("foo")
				text("bar")
			}
		}
	}

	text
	slice {
		from { number }
		to { number }
	}
	does {
		text.string.native
		invoke {
			text("substring")
			it { slice.from.number.int.native }
			it { slice.to.number.int.native }
		}.text
	}
}

fun main() = run_ { list(); text() }