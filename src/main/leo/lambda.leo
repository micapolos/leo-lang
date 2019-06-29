bit has one of
	zero
	one

bit
negate
is bit
	get input bit
	switch
		zero one
		one zero

byte has
	bit
	bit
	bit
	bit
	bit
	bit
	bit
	bit

vec2
	x thing
	y get previous x

define
	vec2 of anything
	gives vec2
		x get given vec2 of it
		y get given vec2 of it

define
	vec2 of anything
	plus quote get previous it
	gives vec2
		x
			this
			get vec2 x
			plus
			  this
			  get plus vec2 x
		y
			this
			get vec2 x
      plus
        this
        get plus vec2 x

optional of anything
is optional one of
	present get given optional of it
	absent meta quote get given optional of it

my bit
is optional present bit zero

my bit
get optional
switch
	present: optional present: case, negate
	absent: optional absent: case

