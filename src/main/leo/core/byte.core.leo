define(
	it(any(byte))
	is(
		bit(any(bit))
		bit(any(bit))
		bit(any(bit))
		bit(any(bit))
		bit(any(bit))
		bit(any(bit))
		bit(any(bit))
		bit(any(bit))))

define(
	it(negate(byte(any(byte))))
	is(byte(
	  bit(negate(bit(last(bit(byte(negate(this))))))))))
