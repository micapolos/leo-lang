package leo16.library

import leo15.dsl.*
import leo16.value_

val text = value_ {
	dictionary {
		define {
			any.text
			cut {
				from { any.number }
				to { any.number }
			}
			gives {
				given.text.native
				invoke {
					"java.lang.String".text.name.native.class_
					method {
						name { "substring".text }
						parameter {
							list {
								this_ { int.native.class_ }
								this_ { int.native.class_ }
							}
						}
					}
					parameter {
						list {
							this_ { given.cut.from.number.int.native }
							this_ { given.cut.to.number.int.native }
						}
					}
				}
				text
			}
		}
	}
}