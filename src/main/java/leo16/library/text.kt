package leo16.library

import leo15.dsl.*
import leo16.compile_
import leo16.dictionary_

fun main() {
	text
}

val text = compile_ {
	number.import
	reflection.import
	list.import

	import {
		dictionary {
			string.class_.is_ {
				"java.lang.String".text.name.class_
			}

			kotlin.string.class_.is_ {
				"leo16.native.StringKt".text.name.class_
			}

			char.sequence.class_.is_ {
				"java.lang.CharSequence".text.name.class_
			}

			string.length.method.is_ {
				string.class_
				method {
					name { "length".text }
					parameter { empty.list }
				}
			}

			string.concat.method.is_ {
				string.class_
				method {
					name { "concat".text }
					parameter {
						list { item { string.class_ } }
					}
				}
			}

			string.substring.method.is_ {
				string.class_
				method {
					name { "substring".text }
					parameter {
						list {
							item { int.class_ }
							item { int.class_ }
						}
					}
				}
			}

			string.replace.method.is_ {
				string.class_
				method {
					name { "replace".text }
					parameter {
						list {
							item { char.sequence.class_ }
							item { char.sequence.class_ }
						}
					}
				}
			}

			string.split.method.is_ {
				kotlin.string.class_
				method {
					name { "split".text }
					parameter {
						list {
							item { string.class_ }
							item { string.class_ }
						}
					}
				}
			}
		}
	}

	any.text.length
	does {
		length.text.native
		invoke {
			string.length.method
			parameter { empty.list }
		}
		int.number.length
	}

	test { "Hello, world!".text.length.equals_ { 13.number.length } }

	any.text
	cut {
		from { any.number }
		to { any.number }
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

	any.text
	plus { any.text }
	does {
		text.native
		invoke {
			string.concat.method
			parameter { list { item { plus.text.native } } }
		}
		text
	}

	test { "Hello, ".text.plus { "world!".text }.equals_ { "Hello, world!".text } }

	any.text
	replace {
		all { any.text }
		with { any.text }
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

	any.text
	split { by { any.text } }
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

	any.text.line.list
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

	any.text
	equals_ { any.text }
	does {
		text.native
		object_.equals_ { equals_.text.native }
		boolean
	}

	test { "hello".text.equals_ { "hello".text }.equals_ { true_.boolean } }
	test { "hello".text.equals_ { "world".text }.equals_ { false_.boolean } }
}
