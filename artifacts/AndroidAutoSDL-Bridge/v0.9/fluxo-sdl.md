# Bridge 0.9 — sessão SDL dedicada

A 0.8 teve estabilidade de vídeo confirmada pelo usuário. A 0.9 introduz um fluxo específico, com preferências SDL separadas das preferências normais de Self Mode. Não cria duas sessões Android Auto simultâneas.

## Fluxo

1. Abra SDL bridge. A página Projeção SDL mostra conexão, resolução da central, DPI e controles.
2. Com o carro estacionado, confirme a caixa de veículo parado e conecte à central. Selecione TomTom na Toyota. Aguarde Central pronta.
3. Escolha o DPI (160 inicial). O valor fica salvo apenas nas preferências sdl_projection. Com o Headunit Server do Android Auto ligado, toque Iniciar vídeo.
4. O telefone passa a mostrar a página de transmissão e controles; os toques dessa página não são encaminhados ao Android Auto. A central continua recebendo os seus próprios toques.
5. Para mudar DPI, pare o vídeo, ajuste e inicie de novo. Parar vídeo preserva SDL; Desconectar SDL pede que o vídeo seja parado primeiro.

## Geometria

O perfil captura a resolução anunciada pelo SDL antes de iniciar Self Mode. Para PLAY+ 800×480, anuncia ao Android Auto 800×480 em paisagem, margem zero, pixels quadrados e DPI escolhido. Vídeo H.264 a 30fps é anunciado nesse perfil. Assim o layout deixa de usar a orientação e as dimensões do telefone. O relay e o mapeamento de toque usam as dimensões dessa sessão.

O protocolo AAP oferece tamanhos enumerados; para painéis com tamanho sem correspondência exata, usa 800×480 com pixel aspect calculado para a proporção anunciada pelo painel. Esse caminho genérico ainda não foi validado em outra central.

A página do telefone substitui visualmente a projeção local por status e botões. O motor de recepção é compartilhado com o Open Headunit, com perfil de sessão dedicado; o Self Mode comum volta às preferências comuns em um novo início pelo botão normal.

## Verificação e limites

Compilação, abertura da página e verificações locais de resolução/DPI e preservação das preferências comuns são realizadas no emulador. A negociação completa e a interface de streaming na Toyota precisam de validação física. O ajuste DPI afeta o layout do Android Auto; valores altos podem reduzir a área útil em dp e simplificar o layout.
