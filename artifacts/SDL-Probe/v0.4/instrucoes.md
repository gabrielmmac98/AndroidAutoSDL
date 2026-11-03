# SDL Probe 0.4 — controle de listagem

## O que a versão 0.3 demonstrou

O diagnóstico enviado pelo usuário contém SUCCESS e success=true nas três operações: RegisterAppInterface, PutFile e SetAppIcon. O envio foi BMP 70×70. Portanto, o ajuste de idioma e a transferência/atribuição do ícone funcionaram. Isso corrige a nota anterior que antecipava 35×35: a execução selecionou 70×70, também anunciado pela central.

A sessão continuou com HMI NONE e userSelected=false, sem o aplicativo aparecer na lista. Não houve teste NAV/vídeo. A queda de transporte ocorreu após aproximadamente 33 segundos, e o usuário confirmou ter retirado o cabo ou fechado o app nesse intervalo. Ela deixa de ser o foco principal; não há evidência suficiente para classificar o encerramento como defeito espontâneo.

Não há prova de uma lista de App IDs bloqueados. Registro, ícone e permissões de RPC aceitos não garantem exposição/seleção pela HMI.

## Ensaio proposto

A versão 0.4 mantém ID experimental, nome, SDK, idiomas e ícone. Acrescenta um controle de categoria: por padrão registra DEFAULT em vez de NAVIGATION, sem pedir vídeo. A categoria de sessão fica registrada e não pode ser trocada sem desconectar. Não copia identidade TomTom, força seleção nem altera políticas.

1. Instale SDL-Probe-0.4.apk por cima da versão anterior.
2. Carro estacionado, freio acionado e TomTom fechado. Abra SDL Probe.
3. **Mantenha marcada a opção Controle de listagem: aplicativo comum (DEFAULT, sem vídeo).**
4. Conecte USB e toque Conectar USB. Aguarde 15 segundos e atualize a lista na PLAY+.
5. Se aparecer SDL Probe, selecione-o. Deve tentar exibir uma mensagem de texto usando Show, em vez de barras de vídeo. Não use o botão de vídeo nesse modo.
6. Mantenha conectado até tocar Exportar diagnóstico. Salve e envie o texto, informando se o ícone apareceu e se a mensagem foi exibida.

Não é necessário repetir NAVIGATION agora: já há um resultado de referência da 0.3. Se precisar comparar depois, desconecte, desmarque o controle e reconecte. Não misture os dois resultados sem registrar o modo.

## Interpretação

- DEFAULT aparece e NAVIGATION não: evidencia diferença de tratamento por categoria; ainda não identifica a regra responsável nem libera vídeo para uma ponte.
- DEFAULT também fica oculto: a ausência não fica isolada à categoria NAVIGATION; investigar exposição da aplicação pela HMI, políticas, resquícios de sessão e comportamento específico da central.
- RAI/ícone deixam de ser aceitos: avaliar a nova resposta antes de tirar conclusões sobre listagem.

A política instalada na central pode substituir o AppHMIType solicitado pelo aplicativo. Portanto, este é um controle informativo, não uma prova definitiva da categoria efetiva. A [documentação oficial de políticas SDL](https://smartdevicelink.com/en/guides/sdl-overview-guides/policies/app-policies/) descreve essa possibilidade. Aplicações de navegação também possuem comportamento de tela diferente, conforme a [documentação oficial de navegação](https://smartdevicelink.com/en/guides/android/video-streaming-for-navigation-apps/introduction/).

## Diagnóstico adicional

Ao exportar, o app tenta anexar os últimos registros logcat do próprio processo, limitados em quantidade e tamanho, sem coletar processos de outros aplicativos. Se o Android impedir ou não houver registros disponíveis, essa limitação aparecerá no arquivo. A exportação continua incluindo o diagnóstico principal. Não exige root, computador ou depuração USB.

## Validação

Compilação e quatro testes existentes de serialização/BMP concluídos. Assinatura verificada. O novo comportamento DEFAULT/Show ainda requer validação na PLAY+. Este modo não testa vídeo e não implementa Android Auto.
