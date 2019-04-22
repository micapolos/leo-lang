define
	bit
	has
		either zero
		either one

comment negate

define
	negate bit zero
	gives bit one

define
	negate bit one
	gives bit zero

comment and

define
	bit zero
	and bit zero
	gives bit zero

define
	bit zero
	and bit one
	gives bit zero

define
	bit one
	and bit zero
	gives bit zero

define
	bit one
	and bit one
	gives bit one

comment or

define
	bit zero
	or bit zero
	gives bit zero

define
	bit zero
	or bit one
	gives bit one

define
	bit one
	or bit zero
	gives bit one

define
	bit one
	or bit one
	gives bit one

comment xor

define
	bit zero
	xor bit zero
	gives bit zero

define
	bit zero
	xor bit one
	gives bit one

define
	bit one
	xor bit zero
	gives bit one

define
	bit one
	xor bit one
	gives bit zero
