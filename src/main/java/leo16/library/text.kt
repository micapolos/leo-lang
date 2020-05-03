package leo16.library

import leo15.dsl.*
import leo16.value_

val text = value_ {
	dictionary {
		import { int.load }
		import { reflection.load }

		import {
			dictionary {
				string.class_.is_ {
					"java.lang.String".text.name.class_
				}

				string.length.method.is_ {
					string.class_
					method {
						name { "length".text }
						parameter { list }
					}
				}

				string.concat.method.is_ {
					string.class_
					method {
						name { "concat".text }
						parameter {
							list { string.class_ }
						}
					}
				}

				string.substring.method.is_ {
					string.class_
					method {
						name { "substring".text }
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

		any.text.length
		gives {
			given.length.text.native
			invoke {
				string.length.method
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
				string.substring.method
				parameter {
					list {
						this_ { given.cut.from.number.int.native }
						this_ { given.cut.to.number.int.native }
					}
				}
			}
			text
		}

		any.text
		plus { any.text }
		gives {
			given.text.native
			invoke {
				string.concat.method
				parameter { list { given.plus.text.native } }
			}
			text
		}
	}
}