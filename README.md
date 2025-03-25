# HubSpot Integration - Meetime Test

Este projeto é responsável pela integração com o HubSpot, permitindo autenticação via OAuth2 e operações de CRUD para contatos.

## Bibliotecas Utilizadas

- Spring(Boot)
    - OAuth2 Client
    - Security
    - Retry
        - Motivação: Tratamento de erros de rate limit no HubSpot.
    - Web
    - Validation
        - Motivação: Facilitar e simplificar a validação dos dados de entrada.
- Jackson
    - Motivação: Serialização e deserialização de objetos JSON, facilitando a comunicação com a API do HubSpot.
- Lombok
    - Motivação: Simplificação de código e redução de boilerplate, agilizando o desenvolvimento.
- Springdoc(OpenAPI)
    - Motivação: Documentação da API, gerada automaticamente com Swagger UI.
- Guava
    - Motivação: Utilizado para tratamento de erros de rate limit no HubSpot, simples de configurar e usar, além de ser amplamente utilizado na comunidade.

## Melhorias Futuras

- Melhorar a validação dos dados de entrada, com mensagens mais descriptivas e informando o(s) campo(s) que estiver(em) com erro.
- Estruturar projeto para modelo hexagonal, separando os serviços de autenticação, gerenciamento de contatos e webhooks.
- Implementar mais testes unitários para garantir a qualidade do código.
- Implementar refresh automático do token.

## Configuração do HubSpot

Para utilizar a integração, siga os passos abaixo para criar um aplicativo no HubSpot:

### Criar uma conta de desenvolvedor

1. Acesse [HubSpot Developer](https://developers.hubspot.com/)
2. Clique em **Get started** e crie sua conta de desenvolvedor.

### Criar um aplicativo no HubSpot

1. Após acessar sua conta de desenvolvedor, vá para **Apps** e clique em **Create App**.
2. Preencha os detalhes do aplicativo e configure as permissões (scopes):
   - `crm.objects.contacts.read`
   - `crm.objects.contacts.write`
   - `crm.schemas.contacts.write`
3. Na seção **Auth settings**, configure a **Redirect URL** para:
   ```
   http://localhost:8080/v1/oauth/callback
   ```

### Obter credenciais do app

1. Após criar o app, anote os dados listados abaixo, pois iremos utiliza-los na configuração do serviço:
   - **Client ID**
   - **Client Secret**
   - **Redirect URI**


## Configuração e Execução do Serviço

### Variáveis de Ambiente

Antes de iniciar o serviço, configure as seguintes variáveis de ambiente:

```yaml
spring:
  application:
    name: meetime-hubspot

hubspot:
  client-id: ${HUBSPOT_CLIENT_ID}
  client-secret: ${HUBSPOT_CLIENT_SECRET}
  api-uri: ${HUBSPOT_API_URI:https://api.hubapi.com}
  redirect-uri: ${HUBSPOT_REDIRECT_URI:http://localhost:8080/v1/oauth/callback}
  token-endpoint: ${HUBSPOT_TOKEN_ENDPOINT:/oauth/v1/token}
  auth-uri: ${HUBSPOT_AUTH_URI:https://app.hubspot.com/oauth/authorize}
  contact-uri: ${HUBSPOT_CONTACT_URI:/crm/v3/objects/contacts}
  rate-limit:
    requests: ${HUBSPOT_RATE_LIMIT_REQUESTS:110}
    interval: ${HUBSPOT_RATE_LIMIT_INTERVAL:10}

```

### Executando o serviço localmente

1. Clone este repositório:
   ```sh
   git clone https://github.com/ggfto/meetime-hubspot
   ```
2. Acesse a pasta do projeto:
   ```sh
   cd meetime-hubspot
   ```
3. Compile e execute o projeto usando Maven:
   ```sh
   mvn spring-boot:run
   ```

## Deploy com Docker

Para rodar o serviço usando Docker, siga os passos abaixo:

### Criando a imagem Docker

1. Certifique-se de que o **Docker** está instalado em seu ambiente.
2. Construa a imagem Docker:
   ```sh
   mvn clean package
   docker build -t meetime-hubspot .
   ```

### Rodando o container

1. Execute o container com:
   ```sh
   docker run -d -p 8080:8080 \
      -e HUBSPOT_CLIENT_ID=<seu_client_id> \
      -e HUBSPOT_CLIENT_SECRET=<seu_client_secret> \
      -e HUBSPOT_REDIRECT_URI=http://localhost:8080/v1/oauth/callback \
      --name meetime-hubspot meetime-hubspot
   ```

### Usando Docker Compose

Para facilitar a execução, você pode usar o arquivo `docker-compose.yml`, alterando as variáveis conforme suas configurações.
Para rodar o serviço usando Docker Compose:
```sh
docker-compose up -d
```

## Endpoints Disponíveis

O serviço expõe endpoints para autenticação, gerenciamento de contatos no HubSpot e Webhooks.

### Webhooks

- **Receber evento de criação de contato:** `POST /v1/webhooks/contact-creation`
   - Este endpoint recebe notificações do HubSpot sempre que um novo contato é criado.
   - Documentado no Swagger UI.

O serviço expõe endpoints para autenticação e gerenciamento de contatos no HubSpot.

### Autenticação

- **Login no HubSpot:** `GET /v1/auth/login`
- **Callback OAuth2 (oculto):** `GET /v1/oauth/callback`

### Contatos

- **Criar contato:** `POST /v1/contacts`
- **Listar contatos:** `GET /v1/contacts`

## Documentação da API

Após iniciar o serviço, a documentação pode ser acessada via Swagger UI:

```
http://localhost:8080/swagger-ui/index.html
```

A documentação completa pode ainda ser visualizada em [API_SPECS](./API_SPECS.md)(formato openapi, pode ser necessário o uso de uma ferramenta como o [Swagger Editor](https://editor.swagger.io/)) e um gráfico pode ser visualizado em [API_GRAPH](./API_GRAPH.md).

## Observações Importantes

- A API do HubSpot exige que a URL de Webhooks seja **HTTPS** vide [documentação](https://developers.hubspot.com/docs/guides/api/app-management/webhooks).
- O callback de OAuth2 funciona localmente, mas para Webhooks externos, o serviço precisa estar exposto publicamente.
- Este projeto foi feito com **Java 21** e **Spring Boot 3.4.3**.

---
