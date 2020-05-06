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
						list { string.class_ }
					}
				}
			}

			string.substring.method.is_ {
				string.class_
				method {
					name { "substring".text }
					parameter {
						list {
							this_ { int.class_ }
							this_ { int.class_ }
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
							this_ { char.sequence.class_ }
							this_ { char.sequence.class_ }
						}
					}
				}
			}

			string.split.method.is_ {
				string.class_
				method {
					name { "split".text }
					parameter { list { string.class_ } }
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
					this_ { cut.from.number.int.native }
					this_ { cut.to.number.int.native }
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
			parameter { list { plus.text.native } }
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
					this_ { replace.all.text.native }
					this_ { replace.with.text.native }
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
			parameter { list { split.regex.text.native } }
		}
		array.list
		map { any.item.giving { item.native.text } }
	}

	test {
		"zero one two".text
		split { regex { " ".text } }
		gives { list { "zero".text; "one".text; "two".text } }
	}

	any.text.line.list
	gives { list.line.text.split { regex { "\n".text } } }

	test {
		"zero\none\ntwo".text.line.list
		gives { list { "zero".text; "one".text; "two".text } }
	}
}
