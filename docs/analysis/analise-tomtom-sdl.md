# Inspeção estática — TomTom 3.1.38 LATAM e Toyota PLAY+

Data: 02/11/2026. APK recuperado do anexo da conversa original. O arquivo original foi somente lido: não foi instalado, executado, modificado, reempacotado nem assinado. Nenhum teste foi realizado na central.

## Conclusão

O APK contém uma integração SDL efetivamente ligada ao código TomTom, com USB direto, registro como NAVIGATION, vídeo H.264 gerado por MediaCodec a partir de uma Surface OpenGL e retorno de toques. Há base concreta para desenvolver um aplicativo experimental separado que teste projeção SDL. Não foi demonstrada uma vulnerabilidade, nem que a PLAY+ aceite registrar e transmitir vídeo de um App ID próprio.

## Identificação e método

| Campo | Valor |
|---|---|
| Pacote Android | `com.tomtom.gplay.navapp.nds` |
| versionName / versionCode | `3.1.38-latam` / `4074` |
| Tamanho | 193.578.537 bytes |
| SHA-256 do APK | `9cad1abc7e4d9eb13d1ed017775ec79c412f610f07065e03f0db0aaed27b5a6b` |
| minSdk / targetSdk | 21 / 30 |
| ABIs | arm64-v8a e armeabi-v7a |
| SHA-256 do certificado v2 extraído | `45311152edfa6e6e18a9a64e41420f9196e562469f1f935866c57f0f4a800205` |

Manifesto e metadados extraídos com Androguard 4.1.4; código e recursos examinados com JADX 1.5.3. O JADX terminou com 19 erros de decompilação no conjunto do aplicativo. Algumas rotinas de toque e estado exibem avisos de reconstrução incorreta. As chamadas críticas de registro, criação de Surface e consulta à lista de centrais foram conferidas também no bytecode DEX. O certificado acima é uma identificação do certificado extraído, não uma comprovação independente de procedência ou auditoria criptográfica completa da assinatura.

As referências de arquivo/linha abaixo correspondem à extração JADX desta análise. O arquivo `evidencias-sdl.txt` conserva trechos numerados e `bytecode-critico.txt` conserva as instruções conferidas.

## USB / Android Open Accessory

`AndroidManifest.xml` declara `android.hardware.usb.accessory` com `required=false` e estes componentes:

- `MobileUsbAccessoryAttachmentActivity`: recebe `android.hardware.usb.action.USB_ACCESSORY_ATTACHED`; a atividade não declara `exported` explicitamente neste manifesto, que tem targetSdk 30. A meta-data aponta a `res/xml/accepted_usb_accessories.xml` (`0x7f150000`).
- `MobileSdlReceiver`: enabled=true, exported=false; recebe a ação interna `com.smartdevicelink.USB_ACCESSORY_ATTACHED` e o desligamento USB.
- `MobileSdlService`: exported=false; filtro `com.smartdevicelink.router.service`. O nome da ação não significa que a conexão escolhida use um roteador externo.

O XML `accepted_usb_accessories.xml` contém manufacturer=`SDL`, model=`Core`, version=`1`. `accessory_filter.xml` repete esses valores. Existe uma diferença importante: `USBTransport.isAccessorySupported()` exige literalmente `SDL`, `Core`, **`1.0`**. É necessário registrar os descritores reais do acessório no telefone; não assumir que `1` e `1.0` são equivalentes. A conexão já observada pelo usuário não explica, por si só, como se dá a seleção inicial pelo Android.

Fluxo concreto: Activity → broadcast explícito para MobileSdlReceiver → verificação `USBTransport.isAccessorySupported()` → `c.a.a.m1.d.a(UsbAccessory)` → `USBTransportConfig(context, accessory)` → SdlProxyALM. O serviço mantém a execução e a notificação em primeiro plano. Não há dependência de aplicativo Toyota nesse caminho.

## SDK e registro

`com.smartdevicelink.BuildConfig` declara VERSION_NAME=`4.12.2`, VERSION_CODE=14. É a versão declarada pela biblioteca embarcada; não garante identidade byte a byte com uma distribuição upstream. `SdlProxyBase.MAX_SUPPORTED_RPC_VERSION` é `6.0.0`, distinto da versão do SDK e da versão negociada com a central.

Em `c/a/a/m1/d.java:292–304`, o aplicativo lê recursos e constrói:

```java
new SdlProxyBuilder.Builder(listener, appId, appName, Boolean.FALSE, context)
    .setVrAppHMITypes(typesWithNAVIGATION)
    .setTransportType(new USBTransportConfig(context, accessory))
    .setCallbackToUIThread(true)
    .build();
```

Valores de `res/values/strings.xml:1078–1079`:

- App ID SDL: **`3610601450`**.
- Nome: **`TomTom`**.
- Tipo: **NAVIGATION**.
- isMediaApplication: **false**.

O SDK monta e envia `RegisterAppInterface` em `SdlProxyBase.java:4490–4519`, com nome, ID, idiomas, DeviceInfo, sinônimos e AppHMIType. O construtor de RegisterAppInterface usa `setFullAppID()`, que deriva também appID; este ID numérico não sofre truncamento de UUID. O builder mantém idiomas EN_US por padrão neste caminho. A versão efetivamente enviada/aceita deve ser registrada em execução.

O fato de isMediaApplication=false não impede este código de iniciar áudio de navegação pelo serviço PCM.

## Vídeo: implementação usada pelo TomTom

Não é apenas uma classe de demonstração incluída no SDK. O código TomTom em `c/a/a/l1/b.java:292–296` chama:

```java
proxy.createOpenGLInputSurface(frameRate, iframeInterval,
    displayWidth, displayHeight, bitrate, false);
```

O último argumento solicita o serviço de vídeo **sem criptografia**. Em `SdlProxyBase.java:3790–3803`, essa chamada negocia o vídeo e cria a Surface. `tryStartVideoStream()` solicita `startService(SessionType.NAV, sessionId, encrypted)`, aguarda ACK e conserva parâmetros rejeitados em caso de NAK. Para protocolo >=5 também verifica a capacidade VIDEO_STREAMING. O sucesso deste caminho depende da resposta da central.

`SessionType` define RPC=`0x07`, PCM=`0x0A`, NAV/vídeo=`0x0B`. `SdlEncoder` usa MIME `video/avc`, `MediaCodec.createEncoderByType()`, formato de cor Surface e `createInputSurface()`. Obtém dados específicos do codec e os antepõe a quadros chave quando disponíveis. Não configura explicitamente perfil/nível H.264 em `prepareEncoder()`; não é possível afirmar um perfil efetivo sem observar o codificador.

Parâmetros padrão dos recursos:

| Parâmetro | Valor |
|---|---|
| Taxa de quadros | 30 fps |
| Bitrate | 1.500.000 bit/s |
| Intervalo de I-frame | 1 segundo |
| DPI | 160 |
| Dimensões presentes nos recursos | 800 × 480 |

**As dimensões usadas no caminho ativo vêm de `DisplayCapabilities.screenParams.imageResolution`**, não necessariamente dos recursos 800×480. Não trate esse tamanho como medição da PLAY+.

O código cria uma thread `SDL Video Stream Thread`, inicia o encoder e drena sua saída. A inicialização de projeção ocorre no ramo HMI FULL, após validação da central na lista local; há verificações de estado FULL/LIMITED para operações de streaming. Não se deve inferir que basta aparecer na lista de aplicativos para o serviço NAV ter sido aceito.

## Toques e áudio

`c/a/a/m1/d.java:155` encaminha OnTouchEvent. `c/a/a/m1/j/b.java` lê IDs de ponteiros e coordenadas x/y, cria MotionEvent e entrega os eventos ao código de interface TomTom por `c.a.a.l1.b.f`. Há, portanto, um caminho bidirecional concreto para adaptar toque à ponte. A reconstrução completa dessa máquina de gestos requer atenção aos avisos do decompilador.

O áudio usa `startAudioStream(encrypted, LPCM, null)`, MIME `audio/raw`, sample-rate padrão 16.000 Hz, e `mobile_sdl_audio_encryption_default=false`. Isso comprova o caminho de áudio de navegação do TomTom; não estabelece suporte a todos os canais, microfone, chamadas ou áudio de mídia do Android Auto.

## Autorização: três camadas diferentes

1. **Permissão USB no Android:** acesso ao acessório concedido ao aplicativo. Não equivale à autorização de navegação na central.
2. **Restrição local no TomTom:** `res/xml/sdl_white_list.xml` contém `TomTom / Mykonos / 2018 / TT` e `Toyota / EFC CORE / 2018 / TY`. `c.a.a.l1.c` lê a lista; o aplicativo obtém VehicleType pela conexão e testa inclusão. `c.a.a.j2.c.equals()` compara os quatro campos ignorando maiúsculas/minúsculas. O ramo incompatível envia um Alert; o ramo aceito instala os manipuladores de vídeo e áudio. O ano 2018 é metadado esperado da central, não uma exigência de ano do veículo.
3. **Políticas na central:** RegisterAppInterface e StartService têm respostas independentes. Este APK não fornece a tabela de políticas instalada na PLAY+, nem comprova quais IDs, nomes, certificados ou permissões ela exigirá de outro aplicativo.

O caminho de construção TomTom não configura `setSdlSecurity()`: a lista de implementações de segurança do builder fica nula. O SDK inclui a infraestrutura SdlSecurityBase, mas sua presença não prova uso neste fluxo. Não foi identificada, nesse caminho examinado, uma troca explícita de certificado de assinatura do APK para liberar vídeo. **Isso não demonstra que a central dispense autorização ou que copiar o App ID seja suficiente.** O hash de assinatura do APK e o App ID SDL são objetos diferentes.

## Bibliotecas nativas

Há 22 bibliotecas por ABI, incluindo libNavKit.so, libNavKitJniBind.so, libTomTomNavKitMap.so, libPlayer.so, libConnector.so e libServicesConnectorJNI.so. Nenhuma tem nome específico SDL. A busca pelas strings `com/smartdevicelink`, `com.smartdevicelink`, `SdlSecurity`, `SdlProxy`, `RegisterAppInterface` e `SmartDeviceLink` nas bibliotecas arm64 não encontrou ocorrências.

O transporte e o encoder SDL examinados estão em Java/DEX e usam APIs Android. A busca negativa de strings não substitui desassemblagem nativa e não exclui código relacionado em bibliotecas ofuscadas ou estaticamente incorporadas. Não foi feita auditoria integral do motor nativo de navegação.

## Ponte Android Auto → SDL: pontos de integração

Arquitetura candidata, a implementar em aplicativo separado:

```text
Receptor Android Auto
  → decodificação do vídeo AA
  → renderização em Surface / OpenGL
  → codificação H.264 com parâmetros aceitos pela central
  → serviço SDL NAV (0x0B)

OnTouchEvent SDL
  → transformação de coordenadas e gestos
  → entrada no receptor Android Auto
```

O ponto inicial mais sustentado pelo APK é substituir conceitualmente a fonte de desenho TomTom por uma cena de teste no aplicativo experimental, usando a mesma categoria de API Surface. Não é necessário modificar o APK TomTom para esse experimento.

Encaminhar H.264 de AA diretamente seria uma otimização posterior, dependente de compatibilidade de resolução, perfil, formato, parâmetros do codec e temporização. O APK não prova essa compatibilidade. Áudio e microfone exigem integração separada. O papel simultâneo de telefone emissor AA e receptor AA no mesmo aparelho também não é resolvido pela análise; um ensaio com dois dispositivos pode isolar essa dificuldade, mantendo a ligação USB da ponte com a PLAY+.

## Próximos testes na PLAY+ do Yaris 2019

Executar estacionado, com freio de estacionamento acionado, mantendo as restrições normais de HMI/atenção do condutor. Os testes abaixo são propostos, não realizados.

| Etapa | Ação | Evidência / decisão |
|---|---|---|
| 1 — Referência original | Abrir o TomTom original e selecionar seu ícone na PLAY+. Registrar tela exibida e resposta a toque, mesmo sem mapas. | Distinguir registro no menu de uma sessão real de projeção. As fotos fornecidas mostram o ícone, não comprovam vídeo. |
| 2 — Transporte | Com depuração USB previamente autorizada, coletar `adb shell dumpsys usb`, versão Android e um logcat da conexão. ADB pode exigir conexão sem fio enquanto a USB é usada pela central. | Descritores manufacturer/model/version reais; permissão USB; identificar a diferença `1` versus `1.0`. A ausência de logs não prova ausência de SDL. |
| 3 — Registro próprio | Aplicativo SDL mínimo separado, nome e App ID próprios, NAVIGATION, USB/AOA e registro completo dos callbacks. Fechar o TomTom durante esse teste. | RegisterAppInterfaceResponse: success/resultCode/info, versão RPC, VehicleType, DisplayCapabilities, OnHMIStatus e OnPermissionsChange. Não assumir que o SDK mais recente se comportará como o embarcado. |
| 4 — Vídeo sintético | Após registro aceito e ativação normal pela central, desenhar barras e um contador. Pedir dimensões informadas pela central; começar com os parâmetros observados do TomTom. | StartService ACK/NAK, rejectedParams, parâmetros aceitos e imagem realmente visível. Se houver NAK, investigar capacidades/políticas antes de integrar AA. |
| 5 — Toque e ciclo de vida | Mostrar coordenadas e contagem de toques; alternar entre aplicativo e rádio; desconectar/reconectar. | Escala x/y, down/move/up/cancel, orientação, eventos duplicados e encerramento correto ao perder HMI. |
| 6 — Ponte AA | Somente após vídeo e toque comprovados, conectar uma fonte AA ao receptor e renderizar na Surface SDL. | Latência, estabilidade e controle. Validar áudio separadamente; não pressupor microfone ou chamadas. |

Não começar copiando o nome/ID TomTom: isso confundiria o teste de autorização de um aplicativo próprio. Não alterar firmware, tabelas de política, bloqueios de movimento ou o APK neste roteiro.

O TomTom desabilita os logs do SDK por padrão; existe a preferência privada `com.tomtom.mobile.setting.MOBILE_SDL_SDK_LOGCAT_ENABLED`, padrão false. Ela não constitui um botão público disponível ao usuário. O roteiro não depende de editar preferências privadas ou obter root. O recurso `mobile_sdl_kill_app_on_disconnect=true` explica por que o aplicativo pode encerrar ao retirar o cabo.

## Referências de protocolo

As conclusões sobre o APK derivam dos arquivos locais examinados. As referências oficiais abaixo servem apenas para interpretar os estados e mensagens, não para provar comportamento da PLAY+:

- [Especificação oficial do protocolo SDL](https://github.com/smartdevicelink/protocol_spec): registro e ACK/NAK dos serviços, incluindo restrições de acesso e transporte.
- [Registro de aplicativos SDL](https://smartdevicelink.com/en/guides/sdl-overview-guides/getting-started/registering-an-application/): sequência RegisterAppInterface, HMI e permissões.

Limite decisivo: a análise confirma que este TomTom implementa projeção SDL; a política e as capacidades efetivas da sua PLAY+ precisam das respostas capturadas nas etapas 3 e 4.
