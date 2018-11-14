define(
	it(any(bit))
	is(
		either(zero)
		either(false)))

define(
	it(negate(bit(zero)))
	is(bit(one)))

define(
  it(negate(bit(one)))
  is(bit(zero)))

define(
	it(any(bool))
	is(bit(any(bit))))

define(
  it(negate(bool(any(bool))))
  is(bool(negate(bool(negate(this))))))
