#!/usr/bin/perl -wT
print "Content-Length: 19\r\n\r\n";
print "Content-Type: text/plain\r\n\r\n";
print "Hello World!\r\n";
print "You entered: \r\n";
print while <STDIN>;