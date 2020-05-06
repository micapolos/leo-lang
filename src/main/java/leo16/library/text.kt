package leo16.library

import leo15.dsl.*
import leo16.dictionary_

val text = dictionary_ {
	number.import
	reflection.import
	list.import

	import {
		dictionary {
			string.class_.is_ {
				"java.lang.String".text.name.class_
			}

			char.sequence.class_.is_ {
				"java.lang.CharSequence".text.name.class_
			}

			string.length.method.is_ {
				string.class_
				method {
					name { "length".text }
					parameter { list }
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
				string.class_
				method {
					name { "split".text }
					parameter { list { item { string.class_ } } }
				}
			}
		}
	}

	any.text.length
	gives {
		length.text.native
		invoke {
			string.length.method
			parameter { list }
		}
		int.number.length
	}

	test { "Hello, world!".text.length.gives { 13.number.length } }

	any.text
	cut {
		from { any.number }
		to { any.number }
	}
	gives {
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
		gives { "world".text }
	}

	any.text
	plus { any.text }
	gives {
		text.native
		invoke {
			string.concat.method
			parameter { list { item { plus.text.native } } }
		}
		text
	}

	test { "Hello, ".text.plus { "world!".text }.gives { "Hello, world!".text } }

	any.text
	replace {
		all { any.text }
		with { any.text }
	}
	gives {
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
		gives { "foo, bar, zoo".text }
	}

	any.text
	split { regex { any.text } }
	gives {
		text.native
		invoke {
			string.split.method
			parameter { list { item { split.regex.text.native } } }
		}
		array.list
		map { any.item.giving { item.native.text } }
	}

	test {
		"zero one two".text
		split { regex { " ".text } }
		gives {
			list {
				item { "zero".text }
				item { "one".text }
				item { "two".text }
			}
		}
	}

	any.text.line.list
	gives { list.line.text.split { regex { "\n".text } } }

	test {
		"zero\none\ntwo".text.line.list
		gives {
			list {
				item { "zero".text }
				item { "one".text }
				item { "two".text }
			}
		}
	}
}
