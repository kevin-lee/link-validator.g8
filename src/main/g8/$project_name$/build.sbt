import com.typesafe.sbt.packager.archetypes.systemloader.ServerLoader.SystemV
import wartremover.{Wart, Warts}

ThisBuild / scalaVersion := props.ScalaVersion
ThisBuild / version := props.ProjectVersion
ThisBuild / organization := props.Org
ThisBuild / organizationName := props.OrgName
ThisBuild / developers := List(
  Developer(
    props.GitHubUsername,
    "$author_name$",
    "$author_email$",
    url(s"https://github.com/\${props.GitHubUsername}"),
  )
)
ThisBuild / homepage := url(s"https://github.com/\${props.GitHubUsername}/\${props.RepoName}").some
ThisBuild / scmInfo :=
  ScmInfo(
    url(s"https://github.com/\${props.GitHubUsername}/\${props.RepoName}"),
    s"https://github.com/\${props.GitHubUsername}/\${props.RepoName}.git",
  ).some

ThisBuild / javaOptions += "-Dcats.effect.warnOnNonMainThreadDetected=false"

ThisBuild / semanticdbEnabled := true

lazy val root = (project in file("."))
  .settings(
    name := props.ProjectName
  )
  .settings(noPublish)
  .aggregate(core, app)

lazy val core = subProject("core")
  .settings(
    libraryDependencies ++=
      libs.refined4s ++
        libs.extras ++
        libs.catsAndCatsEffect
  )

lazy val app = subProject("app")
  .enablePlugins(JavaAppPackaging)
  .settings(debianPackageInfo)
  .settings(
    maintainer := "$author_name$ <$author_email$>",
    description := "A link validator for web pages",
    Compile / run / fork := false, // This is required to get an input from the console.
  )
  .settings(
    libraryDependencies ++= libs.refined4s ++ libs.catsAndCatsEffect ++ libs.decline,
  )
  .dependsOn(
    core % props.IncludeTest
  )

lazy val props =
  new {
    val ScalaVersion = "$scalaVersion$"
    val Org          = "$organization$"
    val OrgName      = "$organizationName$"

    val GitHubUsername = "$github_username$"
    val RepoName       = "$project_name$"
    val ProjectName    = RepoName
    val ProjectVersion = "0.1.0-SNAPSHOT"

    val Refined4sVersion = "$refined4s_version$"

    val ExtrasVersion = "$extras_version$"

    val HedgehogVersion = "$hedgehog_version$"

    val CatsVersion       = "$cats_version$"
    val CatsEffectVersion = "$cats_effect_version$"

    val DeclineVersion = "$decline_version$"

    val IncludeTest: String = "compile->compile;test->test"
  }

lazy val libs =
  new {
    lazy val hedgehogLibs = List(
      "qa.hedgehog" %% "hedgehog-core"   % props.HedgehogVersion,
      "qa.hedgehog" %% "hedgehog-runner" % props.HedgehogVersion,
      "qa.hedgehog" %% "hedgehog-sbt"    % props.HedgehogVersion,
    ).map(_ % Test)

    lazy val refined4s = List(
      "io.kevinlee" %% "refined4s-core"          % props.Refined4sVersion,
      "io.kevinlee" %% "refined4s-cats"          % props.Refined4sVersion,
      "io.kevinlee" %% "refined4s-chimney"       % props.Refined4sVersion,
      "io.kevinlee" %% "refined4s-circe"         % props.Refined4sVersion,
      "io.kevinlee" %% "refined4s-extras-render" % props.Refined4sVersion,
      "io.kevinlee" %% "refined4s-pureconfig"    % props.Refined4sVersion,
      "io.kevinlee" %% "refined4s-doobie-ce3"    % props.Refined4sVersion,
      "io.kevinlee" %% "refined4s-tapir"         % props.Refined4sVersion,
    )

    lazy val extras = List(
      "io.kevinlee" %% "extras-render"       % props.ExtrasVersion,
      "io.kevinlee" %% "extras-string"       % props.ExtrasVersion,
      "io.kevinlee" %% "extras-cats"         % props.ExtrasVersion,
      "io.kevinlee" %% "extras-scala-io"     % props.ExtrasVersion,
      "io.kevinlee" %% "extras-hedgehog-ce3" % props.ExtrasVersion % Test
    )

    lazy val catsAndCatsEffect = List(
      "org.typelevel" %% "cats-core"   % props.CatsVersion,
      "org.typelevel" %% "cats-effect" % props.CatsEffectVersion,
    )

    lazy val decline = List(
      "com.monovore" %% "decline" % props.DeclineVersion
    )

  }

// format: off
def prefixedProjectName(name: String) = s"\${props.ProjectName}\${if (name.isEmpty) "" else s"-\$name"}"
// format: on

def subProject(projectName: String): Project = {
  val prefixedName = prefixedProjectName(projectName)
  Project(prefixedName, file(s"modules/\$prefixedName"))
    .settings(
      name := prefixedName,
      fork := true,
      libraryDependencies ++= libs.hedgehogLibs,
      wartremoverErrors ++= Warts.unsafe,
      wartremoverExcluded ++= (Compile / sourceManaged).value.get,
      /* Exclude specific warts if needed */
      wartremoverErrors --= Seq(
        Wart.Any,
        Wart.Nothing,
      ),
      Compile / console / scalacOptions :=
        (console / scalacOptions)
          .value
          .distinct
          .filterNot(option => option.contains("wartremover") || option.contains("import")),
      Test / console / scalacOptions :=
        (console / scalacOptions)
          .value
          .distinct
          .filterNot(option => option.contains("wartremover") || option.contains("import")),
    )
}

lazy val debianPackageInfo: SettingsDefinition = List(
  Linux / maintainer := "$author_name$ <$author_email$>",
  Linux / packageSummary := "My App",
  packageDescription := "My app is ...",
  Debian / serverLoading := SystemV.some,
)
