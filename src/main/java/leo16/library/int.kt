package leo16.library

import leo15.dsl.*
import leo16.compile_

fun main() {
	int
}

val int = compile_ {
	use { reflection }

	use {
		integer.class_
		is_ {
			"leo16.native.IntKt".text.name.class_
		}

		big.decimal.int.value.method.is_ {
			big.decimal.class_
			method {
				name { "intValueExact".text }
				parameter { list }
			}
		}

		integer.plus.method.is_ {
			integer.class_
			method {
				name { "plus".text }
				parameter {
					list {
						item { int.class_ }
						item { int.class_ }
					}
				}
			}
		}

		integer.minus.method.is_ {
			integer.class_
			method {
				name { "minus".text }
				parameter {
					list {
						item { int.class_ }
						item { int.class_ }
					}
				}
			}
		}

		integer.times.method.is_ {
			integer.class_
			method {
				name { "times".text }
				parameter {
					list {
						item { int.class_ }
						item { int.class_ }
					}
				}
			}
		}
	}

	int.any.is_ { native.int }

	int.any
	plus { int.any }
	does {
		integer.plus.method
		invoke {
			parameter {
				list {
					item { int.native }
					item { plus.int.native }
				}
			}
		}
		int
	}

	test { 2.int.plus { 3.int }.equals_ { 5.int } }

	int.any
	minus { int.any }
	does {
		integer.minus.method
		invoke {
			parameter {
				list {
					item { int.native }
					item { minus.int.native }
				}
			}
		}
		int
	}

	test { 5.int minus { 3.int } equals_ { 2.int } }

	int.any
	times { int.any }
	does {
		integer.times.method
		invoke {
			parameter {
				list {
					item { int.native }
					item { times.int.native }
				}
			}
		}
		int
	}

	test { 2.int times { 3.int } equals_ { 6.int } }
}
