package leo20.dsl

import leo15.dsl.*

val switch_ = dsl_ {
	test {
		shape { circle { radius { number(0) } } }
		switch {
			circle { get { radius } }
			square { get { side } }
		}
		equals_ { radius { number(0) } }
	}

	test {
		shape { square { side { number(0) } } }
		switch {
			circle { get { radius } }
			square { get { side } }
		}
		equals_ { side { number(0) } }
	}

	test {
		triangle { height { side { number(0) } } }
		switch {
			circle { get { radius } }
			square { get { side } }
		}
		equals_ {
			quote {
				triangle { height { side { number(0) } } }
				switch {
					circle { get { radius } }
					square { get { side } }
				}
			}
		}
	}
}