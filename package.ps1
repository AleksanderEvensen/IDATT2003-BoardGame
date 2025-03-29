
# Define configuration variables
$projectName = "BoardGame"
$appVersion = "1.0"
$mainJar = "$projectName-$appVersion.jar"
$mainClass = "edu.ntnu.idi.idatt.boardgame.Application"
$inputDir = "target"

# Build the project using Maven
Write-Host "Building the project with Maven..."
mvn clean package
if ($LASTEXITCODE -ne 0) {
    Write-Error "Maven build failed. Exiting."
    exit $LASTEXITCODE
}

Write-Host "Packaging the application with jpackage..."
jpackage `
    --name $projectName `
    --app-version $appVersion `
    --input $inputDir `
    --main-jar $mainJar `
    --main-class $mainClass `
    --type exe

if ($LASTEXITCODE -ne 0) {
    Write-Error "jpackage failed to create the installer."
    exit $LASTEXITCODE
}

Write-Host "Packaging completed successfully."
