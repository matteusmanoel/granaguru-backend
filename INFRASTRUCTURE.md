# 🏗️ Infraestrutura GranaGuru

## 📋 Visão Geral

O GranaGuru é implantado em um ambiente com três máquinas virtuais (VMs) Alpine Linux, cada uma com uma responsabilidade específica:

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend VM   │    │   Backend VM    │    │   Database VM   │
│    vm_front     │───▶│    vm_back      │───▶│     vm_db       │
│  (frontend_user)│    │ (backend_user)  │    │ (database_user) │
│   (NGINX)       │    │   (Spring Boot) │    │   (MariaDB)     │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### 🌐 Configuração de Rede

**Modo**: VirtualBox Host-only Network

| VM       | IP            | Hostname                             | Usuário       |
| -------- | ------------- | ------------------------------------ | ------------- |
| Frontend | 192.168.56.10 | granaguru.local, www.granaguru.local | frontend_user |
| Backend  | 192.168.56.20 | api.granaguru.local                  | backend_user  |
| Database | 192.168.56.30 | db.granaguru.local                   | database_user |

## 🔧 Configurações por VM

### 💾 Database VM (vm_db)

- **Sistema**: Alpine Linux
- **Serviço**: MariaDB
- **Porta**: 3306
- **Usuário App**: granaguru_user
- **Database**: granagurudb
- **Backup**: A cada 6 horas via crontab
- **Diretório Backup**: /opt/backups/database

### ⚙️ Backend VM (vm_back)

- **Sistema**: Alpine Linux
- **Runtime**: Java 17
- **Framework**: Spring Boot
- **Porta**: 8080
- **Diretório App**: /opt/granaguru
- **Service File**: /etc/init.d/granaguru-api
- **Backup**: A cada 3 horas via crontab
- **Diretório Backup**: /opt/backups/backend

### 🖥️ Frontend VM (vm_front)

- **Sistema**: Alpine Linux
- **Servidor**: NGINX
- **Porta**: 80
- **Framework**: Angular
- **Diretório App**: /var/www/granaguru
- **Config NGINX**: /etc/nginx/http.d/granaguru.conf
- **Backup**: A cada 3 horas via crontab
- **Diretório Backup**: /opt/backups/frontend

## 🔒 Segurança

### 🔑 Usuários e Acessos

- Cada VM possui um usuário dedicado (frontend_user, backend_user, database_user)
- Acesso root via `su -` (senha: myfirstmac)
- SSH configurado para acesso com usuário dedicado

### 🛡️ Banco de Dados

- Conexões restritas ao IP do backend (192.168.56.20)
- Senha root segura armazenada em /root/.my.cnf
- Usuário aplicação com privilégios limitados

## 📂 Sistema de Backup

### 📊 Agendamento

- **Frontend**: A cada 3 horas
- **Backend**: A cada 3 horas
- **Database**: A cada 6 horas

### 📁 Estrutura

- Todos os backups são centralizados na vm_db
- Usuário backup_sys em todas as VMs
- Rotação automática de backups (mantém últimos 7 dias)

### 📦 Conteúdo dos Backups

**Frontend**:

- Arquivos estáticos Angular
- Configurações NGINX
- Logs NGINX
- Chaves SSH

**Backend**:

- JAR da aplicação
- Configurações
- Logs
- Chaves SSH

**Database**:

- Dump completo do banco
- Configurações MariaDB

## 🌐 DNS Local

### 🔧 Configuração

Todas as VMs e host têm as seguintes entradas em `/etc/hosts`:

```
# GranaGuru DNS
192.168.56.10    granaguru.local www.granaguru.local
192.168.56.20    api.granaguru.local
192.168.56.30    db.granaguru.local
```

### 🔄 Resolução de Nomes

- Frontend (NGINX) usa api.granaguru.local para proxy reverso
- Backend usa db.granaguru.local para conexão com banco
- Navegador acessa via www.granaguru.local

## 🚀 Serviços e Portas

| Serviço  | Porta | URL de Acesso                     |
| -------- | ----- | --------------------------------- |
| Frontend | 80    | http://www.granaguru.local        |
| Backend  | 8080  | http://api.granaguru.local:8080   |
| Database | 3306  | jdbc:mariadb://db.granaguru.local |

## 📝 Logs

- **Frontend**: /var/log/nginx/
- **Backend**: /opt/granaguru/granaguru.log
- **Database**: /var/log/mysql/

## ⚡ Health Check

- **Endpoint**: http://api.granaguru.local:8080/health
- **Método**: GET
- **Resposta**: Status da API, versão e timestamp

## 🔄 Comandos Úteis

### 🖥️ Frontend (vm_front)

```bash
# Status NGINX
rc-service nginx status

# Restart NGINX
rc-service nginx restart

# Logs NGINX
tail -f /var/log/nginx/error.log
```

### ⚙️ Backend (vm_back)

```bash
# Status API
rc-service granaguru-api status

# Restart API
rc-service granaguru-api restart

# Logs API
tail -f /opt/granaguru/granaguru.log
```

### 💾 Database (vm_db)

```bash
# Status MariaDB
rc-service mariadb status

# Restart MariaDB
rc-service mariadb restart

# Logs MariaDB
tail -f /var/log/mysql/error.log
```

### 🔄 Backup Manual

```bash
# Frontend
ssh frontend "su - -c '/opt/backups/backup-frontend.sh'"

# Backend
ssh backend "su - -c '/opt/backups/backup-backend.sh'"

# Database
ssh database "su - -c '/opt/backups/backup-database.sh'"
```
