package leo20.dsl

import leo15.dsl.*

val switch_ = dsl_ {
	test {
		x { number(10) }
		switch {
			number {
				does {
					get { number }
					plus { number(1) }
				}
			}
			text {
				does {
					get { text }
					append { text("+1") }
				}
			}
		}
		equals_ { number(11) }
	}

	test {
		x { text("foo") }
		switch {
			number {
				does {
					get { number }
					plus { number(1) }
				}
			}
			text {
				does {
					get { text }
					append { text("+1") }
				}
			}
		}
		equals_ { text("foo+1") }
	}

	test {
		shape { triangle }
		switch {
			circle { does { nothing_ } }
			square { does { nothing_ } }
		}
		equals_ {
			quote {
				shape { triangle }
				switch {
					circle { does { nothing_ } }
					square { does { nothing_ } }
				}
			}
		}
	}
}