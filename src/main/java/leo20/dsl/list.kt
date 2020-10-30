package leo20.dsl

import leo15.dsl.*

val list_ = dsl_ {
	define {
		list { any }
		append { item { any } }
		does {
			list {
				link {
					previous { get { list } }
					last { get { append { item } } }
				}
			}
		}
	}

	test {
		list { empty }
		append { item { number(1) } }
		equals_ {
			list {
				link {
					previous { list { empty } }
					last { item { number(1) } }
				}
			}
		}
	}

	test {
		list { empty }
		append { item { number(1) } }
		append { item { number(2) } }
		equals_ {
			list {
				link {
					previous {
						list {
							link {
								previous { list { empty } }
								last { item { number(1) } }
							}
						}
					}
					last { item { number(2) } }
				}
			}
		}
	}
}