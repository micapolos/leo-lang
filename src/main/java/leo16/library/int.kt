package leo16.library

import leo15.dsl.*
import leo16.dictionary_

val int = dictionary_ {
	reflection.import

	import {
		dictionary {
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
	}

	any.int
	plus { any.int }
	gives {
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

	test { 2.int.plus { 3.int }.gives { 5.int } }

	any.int
	minus { any.int }
	gives {
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

	test { 5.int minus { 3.int } gives { 2.int } }

	any.int
	times { any.int }
	gives {
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

	test { 2.int times { 3.int } gives { 6.int } }
}
