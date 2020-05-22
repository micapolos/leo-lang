package leo16.library.native

import leo.base.print
import leo15.dsl.*
import leo16.compile_

fun main() {
	string.value.print
}

val string = compile_ {
	use { reflection }
	use { char.sequence.native }

	string.class_
	is_ { "java.lang.String".text.name.class_ }

	kotlin.string.class_
	is_ { "leo16.native.StringKt".text.name.class_ }

	string.char { at { int } }.method
	is_ {
		string.class_
		method {
			name { "charAt".text }
			parameter { list { item { int.class_ } } }
		}
	}

	string.length.method
	is_ {
		string.class_
		method {
			name { "length".text }
			parameter { empty.list }
		}
	}

	string.concat.method
	is_ {
		string.class_
		method {
			name { "concat".text }
			parameter { list { item { string.class_ } } }
		}
	}

	string.substring.method
	is_ {
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

	string.replace.method
	is_ {
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

	string.split.method
	is_ {
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