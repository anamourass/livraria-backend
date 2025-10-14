# Deploy da API Livraria no Azure

Este guia contém instruções completas para fazer o deploy da API Spring Boot no Azure e conectá-la com uma aplicação Angular.

## 📋 Pré-requisitos

- Conta no Azure
- Azure CLI instalado
- Docker instalado
- Repositório GitHub
- Aplicação Angular hospedada

## 🚀 Opções de Deploy

### Opção 1: Azure Container Apps (Recomendado)

Azure Container Apps é ideal para aplicações containerizadas com auto-scaling.

#### 1. Criar recursos no Azure

```bash
# Login no Azure
az login

# Criar Resource Group
az group create --name rg-livraria --location eastus

# Criar Azure Container Registry
az acr create --resource-group rg-livraria --name acrlivraria --sku Basic --admin-enabled true

# Criar PostgreSQL Database
az postgres flexible-server create \
  --resource-group rg-livraria \
  --name postgres-livraria \
  --admin-user livrariauser \
  --admin-password 'SuaSenhaSegura123!' \
  --sku-name Standard_B1ms \
  --version 14

# Criar database
az postgres flexible-server db create \
  --resource-group rg-livraria \
  --server-name postgres-livraria \
  --database-name livraria

# Criar Container Apps Environment
az containerapp env create \
  --name livraria-env \
  --resource-group rg-livraria \
  --location eastus
```

#### 2. Configurar Secrets no GitHub

Vá em **Settings > Secrets and variables > Actions** do seu repositório e adicione:

- `AZURE_CREDENTIALS`: Credenciais do Service Principal
- `ACR_USERNAME`: Username do Container Registry
- `ACR_PASSWORD`: Password do Container Registry
- `DATABASE_URL`: `jdbc:postgresql://postgres-livraria.postgres.database.azure.com:5432/livraria?sslmode=require`
- `DB_USERNAME`: `livrariauser`
- `DB_PASSWORD`: `SuaSenhaSegura123!`
- `CORS_ALLOWED_ORIGINS`: URL da sua aplicação Angular (ex: `https://yourapp.azurestaticapps.net`)

#### 3. Obter Credenciais do Azure

```bash
# Criar Service Principal
az ad sp create-for-rbac --name "livraria-deploy" --role contributor \
  --scopes /subscriptions/{subscription-id}/resourceGroups/rg-livraria \
  --sdk-auth

# Obter credenciais do ACR
az acr credential show --name acrlivraria
```

#### 4. Deploy via GitHub Actions

O deploy acontecerá automaticamente quando você fizer push para a branch `main`.

### Opção 2: Azure App Service

#### 1. Criar App Service

```bash
# Criar App Service Plan
az appservice plan create \
  --name livraria-plan \
  --resource-group rg-livraria \
  --sku B1 \
  --is-linux

# Criar Web App
az webapp create \
  --resource-group rg-livraria \
  --plan livraria-plan \
  --name livraria-backend-api \
  --deployment-container-image-name openjdk:17-jre-slim
```

#### 2. Deploy manual

```bash
# Build da aplicação
mvn clean package -DskipTests

# Deploy do JAR
az webapp deploy \
  --resource-group rg-livraria \
  --name livraria-backend-api \
  --src-path target/livraria-backend-1.0.0.jar \
  --type jar
```

## ⚙️ Configuração de Variáveis de Ambiente

### No Azure Container Apps:

```bash
az containerapp update \
  --name livraria-backend \
  --resource-group rg-livraria \
  --set-env-vars \
    SPRING_PROFILES_ACTIVE=prod \
    DATABASE_URL="jdbc:postgresql://postgres-livraria.postgres.database.azure.com:5432/livraria?sslmode=require" \
    DB_USERNAME=livrariauser \
    DB_PASSWORD="SuaSenhaSegura123!" \
    CORS_ALLOWED_ORIGINS="https://yourapp.azurestaticapps.net"
```

### No Azure App Service:

```bash
az webapp config appsettings set \
  --resource-group rg-livraria \
  --name livraria-backend-api \
  --settings \
    SPRING_PROFILES_ACTIVE=prod \
    DATABASE_URL="jdbc:postgresql://postgres-livraria.postgres.database.azure.com:5432/livraria?sslmode=require" \
    DB_USERNAME=livrariauser \
    DB_PASSWORD="SuaSenhaSegura123!" \
    CORS_ALLOWED_ORIGINS="https://yourapp.azurestaticapps.net"
```

## 🔧 Configuração do Frontend Angular

No seu projeto Angular, atualize o arquivo de configuração para apontar para a API no Azure:

### src/environments/environment.prod.ts
```typescript
export const environment = {
  production: true,
  apiUrl: 'https://livraria-backend.{region}.azurecontainerapps.io/api'
  // ou para App Service:
  // apiUrl: 'https://livraria-backend-api.azurewebsites.net/api'
};
```

### src/app/services/livro.service.ts
```typescript
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LivroService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  // seus métodos...
}
```

## 🏗️ Deploy do Frontend Angular

### Azure Static Web Apps (Recomendado)

```bash
# Instalar Azure Static Web Apps CLI
npm install -g @azure/static-web-apps-cli

# Build da aplicação
ng build --prod

# Deploy para Azure Static Web Apps
swa deploy --app-location dist/ --resource-group rg-livraria --app-name livraria-frontend
```

## 🔒 Configuração de Segurança

### 1. Configurar Firewall do PostgreSQL

```bash
# Permitir acesso do Azure
az postgres flexible-server firewall-rule create \
  --resource-group rg-livraria \
  --name postgres-livraria \
  --rule-name AllowAzureServices \
  --start-ip-address 0.0.0.0 \
  --end-ip-address 0.0.0.0
```

### 2. Configurar HTTPS (Automático no Azure)

O Azure fornece certificados SSL automaticamente para domínios .azurecontainerapps.io e .azurewebsites.net.

## 📊 Monitoramento

### Application Insights

```bash
# Criar Application Insights
az monitor app-insights component create \
  --app livraria-insights \
  --location eastus \
  --resource-group rg-livraria
```

### Health Checks

A aplicação expõe endpoints de health check em:
- `/actuator/health` - Status geral da aplicação
- `/actuator/info` - Informações da aplicação

## 🐛 Troubleshooting

### 1. Verificar logs da aplicação

```bash
# Container Apps
az containerapp logs show \
  --name livraria-backend \
  --resource-group rg-livraria

# App Service
az webapp log tail \
  --name livraria-backend-api \
  --resource-group rg-livraria
```

### 2. Problemas CORS

Verifique se a variável `CORS_ALLOWED_ORIGINS` está configurada com a URL correta do seu frontend.

### 3. Problemas de conexão com banco

Verifique se:
- As credenciais do banco estão corretas
- O firewall do PostgreSQL permite conexões do Azure
- A string de conexão inclui `?sslmode=require`

## 💰 Estimativa de Custos

### Container Apps + PostgreSQL + Static Web Apps:
- Container Apps: ~$30-50/mês
- PostgreSQL Flexible Server: ~$25-40/mês  
- Static Web Apps: Gratuito (até 100GB/mês)

**Total estimado: $55-90/mês**

## 📚 Recursos Úteis

- [Azure Container Apps Documentation](https://docs.microsoft.com/azure/container-apps/)
- [Azure Database for PostgreSQL](https://docs.microsoft.com/azure/postgresql/)
- [Azure Static Web Apps](https://docs.microsoft.com/azure/static-web-apps/)
- [Spring Boot on Azure](https://docs.microsoft.com/azure/developer/java/spring-framework/)

## 🆘 Suporte

Para problemas específicos:
1. Verifique os logs da aplicação
2. Consulte a documentação do Azure
3. Use o Azure Support Center
4. Comunidade Stack Overflow com tags [azure] [spring-boot]