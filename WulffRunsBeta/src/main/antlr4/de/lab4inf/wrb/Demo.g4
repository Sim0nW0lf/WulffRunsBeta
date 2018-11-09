grammar Demo;


WHITESPACE: [ \t\n\r]+->skip;	
VARIABLE: LETTER+ NUMBER* VARIABLE*;	
NUMBER: ([0-9]+ (DOT [0-9]+)?) | (DOT [0-9]+);
LETTER: ([a-z] | [A-Z])+;
DOT: [.];


prog: root;

root: statement (';' statement)* ';'?;

statement: (assignment | expression | functionDefinition);

assignment : VARIABLE '=' expression;

expressionList: expression (',' expression)*;
varList: VARIABLE (',' VARIABLE)*;
functionDefinition: VARIABLE '(' varList ')' '=' expression;
functionCall: VARIABLE '(' expressionList ')';

expression: '-'? '(' expression ')'
		  | expression 'e' expression
		  | <assoc=right> expression ('**' | '^') expression
		  | expression ('*' | '/') expression
		  | <assoc=right> expression '%' expression
		  | <assoc=right> expression 'mod' expression
		  | expression ('+' | '-') expression
		  | ('sin' | 'cos' | 'tan') '(' expression ')'
		  | ('min' | 'max') '(' expressionList ')'
		  | functionCall
		  | ('-' | '+')? NUMBER
		  | ('-' | '+')? VARIABLE
		  ;