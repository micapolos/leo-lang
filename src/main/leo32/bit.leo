bit
has
	either zero
	either one

negate bit zero
gives bit one

negate bit one
gives bit zero

bit zero
and bit zero
gives bit zero

bit zero
and bit one
gives bit zero

bit one
and bit zero
gives bit zero

bit one
and bit one
gives bit one

bit
or bit
gives negate
	negate bit self
	and negate bit or self

test
	bit zero
	or bit zero
	gives bit zero

test
	bit zero
	or bit one
	gives bit one

test
	bit one
	or bit zero
	gives bit one

test
	bit one
	or bit one
	gives bit one

bit zero
xor bit zero
gives bit zero

bit zero
xor bit one
gives bit one

bit one
xor bit zero
gives bit one

bit one
xor bit one
gives bit zero
