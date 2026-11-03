# Reteste SDL Bridge 0.4 — Toyota PLAY+

Faça o ensaio apenas com o Yaris estacionado e o freio de estacionamento acionado.

1. Instale `AndroidAuto-SDL-Bridge-0.4.apk` por cima da versão 0.3. O pacote e a assinatura foram mantidos compatíveis.
2. Feche o TomTom original; ele usa o mesmo App ID SDL e não deve concorrer com a Bridge nesta sessão.
3. Conecte à mesma porta USB usada no teste bem-sucedido do SDL Probe 0.5. Abra a Bridge e toque em **Conectar à central**.
4. Se o Android mostrar a autorização para o acessório USB, aceite. Aguarde até 15 segundos e veja se **TomTom** aparece na lista da central.
5. Não selecione vídeo/Android Auto antes de o app aparecer e a central aceitá-lo.
6. Se não aparecer, exporte o log em **Configurações → Export Logs / Exportar logs** dentro do app e compartilhe-o. Não repita várias tentativas seguidas; um único log desta versão já registra contagem de acessórios, descritores, permissão, callbacks SDL e resposta RAI.

A 0.4 é uma build de diagnóstico. A falha da 0.3 ainda não tem causa confirmada.
