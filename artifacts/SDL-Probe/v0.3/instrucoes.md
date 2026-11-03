# SDL Probe 0.3 — idioma e ícone BMP

## Evidências do teste 0.2

O registro devolveu `success=true`, `resultCode=WRONG_LANGUAGE`, idiomas `PT-BR`. A sessão permaneceu em `hmiLevel=NONE`, `userSelected=false`; não houve ativação FULL observada. O estado de vídeo informado foi STREAMABLE, mas não houve pedido/teste de transmissão porque FULL não foi atingido.

A central informou SDL `4.1`, versão RPC `4.2.0`, VehicleType Toyota / EFC CORE / 2018 / TY e tela 800×480. Anunciou toque simples, sem multitouch e sem duplo toque. Os ícones appIcon foram anunciados exclusivamente como GRAPHIC_BMP, em 35×35 e 70×70. Outros campos gráficos aceitam PNG/JPEG, mas isso não implica que o ícone aceite esses formatos.

O diagnóstico registra `ICON: central não anunciou PNG/JPEG para appIcon`: a versão 0.2 interrompeu o envio. Portanto, não houve resposta PutFile/SetAppIcon para avaliar. As notificações de permissões permitem ambos em NONE, sem userDisallowed para eles. Isso ainda não comprova que o ícone faltante explique sozinho o aplicativo oculto.

O sucesso do registro não comprova liberação de vídeo ou exibição pela interface. `hmiCapabilities.navigation=false` é um campo de capacidades retornado, não um resultado de StartService NAV; o funcionamento do TomTom permanece uma observação separada.

## Mudanças 0.3

- Registro com languageDesired=PT_BR e hmiDisplayLanguageDesired=PT_BR, substituindo EN_US padrão.
- Codificador BMP sem compressão, 24 bits BGR, linhas invertidas e alinhamento de quatro bytes; usa dimensões anunciadas pela central. O caminho atual escolhe a primeira entrada appIcon anunciada, 35×35, que a central declarou suportar.
- Mantidos App ID experimental, nome, NAVIGATION, USB, requisitos de ativação FULL e diagnóstico JSON. Sem copiar identidade TomTom, forçar ativação ou editar políticas.

## Teste necessário

1. Instale SDL-Probe-0.3.apk sobre a versão anterior.
2. Com carro estacionado e freio de estacionamento acionado, feche o TomTom, abra SDL Probe e reconecte o cabo.
3. Toque Conectar USB e aguarde 15 segundos. Atualize a lista da central.
4. Se o ícone aparecer, selecione-o e teste o vídeo conforme as instruções anteriores.
5. Se não aparecer, exporte o diagnóstico diretamente. Não precisa insistir em iniciar vídeo.

Os campos decisivos do próximo diagnóstico são: `Probe=0.3`, RAI success/resultCode, `ICON PutFile`, `ICON SetAppIcon` e mudanças de hmiLevel. Uma recusa do ícone poderá indicar formato, parâmetros ou permissões; sucesso do ícone com app ainda oculto deslocará a investigação para a interface/política de listagem da central.

## Verificação

Compilação e quatro testes automatizados passaram: dois de serialização e dois do BMP, incluindo leitura por decodificador independente, orientação/cores e tamanhos 35/70. Assinatura do APK verificada. A versão 0.3 não foi executada na PLAY+ nem em telefone físico; não é garantia de que o ícone aparecerá. Continua sem receptor Android Auto, áudio ou microfone.
