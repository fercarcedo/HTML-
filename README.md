HTMLPP
======

Simple HTML preprocessor built as a hobby, which at this time allows you to easily include html from other file and declare and use variables through an HTML-like syntax.

It is woth noting it's only a hobby project, so at this moment it doesn't use design patterns or do a sophisticated error validation, it's only a first prototype, but it works and you can safely use it to build your webs.


Generating result files
=======================

In order to use it, open a terminal and type
    java -jar HTMLPP.jar file1 file2 file3 ...

The program will then process the specified files and generate a .html standalone result file for each one.

Usage
=====

You can reference html code from other file with the following:
    <include src="file.html" />

And that's it, file.html contents will be pasted right where the tag is placed

Also, you can define variables using the following syntax:
    <variable name="var">
	Content
    </variable>

Later in your HTML file, reference that variable
    <li>${var}</li>


