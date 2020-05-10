package leo16.library

import leo15.dsl.*
import leo16.dictionary_

fun main() {
	list
}

val list = dictionary_ {
	any.list
	append { any }
	gives {
		list {
			link {
				previous { list }
				last { append.content }
			}
		}
	}

	test {
		empty.list
		append { 1.number }
		gives { list { item { 1.number } } }
	}

	test {
		list { item { 1.number } }
		append { 2.number }
		gives { list { item { 1.number }; item { 2.number } } }
	}

	list { any }
	fold {
		to { any }
		function {
			item { any }
			to { any }
		}
	}
	gives {
		repeating {
			list.match {
				empty { fold.to.content }
				link {
					link.previous.list
					fold {
						to {
							fold.function
							take {
								item { link.last.content }
								to { fold.to.content }
							}
						}
						this_ { fold.function }
					}
					repeat
				}
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
				gives { to.list.append { item.content } }
			}
		}
		gives {
			list {
				item { two }
				item { one }
				item { zero }
			}
		}
	}

	any.list.reverse
	gives {
		reverse.list
		fold {
			to { empty.list }
			function {
				item { any }
				to { any }
				gives { to.list.append { item.content } }
			}
		}
	}

	test {
		list {
			item { 1.number }
			item { 2.number }
			item { 3.number }
		}.reverse
		gives {
			list {
				item { 3.number }
				item { 2.number }
				item { 1.number }
			}
		}
	}

	any.list
	map { function { any } }
	gives {
		list.reverse
		fold {
			to { empty.list }
			function {
				item { any }
				to { any }
				gives {
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
		map { function { any.gives { number.ok } } }
		gives {
			list {
				item { 1.number.ok }
				item { 2.number.ok }
				item { 3.number.ok }
			}
		}
	}

	any.list.length
	gives {
		import { number }
		length.list
		fold {
			to { 0.number }
			function {
				item { any }
				to { any }
				gives { to.number.plus { 1.number } }
			}
		}.length
	}

	test {
		empty.list.length
		gives { 0.number.length }
	}

	test {
		list {
			item { 0.number }
			item { 1.number }
			item { 2.number }
		}.length
		gives { 3.number.length }
	}
}
