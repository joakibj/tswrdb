import AssemblyKeys._

assemblySettings

jarName in assembly <<= (version) map { (version) => "tswrdb-" + version + ".jar" }

mainClass in assembly := Some("com.joakibj.tswrdb.TswRdb")
