# SDL Probe 0.5 — comparação da identidade SDL

## Resultado confirmado da versão 0.4

O diagnóstico contém registro solicitado como DEFAULT com ID 8274106392, RegisterAppInterface SUCCESS, PutFile SUCCESS e SetAppIcon SUCCESS. Entre 14:01:08 e 14:01:48, permaneceu HMI NONE, sem seleção nem encerramento registrado. O usuário confirmou que não apareceu na central. O resultado é equivalente ao NAVIGATION com identidade experimental na versão 0.3. Isso não isola o problema à categoria de navegação. Não é correto dizer que a central não respondeu: ela respondeu às RPCs; o que não ocorreu foi exposição/ativação visível do aplicativo.

Idioma, acesso USB e atribuição do ícone já foram comprovados. A política de listagem/seleção da HMI e sua relação com a identidade do aplicativo continuam sem confirmação. Os registros do telefone não contêm a tabela interna de aplicativos da HMI.

## O que este teste muda

Mantém o protótipo Android separado, SDK público 4.12.2, PT-BR, NAVIGATION e ícone azul SDL. Muda apenas o conjunto de identificação no registro SDL em relação ao NAVIGATION da 0.3:

- appName: de SDL Probe para TomTom.
- App ID: de 8274106392 para 3610601450.

Os dois valores vêm da inspeção do APK fornecido. Não são chaves secretas nem cópia do certificado Android. O protótipo não altera o APK, pacote ou assinatura do TomTom e não implementa suas funções. É um teste de compatibilidade com identidade conhecida, não um cadastro autorizado junto ao fabricante.

O nome na central poderá ser **TomTom**, embora o aplicativo no telefone continue sendo **SDL Probe**. O ícone azul com letras SDL e, após ativação, as barras sintéticas permitem distingui-lo. A central pode guardar nome/ícone por ID; reconectar o TomTom original pode restaurar o ícone quando ele executar SetAppIcon. Não mantenha os dois aplicativos conectando ao mesmo tempo.

## Procedimento

1. Instale SDL-Probe-0.5.apk por cima da versão anterior.
2. Desconecte o cabo. Nas configurações do Android, abra Aplicativos → TomTom → Forçar parada. Não precisa desinstalar nem apagar os dados do TomTom.
3. Carro estacionado, freio de estacionamento acionado. Abra SDL Probe.
4. Mantenha marcada **Teste de identidade: TomTom / 3610601450**. O controle DEFAULT deve ficar desmarcado/desabilitado.
5. Conecte USB e escolha SDL Probe para acesso ao acessório. Se o Android abrir o TomTom original, encerre-o e corrija a associação USB antes de continuar.
6. Toque Conectar USB. Aguarde 15 segundos e atualize a lista da central.
7. Procure **TomTom com ícone azul SDL**, não um novo item chamado SDL Probe.
8. Se aparecer, selecione-o. Marque a confirmação de estacionamento no telefone e toque Testar vídeo e toque. O resultado esperado do protótipo são barras coloridas, cruzes e quadrado em movimento, não um mapa TomTom.
9. Se não aparecer, exporte o diagnóstico antes de desconectar. Envie o texto e diga se houve item TomTom antigo, mudança de ícone, ativação ou imagem.

## Como interpretar

- Só a identidade conhecida aparece: evidencia dependência de identidade/contexto associado na listagem. Não identifica se a regra usa nome, ID, ambos ou estado prévio da central. Precisaríamos separar esses fatores em ensaio posterior.
- Identidade conhecida registra com sucesso, mas continua oculta: identificação sozinha não reproduziu o comportamento. Será necessário comparar uma sessão real do TomTom com a sessão do protótipo, em vez de continuar mudando parâmetros sem evidência.
- APPLICATION_REGISTERED_ALREADY ou duplicidade: há sessão concorrente/residual; não interpretar como sucesso de projeção nem tentar manter duas sessões.
- FULL e vídeo: registrar ACK/NAK e imagem visível; aparecer no menu não comprova transmissão.

## Limites e verificação

Não há ativação forçada, alteração de política ou firmware nem desativação das condições normais de HMI. Vídeo continua condicionado a FULL e à confirmação de estacionamento. O teste não comprova vulnerabilidade e não garante que a PLAY+ aceite a identidade conhecida a partir de outro aplicativo.

Compilação e quatro testes de serialização/BMP passaram; assinatura verificada. A comunicação real deste modo ainda precisa de teste. Sem Android Auto, áudio ou microfone.
