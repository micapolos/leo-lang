package leo16.library

import leo15.dsl.*
import leo16.value_

val text = value_ {
	dictionary {
		int.load.import

		any.text.length
		gives {
			given.length.text.native
			invoke {
				"java.lang.String".text.name.class_
				method {
					name { "length".text }
					parameter { list }
				}
				parameter { list }
			}
			int.number
		}

		any.text
		cut {
			from { any.number }
			to { any.number }
		}
		gives {
			given.text.native
			invoke {
				"java.lang.String".text.name.class_
				method {
					name { "substring".text }
					parameter {
						list {
							this_ { int.class_ }
							this_ { int.class_ }
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