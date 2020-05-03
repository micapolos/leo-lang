package leo16.library

import leo15.dsl.*
import leo16.value_

val text = value_ {
	dictionary {
		number.library.import
		reflection.library.import

		import {
			dictionary {
				string.class_.is_ {
					"java.lang.String".text.name.class_
				}

				char.sequence.class_.is_ {
					"java.lang.CharSequence".text.name.class_
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

				string.replace.method.is_ {
					string.class_
					method {
						name { "replace".text }
						parameter {
							list {
								this_ { char.sequence.class_ }
								this_ { char.sequence.class_ }
							}
						}
					}
				}

				string.split.method.is_ {
					string.class_
					method {
						name { "split".text }
						parameter { list { string.class_ } }
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

		any.text
		replace {
			all { any.text }
			with { any.text }
		}
		gives {
			given.text.native
			invoke {
				string.replace.method
				parameter {
					list {
						this_ { given.replace.all.text.native }
						this_ { given.replace.with.text.native }
					}
				}
			}
			text
		}

		any.text
		split { regex { any.text } }
		gives {
			given.text.native
			invoke {
				string.split.method
				parameter { list { given.split.regex.text.native } }
			}
			array.list
			// TODO: Map to text
		}
	}
}