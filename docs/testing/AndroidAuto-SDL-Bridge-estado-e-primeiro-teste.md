# Android Auto → SDL: execução inicial

## O que já sabemos e foi testado

- O TomTom original 3.1.38 LATAM conecta normalmente ao Toyota PLAY+ do Yaris 2019. A central mostra a interface e recebe toques; há um desvio entre a imagem e a posição do toque que já ocorre no app original.
- O SDL Probe 0.5 apareceu na central com identidade TomTom, recebeu HMI FULL e negociou vídeo NAV não criptografado em 800×480. O teste registrou 30 eventos de toque; a configuração usada foi 30 fps e 1,5 Mbit/s. Os 691 ACKs observados em cerca de 35 s não são uma medição de FPS.
- Isso valida a ligação USB/AOA, a identidade SDL, a autorização e a saída SDL de vídeo no PLAY+. Ainda não valida quadros recebidos do Android Auto.

## Execução iniciada

Foi criada uma variante experimental do Open Headunit, sem alterar o APK TomTom:

1. **Sessão SDL:** usa a conexão USB AOA e a combinação App ID 3610601450 / nome TomTom que já apareceu na central. A tela inicial adiciona o botão “SDL bridge” e pede confirmação de veículo parado antes de armar a saída.
2. **Vídeo:** o decodificador Android Auto pode apontar diretamente para a Surface de entrada do encoder H.264 NAV SDL. A criação do encoder ocorre fora da thread da interface. O alvo volta à tela local quando a sessão SDL deixa de estar pronta.
3. **Toque:** eventos SDL BEGIN/MOVE/END/CANCEL são convertidos para eventos de toque AAP e escalados entre a resolução SDL e a resolução Android Auto negociada.
4. **Ciclo de vida:** a sessão SDL fica no escopo do processo, enquanto superfícies e encoder seguem a vida da projeção; há bloqueio pelo estado HMI e por distração do motorista.
5. **Instalação separada:** o pacote de depuração é `br.local.aasdlbridge`, para não substituir o Open Headunit da Play Store. A primeira compilação ainda herdava o nome fixo da permissão de assinatura do app original, o que podia impedir os dois instalados juntos; a versão 0.2 dá à permissão o identificador exclusivo do pacote.

## Marcos do plano

| Marco | Entrega | Estado |
|---|---|---|
| M0 — consolidar evidência existente | TomTom SDL e SDL Probe 0.5 no PLAY+ | Concluído e validado no carro |
| M1 — registrar a sessão SDL no app receptor | USB/AOA, autorização, identidade SDL já comprovada e confirmação de carro parado | Implementado e compilado |
| M2 — encaminhar imagem Android Auto | Surface do decoder AAP ligada ao encoder H.264 NAV SDL | Implementado e compilado; falta validar no carro |
| M3 — devolver toques à origem Android Auto | Eventos SDL convertidos e escalados para AAP | Implementado e compilado; falta validar posição e resposta |
| M4 — ensaio de coexistência | Self Mode/Headunit Server + USB SDL no mesmo telefone e PLAY+ | Pendente de ensaio físico |
| M5 — estabilizar experiência | Corrigir escala/deslocamento, medir fluidez/latência, recuperar após desconexão | Depois de M4 |
| M6 — completar funções | Áudio de navegação/voz, ajustes de produto e build de distribuição | Depois de M5 |

O projeto compilou e gerou um APK de depuração. A verificação de assinatura confirmou os esquemas v1 e v2. O empacotamento imprimiu avisos internos do D8 ao reescrever metadados Kotlin, mas terminou com sucesso. Não foi instalado em telefone nem testado na central nesta etapa.

### Correção 0.2 após o relato de instalação

A 0.1 tinha `applicationId` próprio, mas ainda declarava a permissão de assinatura `com.andrerinas.openheadunit.permission.NAVIGATION_UPDATE`, já pertencente ao app original. Esse conflito é a explicação provável para o Android ter exigido a desinstalação do Open Headunit original. A 0.2 usa `${applicationId}.permission.NAVIGATION_UPDATE` e versão 122. O manifesto compilado confirma a permissão `br.local.aasdlbridge.permission.NAVIGATION_UPDATE`. Isso corrige o conflito de instalação; não explica ainda a mensagem de erro ao abrir. Não há telefone conectado por ADB para capturar o crash.

### Correção 0.3 após reproduzir o fechamento

O fechamento foi reproduzido no emulador Android 16. O stack trace apontou `HomeFragment.inflateHomeContent`, na busca do botão SDL. O telefone/emulador escolheu `layout-port/fragment_home.xml`, variante que não continha esse botão. A 0.3 acrescenta o tile também ao layout de retrato e torna a referência nula-segura. Instalei a 0.3 no emulador: o processo permaneceu ativo, a tela retomada foi `OnboardingActivity` e não houve `FATAL EXCEPTION` no AndroidRuntime. Isso verifica apenas a inicialização; o teste no Yaris continua pendente.

## Primeiro ensaio no Yaris — sempre parado

1. Instale o APK de depuração ao lado dos demais apps; mantenha o TomTom original fechado enquanto a ponte usa a mesma identidade SDL.
2. Com o Yaris estacionado e o freio de estacionamento acionado, conecte o telefone à USB que já funcionou com o TomTom. Abra a variante “SDL bridge”, toque no botão novo e confirme a caixa de segurança.
3. Aceite a permissão USB/AOA do Android e selecione **TomTom** na lista de apps da central. Confirme que a sessão chegou a HMI FULL.
4. No mesmo telefone, inicie o Android Auto pelo fluxo Self Mode / Headunit Server do Open Headunit. Em versões Android Auto 17.4 ou posteriores, o fluxo sem root pode exigir “Start Headunit Server” nas opções de desenvolvedor do Android Auto.
5. Observe se a interface Android Auto aparece na central. Toque somente em um controle conhecido e parado; confirme tanto o evento no telefone quanto a resposta do app Android Auto.
6. Se não aparecer vídeo ou toque, encerre o fluxo parado e exporte os registros do Open Headunit. O próximo teste alternativo é usar um segundo telefone como origem Android Auto, mantendo o primeiro como receptor SDL.

## O que falta validar antes de chamar de funcional

- Se Self Mode/Headunit Server do Android Auto e USB AOA SDL coexistem no mesmo telefone Android.
- Se o decoder MediaCodec consegue renderizar para a Surface do encoder SDL neste aparelho e sustenta a sessão sem tela preta ou queda de FPS.
- Se a resolução SDL e a resolução AAP pedem apenas escala proporcional ou exigem compensação de margens/deslocamento.
- Se os toques chegam corretos em vários apps Android Auto, e se o evento CANCEL encerra o toque sem deixá-lo preso.
- Latência, estabilidade prolongada, áudio de navegação/voz e retomada após perda de USB ainda não foram implementados ou medidos.

## Origem e licença

O código parte do [Open Headunit](https://github.com/andreknieriem/open-headunit), snapshot no commit `667346188bee1c50f300e245f373d13942fbd220`, licenciado sob AGPL-3.0. O arquivo-fonte fornecido contém a licença e as alterações desta ponte. O APK é um build de depuração; para redistribuição pública, publique também o código-fonte correspondente e preserve os avisos de licença.
