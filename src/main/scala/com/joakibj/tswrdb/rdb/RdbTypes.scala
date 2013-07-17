package com.joakibj.tswrdb.rdb

object FileType {
  def apply(extension: String) = new FileType(extension)
}

case class FileType(val extension: String) {
  override def toString = extension
}

object RdbType {
  def apply(id: Int, name: String, fileType: FileType) = new RdbType(id, name, fileType)
}

class RdbType(id: Int, name: String, fileType: FileType)

final class RdbTypes {
  final val types: List[RdbType] = List(
    RdbType(1000001, "Map Info", FileType("dat")),

    RdbType(1000005, "PC64", FileType("dat")),
    RdbType(1000007, "XML - PhysX", FileType("xml")),
    RdbType(1000010, "File Names", FileType("dat")),
    RdbType(1000013, "K-D Tree KDv2", FileType("dat")),
    RdbType(1000015, "K-D Tree KDv2", FileType("dat")),
    RdbType(1000016, "K-D Tree KDv3", FileType("dat")),
    RdbType(1000020, "Items", FileType("dat")),
    RdbType(1000028, "K-D Tree KDv2", FileType("dat")),
    RdbType(1000042, "Unknown", FileType("dat")),
    RdbType(1000043, "Feat Class Info", FileType("dat")),
    RdbType(1000044, "FeatPoints", FileType("dat")),
    RdbType(1000047, "Camera Paths", FileType("dat")),
    RdbType(1000048, "Unknown", FileType("dat")),
    RdbType(1000049, "Lighting", FileType("dat")),
    RdbType(1000050, "XML - Territories", FileType("xml")),
    RdbType(1000052, "Unknown", FileType("dat")),
    RdbType(1000053, "XML - Bounded Area", FileType("xml")),
    RdbType(1000054, "Unknown", FileType("dat")),
    RdbType(1000055, "Bounded Area", FileType("dat")),
    RdbType(1000057, "Unknown", FileType("dat")),
    RdbType(1000058, "Unknown", FileType("dat")),
    RdbType(1000059, "Environ Area", FileType("dat")),
    RdbType(1000060, "Unknown", FileType("dat")),
    RdbType(1000061, "Unknown", FileType("dat")),
    RdbType(1000069, "Unknown", FileType("dat")),
    RdbType(1000070, "XML - Climb", FileType("xml")),
    RdbType(1000071, "Unknown", FileType("dat")),
    RdbType(1000075, "XML - Rivers", FileType("xml")),
    RdbType(1000076, "Unknown", FileType("dat")),
    RdbType(1000080, "Unknown", FileType("dat")),
    RdbType(1000081, "Unknown", FileType("dat")),
    RdbType(1000082, "Treasure Data", FileType("dat")),
    RdbType(1000083, "XML - Treasure Data", FileType("xml")),
    RdbType(1000087, "XML - Teleport Data", FileType("xml")),
    RdbType(1000090, "XML - Resurrect Points", FileType("xml")),
    RdbType(1000094, "Unknown", FileType("dat")),

    RdbType(1000095, "XML - Climb", FileType("xml")),
    RdbType(1000096, "Textures FCTX", FileType("dat")),
    RdbType(1000097, "XML - Ocean Volume", FileType("xml")),
    RdbType(1000098, "XML - Volumetric Fog", FileType("xml")),
    RdbType(1000099, "Feat Point Cost", FileType("dat")),
    RdbType(1000102, "Unknown", FileType("dat")),
    RdbType(1000103, "XML - Static AI Points", FileType("xml")),
    RdbType(1000104, "Unknown", FileType("dat")),
    RdbType(1000105, "Game Triggers", FileType("dat")),
    RdbType(1000107, "Character Tag Types", FileType("dat")),
    RdbType(1000108, "Lore Tree", FileType("dat")),

    RdbType(1000504, "Sound", FileType("dat")),
    RdbType(1000505, "Player Class Map", FileType("dat")),
    RdbType(1000506, "Class Membership Info", FileType("dat")),
    RdbType(1000507, "Unknown", FileType("dat")),
    RdbType(1000508, "Monsters", FileType("dat")),
    RdbType(1000509, "Unknown", FileType("dat")),
    RdbType(1000510, "Unknown", FileType("dat")),
    RdbType(1000511, "Unknown", FileType("dat")),
    RdbType(100050, "Unknown", FileType("dat")),
    RdbType(1000513, "Unknown", FileType("dat")),
    RdbType(1000514, "Unknown", FileType("dat")),
    RdbType(1000515, "Unknown", FileType("dat")),
    RdbType(1000516, "Unknown", FileType("dat")),
    RdbType(1000517, "Guild Gov Data", FileType("dat")),
    RdbType(1000518, "Guild Ranks", FileType("dat")),
    RdbType(1000519, "XML - Decal", FileType("xml")),
    RdbType(1000520, "Tokens", FileType("dat")),
    RdbType(1000521, "Guild Renown Rewards", FileType("dat")),
    RdbType(1000522, "PvP Performance Group", FileType("dat")),
    RdbType(1000523, "PvP Playfield Scores", FileType("dat")),
    RdbType(1000524, "Deck Ability Map", FileType("dat")),

    RdbType(1000620, "Item Data", FileType("dat")),
    RdbType(1000621, "Spells", FileType("dat")),
    RdbType(1000622, "XML - Effect Packages", FileType("xml")),
    RdbType(1000623, "Text Files", FileType("txt")),
    RdbType(1000624, "Media - Flash", FileType("dat")),
    RdbType(1000625, "Token State Data", FileType("dat")),
    RdbType(1000626, "Chat Filter", FileType("dat")),
    RdbType(1000635, "Media - Video", FileType("dat")),
    RdbType(1000636, "Media - Images", FileType("dat")),
    RdbType(1000637, "Unknown", FileType("dat")),
    RdbType(1000638, "Footsteps", FileType("dat")),

    RdbType(1010001, "FME Mesh Data", FileType("fme")),
    RdbType(1010002, "Skeleton Rigs", FileType("cs")),
    RdbType(1010003, "Skeleton Rigs", FileType("ca")),
    RdbType(1010004, "Textures", FileType("dds")),

    RdbType(1010006, "FCTX Textures", FileType("dat")),
    RdbType(1010007, "Morph", FileType("morph")),
    RdbType(1010008, "Icons - PNG", FileType("dat")),

    RdbType(101000, "Materials MTI", FileType("dat")),
    RdbType(1010013, "Maps Merged", FileType("jpg")),

    RdbType(1010028, "XML - Sound Scripts", FileType("xml")),
    RdbType(1010030, "XML - Mesh Index", FileType("xml")),
    RdbType(1010031, "Attractor", FileType("attr")),
    RdbType(1010034, "Unknown", FileType("dat")),
    RdbType(1010035, "XML - Texture Scripts", FileType("xml")),
    RdbType(1010036, "Unknown", FileType("enl")),
    RdbType(1010039, "Unknown", FileType("dat")),
    RdbType(1010040, "Unknown", FileType("dat")),
    RdbType(1010042, "Loading Screen Images", FileType("dat")),
    RdbType(1010043, "Unknown", FileType("dat")),
    RdbType(1010060, "Unknown", FileType("dat")),


    RdbType(1010200, "XML - AnimSys", FileType("xml")),
    RdbType(1010201, "XML - Monsters", FileType("xml")),
    RdbType(1010202, "XML - BCT Group", FileType("xml")),
    RdbType(1010203, "XML - BCT Mesh", FileType("xml")),
    RdbType(1010205, "XML - Movement Set", FileType("xml")),

    RdbType(1010207, "Particle Effects", FileType("particleeffect")),
    RdbType(1010210, "Bump Maps", FileType("png")),
    RdbType(1010211, "Bump Maps", FileType("png")),
    RdbType(1010213, "Del Portal Image - PNG", FileType("dat")),
    RdbType(1010216, "XML - Cars", FileType("xml")),
    RdbType(1010217, "Flora Images - TGA", FileType("dat")),
    RdbType(1010218, "Textures Flora", FileType("dat")),
    RdbType(1010220, "Grass Data", FileType("dat")),
    RdbType(1010221, "Grass Mesh", FileType("dat")),
    RdbType(1010223, "Lua Scripts", FileType("dat")),
    RdbType(1010226, "Looks Package", FileType("lkz")),
    RdbType(1010230, "Compiled Lua", FileType("dat")),

    RdbType(1010300, "XML - Room And Portal", FileType("xml")),
    RdbType(1010301, "Flora FME Mesh Data", FileType("fme")),
    RdbType(1010302, "Flora FME Mesh Data", FileType("fme")),
    RdbType(1010303, "Grass", FileType("dat")),
    RdbType(1010400, "XML - Formation", FileType("xml")),

    RdbType(1010500, "Binary XML - BCT Group", FileType("bxml")),
    RdbType(1010501, "Binary XML - BCT Mesh", FileType("bxml")),
    RdbType(1010503, "Binary XML - Movement Set", FileType("bxml")),
    RdbType(1010504, "Binary XML - Formation", FileType("bxml")),
    RdbType(1010505, "Binary XML - Cars", FileType("bxml")),
    RdbType(1010506, "Territories", FileType("dat")),
    RdbType(1010510, "Binary XML - Territories", FileType("bxml")),
    RdbType(1010511, "Binary XML - AnimSys", FileType("bxml")),
    RdbType(101050, "Binary XML - Static AI Points", FileType("bxml")),
    RdbType(1010514, "Binary XML - Monster Data", FileType("bxml")),
    RdbType(1010515, "Binary XML - Effect Package", FileType("bxml")),
    RdbType(1010516, "Binary XML - Mesh Index", FileType("bxml")),

    RdbType(1010700, "XML - Char Creation", FileType("xml")),

    RdbType(1020002, "Sound Effects", FileType("dat")),
    RdbType(1020003, "Lip Sync Voices", FileType("dat")),
    RdbType(1020004, "Patricia Data?", FileType("dat")),
    RdbType(1020005, "Music", FileType("ogg")),
    RdbType(1020006, "Sound Files - Wave", FileType("dat")),
    RdbType(1020001, "Audio", FileType("dat")),

    RdbType(1030001, "Language Index", FileType("dat")),
    RdbType(1030002, "Strings", FileType("dat")),

    RdbType(1040005, "Feats", FileType("dat")),
    RdbType(1040007, "Unknown", FileType("dat")),
    RdbType(1040008, "Unknown", FileType("dat")),
    RdbType(1040009, "Unknown", FileType("dat")),
    RdbType(1040010, "XML - Bounded Area", FileType("xml")),

    RdbType(1060667, "XML - Env Settings", FileType("xml")),
    RdbType(1060668, "Sky Dome", FileType("dat")),

    RdbType(1065101, "FeatSpecializedTrees", FileType("dat")),
    RdbType(1065102, "Unknown", FileType("dat")),
    RdbType(1065103, "PvPConRating", FileType("dat")),


    RdbType(1066601, "GRND", FileType("dat")),
    RdbType(1066602, "Ground Chunk", FileType("dat")),
    RdbType(1066603, "Weird Textures", FileType("png")),
    RdbType(1066604, "GHM1", FileType("dat")),
    RdbType(1066606, "Ground Textures", FileType("dds")),
    RdbType(1066609, "Collision Tag Map", FileType("ctm")),
    RdbType(1066610, "Textures - FCTX", FileType("dat")),
    RdbType(1066611, "Unknown", FileType("dat")),

    RdbType(1070003, "XML - Playfield", FileType("xml")),
    RdbType(1070010, "XML - DataFilterInstanceInfo", FileType("xml"))
  )
}
