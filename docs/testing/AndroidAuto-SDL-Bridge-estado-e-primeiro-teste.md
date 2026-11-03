# Android Auto → SDL: execução inicial

## O que já sabemos e foi testado

- O TomTom original 3.1.38 LATAM conecta normalmente ao Toyota PLAY+ do Yaris 2019. A central mostra a interface e recebe toques; há um desvio entre a imagem e a posição do toque que já ocorre no app original.
- O SDL Probe 0.5 apareceu na central com identidade TomTom, recebeu HMI FULL e negociou vídeo NAV não criptografado em 800×480. O teste registrou 30 eventos de toque; a configuração usada foi 30 fps e 1,5 Mbit/s. Os 691 ACKs observados em cerca de 35 s não são uma medição de FPS.
- Isso valida a ligação USB/AOA, a identidade SDL, a autorização e a saída SDL de vídeo no PLAY+. Ainda não valida quadros recebidos do Android Auto.

## Implementação

A variante experimental usa o App ID `3610601450` e nome SDL `TomTom`, identidade que já apareceu no PLAY+. O pacote Android independente é `br.local.aasdlbridge`, para não substituir o Open Headunit original.

A Bridge encaminha a Surface do decoder Android Auto ao encoder H.264 NAV SDL e converte eventos de toque SDL em eventos AAP. A sessão SDL fica no escopo do processo, protegida pela confirmação de carro parado e pelos estados HMI e de distração.

## Histórico de versões

### 0.1

A primeira versão tinha uma permissão de assinatura compartilhada com o Open Headunit original e falhava ao abrir no layout retrato.

### 0.2

A documentação registra uma correção do identificador da permissão (versionCode 122). O APK e pacote-fonte isolado desta versão não estavam entre os artefatos disponíveis para este repositório.

### 0.3 — primeiro teste físico sem registro

O usuário informou que a Bridge abriu, mas não apareceu na lista de apps SDL da central. A solicitação de conexão foi feita, porém não houve confirmação de registro. Esse resultado é distinto do SDL Probe 0.5, que apareceu na mesma central. Como o build 0.3 não registrava a resposta RAI nem o resultado do callback de permissão, ainda não é possível afirmar se a falha ocorreu antes do transporte, durante o registro ou por rejeição da central.

### 0.4 — alinhamento do fluxo e diagnóstico

O Probe reenumera o acessório e consulta `UsbManager.hasPermission()` depois da resposta USB; a Bridge 0.3 dependia do extra `EXTRA_ACCESSORY` da resposta. Alguns stacks podem omitir esse extra. A 0.4 agora reenumera o dispositivo, confirma a permissão e tenta abrir a sessão com a tela ativa, como o Probe. Também registra descritores USB, resultado da permissão, callbacks do SDK e `RegisterAppInterfaceResponse` (RAI).

A 0.4 foi compilada como debug, com `versionCode 124` e `versionName 4.0.0-beta1-bridge04`. A assinatura v1/v2 foi verificada e coincide com a da 0.3, permitindo instalar por cima. O reteste físico ainda está pendente; esta alteração é uma hipótese de correção, não um resultado confirmado.

## Próximo ensaio no Yaris — carro parado

1. Instale a 0.4 por cima da 0.3, sem remover outros apps; mantenha o TomTom original fechado enquanto a ponte usa a mesma identidade SDL.
2. Estacione, acione o freio de estacionamento e conecte à USB que funcionou com o Probe. Abra a Bridge 0.4, toque em “Conectar à central” e aceite a permissão USB do Android se for solicitada.
3. Aguarde até 15 segundos e procure **TomTom** na lista da central. Não inicie o vídeo/Android Auto enquanto esse registro não aparecer.
4. Se continuar ausente, no app abra **Configurações → Export Logs / Exportar logs**, compartilhe o arquivo de log aqui e encerre a sessão. Os trechos mais úteis começam com `SDL bridge: USB check`, `USB permission result`, `SDL bridge: callback` e `RAI`.

## O que falta validar

- Se Self Mode/Headunit Server do Android Auto e USB AOA SDL coexistem no mesmo telefone Android.
- Se o decoder MediaCodec consegue renderizar para a Surface do encoder SDL neste aparelho e sustenta a sessão sem tela preta ou queda de FPS.
- Se a resolução SDL e a resolução AAP pedem apenas escala proporcional ou exigem compensação de margens/deslocamento.
- Se os toques chegam corretos em vários apps Android Auto, e se o evento CANCEL encerra o toque sem deixá-lo preso.
- Latência, estabilidade prolongada, áudio de navegação/voz e retomada após perda de USB ainda não foram medidos ou concluídos.

## Origem e licença

O código parte do [Open Headunit](https://github.com/andreknieriem/open-headunit), snapshot no commit `667346188bee1c50f300e245f373d13942fbd220`, licenciado sob AGPL-3.0. Os pacotes-fonte preservam a licença e os avisos originais. O APK é um build de depuração.
