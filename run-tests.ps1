# Script para executar testes e gerar relatório de cobertura
Write-Host "Executando testes e gerando relatório de cobertura com JaCoCo..."
mvn clean test jacoco:report -Dsurefire.failIfNoSpecifiedTests=false

# Abrir o relatório de cobertura
$reportPath = "target/site/jacoco/index.html"
if (Test-Path $reportPath) {
    Write-Host "Relatório de cobertura gerado em: $reportPath"
    Write-Host "Abrindo relatório..."
    Start-Process $reportPath
} else {
    Write-Host "Relatório não foi gerado. Verifique se os testes foram executados com sucesso."
} 