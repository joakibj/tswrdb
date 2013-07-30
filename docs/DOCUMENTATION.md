Documentation
=============

# Overview

1. [Preface](#preface)
2. [Introduction to RDB data](#rdbdata)
3. [RDB Index file (le.idx)](#rdbindex)

##<a id="preface"></a>Preface

All credit of this documentation goes to Jacob Seidelin. His contribution to the TSW community made this much easier to do.
This documentation consists most of his notes, in addition to some corrections/language done by Joakim Bjørnstad.

###<a id="rdbdata"></a> Introduction to RDB data

The RDB data files can be found in the <TSW game>/RDB folder. It typically looks like this:

```
<TSW root>/RDB/
|
+---------- 01.rdbdata
+---------- 02.rdbdata
+---------- 03.rdbdata
+---------- ...
+---------- le.idx
+---------- RDBHashIndex.bin
```

The ``.rdbdata`` files contain all game assets such as art, sound, textures and item data.
The ``le.idx`` file is the index file.
The ``RDBHashIndex`` is presumed to contain the valid files for the current patch version.

The ``.rdbdata`` files are organized like a large disk image, with ``le.idx`` as the index file.
It stores index entries, with a reference to a data entry, grouped by RdbType.
Each index entry in ``ie.idx`` has a corresponding data entry in one of the ``.rdbdata`` files.
The index entries belonging to an RdbType can be spread out on any of the ``.rdbdata`` files, however.

###<a id="rdbindex"></a> RDB Index file (le.idx)

**File Structure (le.idx)**

| File part     | Length        |
|---------------|---------------|
| Header        |24             |
| Index         |8 * NumEntries |
| Entry Details |28 * NumEntries|
| Bundles       |               |
    
**Header**

| Offset | Length |    Contents                        |
|--------|--------|------------------------------------|
| 0      |4       | Magic (0x49 0x42 0x44 0x52 = IBDR) |
| 4      |4       | Version (0x07)                     |
| 8      |16      | MD5 Hash of index data             |
| 24     |4       | Number of index entries            |
    
**Index**

| Offset | Length | Contents |
|--------|--------|----------|
| 0      |4       | RDB Type |
| 4      |4       | RDB Id   |
    
**Entry Details**

| Offset | Length |    Contents            | 
|--------|--------|------------------------|
| 0      | 1      | RDB file number        |
| 1      | 1      | Flags?                 |
| 2      | 2      | ???                    |
| 4      | 4      | Offset in rdbdata file |
| 8      | 4      | Length of entry data   |
| 12     | 16     | Hash (MD5)             |
    
**Bundles**

| Offset | Length | Contents          |
|--------|--------|-------------------|
| 0      | 4      | Number of bundles |
| 4      | -      | Bundle data       |
    
**Bundle data**

| Offset         | Length         | Contents       |
|----------------|----------------|----------------|
| 0              | 4              | NameLength     |
| 4              | NameLength     | Name           |
| 4 + NameLength | 4              | NumEntries     |
| 8 + NameLength | 8 * NumEntries | Bundle Entries |
    
**Bundle Entry**

| Offset | Length | Contents |
|--------|--------|----------|
| 0      | 4      | RDB Type |
| 4      | 4      | RDB Id   |

**Next sections are WIP**

###<a id="rdbdata"></a> RDB Data files (NN.rdbdata)

The format of the data files is simpler. A small 4-byte header with a file signature followed by data and more data. As mentioned earlier, you need to know the start offset of the content file you're after (which you find in the index). Each file entry has a 16 header before the actual data. This header contains some of the same information as what you find in the index, i.e. RDB type, file id, data length.

**File Structure (NN.rdbdata)**

| File part | Length |
|-----------|--------|
| Header    | 4      |
| Data      | ---    |
 
**Header**

| Offset | Length | Contents                           |
|--------|--------|------------------------------------|
| 0      | 4      | Magic (0x52 0x44 0x42 0x30 = RDB0) |
 
**Data Entry**

| Offset | Length     | Contents    |
|--------|------------|-------------|
| 0      | 4          | Data type   |
| 4      | 4          | RDB Id      |
| 8      | 4          | Data length |
| 12     | 4          | ???         |
| 16     | DataLength | File data   |

###<a id="rdbhashindex"></a> Hash index (RDBHashIndex.bin)

The hash index isn't that interesting unless you need to grab individual files on the server that aren't in the index.

Table TODO

###<a id="filenames"></a> Filenames

The RDB data files doesn't store the original names of the content files so they're only identifiable by their type and id. 

However, RDB type 1000010 contains a single file which is a table of filenames for, among other things, the 3D model files and the textures. The format of this file is really simple, just a list of file id / filename pairs, grouped by RDB type.

Table TODO

###<a id="xmldata"></a> XML data

Several of the data types are stored as XML data. Most of the XML types have a corresponding BXML type which is a binary representation of the XML data. You'll also find such ascii/binary XML pairs in "/data/gui/" in the TSW folder. It appears that the game automatically generates the BXML files from the ASCII versions (in the /data/ folder, that is.)

###<a id="textdata"></a> Text data

RDB type 1030002 contains text data collections. Among other things, this is where you'll find all the dialogue text, mission reports, lore, etc. in all the supported languages.

Each file contains a string collection belonging to a certain category. For example, here are all the dialog and mission report strings (converted into a more readable format): http://www.mediafire.com/?hab0pmgcvfbqhqc

The language of the text collection isn't stored in the files, but in "/data/text/" you'll find a *.tdbl file for each language. These files are basically lists of which entries in 1030002 belong to each language.

Table TODO

###<a id="3dmodels"></a> 3D models

The 3D models are the most complex format. I don't recognize the data format and suspect that it is a custom format made by Funcom. There's too much to explain with words, hopefully the specs below are easy enough to understand for someone who knows what to do with the information.

Table TODO

###<a id="textures"></a> Textures

Some of the textures in, for example, 1010004 are plain PNG files but most are in a custom format, FCTX (Funcom texture?). These appear to be DDS files with a custom header so with a bit of hackery, they can be turned into proper DDS files by removing the FCTX header and adding a DDS header.
