/*
 * tswrdb - a program and API to export the TSW resource database
 *
 * Copyright (C) 2013 Joakim Bjørnstad <joakibj@gmail.com>
 *
 * Licensed under the GNU General Public License version 2.
 * Please see the LICENSE file for the license text in verbatim.
 */

package com.joakibj.tswrdb.rdb

case class FileType(val extension: String) {
  override def toString = extension
}

object RdbType {
  def apply(id: Int, name: String, skipBytes: Int, fileType: FileType) =
    new RdbType(id, name, skipBytes, fileType)
  def apply(id: Int, name: String, skipBytes: Int, fileType: FileType, understood: Boolean) =
    new RdbType(id, name, skipBytes, fileType, understood)
}

class RdbType(val id: Int,
              val name: String,
              val skipBytes: Int,
              val fileType: FileType,
              val understood: Boolean = false) {

  override def equals(other: Any) = other match { 
    case that: RdbType => this.id == that.id
    case _ => false 
  }

  override def toString =
    id + " (" + name + ")"
}

object RdbTypes {
  private val types = new RdbTypes

  val Strings = find(1030002).get
  val Filenames = find(1000010).get

  def values: List[RdbType] = types.data
  def find(id: Int): Option[RdbType] = types.data find (_.id == id)
  def exists(id: Int): Boolean = find(id) != None
}

class RdbTypes {
  final val data: List[RdbType] = List(
    RdbType(1000001, "Map Info - Old", 0, FileType("dat"), false), // Old Age of Conan mapdata

    RdbType(1000005, "PC64", 0, FileType("dat")),
    RdbType(1000006, "Unknown", 0, FileType("dat"), false), //Data does not exist
    RdbType(1000007, "XML - PhysX", 0, FileType("xml")),
    RdbType(1000010, "File Names", 12, FileType("dat")),
    RdbType(1000012, "Unknown", 0, FileType("dat"), false), //Data does not exist
    RdbType(1000013, "K-D Tree KDv2", 0, FileType("dat")),
    RdbType(1000015, "K-D Tree KDv2", 0, FileType("dat")),
    RdbType(1000016, "K-D Tree KDv3", 0, FileType("dat")),
    RdbType(1000020, "Items", 12, FileType("dat")),
    RdbType(1000028, "K-D Tree KDv2", 0, FileType("dat")),
    RdbType(1000042, "Unknown", 12, FileType("dat")),
    RdbType(1000043, "Feat Class Info", 12, FileType("dat")),
    RdbType(1000044, "FeatPoints", 12, FileType("dat")),
    RdbType(1000047, "Camera Paths", 12, FileType("dat")),
    RdbType(1000048, "Unknown", 0, FileType("dat")),
    RdbType(1000049, "Lighting", 0, FileType("dat")),
    RdbType(1000050, "XML - Territories", 12, FileType("xml")),
    RdbType(1000052, "Unknown", 0, FileType("dat")),
    RdbType(1000053, "XML - Bounded Area", 12, FileType("xml")),
    RdbType(1000054, "Unknown", 0, FileType("dat")),
    RdbType(1000055, "Bounded Area", 12, FileType("dat")),
    RdbType(1000057, "Unknown", 0, FileType("dat")),
    RdbType(1000058, "Unknown", 0, FileType("dat")),
    RdbType(1000059, "Environ Area", 0, FileType("dat")),
    RdbType(1000060, "Unknown", 0, FileType("dat")),
    RdbType(1000061, "Unknown", 0, FileType("dat")),
    RdbType(1000069, "Unknown", 0, FileType("dat")),
    RdbType(1000070, "XML - Climb", 12, FileType("xml")),
    RdbType(1000071, "Unknown", 0, FileType("dat")),
    RdbType(1000075, "XML - Rivers", 12, FileType("xml")),
    RdbType(1000076, "Unknown", 0, FileType("dat")),
    RdbType(1000080, "Unknown", 0, FileType("dat")),
    RdbType(1000081, "Unknown", 0, FileType("dat")),
    RdbType(1000082, "Treasure Data", 0, FileType("dat")),
    RdbType(1000083, "XML - Treasure Data", 12, FileType("xml")),
    RdbType(1000087, "XML - Teleport Data", 12, FileType("xml")),
    RdbType(1000090, "XML - Resurrect Points", 12, FileType("xml")),
    RdbType(1000094, "Unknown", 0, FileType("dat")),

    RdbType(1000095, "XML - Climb", 12, FileType("xml")),
    RdbType(1000096, "Textures FCTX", 0, FileType("dat")),
    RdbType(1000097, "XML - Ocean Volume", 12, FileType("xml")),
    RdbType(1000098, "XML - Volumetric Fog", 12, FileType("xml")),
    RdbType(1000099, "Feat Point Cost", 0, FileType("dat")),
    RdbType(1000102, "Unknown", 0, FileType("dat")),
    RdbType(1000103, "XML - Static AI Points", 0, FileType("xml")),
    RdbType(1000104, "Unknown", 0, FileType("dat")),
    RdbType(1000105, "Game Triggers", 0, FileType("dat")),
    RdbType(1000107, "Character Tag Types", 0, FileType("dat")),
    RdbType(1000108, "Lore Tree", 0, FileType("dat")),

    RdbType(1000504, "Sound", 12, FileType("dat")),
    RdbType(1000505, "Player Class Map", 0, FileType("dat")),
    RdbType(1000506, "Class Membership Info", 0, FileType("dat")),
    RdbType(1000507, "Unknown", 0, FileType("dat")),
    RdbType(1000508, "Monsters", 0, FileType("dat")),
    RdbType(1000509, "Unknown", 0, FileType("dat")),
    RdbType(1000510, "Unknown", 0, FileType("dat")),
    RdbType(1000511, "Unknown", 0, FileType("dat")),
    RdbType(1000512, "Unknown", 0, FileType("dat")),
    RdbType(1000513, "Unknown", 0, FileType("dat")),
    RdbType(1000514, "Unknown", 0, FileType("dat")),
    RdbType(1000515, "Unknown", 0, FileType("dat")),
    RdbType(1000516, "Unknown", 0, FileType("dat")),
    RdbType(1000517, "Guild Gov Data", 0, FileType("dat")),
    RdbType(1000518, "Guild Ranks", 0, FileType("dat")),
    RdbType(1000519, "XML - Decal", 0, FileType("xml")),
    RdbType(1000520, "Tokens", 12, FileType("dat")),
    RdbType(1000521, "Guild Renown Rewards", 0, FileType("dat")),
    RdbType(1000522, "PvP Performance Group", 0, FileType("dat")),
    RdbType(1000523, "PvP Playfield Scores", 0, FileType("dat")),
    RdbType(1000524, "Deck Ability Map", 0, FileType("dat")),

    RdbType(1000620, "Item Data", 0, FileType("dat")),
    RdbType(1000621, "Spells", 0, FileType("dat")),
    RdbType(1000622, "XML - Effect Packages", 0, FileType("xml")),
    RdbType(1000623, "Text Files", 12, FileType("txt")),
    RdbType(1000624, "Media - Flash", 0, FileType("swf")),
    RdbType(1000625, "Token State Data", 12, FileType("dat")),
    RdbType(1000626, "Chat Filter", 0, FileType("dat")),
    RdbType(1000635, "Media - Video", 0, FileType("dat")),
    RdbType(1000636, "Media - Images", 12, FileType("png"), true),
    RdbType(1000637, "Unknown", 0, FileType("dat")),
    RdbType(1000638, "Footsteps", 0, FileType("dat")),

    RdbType(1010001, "FME Mesh Data", 0, FileType("fme")),
    RdbType(1010002, "Skeleton Rigs", 0, FileType("cs")),
    RdbType(1010003, "Skeleton Rigs", 0, FileType("ca")),
    RdbType(1010004, "Textures", 0, FileType("dds")),
    RdbType(1010005, "Unknown", 0, FileType("dat")),

    RdbType(1010006, "FCTX Textures", 0, FileType("dat")),
    RdbType(1010007, "Morph", 0, FileType("morph")),
    RdbType(1010008, "Icons - PNG", 12, FileType("png"), true),

    RdbType(1010011, "Unknown", 0, FileType("dat")),
    RdbType(1010012, "Materials MTI", 0, FileType("dat")),
    RdbType(1010013, "Maps Merged", 0, FileType("jpg"), true),

    RdbType(1010028, "XML - Sound Scripts", 12, FileType("xml")),
    RdbType(1010029, "Unknown", 0, FileType("dat")),
    RdbType(1010030, "XML - Mesh Index", 12, FileType("xml")),
    RdbType(1010031, "Attractor", 0, FileType("attr")),
    RdbType(1010034, "Unknown", 0, FileType("dat")),
    RdbType(1010035, "XML - Texture Scripts", 12, FileType("xml")),
    RdbType(1010036, "Unknown", 12, FileType("enl")),
    RdbType(1010039, "Unknown", 0, FileType("dat")),
    RdbType(1010040, "Unknown", 0, FileType("dat")),
    RdbType(1010042, "Loading Screen Images", 0, FileType("jpg"), true),
    RdbType(1010043, "Unknown", 0, FileType("dat")),
    RdbType(1010060, "Unknown", 0, FileType("dat")),


    RdbType(1010200, "XML - AnimSys", 12, FileType("xml")),
    RdbType(1010201, "XML - Monsters", 12, FileType("xml")),
    RdbType(1010202, "XML - BCT Group", 12, FileType("xml")),
    RdbType(1010203, "XML - BCT Mesh", 12, FileType("xml")),
    RdbType(1010204, "Unknown", 0, FileType("dat")),
    RdbType(1010205, "XML - Movement Set", 12, FileType("xml")),

    RdbType(1010207, "Particle Effects", 12, FileType("particleeffect")),
    RdbType(1010210, "Bump Maps", 12, FileType("png")),
    RdbType(1010211, "Bump Maps", 12, FileType("png")),
    RdbType(1010213, "Del Portal Image - PNG", 12, FileType("png")),
    RdbType(1010216, "XML - Cars", 12, FileType("xml")),
    RdbType(1010217, "Flora Images - TGA", 0, FileType("dat")),
    RdbType(1010218, "Textures Flora", 0, FileType("dat")),
    RdbType(1010220, "Grass Data", 0, FileType("dat")),
    RdbType(1010221, "Grass Mesh", 0, FileType("dat")),
    RdbType(1010222, "Unknown", 0, FileType("dat")), //new
    RdbType(1010223, "Lua Scripts", 0, FileType("dat")),
    RdbType(1010226, "Looks Package", 0, FileType("lkz")),
    RdbType(1010230, "Compiled Lua", 0, FileType("dat")),

    RdbType(1010300, "XML - Room And Portal", 12, FileType("xml")),
    RdbType(1010301, "Flora FME Mesh Data", 0, FileType("fme")),
    RdbType(1010302, "Flora FME Mesh Data", 0, FileType("fme")),
    RdbType(1010303, "Grass", 0, FileType("dat")),
    RdbType(1010400, "XML - Formation", 12, FileType("xml")),

    RdbType(1010450, "Unknown", 0, FileType("dat")), //new

    RdbType(1010500, "Binary XML - BCT Group", 0, FileType("bxml")),
    RdbType(1010501, "Binary XML - BCT Mesh", 0, FileType("bxml")),
    RdbType(1010503, "Binary XML - Movement Set", 0, FileType("bxml")),
    RdbType(1010504, "Binary XML - Formation", 0, FileType("bxml")),
    RdbType(1010505, "Binary XML - Cars", 0, FileType("bxml")),
    RdbType(1010506, "Territories", 0, FileType("dat")),
    RdbType(1010510, "Binary XML - Territories", 0, FileType("bxml")),
    RdbType(1010511, "Binary XML - AnimSys", 0, FileType("bxml")),
    RdbType(1010512, "Binary XML - Static AI Points", 0, FileType("bxml")),
    RdbType(1010514, "Binary XML - Monster Data", 0, FileType("bxml")),
    RdbType(1010515, "Binary XML - Effect Package", 0, FileType("bxml")),
    RdbType(1010516, "Binary XML - Mesh Index", 0, FileType("bxml")),

    RdbType(1010700, "XML - Char Creation", 12, FileType("xml")),

    RdbType(1020002, "Sound Effects", 0, FileType("ogg"), true),
    RdbType(1020003, "Lip Sync Voices", 0, FileType("dat")),
    RdbType(1020004, "Patricia Data?", 0, FileType("dat")),
    RdbType(1020005, "Music", 0, FileType("ogg"), true),
    RdbType(1020006, "Sound Files - Tones", 12, FileType("wav")),
    RdbType(1020001, "Audio", 0, FileType("dat")),

    RdbType(1030001, "Language Index", 12, FileType("dat")),
    RdbType(1030002, "Strings", 12, FileType("dat")),

    RdbType(1040005, "Feats", 0, FileType("dat")),
    RdbType(1040007, "Unknown", 12, FileType("dat")),
    RdbType(1040008, "Unknown", 12, FileType("dat")),
    RdbType(1040009, "Unknown", 12, FileType("dat")),
    RdbType(1040010, "XML - Bounded Area", 12, FileType("xml")),

    RdbType(1050002, "Unknown", 0, FileType("dat")),

    RdbType(1060667, "XML - Env Settings", 12, FileType("xml")),
    RdbType(1060668, "Sky Dome", 0, FileType("dat")),

    RdbType(1065101, "FeatSpecializedTrees", 0, FileType("dat")),
    RdbType(1065102, "Unknown", 0, FileType("dat")),
    RdbType(1065103, "PvPConRating", 0, FileType("dat")),


    RdbType(1066601, "GRND", 0, FileType("dat")),
    RdbType(1066602, "Ground Chunk", 0, FileType("dat")),
    RdbType(1066603, "Weird Textures", 12, FileType("png"), true),
    RdbType(1066604, "GHM1", 0, FileType("dat")),
    RdbType(1066606, "Ground Textures", 0, FileType("dds")),
    RdbType(1066609, "Collision Tag Map", 0, FileType("ctm")),
    RdbType(1066610, "Textures - FCTX", 0, FileType("dat")),
    RdbType(1066611, "Unknown", 0, FileType("dat")),

    RdbType(1070001, "Unknown", 0, FileType("dat")),
    RdbType(1070003, "XML - Playfield", 12, FileType("xml")),
    RdbType(1070010, "XML - DataFilterInstanceInfo", 0, FileType("xml")),
    RdbType(1070014, "Unknown", 0, FileType("dat")), //new
    RdbType(1070020, "Unknown", 0, FileType("dat"))  //new
    )
}
