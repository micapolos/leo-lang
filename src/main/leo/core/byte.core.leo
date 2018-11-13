define(
	it(a(byte))
	is(
		bit(a(bit))
		bit(a(bit))
		bit(a(bit))
		bit(a(bit))
		bit(a(bit))
		bit(a(bit))
		bit(a(bit))
		bit(a(bit))))

define(
	it(negate(byte(a(byte))))
	is(byte(
	  bit(negate(bit(last(bit(byte(negate(this))))))))))
