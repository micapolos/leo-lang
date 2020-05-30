package leo16.library

import leo15.dsl.*
import leo16.compile_

fun main() {
	list
}

val list = compile_ {
	list.any.is_ {
		list {
			empty
			or {
				link {
					previous { list.repeating }
					last { anything }
				}
			}
		}
	}

	list.any
	append { anything }
	does {
		list {
			link {
				previous { list }
				last { append.thing }
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

	list.any
	fold {
		to { anything }
		function {
			item { anything }
			to { anything }
		}
	}
	does {
		list.match {
			empty { fold.to.thing }
			link {
				link.previous.list
				fold {
					to {
						fold.function
						take {
							item { link.last.thing }
							to { fold.to.thing }
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
				item { anything }
				to { anything }
				does { to.list.append { item.thing } }
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

	list.any.reverse
	does {
		reverse.list
		fold {
			to { empty.list }
			function {
				item { anything }
				to { anything }
				does { to.list.append { item.thing } }
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

	list.any
	map { function { anything } }
	does {
		list.reverse
		fold {
			to { empty.list }
			function {
				item { anything }
				to { anything }
				does {
					to.list
					append { map.function.take { item.thing } }
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
		map { function { anything.does { number.ok } } }
		equals_ {
			list {
				item { 1.number.ok }
				item { 2.number.ok }
				item { 3.number.ok }
			}
		}
	}

	list.any.length
	does {
		use { number }
		length.list
		fold {
			to { 0.number }
			function {
				item { anything }
				to { anything }
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

	list.any.flat
	does {
		flat.list.reverse
		fold {
			to { meta { flat } }
			function {
				item { anything }
				to { anything }
				does {
					to.flat.thing
					this_ { item.thing }
					flat
				}
			}
		}
	}

	test {
		list {
			item { 0.number }
			item { 1.number }
		}
		flat.thing
		equals_ {
			0.number
			1.number
		}
	}
}
