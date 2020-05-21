package leo16.library.leo

import leo.base.print
import leo15.dsl.*
import leo16.compile_

fun main() {
	letter.value.print
}

val letter = compile_ {
	use { list.library }

	any.letter.check
	does { check.letter.error }

	list {
		item { a }
		item { b }
		item { c }
		item { d }
		item { e }
		item { f }
		item { g }
		item { h }
		item { i }
		item { j }
		item { k }
		item { l }
		item { m }
		item { n }
		item { o }
		item { p }
		item { q }
		item { r }
		item { s }
		item { t }
		item { u }
		item { v }
		item { w }
		item { x }
		item { y }
		item { z }
	}
	map {
		function {
			any
			does {
				this_ {
					word { word { word { check { content.letter } } } }
					word { is_ { content.letter } }
				}

				this_ {
					content.letter.text
					word { is_ { content.as_ { text } } }
				}

				this_ {
					content.as_ { text }.letter
					word { is_ { content.letter } }
				}
			}
		}
	}
	flat.content.compile

	test { zero.letter.check.equals_ { zero.letter.error } }
	test { f.letter.check.equals_ { f.letter } }
	test { f.letter.text.equals_ { "f".text } }
	test { "f".text.letter.equals_ { f.letter } }
}