# Bridge 0.13 — fonte de microfone exclusiva do SDL

Implementação de 07/11/2026. Base: Bridge 0.12. Package br.local.aasdlbridge, versionCode 133, versionName 4.0.0-beta1-bridge13. Atualização sobre a instalação anterior; não exige desinstalar.

## Uso

1. Abra a página SDL e, em Microfone para comandos de voz, selecione Celular (padrão), Automático ou Bluetooth.
2. Conceda acesso ao microfone quando solicitado. Bluetooth também pede a permissão de dispositivos próximos no Android 12 ou superior.
3. Inicie o vídeo e acione o microfone na interface do Android Auto.
4. A página SDL mostra a entrada efetivamente informada pelo Android durante a captura; ao encerrar, mostra a última entrada usada. Para alterar a escolha, pare e inicie o vídeo.

Para Bluetooth do carro conectado apenas para mídia, use Celular. Não é necessário habilitar chamadas no Bluetooth para essa opção. Música, navegação e respostas mantêm o caminho de reprodução existente.

## Comportamento

- Preferência persistida em sdl_projection/microphone, separada de mic-input-source e use-head-unit-microphone do Self Mode comum. Novas sessões SDL carregam a escolha salva.
- Celular: usa AudioSource.VOICE_RECOGNITION e solicita TYPE_BUILTIN_MIC com AudioRecord.setPreferredDevice; não ativa SCO nem altera AudioManager.mode.
- Automático: usa AudioSource.DEFAULT sem fixar dispositivo. A escolha fica com o Android; não herda a configuração Bluetooth do Self Mode comum.
- Bluetooth: exige uma entrada SCO/BLE disponível e usa o caminho de comunicação já existente. A2DP (apenas mídia) não é um microfone. Se não existir entrada compatível, a captura é recusada com indicação na página; não há troca silenciosa para o celular.
- A seleção específica requer Android 6 ou superior; versões anteriores podem usar Automático.
- O serviço de microfone do AAP e a autorização do serviço em primeiro plano consideram o perfil SDL, mesmo quando a captura do Self Mode comum está desativada. A preferência geral não é reescrita.
- getRoutedDevice e o observador de mudanças de rota mostram o dispositivo real. Se o Android escolher outro dispositivo apesar da preferência aceita, a página informa a divergência. Antes de o Android informar uma entrada, a interface não afirma que a seleção foi confirmada.
- A captura segue os pedidos Open/Close do Android Auto; não fica gravando continuamente. O log registra tipo/id da entrada e contadores técnicos, sem gravar falas ou arquivos de áudio.
- A escolha não controla o microfone de ligações telefônicas. Roteamento físico na Toyota e reconhecimento pelo assistente ainda precisam de teste no telefone do usuário.

## Referências

- https://developer.android.com/reference/android/media/AudioRecord#setPreferredDevice(android.media.AudioDeviceInfo)
- https://developer.android.com/reference/android/media/AudioRouting#getRoutedDevice()

Os testes locais e seus limites são registrados em verificacao-local.txt.
