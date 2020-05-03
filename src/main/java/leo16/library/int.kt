package leo16.library

import leo15.dsl.*
import leo16.dictionary_

val int = dictionary_ {
	reflection.dictionary.import

	import {
		dictionary {
			integer.class_.is_ {
				"leo16.library.IntKt".text.name.class_
			}

			integer.plus.method.is_ {
				integer.class_
				method {
					name { "plus".text }
					parameter {
						list {
							this_ { int.class_ }
							this_ { int.class_ }
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
							this_ { int.class_ }
							this_ { int.class_ }
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
							this_ { int.class_ }
							this_ { int.class_ }
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
					this_ { given.int.native }
					this_ { given.plus.int.native }
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
					this_ { given.int.native }
					this_ { given.minus.int.native }
				}
			}
		}
		int
	}

	test { 5.int.minus { 3.int }.gives { 2.int } }

	any.int
	times { any.int }
	gives {
		integer.times.method
		invoke {
			parameter {
				list {
					this_ { given.int.native }
					this_ { given.times.int.native }
				}
			}
		}
		int
	}

	test { 2.int.times { 3.int }.gives { 6.int } }
}

fun plus(a: Int, b: Int) = a.plus(b)
fun minus(a: Int, b: Int) = a.minus(b)
fun times(a: Int, b: Int) = a.times(b)