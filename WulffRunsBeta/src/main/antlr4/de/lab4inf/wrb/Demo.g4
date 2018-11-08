grammar Demo;


WHITESPACE: [ \t\n\r]+->skip;	
VARIABLE: LETTER+ NUMBER*;	
NUMBER: [0-9]+ (DOT NUMBER*)?;
LETTER: ('a-z' | 'A-z')+;
DOT: [.];


prog: root;

root: statement (';' statement)* ';'?;

statement: (assignment | expression | functionDefinition);

assignment : VARIABLE '=' expression;

varList: VARIABLE (',' VARIABLE);
functionDefinition: LETTER '(' varList ')' '=' expression;
functionCall: LETTER '(' varList ')' '=' expression;

expression: '(' expression ')'
		  | expression 'e' expression
		  | expression ('^'<assoc=right> | '**'<assoc=right>) expression
		  | expression '/' expression
		  | expression '*' expression
		  | expression ('%'<assoc=right> | 'mod'<assoc=right>) expression
		  | expression '+' expression
		  | expression '-' expression
		  | functionCall
		  | ('-' | '+')?NUMBER
		  | VARIABLE
		  ;