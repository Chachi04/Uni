@ Game_Template.s 
@ Adam Watkins, Richard Verhoeven 
@ December 2021

@ Adapted by:
@ <Student Name 1> <Student Number 1>
@ <Student Name 2> <Student Number 2>

.global main
.text

@ Game control loop (between main: and _exit:)
@ Register usage:
@ R8: generated random number
@ R9: guesses remaining
main:
        BL    gen_number

        MOV   R8, R0            @ Store 'hidden' number in R8
        MOV   R9, #3            @ Initialise remaining guesses to 3
        LDR   R1, =new_game     @ Load new game string
        MOV   R2, #new_game_len @ Load new game string length
        BL    print             @ Print the new game string
next_guess:
        LDR   R1, =prompt       @ Load prompt string address
        MOV   R2, #prompt_len   @ Load prompt length
        BL    print             @ Print the prompt

        LDR R0, =input	        @ TASK: Load input buffer address
	MOV R1, #3		@ TASK: Load input buffer length
        BL    read		@ Read 3 chars to input buffer (including newline)

        BL    asctonum          @ Convert string to integer.
        MOV   R1, R8            @ Copy hidden number
        MOV   R10, R0           @ Backup guessed number
        BL    print_hint        @ Print a hint

        CMP   R10, R8           @ If the guess was correct,
        BEQ   extra_game        @ go to ask for an extra game.
        SUBS  R9, #1            @ Reduce the remaining guesses (!)
        BGT   next_guess        @ Try next guess if available
        MOV   R0, R8            @ Pass 'hidden' number as argument.
        BL    print_lose        @ No guess remaining, you lose.
extra_game:
        BL    play_again
        CMP   R0, #0
        BNE   main

_exit:
        MOV   R7, #1			@ exit syscall
        SWI 0

@ Functions

play_again:
        STMFD SP!, {R1-R2, R10, LR}
        LDR R1, =again_st
        MOV R2, #again_st_len
        BL print
        LDR R0, =input	        @ TASK: Load input buffer address
	MOV R1, #3		@ TASK: Load input buffer length
        BL read
        LDRB R10, [R0]
        SUB R10, #0x6E
        MOV R0, R10 
        LDMFD SP!, {R1-R2, R10, LR}
        MOV PC, LR


@@@@ print: Print a string to the terminal
@ Parameters:
@   R1: address of string
@   R2: length of string
@ Returns:
@   none
print:                     
        STMFD   SP!, {R7,LR}    	@ Push used registers and LR on the stack;
        MOV R7, #4    				@ TASK: Put the Syscall number in R
        MOV R0, #1            		@ TASK: Put the monitor STDOUT in R
        SWI 0                 	    @ TASK: Uncomment this line to make the syscall
        LDMFD   SP!, {R7,LR}     @ Restore used registers (update SP with !)
        MOV     PC, LR          	@ Return

@@@@ read: read a string from keyboard and store in variable
@ Parameters:
@   R0: address of where to store string
@   R1: number of characters to store
@ Returns:
@   none
read:
        STMFD SP!, {R7, LR}     	@ Push used registers and LR to stack
        MOV R2, R1                  @ TASK: Move number of characters(R1) to R2
        MOV R1, R0                  @ Task: Move address of input string(R0) to R1
        MOV R7, #3                  @ TASK: Put the Syscall number in R?
        MOV R0, #0                  @ TASK: Put the keyboard STDIN in R?
        SWI 0						@ TASK: Uncomment this line to make the syscall
        LDMFD SP!, {R7, LR}     	@ Restore used registers (update SP with !)
        MOV  PC, LR

@@@@ asctonum: convert the ASCII hex characters in input to a number
@ Parameters: 
@   R1: address of ASCII representation
@ Returns: 
@   R0: calculated value
asctonum:  
        STMFD   SP!, {R4-R5, LR}    @ TASK: Explain why this push occurs
        MOV     R4, #0              @ character count: find out where the newline is
        MOV     R5, #0              @ number entered in hex
nextchar:
        LDRB    R0, [R1,R4]         @ load byte from address R1 + R4
        CMP     R0, #0xA            @ TASK: Explain the purpose of this line of code
        BEQ     readall             @ done reading
        BL      atoi                @ convert to hex
        CMP     R4, #1              @ is this the first character read?
        BLT     first
                                    @ shift R5 4 bits to the left
        MOV     R5, R5, LSL #4      @ (most significant digit)
                                    @ TASK: Explain why (in the above) we perfom a shift
first:    
        ADD     R5, R0              @ add R0
        ADD     R4, #1              @ increment counter
        BAL     nextchar
readall:
        MOV     R0, R5
        LDMFD   SP!, {R4-R5, LR}
        MOV     PC, LR

@@@@ numtoasc: Convert the number to a hexadecimal ASCII string
@ Parameters: 
@   R0: value to convert
@   R1: address of string
@ Returns:
@   none
numtoasc: 
        STMFD   SP!, {R4, LR}   	@ TASK: Explain why this push occurs
        MOV     R4, R0          	@ copy number
        AND     R0, #0xF0       	@ mask off ms-nibble
        MOV     R0, R0, LSR #4  	@ shift to right
        BL      itoa            	@ convert to ASCII
        STRB    R0, [R1]        	@ store byte at R1
        MOV     R0, R4          	@ reload R0
        AND     R0, #0xF        	@ mask off ls-nibble
        BL      itoa            	@ convert to ASCII
        STRB    R0, [R1, #1]    	@ store 2nd character
        MOV     R0, #0xA        	@ newline
        STRB    R0, [R1, #2]    	@ store at end of string
        LDMFD   SP!, {R4, LR}
        MOV     PC, LR

@@@@ atoi: 		Convert ASCII hex character to its integer value
@ Parameters: 
@   R0: ASCII character (assumed '0'-'9', 'A'-'F' or 'a'-'f')
@ Returns:
@   R0: Integer value of provided character
atoi:
        CMP     R0, #0x40       	@ Compare with the character smaller than 'A/a'
        SUBLT   R0, #0x30       	@ If in range 0-9, substract '0'
        ORRGT   R0, #0x60       	@ If in range A-F or a-f, force lower case ...
        SUBGT   R0, #0x57       	@    and substract 'a'-10
        MOV     PC, LR
@ IF R0 < 'A':
@     Substract '0' from R0                     
@ ElSE IF R= > 'A':
@     Force to lower case by logical or, 
@     works since the only difference between lower 
@     and uppercase is the 6th bit being 1, so we or with #0x60
@     then substract ('a'-10) = #0x57 to get a number between 10 and 15
@ 
@ Example 1:
@ '1' (#0x31) => less than 'A' => substract '0'(#0x30) => return 1
@ Example 2:
@ 'B' (#0x42) => greater than 'A' => ORR with #0x60 (set 6th bit to 1) => 'b'(#0x62) => substract ('a'(#0x61) - 10) = #0x57 => return 11

@@@@ itoa: 		Convert integer value to ASCII hex character
@ Parameters: 
@   R0: integer value in range 0-15
@ Returns:   
@   R0: related ASCII character ('0'-'9', 'A'-'F')
itoa:
        CMP     R0, #9				@ Compare to 9
        ADDLE   R0, #0x30           @ TASK: If <= (i.e. 0 to 9), add '0'
        ADDGT   R0, #0x37           @ TASK: If > (i.e. 10-15), add 'A'-10
        MOV     PC, LR
@ Example 1:
@ 12 => greater than 9 => add ('A'(#0x41) - 10) = #0x37 => return 'C'(#0x43)
@ '9' => less or equal than 9 => add '0'(#0x30) => return '9'(#0x39)

@@@@ gen_number: Generate a number based on the current time
@ Parameters :
@   none
@ Returns:  
@   R0: 7-bit 'random' value
gen_number:
        STMFD   SP!, {R1,R7,LR}
        @ MOV     R0, #30             @ TASK: This function will be written later
        LDR R0, =time
        MOV R1, #0
        MOV R7, #0x4E
        SWI 0
        LDR R1, =musecs
        LDR R0, [R1]
        AND R0, #0x7F
        LDMFD   SP!, {R1,R7,LR}
        MOV     PC, LR

@@@@ print_hint:	Indicate whether the number is higher, lower or correct.
@ Parameters: 
@   R0: guessed value
@   R1: 'hidden' random value
@ Returns:
@   none
print_hint:
        STMFD   SP!, {R1-R2,LR}
        CMP     R1, R0              @ Compare hidden and guessed value
        LDREQ   R1, =congrats       @ If equal, select congrats ...
        MOVEQ   R2, #congrats_len   @   ... and its length
        LDRLT   R1, =lower          @ If less than, select lower
        MOVLT   R2, #lower_len
        LDRGT   R1, =higher         @ If greater than, select higher
        MOVGT   R2, #higher_len
        BL      print               @ Print that was selected.
        LDMFD   SP!, {R1-R2,LR}
        MOV     PC, LR

@@@@ print_lose: Reveal the hidden number
@ Parameters: 
@   R0: 'hidden' random value
@ Returns:
@   none
print_lose:
        STMFD   SP!, {R1-R2, LR}
        LDR     R1, =lostgame       @ Load 'lost-game' string buffer reference
        ADD     R1, #value_offset   @ Adjust to the position of the number
        BL      numtoasc            @ Write hidden value to buffer
        LDR     R1, =lostgame       @ Restore buffer reference
        MOV     R2, #lostgame_len   @ ... and its length
        BL      print               @ Print string with the number
        LDMFD   SP!, {R1-R2, LR}
        MOV     PC, LR

@@@@@ Constants 
.data

prompt:           .asciz  "Guess a number between 0 and 0x7F:"
.equ              prompt_len, 34
higher:           .asciz  "Higher\n"
.equ              higher_len, 7
lower:            .asciz  "Lower\n"
.equ              lower_len, 6
congrats:         .asciz  "Congrats, you guessed it\n"
.equ              congrats_len, 25
new_game:         .asciz  "You have 3 attempts to guess\n"
.equ              new_game_len, 29
lostgame:         .asciz  "You lose, the number was 00\n"
.equ              lostgame_len, 28
.equ              value_offset, 25  @ index into the lostgame 
                                    @ string so the number
                                    @ can be written into it.
again_st:       .asciz "Play again?: "
.equ            again_st_len, 13

@@@@ Variables
input:            .space 3          @ TASK: Create user guess variable here
time:             .space 4      	@ Time (s) since Jan 1 1970
musecs:           .space 4      	@ Time (ms)
