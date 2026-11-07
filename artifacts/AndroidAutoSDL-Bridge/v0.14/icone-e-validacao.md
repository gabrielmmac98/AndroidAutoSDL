# Bridge 0.14 — ícone do Open Headunit e documentação bilíngue

Implementação de 07/11/2026. Base: Bridge 0.13. Pacote br.local.aasdlbridge; versionCode 134; versionName 4.0.0-beta1-bridge14.

## Alteração

- Usa o próprio recurso mipmap/ic_launcher herdado do Open Headunit, incluindo sua composição adaptativa no Android compatível.
- Preserva SDL_APP_NAME=TomTom e SDL_APP_ID=3610601450. Na 0.6 o nome e o ícone foram alterados juntos; esta versão permite testar somente o ícone.
- Após o registro, ao obter as capacidades de display, envia PutFile seguido de SetAppIcon apenas se o upload tiver sucesso. Cada sessão faz uma tentativa; não existe repetição contínua em caso de rejeição.
- Prioriza PNG e respeita os formatos JPEG/BMP anunciados para appIcon. Usa a resolução anunciada ou 64x64 se ausente; valida dimensões de 1 a 1024 e preserva o desenho quadrado dentro de áreas retangulares.
- Upload não persistente. Não força limpeza do cache da central; a apresentação anterior pode continuar em cache mesmo depois de desativar a opção.
- Rejeições/erros são registrados como ICON PutFile, ICON SetAppIcon ou ICON ERROR e não encerram a sessão por decisão do nosso código. O comportamento da PLAY+ diante dessas chamadas ainda não foi testado nesta versão.
- Respostas de sessões encerradas são ignoradas por identidade do proxy e época de conexão, inclusive antes de SetAppIcon.
- A página SDL oferece Enviar ícone à central, ativado por padrão. Alterações valem para a próxima conexão SDL.
- README.md em inglês e README.pt-BR.md em português substituem as notas acumuladas e desatualizadas da página inicial. Incluem origem, créditos, licença, objetivos, recursos, uso, compilação e estado das validações.

## Verificação local

- assembleGithubDebug com JDK21, modo offline e compilação sequencial: BUILD SUCCESSFUL.
- APK instalado como atualização no emulador.
- SdlProfileSelfTestActivity: drawable real renderizado; PNG, JPEG e BMP em 96x64 decodificados nas dimensões esperadas; tamanho inválido recusado. Prévia PNG128x128 salva e inspecionada visualmente (icone-previa.png).
- Testes anteriores de perfil/geometria, restrições NAV e início automático aprovados novamente. Ver verificacao-local.txt.
- UIAutomator confirmou switch inicialmente marcado, persistência de send_icon=false após encerrar/reabrir o processo e opção restaurada para true. As configurações de microfone, DPI e proporção permaneceram presentes.
- A exibição real na PLAY+, as respostas PutFile/SetAppIcon e o efeito de cache ainda dependem do teste no veículo. Não afirmar sucesso na central a partir dos testes do emulador.

## Teste no veículo estacionado

1. Instale a atualização sem desinstalar a versão anterior.
2. Deixe Enviar ícone à central ativado, desconecte e reconecte o SDL.
3. Verifique se o aplicativo continua aparecendo como TomTom e se o ícone muda para o Open Headunit. Atualize a lista da central se necessário.
4. Confirme abertura, vídeo e toque. Se o ícone não aparecer, compartilhe o log para verificar ICON PutFile e ICON SetAppIcon.
5. Se o registro regredir, desative o envio e faça nova conexão. A versão 0.13 permanece disponível. A desativação impede novos envios, mas não garante remoção imediata de ícone já armazenado pela central.

## Referência do protocolo

- https://smartdevicelink.com/en/guides/sdl-overview-guides/getting-started/registering-an-application/
- https://2406.smartdevicelink.com/en/docs/android/master/com/smartdevicelink/proxy/rpc/SetAppIcon/

O fonte completo preserva os avisos e a licença do Open Headunit. O original TomTom não foi modificado.
