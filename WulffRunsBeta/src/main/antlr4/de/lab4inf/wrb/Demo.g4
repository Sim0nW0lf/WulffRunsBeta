grammar Demo;


WHITESPACE: [ \t\n\r]+->skip;		
NUMBER: '-'? [0-9]+ (DOT NUMBER)?;
LETTER: ('a-z' | 'A-z')+;
DOT: [.];
VARIABLE: LETTER+ NUMBER*;

prog: root;

root: expression (';' expression)* ';'?;

assignment : VARIABLE '=' expression;

function: LETTER '(' VARIABLE ')' '=' expression;

expression: '(' expression ')'
		  | expression '/' expression
		  | expression '*' expression
		  | expression '+' expression
		  | expression '-' expression
		  |	NUMBER
		  | VARIABLE
		  ;