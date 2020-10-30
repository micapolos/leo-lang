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

	define {
		list { any }
		fold { function { any } }
		does {
			recursively {
				get { list }
				switch {
					empty {
						does {
							list { get { empty } }
						}
					}
					link {
						does {
							get { link { previous { list } } }
							fold { get { fold { function } } }
							append {
								get { fold { function } }
								apply { get { link { last { item } } } }
							}
						}
					}
				}
			}
		}
	}

	test {
		list { empty }
		fold {
			function {
				item {
					get { item { number } }
					plus { number(1) }
				}
			}
		}
		equals_ {
			list { empty }
		}
	}

	test {
		list { empty }
		append { item { number(1) } }
		append { item { number(2) } }
		fold {
			function {
				item {
					get { item { number } }
					plus { number(10) }
				}
			}
		}
		equals_ {
			list { empty }
			append { item { number(11) } }
			append { item { number(12) } }
		}
	}
}