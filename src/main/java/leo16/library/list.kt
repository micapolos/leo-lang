package leo16.library

import leo15.dsl.*
import leo16.compile_

fun main() {
	list
}

val list = compile_ {
	any.list
	append { any }
	does {
		list {
			linked {
				previous { list }
				last { append.content }
			}
		}
	}

	test {
		empty.list
		append { 1.number }
		equals_ { list { item { 1.number } } }
	}

	test {
		list { item { 1.number } }
		append { 2.number }
		equals_ { list { item { 1.number }; item { 2.number } } }
	}

	list { any }
	fold {
		to { any }
		function {
			item { any }
			to { any }
		}
	}
	does {
		list.match {
			empty { fold.to.content }
			linked {
				linked.previous.list
				fold {
					to {
						fold.function
						take {
							item { linked.last.content }
							to { fold.to.content }
						}
					}
					this_ { fold.function }
				}
				repeat
			}
		}
	}

	test {
		list {
			item { zero }
			item { one }
			item { two }
		}
		fold {
			to { empty.list }
			function {
				item { any }
				to { any }
				does { to.list.append { item.content } }
			}
		}
		equals_ {
			list {
				item { two }
				item { one }
				item { zero }
			}
		}
	}

	any.list.reverse
	does {
		reverse.list
		fold {
			to { empty.list }
			function {
				item { any }
				to { any }
				does { to.list.append { item.content } }
			}
		}
	}

	test {
		list {
			item { 1.number }
			item { 2.number }
			item { 3.number }
		}.reverse
		equals_ {
			list {
				item { 3.number }
				item { 2.number }
				item { 1.number }
			}
		}
	}

	any.list
	map { function { any } }
	does {
		list.reverse
		fold {
			to { empty.list }
			function {
				item { any }
				to { any }
				does {
					to.list
					append { map.function.take { item.content } }
				}
			}
		}
	}

	test {
		list {
			item { 1.number }
			item { 2.number }
			item { 3.number }
		}
		map { function { any.does { number.ok } } }
		equals_ {
			list {
				item { 1.number.ok }
				item { 2.number.ok }
				item { 3.number.ok }
			}
		}
	}

	any.list.length
	does {
		import { number }
		length.list
		fold {
			to { 0.number }
			function {
				item { any }
				to { any }
				does { to.number.plus { 1.number } }
			}
		}.length
	}

	test {
		empty.list.length
		equals_ { 0.number.length }
	}

	test {
		list {
			item { 0.number }
			item { 1.number }
			item { 2.number }
		}.length
		equals_ { 3.number.length }
	}
}
