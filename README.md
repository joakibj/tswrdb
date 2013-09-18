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

Fetch the source code (if you have added a [SSH key to github](https://help.github.com/articles/generating-ssh-keys)):

    git clone git@github.com:joakibj/tswrdb.git

Alternatively:

    git clone https://github.com/joakibj/tswrdb.git

In the tswrdb root directory (e.g. ``~/dev/tswrdb``), run sbt by typing:

    sbt

It will fetch sbt itself, in addition to scala 2.10.0 and all dependencies.

When in the sbt command shell, to compile:

    compile

And to run tests:

    test

Usage
=====

**All use of tswrdb is at your own risk!**

Currently there is no elegant packaging for tswrdb. It uses the sbt to manage the lifecycle, including running the application with no additional hassle.

To use tswrdb you need the following:

1. A shell (cmd.exe or any *nix shell)
2. Java 6 runtime
3. sbt
4. A legal copy of The Secret World by Funcom
    * An installation of TSW ([TestLive](http://forums.thesecretworld.com/showthread.php?t=55882) is recommended)

When in the tswrdb root directory (e.g. ``~/dev/tswrdb``):

    sbt

*The following commands are all done in the sbt command shell.*

Change to the tswrdb program:

    project tswrdb-cmdui

Run the program (prints usage):

    run

A few examples of usage:

    run export rdbtype 1010042 --tsw """D:\Programs\TSW TestLive"""

The previous command exports all Loading Screen Images to ``<tswrdb folder>/tswrdb-cmdui/exported/1010042 (Loading Screen Images)``.

    run export strings --lang de --tsw """D:\Programs\TSW TestLive"""

The previous command exports all german strings to XML files in ``<tswrdb folder>/tswrdb-cmdui/exported/1030002 (Strings)``.

    run export strings --lang en --json --tsw """D:\Programs\TSW TestLive"""

The previous command exports all english strings to JSON files in ``<tswrdb folder>/tswrdb-cmdui/exported/1030002 (Strings)``.

See the usage below for all available commands.

```
tswrdb 0.1
Usage: tswrdb [list|export|index] [options]

  --tsw <directory>
        tsw points to the TSW install directory and is required.

Command: list [options]
Lists the valid rdb types available. Per default and to keep the user sane, only well understood RdbTypes are listed.
  -a | --all
        List all rdbtypes, regardless. Note that some are highly mysterious and/or esoteric. You will have to make sense of them yourself

Command: export [rdbtype|strings] <args>...

Command: export rdbtype <rdbType>
Export any RdbType as they appear in the resource database.
  <rdbType>
        rdbType of the data that is going to be exported.
Command: export strings [options]
Export strings (RdbType 1030002). XML is output per default, this can be overriden with Option --json.
  -l <value> | --lang <value>
        Exports all strings for the language. Valid options are en, fr or de. Required.
  --json
        Strings are exported as JSON.

Command: index [info]

Command: index info
Show information about index file: version, hash, number of entries

  --help
        prints this usage text.
  --version
        prints the version
```

Documentation
=============
**Note: Work in progress!**

Please see the [DOCUMENTATION](docs/DOCUMENTATION.md) file for documentation about the RDB data formats.

Known Issues
============

* Currently no known issues

Acknowledgements
================

Thanks to:

* Jacob Seidelin for compiling the RDB documentation and software for extracting TSW rdbdata.
* The [scopt](https://github.com/scopt/scopt) project for command line parsing.

Please see [ACKNOWLEDGEMENTS](docs/ACKNOWLEDGEMENTS.md) file for license and copyright notices in verbatim.

License
=======

The source code is licensed under GPLv2. Please refer to the [LICENSE](LICENSE) file for the license text in verbatim.
