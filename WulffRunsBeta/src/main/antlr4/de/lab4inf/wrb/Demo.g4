grammar Demo;

@DemoParser::members {
  @Override
  public void reportError(RecognitionException e) {
    throw new IllegalArgumentException("I quit!\n" + e.getMessage()); 
  }
  @Override
  public void notifyErrorListeners(Token offendingToken, String msg, RecognitionException ex)
  {
    throw new IllegalArgumentException(msg); 
  }
}

@DemoLexer::members {
  @Override
  public void reportError(RecognitionException e) {
    throw new IllegalArgumentException("I quit!\n" + e.getMessage()); 
  }
  @Override
  public void recover(RecognitionException ex) 
  {
    throw new IllegalArgumentException(ex.getMessage()); 
  }
}

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

root: statement (TERMINATOR statement)* TERMINATOR ? EOF;

statement: (assignment | expression | functionDefinition);

assignment : VARIABLE ASSIGN expression;

functionDefinition: name=VARIABLE LBRACKET (VARIABLE (SEPERATOR VARIABLE)*) RBRACKET ASSIGN expression;	
functionCall: name=VARIABLE LBRACKET (expression (SEPERATOR expression)*) RBRACKET;					

expression: sign = SUB? LBRACKET expression RBRACKET						#Bracket
		  | links = expression E rechts = expression						#Tiny
		  |<assoc=right> links =  expression POW rechts = expression 		#Power
		  | links = expression DIV rechts = expression						#Division
		  | links = expression MUL rechts = expression						#Multiplikation
		  |<assoc=right> links = expression MOD rechts = expression			#Modulo
		  | links = expression SUB rechts = expression						#Subtraktion
		  | links = expression ADD rechts = expression						#Addition
		  | functionCall													#FunctionCaller
		  | sign = (SUB | ADD)? NUMBER										#Number
		  | sign = (SUB | ADD)? VARIABLE									#Variable
		  ;