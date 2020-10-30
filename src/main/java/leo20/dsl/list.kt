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
		do_ { append { item { number(1) } } }
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
		do_ { append { item { number(1) } } }
		do_ { append { item { number(2) } } }
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
		map { function { any } }
		does {
			recursively {
				get { list }
				switch {
					empty {
						does {
							get { list }
						}
					}
					link {
						does {
							get { link { previous { list } } }
							do_ { map { get { map { function } } } }
							do_ {
								append {
									get { map { function } }
									apply { get { link { last { item } } } }
								}
							}
						}
					}
				}
			}
		}
	}

	test {
		list { empty }
		do_ {
			map {
				function {
					item {
						get { item { number } }
						do_ { plus { number(1) } }
					}
				}
			}
		}
		equals_ {
			list { empty }
		}
	}

	test {
		list { empty }
		do_ { append { item { number(1) } } }
		do_ { append { item { number(2) } } }
		do_ {
			map {
				function {
					item {
						get { item { number } }
						do_ { plus { number(10) } }
					}
				}
			}
		}
		equals_ {
			list { empty }
			do_ { append { item { number(11) } } }
			do_ { append { item { number(12) } } }
		}
	}
}