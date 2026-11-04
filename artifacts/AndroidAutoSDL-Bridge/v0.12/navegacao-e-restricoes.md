# Bridge 0.12 — navegação e distração do motorista

A 0.11 condicionava isSessionAllowed a !distracted, convertendo DD_ON em interrupção universal do vídeo. Esse era um bloqueio da Bridge, não uma evidência de recusa do serviço NAV pela central. A regra era ampla demais para projeção de navegação.

A 0.12 mantém a projeção elegível quando a sessão está registrada, HMI FULL e videoStreamingState não é NOT_STREAMABLE. O SDK continua negociando o serviço NAV e uma recusa não é substituída por sucesso. Se uma versão antiga da central omitir videoStreamingState, o caminho segue HMI e negociação NAV do SDK. Perda de HMI FULL ou NOT_STREAMABLE encerra a saída pelo fluxo existente.

DD_ON é repassado ao Android Auto como bitmask de restrições 27: NO_VIDEO (entretenimento), NO_KEYBOARD_INPUT, NO_CONFIG e LIMIT_MESSAGE_LEN. A navegação não é encerrada apenas porque essas restrições estão em vigor. DD_OFF anuncia 0. Estado DD desconhecido usa 27 de forma conservadora. O valor atual é enviado ao abrir o canal de sensores e atualizado durante uma sessão ativa. O Self Mode comum mantém o comportamento anterior.

Referências primárias:
- https://smartdevicelink.com/en/docs/javase-docs/master/com/smartdevicelink/proxy/rpc/OnDriverDistraction/
- https://2406.smartdevicelink.com/en/guides/android/video-streaming-for-navigation-apps/introduction/
- https://2406.smartdevicelink.com/en/guides/sdl-overview-guides/getting-started/starting-an-sdl-session/

O comportamento específico da PLAY+ durante movimento ainda não foi observado em um log contendo a transição. A correção remove a interpretação indevida do app, mas não afirma antecipadamente que a central manterá HMI FULL ou STREAMABLE nessa situação. Não há falsificação de DD_OFF nem de estados da central.

O teste local verifica autorização, recusa NOT_STREAMABLE, perda de HMI e máscara de restrições. Compilação e assinatura são verificadas. A integração real com a central permanece pendente.
