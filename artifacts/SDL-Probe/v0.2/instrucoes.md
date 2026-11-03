# SDL Probe 0.2 — segundo teste

Instale `SDL-Probe-0.2.apk` sobre a versão 0.1; o pacote e a chave de desenvolvimento foram mantidos. Não precisa desinstalar o TomTom.

## O que o primeiro diagnóstico comprovou

O acessório foi identificado como SDL/Core/1.0 e o aplicativo recebeu OnHMIStatus, DisplayCapabilities, OnLockScreenStatus e OnPermissionsChange. O caminho USB/SDL respondeu. A ausência de ícone não permite concluir que o App ID foi recusado: faltaram os valores de success/resultCode, HMI e permissões porque a versão 0.1 usava toString(), que nesse SDK imprime o endereço do objeto.

A mensagem ao tentar vídeo reunia várias condições em uma só frase. Não permite identificar qual delas bloqueou o teste.

## Correções desta versão

- Serialização JSON real das respostas e notificações do SDK, com testes automatizados para RAI e OnHMIStatus.
- Registro explícito de success/resultCode/info de RegisterAppInterface e do nível HMI.
- Registro separado das condições que bloqueiam vídeo.
- Envio de ícone por PutFile seguido de SetAppIcon, registrando ambas as respostas. O TomTom original executa essa sequência; ela faltava na versão 0.1. Não há comprovação de que seja a causa da ausência do ícone.
- Botão Registrar estado atual e captura automática de estado antes da exportação.

O App ID, nome, SDK, tipo NAVIGATION e parâmetros de vídeo foram mantidos. Não foi adicionada imitação da identidade TomTom, ativação forçada nem alteração de políticas.

## Como testar

1. Carro estacionado e freio de estacionamento acionado. Feche o TomTom.
2. Atualize o SDL Probe com o APK 0.2 e abra-o.
3. Conecte o cabo, conceda acesso USB se solicitado e toque Conectar USB.
4. Aguarde aproximadamente 15 segundos; atualize a lista da central uma vez.
5. Se SDL Probe aparecer, selecione-o. Marque a confirmação de estacionamento no telefone e teste vídeo.
6. Se não aparecer, não é necessário insistir no botão de vídeo. Toque Registrar estado atual e Exportar diagnóstico.
7. Anexe o novo texto à conversa e informe se o ícone apareceu depois do envio.

O diagnóstico deve conter `Probe=0.2`, conteúdo JSON de RAI/HMI/permissões e, se a consulta DISPLAY funcionar, resultados `ICON PutFile` e `ICON SetAppIcon`. Isso permite distinguir registro, permissão de arquivos/ícone e ativação pela HMI. Ausência de uma resposta também é informação útil.

## Validação

Compilação e dois testes automatizados de serialização concluídos. Assinatura do APK verificada. A instalação/abertura em emulador havia sido validada na versão 0.1; esta atualização não foi validada com a PLAY+ nem com telefone físico. Envio de ícone e vídeo precisam do ensaio real. O protótipo continua sem Android Auto, áudio ou microfone.
