# Bridge 0.11 — controles SDL

A entrada da tela inicial passa a SDL. A página usa AppTheme/Material 3, cores do aplicativo, toolbar, cartões, botões Material e alternância automática de tema. Inclui descrição do modo, estado da conexão e controles de transmissão. Os ajustes de DPI e proporção permanecem; há preset 115%, além de 16:9 e restauração de 100%.

## Início automático

A opção Iniciar vídeo automaticamente fica salva nas preferências SDL e vem desligada. O coordenador do processo a avalia quando chegam estados SDL, inclusive sem a página aberta. Só inicia com HMI FULL, capacidade de tela disponível e DD_OFF informado. Estado de distração desconhecido é tratado como bloqueado. O Headunit Server do Android Auto ainda precisa estar ligado.

Há uma tentativa automática por conexão SDL, evitando repetição quando o servidor AA está indisponível. Parar vídeo suprime novos inícios automáticos nessa conexão; o início manual continua possível quando autorizado. Uma nova conexão SDL libera a próxima tentativa automática.

## Parar vídeo

Antes, ACTION_STOP_SELF_MODE encerrava somente os lançadores, sem encerrar necessariamente o transporte AAP já ativo. Agora Parar vídeo desarma a saída SDL, desconecta CommManager e cancela o lançador Self Mode. O registro SDL permanece. O botão da página de streaming usa o mesmo coordenador.

## Segurança e estados da central

O checkbox manual foi removido. A autorização efetiva continua vindo da central: HMI FULL e DD_OFF. Não foi implementada sobreposição do bloqueio por movimento/freio. A interface informa quando a transmissão está suspensa pela central; a retomada permanece condicionada à autorização. A indicação DD_OFF não é apresentada como prova independente de freio acionado.

## Verificação

Verificação local inclui compilação, assinatura, teste de perfil e política de início automático (autorização, preferência, início em andamento, sessão existente, parada manual e tentativa já feita). A aparência e a persistência da opção são verificadas no emulador. O efeito da parada sobre uma sessão Toyota real e o início automático com USB/AOA permanecem dependentes de validação física.
