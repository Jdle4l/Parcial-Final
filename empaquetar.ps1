param(
    [Parameter(Mandatory = $true)]
    [string]$Apellidos
)

$raiz = $PSScriptRoot
$nombre = ($Apellidos.ToUpper() -replace '\s+', '_') + "_PARCIALFINAL_DES3"
$temporal = Join-Path $env:TEMP $nombre
$destino = Join-Path (Split-Path $raiz -Parent) ($nombre + ".zip")
$excluir = @('node_modules', 'target', 'dist', '.angular', '.git', '.idea', '.vscode')

if (Test-Path $temporal) { Remove-Item $temporal -Recurse -Force }
New-Item -ItemType Directory -Path $temporal | Out-Null

foreach ($carpeta in @('backend', 'frontend')) {
    $origen = Join-Path $raiz $carpeta
    Get-ChildItem $origen -Recurse -Force -File | Where-Object {
        $relativa = $_.FullName.Substring($origen.Length + 1)
        -not ($relativa.Split([IO.Path]::DirectorySeparatorChar) | Where-Object { $excluir -contains $_ })
    } | ForEach-Object {
        $relativa = $_.FullName.Substring($raiz.Length + 1)
        $archivo = Join-Path $temporal $relativa
        New-Item -ItemType Directory -Path (Split-Path $archivo -Parent) -Force | Out-Null
        Copy-Item $_.FullName $archivo
    }
}
Copy-Item (Join-Path $raiz 'README.md') $temporal
Copy-Item (Join-Path $raiz 'INSTRUCCIONES.txt') $temporal

if (Test-Path $destino) { Remove-Item $destino -Force }
Add-Type -AssemblyName System.IO.Compression.FileSystem
[IO.Compression.ZipFile]::CreateFromDirectory($temporal, $destino)
Remove-Item $temporal -Recurse -Force
Write-Host "Entrega generada: $destino"
