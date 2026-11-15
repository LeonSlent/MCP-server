# MCP Client/Server - Ferramentas Utilitárias

Projeto Java (Maven) que disponibiliza **duas aplicações no mesmo projeto**:

- **Servidor MCP** (transporte STDIO) com 5 ferramentas (*tools*);
- **Cliente MCP** interativo, que lista e executa qualquer ferramenta do servidor.

Não usa Spring, Lombok nem qualquer biblioteca que gere código.

## Requisitos

- JDK 25 (definido no `pom.xml`) com `java` no `PATH`
- Apache Maven 3.9+
- Para o MCP Inspector: Node.js (`npx`)
- Para o Claude Desktop: o aplicativo instalado

## Ferramentas do servidor

Todas as ferramentas retornam **texto**. Erros de entrada (parâmetro ausente,
valor inválido, etc.) **não derrubam o servidor**: a resposta MCP volta com
`isError = true` e uma mensagem explicando o problema.

### `calcular_imc`

Calcula o Índice de Massa Corporal e a classificação.

| Parâmetro  | Tipo   | Obrigatório | Descrição                  |
|------------|--------|-------------|----------------------------|
| `peso_kg`  | number | sim         | Peso em kg (maior que 0)   |
| `altura_m` | number | sim         | Altura em metros (0 a 3)   |

- **Resultado:** `IMC: 22.86 - Peso normal`
- **Classificações:** Abaixo do peso (< 18,5), Peso normal (< 25), Sobrepeso (< 30),
  Obesidade grau I (< 35), grau II (< 40), grau III (≥ 40).
- **Erros:** parâmetro ausente ou não numérico; peso ≤ 0; altura ≤ 0 ou > 3 m.

### `converter_unidade`

Converte entre unidades de comprimento, massa e temperatura.

| Parâmetro         | Tipo   | Obrigatório | Descrição                       |
|-------------------|--------|-------------|---------------------------------|
| `valor`           | number | sim         | Valor a converter               |
| `unidade_origem`  | string | sim         | Unidade de origem (ex.: `km`)   |
| `unidade_destino` | string | sim         | Unidade de destino (ex.: `mi`)  |

- **Unidades:** comprimento `m, km, cm, mi, ft`; massa `kg, g, lb`;
  temperatura `c, f, k` (maiúsculas ou minúsculas).
- **Resultado:** `1.0000 km = 1000.0000 m`
- **Erros:** parâmetro ausente; valor não numérico; unidade desconhecida;
  conversão entre categorias diferentes (ex.: `kg` para `m`).

### `gerar_senha`

Gera uma senha aleatória (`SecureRandom`) com ao menos uma minúscula, uma
maiúscula e um número (e um símbolo, se pedido).

| Parâmetro          | Tipo    | Obrigatório | Descrição                         |
|--------------------|---------|-------------|-----------------------------------|
| `tamanho`          | integer | sim         | Tamanho da senha (8 a 64)         |
| `incluir_simbolos` | boolean | não         | Inclui `!@#$%&*?-_` (padrão: não) |

- **Resultado:** somente a senha, ex.: `aB3dE9fGh2Jk`
- **Erros:** `tamanho` ausente, não inteiro ou fora de 8–64; `incluir_simbolos`
  que não seja `true`/`false`.

### `validar_cpf`

Verifica um CPF pelos dois dígitos verificadores.

| Parâmetro | Tipo   | Obrigatório | Descrição                               |
|-----------|--------|-------------|-----------------------------------------|
| `cpf`     | string | sim         | CPF com ou sem pontuação                |

- **Resultado:** `CPF válido.` ou `CPF inválido.` (inclui tamanho incorreto e
  dígitos repetidos, como `111.111.111-11`).
- **Erros:** `cpf` ausente ou em branco.
- **Exemplo válido:** `529.982.247-25`

### `contar_palavras`

Conta palavras, caracteres e linhas de um texto.

| Parâmetro | Tipo   | Obrigatório | Descrição            |
|-----------|--------|-------------|----------------------|
| `texto`   | string | sim         | Texto a ser analisado|

- **Resultado:** `Palavras: 3 | Caracteres: 15 | Caracteres sem espaços: 13 | Linhas: 1`
- **Erros:** `texto` ausente ou em branco.

## Arquitetura e boas práticas

```
br.edu.ifpr.mcp_server
├── main     McpServer01SyncStdio, McpClient01SyncStdio (pontos de entrada)
├── mcp
│   ├── utils    McpToolDefinition, AbstractMcpTool, ToolArguments, ToolSchema
│   └── tools    BmiTool, UnitConverterTool, PasswordGeneratorTool,
│                CpfValidatorTool, WordCounterTool, ToolCatalog
└── domain   Unit, CpfValidator (regras de negócio, sem dependência do MCP)
```

- **Template Method** (`AbstractMcpTool`): registro, montagem da resposta e
  tratamento de erro ficam em um só lugar; cada ferramenta só define nome,
  parâmetros e lógica.
- **Factory** (`ToolCatalog`): único ponto que conhece as ferramentas.
- **S** (responsabilidade única): uma classe por ferramenta; validação de
  argumentos em `ToolArguments`; esquema JSON em `ToolSchema`; regras em `domain`.
- **O** (aberto/fechado): nova ferramenta = nova classe + uma linha em
  `ToolCatalog`; nova unidade = uma linha em `Unit`.
- **L / I**: toda ferramenta pode ser usada como `McpToolDefinition`; a interface é mínima.
- **D**: `PasswordGeneratorTool` recebe o gerador aleatório (`Random`) por construtor.
- **DRY / KISS / YAGNI**: sem código repetido entre ferramentas, sem recursos
  desnecessários.

## Builds

Todos os comandos na raiz do projeto.

| Objetivo                                      | Comando                          |
|-----------------------------------------------|----------------------------------|
| Compilar e rodar **testes unitários**         | `mvn clean test`                 |
| Gerar o **servidor** (sem testes)             | `mvn -Pserver clean package -DskipTests` |
| Gerar o servidor + testes **unitários e de integração** | `mvn -Pserver clean verify` |
| Gerar o **cliente**                           | `mvn -Pclient clean package`     |

- Os **testes unitários** (`*Test`) rodam na fase `test` (Surefire).
- Os **testes de integração** (`*IT`) sobem o servidor real e executam as
  ferramentas pelo protocolo MCP (Failsafe). Precisam do JAR do servidor,
  por isso só rodam com `-Pserver verify`.
- Artefato do servidor: `server/mcp_server01_server.jar` (dependências em `server/libs`).
- Artefato do cliente: `target/dist-client/mcp_server01_client.jar` (dependências em `target/dist-client/libs`).

## Executando o cliente MCP

O cliente procura o servidor em `../../server/mcp_server01_server.jar`,
então execute-o de dentro de `target/dist-client`:

```
mvn -Pserver clean package -DskipTests
mvn -Pclient clean package -DskipTests
cd target/dist-client
java -jar mcp_server01_client.jar
```

Digite o número ou o nome da ferramenta, informe os parâmetros pedidos,
`listar` para atualizar a lista e `sair` para encerrar.

## Usando no MCP Inspector

Com o JAR do servidor gerado:

```
npx @modelcontextprotocol/inspector java -jar CAMINHO/server/mcp_server01_server.jar
```

Abra o endereço mostrado no terminal, clique em **Connect**, aba **Tools**,
**List Tools** e execute cada ferramenta.

## Usando no Claude Desktop

1. Gere o JAR do servidor (`mvn -Pserver clean package -DskipTests`).
2. No Claude Desktop: **Settings > Developer > Edit Config** (abre o arquivo
   `claude_desktop_config.json`).
3. Acrescente o servidor (use o caminho **absoluto** do JAR; no Windows use `\\`):

```json
{
  "mcpServers": {
    "ferramentas-utilitarias": {
      "command": "java",
      "args": ["-jar", "C:\\CAMINHO\\projeto\\server\\mcp_server01_server.jar"]
    }
  }
}
```

4. Feche o Claude Desktop por completo e abra de novo.
5. Peça, por exemplo: *"Calcule o IMC de 70 kg e 1,75 m"* ou *"Valide o CPF 529.982.247-25"*.

Outros assistentes de IA que aceitem servidores MCP via STDIO usam a mesma
configuração: `command` = `java` e `args` = `-jar` + caminho do JAR.

Antes de rodar mvn -Pserver clean verify, feche o Claude Desktop e o MCP Inspector, pois eles mantêm o JAR do servidor em uso e impedem o clean
