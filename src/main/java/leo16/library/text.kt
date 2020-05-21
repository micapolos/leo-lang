package leo16.library

import leo15.dsl.*
import leo16.compile_
import leo16.nativeString

fun main() {
	text
}

val text = compile_ {
	number.import
	reflection.import
	list.import
	use { string.native.library }

	any.native.text.length
	does {
		length.text.native
		invoke {
			string.length.method
			parameter { empty.list }
		}
		int.number.length
	}

	test { "Hello, world!".text.length.equals_ { 13.number.length } }

	any.native.text
	cut {
		from { any.native.number }
		to { any.native.number }
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

	any.native.text
	plus { any.native.text }
	does {
		text.native
		invoke {
			string.concat.method
			parameter { list { item { plus.text.native } } }
		}
		text
	}

	test { "Hello, ".text.plus { "world!".text }.equals_ { "Hello, world!".text } }

	any.native.text
	replace {
		all { any.native.text }
		with { any.native.text }
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

	any.native.text
	split { by { any.native.text } }
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
		map { function { any.does { native.text } } }
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

	any.native.text.line.list
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

	any.native.text
	character { any.native.number }
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
}
