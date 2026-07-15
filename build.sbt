import sbt.Keys.*
import sbt.*
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport.*

lazy val common = project
  .in(file("common"))
  .settings(
    name := "todo-common",
    version := "0.1.0",
    scalaVersion := "3.6.4",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core" % "2.12.0",
      "io.circe"    %% "circe-generic"  % "0.14.10",
      "io.circe"    %% "circe-parser"   % "0.14.10",
      "org.scalatest" %% "scalatest"     % "3.2.18" % Test
    )
  )

lazy val backend = project
  .in(file("backend"))
  .dependsOn(common)
  .settings(
    name := "todo-backend",
    version := "0.1.0",
    scalaVersion := "3.6.4",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-ember-server" % "0.23.30",
      "org.http4s" %% "http4s-ember-client" % "0.23.30",
      "org.http4s" %% "http4s-dsl"          % "0.23.30",
      "org.http4s" %% "http4s-circe"        % "0.23.30",
      "io.circe"   %% "circe-generic"       % "0.14.10",
      "io.circe"   %% "circe-parser"        % "0.14.10",
      "org.typelevel" %% "cats-effect"    % "3.5.4",
      "org.scalatest" %% "scalatest"    % "3.2.18" % Test
    ),
    Compile / mainClass := Some("TodoServer"),
    packageBin / publishArtifact := false,
    run / fork := true
  )

lazy val frontend = project
  .in(file("frontend"))
  .dependsOn(common)
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "todo-frontend",
    version := "0.1.0",
    scalaVersion := "3.6.4",
    libraryDependencies ++= Seq(
      "io.indigoengine" %%% "tyrian"           % "0.12.0",
      "io.indigoengine" %%% "tyrian-io"        % "0.12.0",
      "io.indigoengine" %%% "tyrian-tags"      % "0.12.0"
    ),
    scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
    packageBin / publishArtifact := false
  )

lazy val root = project
  .in(file("."))
  .aggregate(common, backend, frontend)
  .settings(
    name := "todo-scala-app",
    version := "0.1.0",
    scalaVersion := "3.6.4"
  )
