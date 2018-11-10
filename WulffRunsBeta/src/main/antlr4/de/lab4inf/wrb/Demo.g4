grammar Demo;

WHITESPACE: [ \t\n\r]+->skip;	

E: 'e';
VARIABLE: LETTER+ NUMBER* VARIABLE*;	
NUMBER: ([0-9]+ (DOT [0-9]+)?) | (DOT [0-9]+);
LETTER: ([a-z] | [A-Z])+;
DOT: [.];
ADD: '+';
SUB: '-';
MUL: '*';
DIV: '/';
POW: '^' | '**';
LBRACKET: '(';
RBRACKET: ')';
ASSIGN: '=';
MOD: 'mod' | '%';
TERMINATOR: ';';
SEPERATOR: ',';

prog: root;

root: statement (TERMINATOR statement)* TERMINATOR ?;

statement: (assignment | expression | functionDefinition);

assignment : VARIABLE ASSIGN expression;

expressionList: expression (SEPERATOR expression)*;
varList: VARIABLE (SEPERATOR VARIABLE)*;
functionDefinition: VARIABLE LBRACKET varList RBRACKET ASSIGN expression;
functionCall: VARIABLE LBRACKET expressionList RBRACKET;

expression: SUB? LBRACKET expression RBRACKET
		  | expression E expression
		  |<assoc=right> expression (POW) expression 
		  | expression (DIV | MUL) expression
		  |<assoc=right> expression (MOD) expression
		  | expression (SUB | ADD) expression
		  | functionCall
		  | (SUB | ADD)? NUMBER
		  | (SUB | ADD)? VARIABLE
		  ;