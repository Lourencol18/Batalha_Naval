import java.io.File
import java.io.PrintWriter
fun tamanhoTabuleiroValido(numLinhas: Int, numColunas: Int): Boolean {
    return when {
        (numLinhas == 4 && numColunas ==4) ||
                (numLinhas == 5 && numColunas == 5) ||
                (numLinhas == 7 && numColunas == 7) ||
                (numLinhas == 8 && numColunas == 8) ||
                (numLinhas == 10 && numColunas == 10) -> true
        else -> false
    }
}
fun processaCoordenadas(coordenadas: String, numLinhas: Int, numColunas: Int): Pair<Int, Int>? {
    var contador = 0
    var numeroLinha = ""

    // Processa os caracteres até encontrar uma vírgula ou um caractere não numérico
    while (contador < coordenadas.length && coordenadas[contador] in '0'..'9') {
        numeroLinha += coordenadas[contador]
        contador++
    }

    // Converte a parte da linha para Int
    val linha = numeroLinha.toIntOrNull()

    // Verifica se a conversão é bem-sucedida e se está no intervalo válido
    if (linha == null || linha !in 1..numLinhas) {
        return null
    }

    // Verifica se há uma vírgula
    if (contador < coordenadas.length && coordenadas[contador] == ',') {
        contador++
    } else {
        return null
    }

    var letraColuna = ""

    // Processa os caracteres restantes para a coluna (pode ser uma letra)
    while (contador < coordenadas.length) {
        letraColuna += coordenadas[contador]
        contador++
    }

    // Converte a parte da coluna para Int
    val coluna = if (letraColuna.length == 1 && letraColuna[0] in 'A'..'Z') {
        letraColuna[0] - 'A' + 1
    } else {
        return null
    }

    // Verifica se a conversão é bem-sucedida e se está no intervalo válido
    if (coluna !in 1..numColunas) {
        return null
    }

    // Retorna o par de coordenadas se tudo estiver correto
    return Pair(linha, coluna)
}
fun criaLegendaHorizontal(numColunas: Int): String {
    // Verifica se o número de colunas está no intervalo válido (de 1 a 26)
    if (numColunas < 1 || numColunas > 26) {
        return ""
    }

    // Inicializa uma string vazia para armazenar a legenda
    var retorno = ""
    var contador = 0

    // Loop do-while para iterar sobre as colunas
    do {
        // Obtém a letra correspondente à coluna atual (A, B, C, ..., Z)
        val letra = ('A' + contador).toString()

        // Adiciona a letra à string de retorno
        retorno += letra

        // Adiciona um separador (" | ") se não for a última coluna
        if (contador < numColunas - 1) {
            retorno += " | "
        }

        // Incrementa o contador para passar para a próxima letra
        contador++
    } while (contador < numColunas)

    // Retorna a string de legenda gerada
    return retorno
}


fun calculaNumNavios(numLinhas: Int, numColunas: Int): Array<Int> {
    return when (numLinhas) {
        4 -> arrayOf(2, 0, 0, 0)
        5 -> arrayOf(1, 1, 1, 0)  // submarinos,contratorpederios,navios-tanque,porta-avioes
        7 -> arrayOf(2, 1, 1, 1)
        8 -> arrayOf(2, 2, 1, 1)
        10 -> arrayOf(3, 2, 1, 1)
        else -> arrayOf(0, 0, 0, 0)
    }
}
fun criaTabuleiroVazio(numLinhas: Int, numColunas: Int): Array<Array<Char?>> {
    // Verifica se as dimensões do tabuleiro são válidas (maiores que zero)
    if (numLinhas <= 0 || numColunas <= 0) {
        // Retorna um tabuleiro com uma posição contendo null se as dimensões não são válidas
        return arrayOf(arrayOf(null))
    }

    // Cria um tabuleiro (matriz) com as dimensões especificadas
    val tabuleiro = Array(numLinhas) { arrayOfNulls<Char?>(numColunas) }

    // Inicializa o índice da linha
    var linha = 0
    while (linha < numLinhas) {
        // Inicializa o índice da coluna
        var coluna = 0
        while (coluna < numColunas) {
            // Preenche cada posição da matriz com null
            tabuleiro[linha][coluna] = null
            coluna++
        }
        linha++
    }

    // Retorna o tabuleiro preenchido com nulls
    return tabuleiro
}



fun coordenadaContida(tabuleiroVazio: Array<Array<Char?>>, linha: Int, coluna: Int): Boolean {
    // Verifica se a linha e a coluna estão dentro dos limites do tabuleiro
    return linha in 1..tabuleiroVazio.size && coluna in 1..tabuleiroVazio[0].size
}
fun  limparCoordenadasVazias(coordenadas: Array<Pair<Int, Int>>): Array<Pair<Int, Int>> {
// Contar o número de pares não vazios
    var countNaoVazios = 0
    for (coordenada in coordenadas) {
        if (coordenada != Pair(0, 0)) {
            countNaoVazios++
        }
    }

    // Criar um novo array com tamanho igual ao número de pares não vazios
    val coordenadasNaoVazias = Array<Pair<Int, Int>>(countNaoVazios) { Pair(0, 0) }

    // Preencher o novo array apenas com os pares não vazios
    var index = 0
    var tamanho = 0
    while (tamanho < coordenadas.size) {
        val coordenada = coordenadas[tamanho]
        if (coordenada != Pair(0, 0)) {
            coordenadasNaoVazias[index] = coordenada
            index++
        }
        tamanho++
    }

    return coordenadasNaoVazias
}

fun juntarCoordenadas(coordenada1: Array<Pair<Int, Int>>, coordenada2: Array<Pair<Int, Int>>): Array<Pair<Int, Int>> {
    val coordenadasJuntas = (coordenada1 + coordenada2)
    return  coordenadasJuntas
}
fun gerarCoordenadasNavio(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, orientacao: String, dimensao: Int): Array<Pair<Int, Int>> {
    var resultado = emptyArray<Pair<Int, Int>>()

    // Caso para dimensão igual a 1
    if (dimensao == 1 && linha in 1..tabuleiro.size && coluna in 1..tabuleiro[0].size) {
        resultado += Pair(linha, coluna)
    } else if (dimensao in 2..4) {
        var coordenadasValidas = true

        // Loop para gerar coordenadas com base na orientação e dimensão
        for (i in 0 until dimensao) {
            val deltaLinha = if (orientacao == "N") -i else if (orientacao == "S") i else 0
            val deltaColuna = if (orientacao == "O") -i else if (orientacao == "E") i else 0

            val linhaAtual = linha + deltaLinha
            val colunaAtual = coluna + deltaColuna

            // Verifica se as coordenadas são válidas
            coordenadasValidas = coordenadasValidas &&
                    linhaAtual in 1..tabuleiro.size &&
                    colunaAtual in 1..tabuleiro[0].size

            // Adiciona as coordenadas ao resultado
            resultado += Pair(linhaAtual, colunaAtual)
        }

        // Se alguma coordenada não for válida, redefine o resultado como um array vazio
        if (!coordenadasValidas) {
            resultado = emptyArray()
        }
    }

    return resultado
}


fun gerarCoordenadasFronteira(
        tabuleiroVazio: Array<Array<Char?>>,
        linha: Int,
        coluna: Int,
        orientacao: String,
        dimensao: Int
): Array<Pair<Int, Int>> {

    val deslocamentoLinha: Int
    val deslocamentoColuna: Int

    // Define os deslocamentos com base na orientação
    when (orientacao) {
        "N" -> {
            deslocamentoLinha = -1
            deslocamentoColuna = 0
        }
        "S" -> {
            deslocamentoLinha = 1
            deslocamentoColuna = 0
        }
        "O" -> {
            deslocamentoLinha = 0
            deslocamentoColuna = -1
        }
        "E" -> {
            deslocamentoLinha = 0
            deslocamentoColuna = 1
        }
        else -> {
            deslocamentoLinha = 0
            deslocamentoColuna = 0
        }
    }

    var coordenadasNavio: Array<Pair<Int, Int>> = emptyArray()

    // Gera as coordenadas do navio com base na orientação e dimensão
    for (andar in 0 until dimensao) {
        val novaLinha = linha + deslocamentoLinha * andar
        val novaColuna = coluna + deslocamentoColuna * andar
        if (novaLinha in 1..tabuleiroVazio.size && novaColuna in 1..tabuleiroVazio[0].size) {
            coordenadasNavio += Pair(novaLinha, novaColuna)
        }
    }

    var coordenadasFronteira: Array<Pair<Int, Int>> = emptyArray()

    // Gera as coordenadas da fronteira
    for (coordenadaNavio in coordenadasNavio) {
        for (mexerLinha in -1..1) {
            for (mexerColuna in -1..1) {
                val linhaFronteira = coordenadaNavio.first + mexerLinha
                val colunaFronteira = coordenadaNavio.second + mexerColuna

                // Verifica se as coordenadas estão no tabuleiro e não são do próprio navio
                if (coordenadaContida(tabuleiroVazio, linhaFronteira, colunaFronteira) &&
                        !(mexerLinha == 0 && mexerColuna == 0)
                ) {
                    var contida = false

                    // Verifica se a coordenada está no navio
                    for (coordenadaExistente in coordenadasNavio) {
                        if (coordenadaExistente.first == linhaFronteira &&
                                coordenadaExistente.second == colunaFronteira
                        ) {
                            contida = true
                        }
                    }

                    // Adiciona a coordenada à fronteira se não estiver no navio
                    if (!contida) {
                        coordenadasFronteira += Pair(linhaFronteira, colunaFronteira)
                    }
                }
            }
        }
    }
    return coordenadasFronteira
}


fun estaLivre(tabuleiro: Array<Array<Char?>>, coordenadasVazias: Array<Pair<Int, Int>>): Boolean {
    // Verifica se as coordenadas estão dentro dos limites do tabuleiro
    for ((linha, coluna) in coordenadasVazias) {
        if (!coordenadaContida(tabuleiro, linha, coluna)) {
            return false
        }
    }

    // Verifica se as coordenadas estão em espaços vazios
    for ((linha, coluna) in coordenadasVazias) {
        if (tabuleiro[linha - 1][coluna - 1] != null) {
            return false
        }
    }


    // Todas as coordenadas estão livres
    return true
}

fun insereNavioSimples(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, dimensao: Int): Boolean {
    if (linha !in 1..tabuleiro.size || coluna !in 1..tabuleiro[0].size) return false

    val tiposNavio = charArrayOf('1', '2', '3', '4')

    // Verifica se as coordenadas estão livres
    for (i in 0 until dimensao) {
        val novaColuna = coluna - 1 + i
        if (novaColuna !in 0 until tabuleiro[0].size) {
            return false
        }
        if (tabuleiro[linha - 1][novaColuna] != null) {
            return false
        }
    }

    // Insere o navio se as coordenadas estiverem livres
    for (i in 0 until dimensao) {
        val novaColuna = coluna - 1 + i
        tabuleiro[linha - 1][novaColuna] = tiposNavio[dimensao - 1]
    }
    return true
}
fun insereNavio(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, orientacao: String, dimensao: Int): Boolean {
    // Gera as coordenadas do navio e suas fronteiras
    val coordenadasJuntas = juntarCoordenadas(
            gerarCoordenadasNavio(tabuleiro, linha, coluna, orientacao, dimensao),
            gerarCoordenadasFronteira(tabuleiro, linha, coluna, orientacao, dimensao)
    )

    // Verifica se as coordenadas estão livres
    if (!estaLivre(tabuleiro, coordenadasJuntas)) {
        return false
    }

    // Converte a dimensão do navio para o caractere correspondente
    val navio = when (dimensao) {
        1 -> '1'
        2 -> '2'
        3 -> '3'
        4 -> '4'
        else -> return false
    }

    // Insere o navio com base na orientação
    when (orientacao) {
        "N" -> {
            // Verifica se as coordenadas estão dentro dos limites do tabuleiro
            if (linha !in 1..tabuleiro.size || coluna !in 1..tabuleiro[0].size || linha - dimensao + 1 !in 1..tabuleiro.size) {
                return false
            }
            // Verifica se as posições estão livres
            for (i in 0 until dimensao) {
                if (tabuleiro[(linha - 1) - i][coluna - 1] != null) {
                    return false
                }
            }
            // Insere o navio
            for (i in 0 until dimensao) {
                tabuleiro[(linha - 1) - i][coluna - 1] = navio
            }
            return true
        }
        "S" -> {
            // Verifica se as coordenadas estão dentro dos limites do tabuleiro
            if (linha !in 1..tabuleiro.size || coluna !in 1..tabuleiro[0].size || linha + dimensao - 1 !in 1..tabuleiro.size) {
                return false
            }
            // Verifica se as posições estão livres
            for (i in 0 until dimensao) {
                if (tabuleiro[(linha - 1) + i][coluna - 1] != null) {
                    return false
                }
            }
            // Insere o navio
            for (i in 0 until dimensao) {
                tabuleiro[(linha - 1) + i][coluna - 1] = navio
            }
            return true
        }
        "E" -> return insereNavioSimples(tabuleiro, linha, coluna, dimensao)
        "O" -> {
            // Verifica se as coordenadas estão dentro dos limites do tabuleiro
            if (linha !in 1..tabuleiro.size || coluna !in 1..tabuleiro[0].size || coluna - dimensao + 1 !in 1..tabuleiro[0].size) {
                return false
            }
            // Verifica se as posições estão livres
            for (i in 0 until dimensao) {
                if (tabuleiro[linha - 1][(coluna - 1) - i] != null) {
                    return false
                }
            }
            // Insere o navio
            for (i in 0 until dimensao) {
                tabuleiro[linha - 1][(coluna - 1) - i] = navio
            }
            return true
        }
        else -> return false
    }
}


fun preencheTabuleiroComputador(tabuleiro: Array<Array<Char?>>, tamanho: Array<Int>): Array<Array<Char?>> {
    // Itera sobre os tamanhos dos navios, do maior para o menor
    for (tamanhonavioindex in tamanho.size - 1 downTo 0) {
        val dimensao = tamanhonavioindex + 1
        // Verifica se há navios do tamanho atual para colocar
        if (tamanho[tamanhonavioindex] > 0) {
            // Coloca a quantidade específica de navios do tamanho atual
            for (navioindex in 1..tamanho[tamanhonavioindex]) {
                var linha: Int
                var coluna: Int
                var orientacao: String

                // Gera coordenadas aleatórias até encontrar uma posição válida
                do {
                    linha = (1..tabuleiro.size).random()
                    coluna = (1..tabuleiro[0].size).random()
                    orientacao = arrayOf("N", "S", "E", "O").random()
                } while (!insereNavio(tabuleiro, linha, coluna, orientacao, dimensao))
                // Repete o processo até encontrar uma posição válida para o navio

                // Após encontrar uma posição válida, o navio é inserido no tabuleiro
            }
        }
    }
    return tabuleiro
}

fun navioCompleto(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int): Boolean {
    // Cria um par de coordenadas (linha, coluna) para facilitar o acesso ao tabuleiro
    val coordenada = Pair(linha - 1, coluna - 1)
// Verifica se as coordenadas estão dentro dos limites do tabuleiro
    if (!coordenadaContida(tabuleiro, linha, coluna)) {
        return false
    }
// Verifica o tipo de navio nas coordenadas e se está totalmente atingido
    return when (tabuleiro[coordenada.first][coordenada.second]) {
        '1' -> true // submarino de dimensão 1 está sempre totalmente atingido com um único tiro
        '2' ->  // Verifica padrões específicos para o navio de dimensão 2
            (coordenada.second + 1 < tabuleiro[0].size && tabuleiro[coordenada.first][coordenada.second + 1] == '2') ||
                (coordenada.second - 1 >= 0 && tabuleiro[coordenada.first][coordenada.second - 1] == '2') ||
                (coordenada.first + 1 < tabuleiro.size && tabuleiro[coordenada.first + 1][coordenada.second] == '2') ||
                (coordenada.first - 1 >= 0 && tabuleiro[coordenada.first - 1][coordenada.second] == '2')
        '3' ->  // Verifica padrões específicos para o navio de dimensão 3
            (coordenada.second + 2 < tabuleiro[0].size && tabuleiro[coordenada.first][coordenada.second + 1] == '3' &&
                tabuleiro[coordenada.first][coordenada.second + 2] == '3') ||
                (coordenada.second - 2 >= 0 && tabuleiro[coordenada.first][coordenada.second - 1] == '3' &&
                        tabuleiro[coordenada.first][coordenada.second - 2] == '3') ||
                (coordenada.first + 2 < tabuleiro.size && tabuleiro[coordenada.first + 1][coordenada.second] == '3' &&
                        tabuleiro[coordenada.first + 2][coordenada.second] == '3') ||
                (coordenada.first - 2 >= 0 && tabuleiro[coordenada.first - 1][coordenada.second] == '3' &&
                        tabuleiro[coordenada.first - 2][coordenada.second] == '3') ||
                (coordenada.first - 1 >= 0 && coordenada.first + 1 < tabuleiro.size &&
                        tabuleiro[coordenada.first - 1][coordenada.second] == '3' && tabuleiro[coordenada.first + 1][coordenada.second] == '3') ||
                (coordenada.second - 1 >= 0 && coordenada.second + 1 < tabuleiro[0].size && coordenada.first + 1 < tabuleiro.size &&
                        tabuleiro[coordenada.first][coordenada.second - 1] == '3' && tabuleiro[coordenada.first][coordenada.second + 1] == '3')
        '4' ->  // Verifica padrões específicos para o navio de dimensão 4
            (coordenada.second + 3 < tabuleiro[0].size && tabuleiro[coordenada.first][coordenada.second + 1] == '4' &&
                tabuleiro[coordenada.first][coordenada.second + 2] == '4' && tabuleiro[coordenada.first][coordenada.second + 3] == '4') ||
                (coordenada.second - 3 >= 0 && tabuleiro[coordenada.first][coordenada.second - 1] == '4' &&
                        tabuleiro[coordenada.first][coordenada.second - 2] == '4' && tabuleiro[coordenada.first][coordenada.second - 3] == '4') ||
                (coordenada.first + 3 < tabuleiro.size && tabuleiro[coordenada.first + 1][coordenada.second] == '4' &&
                        tabuleiro[coordenada.first + 2][coordenada.second] == '4' && tabuleiro[coordenada.first + 3][coordenada.second] == '4') ||
                (coordenada.first - 3 >= 0 && tabuleiro[coordenada.first - 1][coordenada.second] == '4' &&
                        tabuleiro[coordenada.first - 2][coordenada.second] == '4' && tabuleiro[coordenada.first - 3][coordenada.second] == '4') ||
                (coordenada.first - 1 >= 0 && coordenada.first + 2 < tabuleiro.size &&
                        tabuleiro[coordenada.first - 1][coordenada.second] == '4' && tabuleiro[coordenada.first + 1][coordenada.second] == '4' &&
                        tabuleiro[coordenada.first + 2][coordenada.second] == '4') ||
                (coordenada.first - 2 >= 0 && coordenada.first + 1 < tabuleiro.size &&
                        tabuleiro[coordenada.first - 2][coordenada.second] == '4' && tabuleiro[coordenada.first - 1][coordenada.second] == '4' &&
                        tabuleiro[coordenada.first + 1][coordenada.second] == '4') ||
                (coordenada.second - 1 >= 0 && coordenada.second + 2 < tabuleiro[0].size &&
                        tabuleiro[coordenada.first][coordenada.second - 1] == '4' && tabuleiro[coordenada.first][coordenada.second + 1] == '4' &&
                        tabuleiro[coordenada.first][coordenada.second + 2] == '4') ||
                (coordenada.second - 2 >= 0 && coordenada.second + 1 < tabuleiro[0].size &&
                        tabuleiro[coordenada.first][coordenada.second - 2] == '4' && tabuleiro[coordenada.first][coordenada.second - 1] == '4' &&
                        tabuleiro[coordenada.first][coordenada.second + 1] == '4')
        else -> false  // Se não for nenhum dos tipos de navio, retorna falso
    }
}
fun obtemMapa(tabuleiro: Array<Array<Char?>>, valor: Boolean): Array<String> {
    // Inicializa o array de strings que representará o mapa
    val resultado = Array<String>(tabuleiro.size + 1) { "" }
// Adiciona a legenda horizontal ao topo do mapa
    resultado[0] = "| ${criaLegendaHorizontal(tabuleiro[0].size)} |"

    // Itera sobre as linhas do tabuleiro para preencher o mapa
    for (linha in 1 until resultado.size) {
        // String que representa a linha do mapa
        var linhaMapa = ""
        // Itera sobre as colunas do tabuleiro
        for (coluna in 1 until tabuleiro[0].size + 1) {
            // Adiciona elementos ao mapa com base nas condições
            linhaMapa += when {
                valor -> when (tabuleiro[linha - 1][coluna - 1]) {
                    null -> "| ~ "
                    '1' -> "| 1 "
                    '2' -> "| 2 "
                    '3' -> "| 3 "
                    '4' -> "| 4 "
                    else -> ""
                }
                else -> when (tabuleiro[linha - 1][coluna - 1]) {
                    null -> "| ? "
                    'X' -> "| X "
                    '1' -> if (navioCompleto(tabuleiro, linha, coluna)) "| 1 " else "| \u2081 "
                    '2' -> if (navioCompleto(tabuleiro, linha, coluna)) "| 2 " else "| \u2082 "
                    '3' -> if (navioCompleto(tabuleiro, linha, coluna)) "| 3 " else "| \u2083 "
                    '4' -> if (navioCompleto(tabuleiro, linha, coluna)) "| 4 " else "| \u2084 "
                    else -> ""
                }
            }
        }
        // Adiciona a linha ao array resultado, incluindo o número da linha
        resultado[linha] = "$linhaMapa| $linha"
    }
    // Retorna o resultado, que é o mapa completo
    return resultado
}


fun lancarTiro(
        tabuleiroRealComputador: Array<Array<Char?>>,
        tabuleiroPalpitesDoHumano: Array<Array<Char?>>,
        coordenadas: Pair<Int, Int>
): String {
    // Obtém as coordenadas
    val linha = coordenadas.first
    val coluna = coordenadas.second

    // Atualiza o tabuleiro de palpites com 'X' na posição do tiro
    tabuleiroPalpitesDoHumano[linha - 1][coluna - 1] =
            when (tabuleiroRealComputador[linha - 1][coluna - 1]) {
                null -> 'X'
                else -> tabuleiroRealComputador[linha - 1][coluna - 1]!!
            }

    // Retorna a mensagem correspondente ao resultado do tiro
    return when (tabuleiroPalpitesDoHumano[linha - 1][coluna - 1]) {
        'X' -> "Agua."
        '1' -> "Tiro num submarino."
        '2' -> "Tiro num contra-torpedeiro."
        '3' -> "Tiro num navio-tanque."
        '4' -> "Tiro num porta-aviões."
        else -> ""
    }
}

fun geraTiroComputador(tabuleiroPalpitesComputador: Array<Array<Char?>>): Pair<Int, Int> {
    val tamanhoLinhas = tabuleiroPalpitesComputador.size
    val tamanhoColunas = tabuleiroPalpitesComputador[0].size

    var linha: Int
    var coluna: Int
// Gera coordenadas aleatórias até encontrar uma posição não atingida
    do {
        linha = (1..tamanhoLinhas).random()
        coluna = (1..tamanhoColunas).random()
    } while (tabuleiroPalpitesComputador[linha - 1][coluna - 1] != null)

    return Pair(linha, coluna)
}
fun contarNaviosDeDimensao(tabuleiro: Array<Array<Char?>>, dimensao: Int): Int {
    // Inicializa o contador
    var count = 0

    // Itera sobre as linhas do tabuleiro
    for (linha in 1..tabuleiro.size) {
        // Itera sobre as colunas do tabuleiro
        for (coluna in 1..tabuleiro[0].size) {
            // Obtém o caractere na célula atual
            val celula = tabuleiro[linha - 1][coluna - 1]

            // Verifica a dimensão do navio a ser contado
            when (dimensao) {
                0 -> if (celula in '1'..'4') {
                    // Se a célula contiver um navio da dimensão especificada e estiver totalmente atingido
                    if (navioCompleto(tabuleiro, linha, coluna)) {
                        count++
                    }
                }
                in 1..4 -> if (celula == dimensao.toString()[0]) {
                    // Se a célula contiver um navio da dimensão especificada e estiver totalmente atingido
                    if (navioCompleto(tabuleiro, linha, coluna)) {
                        count++
                    }
                }
            }
        }
    }

    // Se a dimensão for maior que zero, divide o contador pelo tamanho da dimensão
    if (dimensao > 0) {
        count /= dimensao
    }

    // Retorna o resultado do contador
    return count
}





fun venceu(tabuleiroPalpites: Array<Array<Char?>>): Boolean {
    val numeronavios = calculaNumNavios(tabuleiroPalpites.size, tabuleiroPalpites[0].size)

// Itera sobre as dimensões dos navios
    for (dimensao in numeronavios.indices) {
        // Calcula o número de navios restantes para cada dimensão
        val naviosquerestam = numeronavios[dimensao] - contarNaviosDeDimensao(tabuleiroPalpites, dimensao + 1)
        // Se ainda houver navios não afundados, o jogador não venceu
        if (naviosquerestam > 0) {
            return false  // Ainda há navios não afundados
        }
    }

    return true  // Todos os navios foram afundados, o jogador venceu
}
fun lerJogo (nomeFicheiro: String, tipoTabuleiro: Int): Array<Array<Char?>> {
    // Lê todas as linhas do arquivo de texto
    val ficheiroGravado = File(nomeFicheiro).readLines()
    // Extrai as dimensões do tabuleiro do cabeçalho do arquivo
    numLinhas = ficheiroGravado[0][0].digitToInt()
    numColunas = ficheiroGravado[0][2].digitToInt()
    // Calcula as linhas no arquivo que contêm a representação do tabuleiro
    val linhasNoFicheiro = 3 * tipoTabuleiro + 1 + numLinhas * (tipoTabuleiro- 1) until 3 * tipoTabuleiro + 1 + numLinhas * tipoTabuleiro
    // Cria um tabuleiro vazio com as dimensões lidas
    val tabuleiro = criaTabuleiroVazio(numLinhas,numColunas)
    // linhas no arquivo que contêm a representação do tabuleiro
    var linha = linhasNoFicheiro.min()
    while(linha in linhasNoFicheiro){
        var coluna = 0
        var adicionavirgulas = 0
        // caracteres na linha atual
        while (adicionavirgulas < numColunas && coluna < ficheiroGravado [linha].length) {
            when (ficheiroGravado[linha] [coluna]){
                ',' -> adicionavirgulas ++
                '1', '2', '3','4', 'X' -> tabuleiro[linha - linhasNoFicheiro.min()][adicionavirgulas] = ficheiroGravado[linha][coluna]
            }
            coluna++
        }
        linha++
    }
    // Retorna o tabuleiro lido do arquivo
    return tabuleiro
}

fun gravarTabuleiro(jogoGravado: PrintWriter, tabuleiro: Array<Array<Char?>>){
    for(linha in  0 until tabuleiro.size) {
        jogoGravado.println()
        for (coluna in 0 until tabuleiro[0].size) {
            when (tabuleiro[linha][coluna]) {
                'X' -> jogoGravado.print("X")
                '1', '2', '3', '4' -> jogoGravado.print(tabuleiro[linha][coluna])
            }
            if (coluna < tabuleiro[0].size - 1) {
                jogoGravado.print(",")
            }

        }
    }
}
fun gravarJogo(nomeDoFicheiro: String, tabuleiroRealHumano: Array<Array<Char?>>, tabuleiroPalpitesHumano: Array<Array<Char?>>,
               tabuleiroRealComputador: Array<Array<Char?>>, tabuleiroPalpitesDoComputador: Array<Array<Char?>>){
    val jogoGravado = File(nomeDoFicheiro).printWriter()
    jogoGravado.print("${tabuleiroRealHumano.size},${tabuleiroRealHumano[0].size}")
    jogoGravado.print("\n\nJogador\nReal")
    gravarTabuleiro(jogoGravado, tabuleiroRealHumano)
    jogoGravado.print("\n\nJogador\nPalpites")
    gravarTabuleiro(jogoGravado, tabuleiroPalpitesHumano)
    jogoGravado.print("\n\nComputador\nReal")
    gravarTabuleiro(jogoGravado, tabuleiroRealComputador)
    jogoGravado.print("\n\nComputador\nPalpites")
    gravarTabuleiro(jogoGravado, tabuleiroPalpitesDoComputador)
    jogoGravado.close()
    return
}
fun calculaEstatisticas(tabuleiroPalpites: Array<Array<Char?>>): Array<Int> {
    // Inicializa as variáveis para contar estatísticas
    var totalJogadas = 0
    var tirosCertos = 0
    var naviosAfundados = 0

    // Itera sobre as linhas do tabuleiro de palpites
    for (linha in 0 until tabuleiroPalpites.size) {
        // Itera sobre as colunas do tabuleiro de palpites
        for (coluna in 0 until tabuleiroPalpites[linha].size) {
            // Obtém o palpite na célula atual
            val palpite = tabuleiroPalpites[linha][coluna]

            // Verifica se há um palpite na célula
            if (palpite != null) {
                // Incrementa o contador total de jogadas
                totalJogadas++

                // Verifica se o palpite foi um acerto e se o navio está totalmente afundado
                if (palpite != 'X' && navioCompleto(tabuleiroPalpites, linha + 1, coluna + 1)) {
                    // Incrementa os contadores de tiros certos e navios afundados
                    tirosCertos++
                    naviosAfundados++
                } else if (palpite != 'X') {
                    // Incrementa apenas o contador de tiros certos
                    tirosCertos++
                }
            }
        }
    }

    // Retorna um array contendo as estatísticas calculadas
    return arrayOf(totalJogadas, tirosCertos, naviosAfundados)
}

fun calculaNaviosFaltaAfundar(tabuleiroPalpites: Array<Array<Char?>>): Array<Int> {
    // Calcula o número total de navios no jogo com base no tamanho do tabuleiro
    val numNavios = calculaNumNavios(tabuleiroPalpites.size, tabuleiroPalpites[0].size)

    // Inicializa um array para contar quantas partes de cada tipo de navio foram atingidas
    val naviosAtingidos = Array(4) { 0 }

    // Itera sobre as linhas do tabuleiro de palpites
    for (linha in tabuleiroPalpites) {
        // Itera sobre os elementos (colunas) de cada linha
        for (elemento in linha) {
            // Incrementa o contador de partes atingidas para cada tipo de navio
            when (elemento) {
                '1' -> naviosAtingidos[0]++
                '2' -> naviosAtingidos[1]++
                '3' -> naviosAtingidos[2]++
                '4' -> naviosAtingidos[3]++
            }
        }
    }

    // Calcula a quantidade de partes de cada tipo de navio que ainda precisam ser afundadas
    val portaAvioesAfundar = numNavios[3] - naviosAtingidos[3]
    val naviosTanqueAfundar = numNavios[2] - naviosAtingidos[2]
    val contratorpedeirosAfundar = numNavios[1] - naviosAtingidos[1]
    val submarinosAfundar = numNavios[0] - naviosAtingidos[0]

    // Retorna um array contendo a quantidade de partes de cada tipo de navio que ainda precisam ser afundadas
    return arrayOf(
            if (portaAvioesAfundar > 0) portaAvioesAfundar else 0,
            if (naviosTanqueAfundar > 0) naviosTanqueAfundar else 0,
            if (contratorpedeirosAfundar > 0) contratorpedeirosAfundar else 0,
            if (submarinosAfundar > 0) submarinosAfundar else 0
    )
}




var numLinhas = -1
var numColunas = -1

var tabuleiroHumano: Array<Array<Char?>> = emptyArray()
var tabuleiroComputador: Array<Array<Char?>> = emptyArray()

var tabuleiroPalpitesDoHumano: Array<Array<Char?>> = emptyArray()
var tabuleiroPalpitesDoComputador: Array<Array<Char?>> = emptyArray()


const val MENU_PRINCIPAL = -1
const val MENU_DEFINIR_TABULEIRO_E_NAVIOS = 1
const val MENU_JOGAR = 2
const val MENU_LER_FICHEIRO = 4
const val MENU_GRAVAR_FICHEIRO = 3
const val SAIR = 0

fun main() {
    val frase = "!!! Tem que primeiro definir o tabuleiro do jogo, tente novamente"
    val frase2 = "\n> > Batalha Naval < <\n"
    fun exibirMenuPrincipal(): Int {
        println(frase2)
        println("1 - Definir Tabuleiro e Navios")
        println("2 - Jogar")
        println("3 - Gravar")
        println("4 - Ler")
        println("0 - Sair\n")
        do {
            val opcao = readlnOrNull()?.toIntOrNull()

            when (opcao) {
                1 -> return MENU_DEFINIR_TABULEIRO_E_NAVIOS
                2 -> return MENU_JOGAR
                3 -> return MENU_GRAVAR_FICHEIRO
                4 -> return MENU_LER_FICHEIRO
                0 -> return 0
                null -> println("!!! Opcao invalida, tente novamente")
                else -> println("!!! Opcao invalida, tente novamente")

            }
        } while (opcao != 0)
        return exibirMenuPrincipal()
    }

    fun menuDefinirTabuleiroENavios(): Int {
        if (numLinhas == 4) {
            println("Tabuleiros ja foram definidos")
            return MENU_PRINCIPAL }
        println("$frase2\nDefina o tamanho do tabuleiro:\nQuantas linhas?")
        var linhas: Int?
        do {
            linhas = readlnOrNull()?.toIntOrNull()
            if (linhas == -1) {
                return MENU_PRINCIPAL }
            while (linhas == null) {
                println(frase)
                linhas = readlnOrNull()?.toIntOrNull() }
        } while (linhas == null)
        println("Quantas colunas?")
        var colunas: Int?
        do {
            colunas = readlnOrNull()?.toIntOrNull()
            if (colunas == -1) {
                return MENU_PRINCIPAL }
            while (colunas == null) {
                println(frase)
                colunas = readlnOrNull()?.toIntOrNull() }
        } while (colunas == null)
        if (!tamanhoTabuleiroValido(linhas, colunas)) {
            println("Tamanho do tabuleiro inválido")
            return MENU_PRINCIPAL }
        numLinhas = linhas
        numColunas= colunas
        tabuleiroComputador = criaTabuleiroVazio(numLinhas, numColunas)
        tabuleiroHumano = criaTabuleiroVazio(linhas,colunas)
        tabuleiroPalpitesDoComputador = criaTabuleiroVazio(numLinhas, numColunas)
        tabuleiroPalpitesDoHumano= criaTabuleiroVazio(numLinhas, numColunas)
        for (linha in obtemMapa(tabuleiroHumano, true)) {
            println(linha) }
        for (i in 1..2) { println("Insira as coordenadas de um submarino:\nCoordenadas? (ex: 6,G)")
            var coordenadasSubmarino: Pair<Int, Int>? = null
            while (coordenadasSubmarino == null) { val coordenadasInput = readlnOrNull() ?: ""
                val coordenadasProcessadas = processaCoordenadas(coordenadasInput, linhas, colunas)
                if (coordenadasProcessadas != null) { coordenadasSubmarino = coordenadasProcessadas
                    if (tabuleiroHumano[coordenadasSubmarino.first - 1][coordenadasSubmarino.second - 1] != null) {
                        println("!!! Posicionamento invalido, tente novamente")
                        coordenadasSubmarino = null
                    } else { val sucessoInsercao = insereNavio(
                                tabuleiroHumano, coordenadasSubmarino.first,
                                coordenadasSubmarino.second, "N", 1
                        )
                        if (!sucessoInsercao) { println("!!! Posicionamento invalido, tente novamente")
                            coordenadasSubmarino = null
                        } else { for (linha in obtemMapa(tabuleiroHumano, true)) {
                                println(linha) } } } } else { println("Coordenadas inválidas, tente novamente.") } } }
        println("Pretende ver o mapa gerado para o Computador? (S/N)")
        var resposta: String?
        do { resposta = readlnOrNull()?.toUpperCase()
            when (resposta) {"S" -> { tabuleiroComputador = criaTabuleiroVazio(4, 4)
                    preencheTabuleiroComputador(tabuleiroComputador, calculaNumNavios(numLinhas, numColunas)
                    )
                    for (linha in obtemMapa(tabuleiroComputador, true)) {
                        println(linha)
                    }
                    return MENU_PRINCIPAL
                }"N" -> { tabuleiroComputador = criaTabuleiroVazio(4, 4)
                    preencheTabuleiroComputador(tabuleiroComputador, calculaNumNavios(numLinhas, numColunas)
                    )
                    return MENU_PRINCIPAL
                }"-1" -> { return MENU_PRINCIPAL
                }else -> { println("Resposta inválida. Responda com S (Sim) ou N (Não).")
                } } } while (!(resposta == "S" || resposta == "N"))
                return MENU_PRINCIPAL }
    fun menuJogar(): Int {
        if (numLinhas == -1) {
            println(frase)
            return MENU_PRINCIPAL
        } else {
            var gameOver = false
            var coordenadas = ""

            while (!gameOver) {
                val tabuleiroFeito = obtemMapa(tabuleiroPalpitesDoHumano, false)

                // Verifica se a entrada é "?" antes de imprimir o mapa
                if (coordenadas != "?") {
                    for (linha in 0 until tabuleiroFeito.size) {
                        println(tabuleiroFeito[linha])
                    }
                }

                println("Indique a posição que pretende atingir\nCoordenadas? (ex: 6,G)")
                coordenadas = readln()

                when {
                    coordenadas == "-1" -> return MENU_PRINCIPAL
                    coordenadas == "?" -> {
                        val naviosFalta = calculaNaviosFaltaAfundar(tabuleiroPalpitesDoHumano)
                        println("Falta afundar: ${naviosFalta[3]} submarino(s)")
                    }
                    else -> {
                        var coordenadasProcessadas = processaCoordenadas(coordenadas, numLinhas, numColunas)

                        while (coordenadasProcessadas == null) {
                            println("!!! Posicionamento inválido, tente novamente\nIndique a posição que pretende atingir\nCoordenadas? (ex: 6,G)")
                            coordenadas = readln()
                            if (coordenadas == "-1") {
                                return MENU_PRINCIPAL
                            }
                            coordenadasProcessadas = processaCoordenadas(coordenadas, numLinhas, numColunas)
                        }

                        var alvo = lancarTiro(tabuleiroComputador, tabuleiroPalpitesDoHumano, coordenadasProcessadas)

                        if (!navioCompleto(tabuleiroPalpitesDoHumano, coordenadasProcessadas.first, coordenadasProcessadas.second)) {
                            println(">>> HUMANO >>>$alvo")
                        } else println(">>> HUMANO >>>$alvo Navio ao fundo!")

                        if (!venceu(tabuleiroPalpitesDoComputador) && !venceu(tabuleiroPalpitesDoHumano)) {
                            val tiroComputador = geraTiroComputador(tabuleiroPalpitesDoComputador)
                            println("Computador lançou tiro para a posição $tiroComputador")
                            alvo = lancarTiro(tabuleiroHumano, tabuleiroPalpitesDoComputador, tiroComputador)

                            if (!navioCompleto(tabuleiroPalpitesDoComputador, tiroComputador.first, tiroComputador.second)) {
                                println(">>> COMPUTADOR >>>$alvo")
                            } else println(">>> COMPUTADOR >>>$alvo Navio ao fundo!")

                            println("Prima enter para continuar")
                            readln()
                        }

                        gameOver = venceu(tabuleiroPalpitesDoHumano) || venceu(tabuleiroPalpitesDoComputador)
                    }
                }
            }

            if (venceu(tabuleiroPalpitesDoHumano)) {
                println("PARABENS! Venceu o jogo!\nPrima enter para voltar ao menu principal")
            } else {
                println("OPS! O computador venceu o jogo!\nPrima enter para voltar ao menu principal")
            }

            readln()
            return MENU_PRINCIPAL
        }
    }







    fun menuLerFicheiro(): Int {


        println("Introduza o nome do ficheiro (ex: jogo.txt)")
        val ficheiro = readln()
        if (ficheiro == "-1"){
            return MENU_PRINCIPAL
        }
        tabuleiroHumano = lerJogo(ficheiro,1)
        tabuleiroPalpitesDoHumano = lerJogo(ficheiro,2)
        tabuleiroComputador = lerJogo(ficheiro,3)
        tabuleiroPalpitesDoComputador = lerJogo(ficheiro,4)
        println("Tabuleiro ${numLinhas}x$numColunas lido com sucesso")
        val tabuleiroFeito = obtemMapa(tabuleiroHumano,true)
        for (linha in 0 until tabuleiroFeito.size){
            println(tabuleiroFeito[linha])
        }
        return MENU_PRINCIPAL
    }

    fun menuGravarFicheiro(): Int {
        if (numLinhas == -1) {
            println(frase)
            return MENU_PRINCIPAL
        }

        println("Introduza o nome do ficheiro (ex: jogo.txt)")
        val ficheiro = readln().toString()
        if (ficheiro == "-1"){
            return MENU_PRINCIPAL
        }
        gravarJogo(ficheiro,tabuleiroHumano,tabuleiroPalpitesDoHumano,tabuleiroComputador,tabuleiroPalpitesDoComputador)
        println("Tabuleiro ${numLinhas}x$numColunas gravado com sucesso")
        return MENU_PRINCIPAL
    }

    var menuAtual = MENU_PRINCIPAL

    while (true) {
        menuAtual = when (menuAtual) {
            MENU_PRINCIPAL -> exibirMenuPrincipal()
            MENU_DEFINIR_TABULEIRO_E_NAVIOS -> menuDefinirTabuleiroENavios()
            MENU_JOGAR -> menuJogar()
            MENU_LER_FICHEIRO -> menuLerFicheiro()
            MENU_GRAVAR_FICHEIRO -> menuGravarFicheiro()
            SAIR -> return
            else -> return
        }
    }
}


