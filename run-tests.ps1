# Limpa e executa os testes com cobertura
Write-Host "Executando testes com cobertura..." -ForegroundColor Green
mvn clean test jacoco:report

# Verifica se os testes passaram
if ($LASTEXITCODE -eq 0) {
    Write-Host "`nTestes executados com sucesso!" -ForegroundColor Green
    Write-Host "Relatório de cobertura disponível em: target/site/jacoco/index.html" -ForegroundColor Yellow
    
    # Pergunta se deseja abrir o relatório
    $abrir = Read-Host "Deseja abrir o relatório de cobertura? (S/N)"
    if ($abrir -eq "S" -or $abrir -eq "s") {
        Start-Process "target/site/jacoco/index.html"
    }
} else {
    Write-Host "`nErro na execução dos testes!" -ForegroundColor Red
} 