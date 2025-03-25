```yaml
openapi: 3.1.0
info:
  title: Integração HubSpot
  description: Serviço responsável pela integração com o HubSpot
servers:
  - url: http://localhost:8080
    description: Generated server url
paths:
  /v1/webhooks/contact-creation:
    post:
      tags:
        - web-hook-controller
      summary: Recebe dados de criação de contato do HubSpot
      description: Recebe os dados de contato criados no HubSpot.
      operationId: contactCreation
      requestBody:
        content:
          application/json:
            schema:
              type: array
              items:
                $ref: '#/components/schemas/HubspotWebhookDTO'
        required: true
      responses:
        '200':
          description: Lista de contatos recuperada com sucesso
  /v1/contacts:
    get:
      tags:
        - contact-controller
      summary: Listar contatos do HubSpot
      description: Obtém a lista de contatos do HubSpot.
      operationId: getContacts
      responses:
        '200':
          description: Lista de contatos recuperada com sucesso
          content:
            '*/*':
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/HubspotContactResponse'
    post:
      tags:
        - contact-controller
      summary: Criar um contato no HubSpot
      description: "Cria um novo contato\tna plataforma HubSpot."
      operationId: createContact
      requestBody:
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/HubspotContactDTO'
        required: true
      responses:
        '201':
          description: Contato criado com sucesso
          content:
            '*/*':
              schema:
                $ref: '#/components/schemas/HubspotContactResponse'
  /v1/auth/login:
    get:
      tags:
        - auth-controller
      summary: Realizar login na plataforma HubSpot
      description: Direciona para a plataforma HubSpot para login
      externalDocs:
        description: Essa URL deve ser acessada pelo navegador para retorno do callback
        url: http://localhost:8080/v1/auth/login
      operationId: loginHubSpot
      responses:
        '302':
          description: Redireciona para o HubSpot para autenticação
        '500':
          description: Erro ao tentar gerar a URL de login
components:
  schemas:
    HubspotWebhookDTO:
      type: object
      properties:
        eventId:
          type: integer
          format: int64
        subscriptionId:
          type: integer
          format: int64
        portalId:
          type: integer
          format: int64
        appId:
          type: integer
          format: int64
        occurredAt:
          type: integer
          format: int64
        subscriptionType:
          type: string
        attemptNumber:
          type: integer
          format: int32
        objectId:
          type: integer
          format: int64
        changeFlag:
          type: string
        changeSource:
          type: string
        sourceId:
          type: string
    HubspotContactDTO:
      type: object
      properties:
        id:
          type: string
        properties:
          type: object
          additionalProperties:
            type: object
    HubspotContactResponse:
      type: object
      properties:
        results:
          type: array
          items:
            $ref: '#/components/schemas/HubspotContactDTO'
```
