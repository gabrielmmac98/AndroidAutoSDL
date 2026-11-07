# AndroidAutoSDL

**Projeção do Android Auto via SmartDeviceLink para a Toyota PLAY+.**

[English](README.md) · [Português brasileiro](README.pt-BR.md)

<img src="docs/images/open-headunit-icon.png" width="96" height="96" alt="Open Headunit icon" />

AndroidAutoSDL é uma extensão experimental do [Open Headunit](https://github.com/andreknieriem/open-headunit). O aplicativo roda no celular Android e faz a ponte entre a interface do Android Auto e uma central SmartDeviceLink (SDL) pelo USB, permitindo navegação e toque em um equipamento sem Android Auto nativo.

O desenvolvimento e os testes físicos estão concentrados na **Toyota PLAY+ de um Toyota Yaris 2019**. Outros veículos e versões de firmware não foram validados.

## Origem e objetivos

O [Open Headunit, mantido por andreknieriem e seus colaboradores](https://github.com/andreknieriem/open-headunit), fornece o receptor Android Auto, o Self Mode, o tratamento de áudio e grande parte da interface. Este projeto parte desse trabalho; não implementa o Android Auto do zero.

A base utilizada é o commit [`667346188bee1c50f300e245f373d13942fbd220`](https://github.com/andreknieriem/open-headunit/tree/667346188bee1c50f300e245f373d13942fbd220) do projeto original. AndroidAutoSDL acrescenta a integração SDL e um perfil de projeção dedicado. Os objetivos são navegação e toque confiáveis na central testada, configurações próprias e um histórico reproduzível de versões e testes. O APK original do TomTom foi inspecionado como referência de compatibilidade e não foi modificado.

## Implementações deste projeto

- Registro SDL por USB, permissão de acessório, consulta às capacidades do display e diagnóstico com SDL Android SDK 4.12.2.
- Um intermediário EGL/SurfaceTexture entre a decodificação do Android Auto e a codificação H.264 SDL, com escala e correções das verificações da tela local que interrompiam a transmissão.
- Eventos de toque SDL convertidos para as coordenadas do Android Auto.
- Página SDL e tela de transmissão no celular, geometria própria, DPI e correção de proporção salvos, incluindo o ajuste de 115%.
- Iniciar, parar, desconectar e iniciar vídeo automaticamente quando a central autorizar.
- Seleção independente de microfone: celular, automático ou Bluetooth, com indicação da entrada efetiva durante a captura.
- Envio opcional do ícone do Open Headunit após o registro SDL, acrescentado na 0.14 sem mudar a identidade de compatibilidade.
- Notificações de condução encaminhadas como restrições ao Android Auto; recusas de HMI e transmissão de vídeo da central continuam respeitadas.

```text
Android Auto → perfil SDL do Open Headunit → ajuste de quadros / H.264 → USB SDL → PLAY+
Android Auto ← conversão de coordenadas ← eventos de toque SDL ←──────────────── PLAY+
Áudio de mídia ───────────────────────── Bluetooth ───────────────────────────→ alto-falantes
Microfone selecionado ─────────────────→ comandos de voz do Android Auto
```

O áudio de mídia utiliza **Bluetooth**, sem som SDL pelo USB. Com Bluetooth apenas para mídia, selecione **Celular** para captar sua voz. O Android pode usar outra entrada apesar da preferência solicitada; a página SDL informa a entrada efetiva. O microfone das ligações permanece sob controle do telefone e do sistema Bluetooth.

## Versão atual e estado dos testes

**Bridge 0.14 — versão experimental de ícone**

[APK](artifacts/AndroidAutoSDL-Bridge/v0.14/AndroidAuto-SDL-Bridge-0.14.apk) · [SHA-256](artifacts/AndroidAutoSDL-Bridge/v0.14/AndroidAuto-SDL-Bridge-0.14.sha256.txt) · [Fonte completo](artifacts/AndroidAutoSDL-Bridge/v0.14/fonte.zip) · [Notas da versão](artifacts/AndroidAutoSDL-Bridge/v0.14/icone-e-validacao.md)

| Recurso | Evidências e limites |
| --- | --- |
| Registro, imagem do Android Auto e toque | Confirmados pelo proprietário na PLAY+ testada. |
| Vídeo estável, interface dedicada, DPI/proporção e reprodução Bluetooth | Confirmados nos testes físicos anteriores; o proprietário preferiu proporção de 115%. |
| Seleção dedicada de microfone | Captura e persistência verificadas localmente na 0.13; teste no telefone/veículo pendente. |
| Ícone do Open Headunit | Acrescentado na 0.14; aceitação e aparência na PLAY+ dependem de teste físico. |
| Transições do estado de condução | Tratamento implementado na 0.12; autorização em movimento não é garantida. |
| Outras centrais | Não validadas. |

Uma confirmação de recebimento SDL, isoladamente, não prova que imagem ou ícone foram exibidos. As notas por versão distinguem testes locais e de veículo.

### Identidade de compatibilidade

A central aceitou a identidade experimental **TomTom / App ID 3610601450**. Uma versão anterior alterou nome e ícone juntos e deixou de aparecer; restaurar a identidade recuperou o registro. As alterações não foram isoladas, portanto a regra exata de aceitação ainda não é totalmente conhecida.

A central pode continuar listando a ponte como **TomTom**. Isso é uma condição de compatibilidade, não uma alegação de vínculo ou aprovação por Toyota, TomTom, Google ou pelo consórcio SDL. Evite executar o TomTom original e a ponte simultaneamente com essa identidade. A versão 0.14 altera apenas a arte e usa arquivos limitados à sessão; o cache da central ainda pode influenciar o ícone.

## Como utilizar

1. Instale o APK no celular. Pacote: `br.local.aasdlbridge`. As versões atuais permitem atualizar a ponte anterior com o mesmo certificado de assinatura.
2. Ative as configurações de desenvolvedor do Android Auto e inicie o **Headunit Server**, necessário para o fluxo de Self Mode utilizado aqui.
3. Conecte à porta USB da PLAY+ e conceda acesso ao acessório.
4. Abra **SDL** no aplicativo, conecte e abra o aplicativo registrado na multimídia quando necessário. Inicie o vídeo quando houver autorização ou ative o início automático.
5. Ajuste DPI e proporção com o vídeo parado. As preferências ficam salvas.
6. Com Bluetooth apenas para mídia, selecione **Celular** e conceda acesso ao microfone.

Para testar o ícone, mantenha **Enviar ícone à central** ativado e reconecte o SDL. Se ocorrer regressão, desative a opção e reconecte; o ícone em cache pode permanecer. A [versão 0.13](artifacts/AndroidAutoSDL-Bridge/v0.13) continua disponível.

Faça configurações e testes manuais no celular com o veículo estacionado. A autorização de transmissão depende da central.

## Organização e compilação

Este repositório guarda **artefatos de entrega por versão**, em vez de uma árvore de código convencional na raiz:

- [Artefatos da ponte](artifacts/AndroidAutoSDL-Bridge/): APKs, hashes, fontes completos, patches e notas por versão.
- [Artefatos do SDL Probe](artifacts/SDL-Probe/): protótipos usados para estabelecer compatibilidade de registro, vídeo e toque.
- [Análise](docs/analysis/) e [registros de testes](docs/testing/): evidências do protocolo, resultados físicos e limitações.

Extraia o `fonte.zip` da versão, configure o Android SDK e utilize **JDK 21** com o wrapper incluído:

```powershell
.\gradlew.bat :app:assembleGithubDebug --no-daemon
```

O fonte especifica compile SDK 36, NDK 29.0.14206865 e CMake 3.22.1. O patch de cada versão é uma diferença completa contra o snapshot original indicado acima: utilize o fonte completo **ou** aplique o patch sobre aquele snapshot exato, não ambos. Requisitos e resultados de testes são registrados por versão.

As alterações são registradas diretamente em **main**, preservando os artefatos anteriores.

## Créditos e licença

A aplicação principal e o receptor Android Auto vêm do [Open Headunit](https://github.com/andreknieriem/open-headunit) e de seus colaboradores, incluindo o trabalho original creditado no projeto original. A integração SDL utiliza a [biblioteca Android SmartDeviceLink](https://github.com/smartdevicelink/sdl_android). A arte do ícone vem do mesmo snapshot do Open Headunit.

A aplicação derivada permanece sob **AGPL-3.0**. Consulte [LICENSE](LICENSE) e o [aviso de copyright herdado](COPYRIGHT_MICHAEL_REID_GPLv3AFFERO.txt). Os fontes preservam os avisos originais; as dependências mantêm suas respectivas licenças.
