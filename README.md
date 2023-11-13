# Sistema de Gerenciamento de Votação em Assembleias Cooperativas

Esta é uma aplicação de backend dedicada ao gerenciamento eficaz de sessões de votação em assembleias cooperativas. 
Desenvolvida para proporcionar controle otimizado dessas sessões, a solução é acessível por meio de uma [API REST
hospedada na nuvem [https://coop-voting-backend.onrender.com]](https://coop-voting-backend.onrender.com). 
A aplicação oferece funcionalidades essenciais para a gestão de pautas, votos e resultados. 

## [Documentação da API](https://coop-voting-backend.onrender.com/swagger-ui/index.html)

## ✔️ Tecnologias utilizadas
- Java 17
- Spring Boot
- Spring Data JPA
- Testes automatizados
- JUnit5
- Mockito
- JaCoCo
- Maven
- PostgreSQL
- Flyway
- Docker

## Funcionalidades Principais

1. **Cadastro de Novo Associado:**
    - Endpoint: `POST /api/v1/associados`
    - Permite cadastrar um novo associado através do CPF.
    - Exemplo de JSON:
    ```json
    {
      "cpf": "841.587.143-03"
    }
    ```

2. **Cadastro de Nova Pauta:**
    - Endpoint: `POST /api/v1/pautas`
    - Permite cadastrar uma nova pauta para votação, sendo obrigatório informar a descrição da Pauta.
    - Exemplo de JSON:
    ```json
    {
      "descricao": "Pauta com descrição"
    }
    ```

3. **Abertura de Sessão de Votação:**
    - Endpoint: `POST /api/v1/sessoesvotacoes/`
    - Abre uma sessão de votação para uma pauta específica através do ID da pauta. Pode ser definido 
   um tempo customizado para o tempo de duração da sessão através do parâmetro 'tempoAberturaEmMinutos' ou 
   assume-se o valor padrão de 1 minuto.
    - Exemplo de JSON:
    ```json
    {
      "pauta" : {
        "id": 1
      }
    }
    ```

4. **Votação:**
    - Endpoint: `POST /api/v1/votos`
    - Permite que os associados registrem seus votos ('Sim' ou 'Não') em uma pauta, informando o ID da Pauta e o ID do Associado. Cada associado é identificado por um ID único e pode votar apenas uma vez por pauta.
    - Endpoint: `POST /api/v2/votos`
    - Nesse endpoint, antes de permitir o associado votar, é realizada uma integração com outro sistema
   que verifica, através do CPF do Associado, se o mesmo pode ou não votar.
    - Exemplo de JSON:
    ```json
    {
      "associado" : {
          "id": 1
      },
      "voto": "Não",
      "pauta" : {
          "id": 1
      }
    }
    ```

5. **Contabilização e Resultado da Votação:**
    - Endpoint: `GET /api/v1/resultados`
    - Retorna o resultado da votação para uma pauta específica através do parâmetro 'pautaId',
   e se a sessão de votação já estiver fechada, a mesma é encerrada, 
   permitindo abrir uma nova sessão para a pauta caso necessário.


### Integração com sistemas externos
- Na aplicação, é realizada a integração com um serviço externo, uma API que verifica, por meio do
CPF do associado, se o mesmo está apto para votar ou não. [URL: /api/v2/votos]
- Endpoint externo: `GET https://run.mocky.io/v3/57f23672-c15f-48f8-90d3-d84ce00250b8/users/`


### Versionamento de API
- Um padrão utilizado para o versionamento de APIs é o versionamento através da URI, como `/api/v1/votos` 
ou `/api/v2/votos`. Esse formato é útil para controlar mudanças na API, 
garantir compatibilidade com versões anteriores, facilitar a documentação e proporcionar uma transição
mais suave para os desenvolvedores quando uma nova versão é introduzida.
Como exemplo, conforme mencionado no tópico de integração com sistemas externos,
foi empregado o padrão de versionamento na URI para disponibilizar o voto sem integração com 
o serviço que valida se o CPF do associado está apto a votar ou não `[/api/v1/votos]`,
e a opção com integração na URL `[/api/v2/votos]`. 
Posteriormente, a versão 1 poderia ser depreciada, permitindo apenas 
o voto com integração, ou seja, a versão 2.


### Testes Unitários
- Para assegurar a qualidade do código e prevenir bugs em produção, 
foram implementados testes unitários no projeto, abrangendo diferentes partes do código.
Durante o desenvolvimento dos testes, foi dada ênfase especial às classes que envolvem a lógica de 
negócios (Services), resultando em uma cobertura de 100% em todas as classes de serviço da aplicação.
- Adicionalmente, para avaliar a eficácia dos testes, foi utilizada a ferramenta JaCoCo,
que fornece métricas de cobertura de código, permitindo uma análise detalhada da extensão
dos testes em relação ao código fonte. O JaCoCo é uma ferramenta amplamente empregada para
medir a cobertura de testes em projetos Java, auxiliando os desenvolvedores a garantir 
uma cobertura abrangente e eficiente.

  ![Cobertura_JaCoCo](https://github.com/matheushso/coop-voting-backend/assets/51098870/9c87d9a3-eacf-48a3-924d-1f3f23e1b63e)

- Além disso, com a implementação de testes automatizados,
é possível garantir a integridade do código, facilitando a identificação
precoce de erros e assegurando a qualidade da aplicação entregue ao usuário final.
Essa abordagem automatizada também simplifica a manutenção do código, permitindo alterações
e refatorações com maior segurança e eficiência.
