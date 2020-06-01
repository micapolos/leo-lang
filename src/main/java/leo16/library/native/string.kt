package leo16.library.native

import leo15.dsl.*
import leo16.library_

fun main() {
	library_(string)
}

val string = dsl_ {
	use { native.reflection }
	use { char.sequence.native }

	string.class_
	is_ {
		"java.lang.String".text.name.class_
		matching { native.any.class_ }
	}

	kotlin.string.class_
	is_ {
		"leo16.native.StringKt".text.name.class_
		matching { native.any.class_ }
	}

	string.char { at { int } }.method
	is_ {
		string.class_
		method {
			name { "charAt".text }
			parameter { list { item { int.class_ } } }
		}
		matching { native.any.method }
	}

	string.length.method
	is_ {
		string.class_
		method {
			name { "length".text }
			parameter { empty.list }
		}
		matching { native.any.method }
	}

	string.concat.method
	is_ {
		string.class_
		method {
			name { "concat".text }
			parameter { list { item { string.class_ } } }
		}
		matching { native.any.method }
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
		matching { native.any.method }
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
		matching { native.any.method }
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
		matching { native.any.method }
	}
}