# MVP - Sistema de Mercado de Economia Circular

## Descrição do Projeto

Este projeto é um Minimum Viable Product (MVP) desenvolvido para a disciplina de Projetos de Sistemas de Software. 
O sistema simula uma plataforma de mercado local para a comercialização de vestuário e acessórios usados, aplicando os princípios da economia circular.

As funcionalidades principais incluem o cadastro de usuários e itens, um catálogo de produtos com cálculo de preço dinâmico baseado em defeitos, e a implementação de indicadores ambientais como o GWP (Global Warming Potential) evitado e o MCI (Material Circularity Indicator). O sistema também conta com um mecanismo de reputação e gamificação para incentivar a participação e a confiança na plataforma.

---

## Membros da Equipe

*   André Peixoto Borlini - Matrícula: 2019201303
*   Lernardo Ramon Lima Carlos - Matrícula: 2016203686
*   Ana Beztriz Lombardi da Silva - Matrícula: 2017203902

---

## Funcionalidades Implementadas

*   **Dashboard de Início Rápido:** Interface de entrada com botões para as principais funcionalidades.
*   **Gerenciamento de Usuários (CRUD):** Cadastro, edição e exclusão de usuários.
*   **Gerenciamento de Itens (CRUD):** Cadastro de itens para venda, associando defeitos.
*   **Catálogo de Produtos:** Visualização de todos os itens disponíveis para venda.
*   **Sistema de Transações:** Lógica para simular a compra de um item.
*   **Sistema de Defeitos:** Módulo administrativo para gerenciar defeitos e seus respectivos descontos.
*   **Cálculo de Preço e Indicadores:** Cálculo automático do preço final (com descontos em cascata) e dos indicadores ambientais (GWP e MCI).
*   **Sistema de Reputação:** Pontuação de usuários (compradores e vendedores) baseada em suas ações (cadastro de itens, vendas concluídas).
*   **Visualização de Perfil:** Tela para o usuário visualizar seu nível de reputação.
*   **Log Configurável:** Geração de logs de eventos em formatos CSV ou JSON, selecionáveis em tempo de execução.

---

## Tecnologias e Padrões de Projeto

*   **Linguagem:** Java 17 (OpenJDK Temurin)
*   **Build Tool:** Apache Maven
*   **Interface Gráfica:** Java Swing (com padrão MDI)
*   **Banco de Dados:** SQLite
*   **Padrões de Projeto Utilizados:**
    *   Model-View-Presenter (MVP)
    *   Repository
    *   Adapter (para o sistema de Log)
    *   Singleton (para serviços como `LogService` e `SessaoService`)

---

## Como Executar o Projeto

### Pré-requisitos
*   JDK 17.
*   Apache Maven.
*   IDE Java.

### Passos para Execução

1.  **Clonar o Repositório**

2.  **Abrir na IDE:**
    *   Abra o IntelliJ IDEA ou sua IDE de preferência.
    *   Abra a pasta do projeto clonado.
    *   O IntelliJ deve reconhecer o arquivo `pom.xml` e configurar o projeto como um projeto Maven automaticamente. Aguarde ele baixar as dependências.

3.  **Executar a Aplicação:**
    *   Navegue até a classe principal no seguinte caminho:
      `src/main/java/br/ufes/dcomp/trabalhoProjetos/main/App.java`
    *   Clique com o botão direito no arquivo `App.java` e selecione **"Run 'App.main()'"**.

4.  **Observações:**
    *   O arquivo do banco de dados (`mvp-database.db`) será criado automaticamente na pasta raiz do projeto na primeira execução.
    *   Os arquivos de log (`sistema.log.csv` ou `sistema.log.json`) também serão criados na raiz do projeto conforme as ações forem sendo executadas.