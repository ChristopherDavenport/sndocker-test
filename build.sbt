// import com.armanbilge.scalanative.brew.BrewNativeConfig
import com.indoorvivants.detective.Platform.OS.*
import com.indoorvivants.detective.Platform
import bindgen.interface.Binding
import bindgen.interface.LogLevel

ThisBuild / tlBaseVersion := "0.0" // your current series x.y

ThisBuild / organization := "io.chrisdavenport"
ThisBuild / organizationName := "Christopher Davenport"
ThisBuild / licenses := Seq(License.MIT)
ThisBuild / developers := List(
  tlGitHubDev("christopherdavenport", "Christopher Davenport")
)
ThisBuild / versionScheme := Some("early-semver")

ThisBuild / tlCiReleaseBranches := Seq()
ThisBuild / tlSonatypeUseLegacyHost := true


val Scala3 = "3.2.2"

ThisBuild / crossScalaVersions := Seq(Scala3)
ThisBuild / scalaVersion := Scala3

ThisBuild / testFrameworks += new TestFramework("munit.Framework")

ThisBuild / githubWorkflowJavaVersions := Seq(JavaSpec.temurin("17"))
ThisBuild / tlJdkRelease := Some(8)


val catsV = "2.9.0"
val catsEffectV = "3.4.8"
val fs2V = "3.6.1"
val http4sV = "0.23.18"
val circeV = "0.14.5"
val munitCatsEffectV = "2.0.0-M3"


// Projects
lazy val `sndocker-test` = tlCrossRootProject
  .aggregate(core)

lazy val core = crossProject(JVMPlatform, NativePlatform)
  .crossType(CrossType.Pure)
  .enablePlugins(BindgenPlugin, VcpkgPlugin, NoPublishPlugin)
  .in(file("core"))
  .settings(
    name := "sndocker-test",
    libraryDependencies ++= Seq(
      "org.typelevel"               %%% "cats-core"                  % catsV,
      "org.typelevel"               %%% "cats-effect"                % catsEffectV,

      "co.fs2"                      %%% "fs2-core"                   % fs2V,
      "co.fs2"                      %%% "fs2-io"                     % fs2V,

      "org.http4s"                  %%% "http4s-dsl"                 % http4sV,
      "org.http4s"                  %%% "http4s-ember-server"        % http4sV,
      "org.http4s"                  %%% "http4s-ember-client"        % http4sV,
      "org.http4s"                  %%% "http4s-circe"               % http4sV,

      "io.chrisdavenport"           %%% "crossplatformioapp"         % "0.1.0",

      "org.typelevel"               %%% "munit-cats-effect"        % munitCatsEffectV         % Test,
    )
  ).nativeSettings(
    vcpkgDependencies := VcpkgDependencies("s2n", "openssl"),
    Compile / run / envVars := Map("S2N_DONT_MLOCK" -> "1"),
    bindgenBindings += {
      Binding(
        vcpkgConfigurator.value.includes("s2n") / "s2n.h",
        "s2n",
        cImports = List("s2n.h"),
        clangFlags = List("-I" + vcpkgConfigurator.value.includes("s2n"))
      )
    },
    nativeConfig := {
      val conf = nativeConfig.value
      val arch64 =
        if (
          Platform.arch == Platform.Arch.Arm && Platform.bits == Platform.Bits.x64
        )
          List("-arch", "arm64")
        else Nil

      conf
        .withLinkingOptions(
          conf.linkingOptions ++ arch64
        )
        .withCompileOptions(
          conf.compileOptions ++ arch64
        )
    },
  )

