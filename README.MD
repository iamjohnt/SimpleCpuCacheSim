# Simplified CPU Cache Simulator

A project done for a computer architecture course, written in Java. This project simulates a simplified CPU cache. You can feed the program a sequence of reads or writes, and it can display the state of cache at any point. 

# Installation

Clone this repo, and run in any Java IDE. 

# Instructions

* Run the main method in Main.java. 
* Currently for demonstration purposes, the program consumes a default sequence of commands, data, and memory locations in the text file "input.txt". Everything must be separated by a new line. 
    * R = read. Immediately after R is the memory location you want to read. 
    * W = write. Immediately after W are two values. First is memory address you want to write to. Second is the value. 
    * D = print cache state to the console
    * see "input.txt" for examples
* If you'd like to create your own sequence of inputs, simply overwrite "input.txt"

# Known issues

Currently missing functionality for writing dirty slots back to memory upon a cache miss. Will be added in potential future update. 