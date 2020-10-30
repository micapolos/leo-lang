package leo20.dsl

import leo15.dsl.*

val switch_ = dsl_ {
	test {
		x { number(10) }
		switch {
			number { to { plus { number(1) } } }
			text { to { append { text("+1") } } }
		}
		equals_ { number(11) }
	}

	test {
		x { text("foo") }
		switch {
			number { to { plus { number(1) } } }
			text { to { append { text("+1") } } }
		}
		equals_ { text("foo+1") }
	}

	test {
		shape { circle { radius { number(0) } } }
		switch {
			circle { to { get { radius } } }
			square { to { get { side } } }
		}
		equals_ { radius { number(0) } }
	}

	test {
		shape { square { side { number(0) } } }
		switch {
			circle { to { get { radius } } }
			square { to { get { side } } }
		}
		equals_ { side { number(0) } }
	}

	test {
		triangle { height { side { number(0) } } }
		switch {
			circle { to { get { radius } } }
			square { to { get { side } } }
		}
		equals_ {
			quote {
				triangle { height { side { number(0) } } }
				switch {
					circle { to { get { radius } } }
					square { to { get { side } } }
				}
			}
		}
	}
}