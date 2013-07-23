tswrdb
======

tswrdb is a project that aims to document and export the contents of rdb data files (.rdbdata) from The Secret World, a MMORPG by Funcom. This project is for *fun*, *curiosity* and for the community to create useful tools and fansites that benefit other TSW fans.

tswrdb is written in Scala.

The project consists of two components:

* `tswrdb-api`: the Scala API for exporting rdb data.
* `tswrdb-cmdui`: the Scala program that enables *users* to export rdb data. It uses `tswrdb-api` client code.

Note: all active development takes place in the `develop` branch. Stable releases are merged into `master`.

Setup
==========

Requirements
* sbt 0.12.4+
* Scala 2.10.0+

tswrdb uses [sbt to build](http://www.scala-sbt.org/release/docs/Getting-Started/Setup.html).

Unless wanted for development, there should be no need to fetch Scala aswell. During build, sbt will fetch it for you to run with tswrdb.

Building
========

Fetch the source code:

    git clone git@github.com:joakibj/tswrdb.git

In the tswrdb project directory, run sbt by typing:

    sbt

It will fetch sbt itself, in addition to scala 2.10.0 and all dependencies.

To compile:

    compile

To run tests:

    test

Usage
=====

**All use of tswrdb is at your own risk!**

Currently there is no elegant packaging for tswrdb. It uses the sbt to manage the lifecycle, including running the application with no additional hassle.

To use tswrdb you need the following:

1. A shell (cmd.exe or any *nix shell)
2. Java 6 runtime
3. A legal copy of The Secret World by Funcom
    * A set of .rdbdata files with the associated le.idx file. (TestLive is good for this)

When in the tswrdb project directory:

    sbt

Change to the tswrdb program:

    project tswrdb-cmdui

Run the program (prints usage):

    run

Example of usage:

    run export --rdb """D:\Programs\TSW TestLive\RDB""" 1010042

The last command exports all Loading Screen Images to ``<tswrdb project folder>/exported/1010042``.

See the usage below for all available commands.

```
tswrdb 0.1
Usage: tswrdb [list|export|index] [options] <args>...

  --help
        prints this usage text.
  --version

Data is exported to the export folder.
Command: list [options]
Lists the valid rdb types available. Per default and to keep the user sane, only well understood RdbTypes are listed.
  -a | --all
        List all rdbtypes, regardless. Note that some are highly mysterious and/or esoteric. You will have to make sense
 of them yourself
Command: export [options] <rdbType>
Export entries belonging to this rdbtype
  -r <directory> | --rdb <directory>
        rdb points to the directory that has RDB files and is required.
  <rdbType>
        rdbType of the data that is going to be exported.
Command: index [info]

Command: index info [options]
Show information about index file: version, hash, entries
  -r <directory> | --rdb <directory>
        rdb points to the directory that has RDB files and is required.
Exiting...
```

Documentation
=============
TODO


License
=======

Licensed under GPLv2. Please refer to the LICENSE file for the license text in verbatim.
