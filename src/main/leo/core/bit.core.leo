define(
	it(a(bit))
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
	it(a(bool))
	is(bit(a(bit))))

define(
  it(negate(bool(a(bool))))
  is(bool(negate(bool(negate(this))))))
