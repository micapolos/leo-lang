package leo16.library

import leo15.dsl.*
import leo16.compile_
import leo16.nativeString

fun main() {
	text
}

val text = compile_ {
	use { number }
	use { reflection }
	use { list }
	use { string.native }
	use { character }

	text.any.is_ { native.any.text }

	text.any.length
	does {
		length.text.native
		invoke {
			string.length.method
			parameter { empty.list }
		}
		int.number.length
	}

	test { "Hello, world!".text.length.equals_ { 13.number.length } }

	text.any
	cut {
		from { number.any }
		to { number.any }
	}
	does {
		text.native
		invoke {
			string.substring.method
			parameter {
				list {
					item { cut.from.number.int.native }
					item { cut.to.number.int.native }
				}
			}
		}
		text
	}

	test {
		"Hello, world!".text
		cut {
			from { 7.number }
			to { 12.number }
		}
		equals_ { "world".text }
	}

	text.any
	plus { text.any }
	does {
		text.native
		invoke {
			string.concat.method
			parameter { list { item { plus.text.native } } }
		}
		text
	}

	test { "Hello, ".text.plus { "world!".text }.equals_ { "Hello, world!".text } }

	text.any
	replace {
		all { text.any }
		with { text.any }
	}
	does {
		text.native
		invoke {
			string.replace.method
			parameter {
				list {
					item { replace.all.text.native }
					item { replace.with.text.native }
				}
			}
		}
		text
	}

	test {
		"foo|bar|zoo".text
		replace {
			all { "|".text }
			with { ", ".text }
		}
		equals_ { "foo, bar, zoo".text }
	}

	text.any
	split { by { text.any } }
	does {
		string.split.method
		invoke {
			parameter {
				list {
					item { text.native }
					item { split.by.text.native }
				}
			}
		}
		array.list
		map { function { anything.does { native.text } } }
	}

	test {
		"zero one two".text
		split { by { " ".text } }
		equals_ {
			list {
				item { "zero".text }
				item { "one".text }
				item { "two".text }
			}
		}
	}

	text.any.line.list
	does {
		list.line.text
		split { by { "\n".text } }
	}

	test {
		"zero\none\ntwo".text.line.list
		equals_ {
			list {
				item { "zero".text }
				item { "one".text }
				item { "two".text }
			}
		}
	}

	text.any
	character { number.any }
	does {
		text.native
		invoke {
			string.char { at { int } }.method
			parameter { list { item { character.number.int.native } } }
		}
		character
	}

	test {
		"hello".text
		character { 0.number }
		as_ { text }
		equals_ { "character ${'h'.nativeString}".text }
	}

	text.any.character
	does {
		character.text.length.number.equals_ { 1.number }
		match {
			false_ { content }
			true_ { character.text.character { 0.number } }
		}
	}

	test { "".text.character.equals_ { "".text.character } }
	test { "a".text.character.equals_ { "a".text.character { 0.number } } }
	test { "ab".text.character.equals_ { "ab".text.character } }

	text.any
	plus { character.any }
	does { text.plus { plus.character.text } }

	test {
		"Hello".text
		plus { "!".text.character { 0.number } }
		equals_ { "Hello!".text }
	}
}
