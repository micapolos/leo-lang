package leo14.untyped.dsl2.library

import leo14.untyped.dsl2.*

val text = library_ {
	text.length
	does {
		given.length.text.string.native
		invoke { text("length") }
		number
	}
	assert { text("foo").length.gives { number(3) } }

	text.lower.case
	does {
		given.case.lower.text.string.native
		invoke { text("toLowerCase") }
		text
	}
	assert { text("FoO").lower.case.gives { text("foo") } }

	text.upper.case
	does {
		given.case.upper.text.string.native
		invoke { text("toUpperCase") }
		text
	}
	assert { text("FoO").upper.case.gives { text("FOO") } }

	text.lines
	does {
		given.lines.text.string.native
		invoke {
			it { text("split") }
			it { text("\n").string.native }
		}
		list
		reverse { list }
		do_ {
			given.list
			equals_ { list }
			match {
				true_ { given.reverse.list }
				false_ {
					given.list.previous.list
					reverse {
						given.reverse.list.append {
							given.list.last.native
						}
					}
					repeat
				}
			}
		}
		reverse { lines }
		do_ {
			given.list
			equals_ { list }
			match {
				true_ { given.reverse.lines }
				false_ {
					given.list.previous.list
					reverse {
						given.reverse.lines.append {
							given.list.last.native.text
						}
					}
					repeat
				}
			}
		}
	}

	assert {
		text("foo\nbar").lines
		gives {
			lines {
				text("foo")
				text("bar")
			}
		}
	}
}

fun main() = run_(text)