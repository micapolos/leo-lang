package leo16.library.native

import leo15.dsl.*
import leo16.library_

fun main() {
	library_(double)
}

val double = dsl_ {
	use { reflection }

	java.double.class_
	is_ { "java.lang.Double".text.name.class_ }

	leo.double.class_
	is_ { "leo16.native.DoubleKt".text.name.class_ }

	double.plus.method
	is_ {
		leo.double.class_
		method {
			name { "plus".text }
			parameter {
				list {
					item { double.class_ }
					item { double.class_ }
				}
			}
		}
	}

	double.minus.method
	is_ {
		leo.double.class_
		method {
			name { "minus".text }
			parameter {
				list {
					item { double.class_ }
					item { double.class_ }
				}
			}
		}
	}

	double.times.method
	is_ {
		leo.double.class_
		method {
			name { "times".text }
			parameter {
				list {
					item { double.class_ }
					item { double.class_ }
				}
			}
		}
	}

	double.div.method
	is_ {
		leo.double.class_
		method {
			name { "div".text }
			parameter {
				list {
					item { double.class_ }
					item { double.class_ }
				}
			}
		}
	}

	double.mod.method
	is_ {
		leo.double.class_
		method {
			name { "mod".text }
			parameter {
				list {
					item { double.class_ }
					item { double.class_ }
				}
			}
		}
	}
}