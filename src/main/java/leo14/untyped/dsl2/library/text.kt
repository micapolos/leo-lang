package leo14.untyped.dsl2.library

import leo14.untyped.dsl2.*

val text = library_ {
	text.lines
	does {
		given.text.native.string
		invoke {
			text("split")
			it { text("\n").native.string }
		}
		list
		reverse
		recursively {
			do_ {
				given.list.object_
				equals_ { nothing_ }
				match {
					true_ { given.reverse }
					false_ {
						given.list.previous.list
						it {
							given.reverse.append {
								given.list.last.native.text
							}
						}
						recurse
					}
				}
			}
		}
		lines
		recursively {
			do_ {
				given.reverse.object_
				equals_ { nothing_ }
				match {
					true_ { given.lines }
					false_ {
						given.reverse.previous.reverse
						it {
							given.lines.append {
								given.reverse.last.text
							}
						}
						recurse
					}
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