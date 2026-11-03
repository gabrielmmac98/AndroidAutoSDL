# Bridge 0.7 — análise e limites

O log 18:06–18:10 registra decoder Android Auto configurado em 1080×1920 e encoder SDL em 800×480. O decoder chega a renderizar 41–59 fps na saída marcada SDL, mas isso não comprova envio H.264 nem apresentação na central. A implementação anterior ligava diretamente as duas superfícies sem etapa explícita de escala.

A 0.7 usa SurfaceTexture e textura externa OpenGL entre decoder e encoder. A saída permanece em 800×480; timestamps são monotônicos e a thread do encoder controla o EGL. Um quadro verde-azulado inicial permite distinguir saída SDL de imagem Android Auto. O log SDL relay: scaled frames informa quadros recebidos e submetidos ao encoder, sem confundi-los com exibição pela central.

Foi restaurado TomTom / 3610601450 e suspenso o envio do ícone experimental. A rejeição da 0.6 é consistente com validação do nome, mas nome e ícone mudaram juntos e não houve log dessa sessão para isolar a regra.

A recuperação invalida callbacks de proxies anteriores, descarta referências e pendências no encerramento e evita esperar indefinidamente pelo EOS ao fechar o encoder. O decoder continua quando a interface local está encoberta e a saída SDL ainda está pronta.

No log, 18:07:17 contém Disconnect action received/user_exit; o erro USB EIO aparece depois, às 18:07:24. Portanto essa queda específica não deve ser atribuída automaticamente a timeout da central. O posterior aviso de router não confiável ocorreu após a falha de transporte; não há evidência suficiente para tratá-lo como causa original.

## Ensaio único no carro

Com o carro estacionado e freio acionado: inicie Headunit Server do Android Auto, conecte USB, abra SDL bridge, selecione TomTom na central e inicie Self Mode. Observe se aparece o quadro verde-azulado e depois Android Auto. Aguarde 30 segundos sem mudar orientação/configurações ou desconectar. Exporte um log ao final, mesmo se funcionar. O quadro inicial e os contadores permitem localizar a falha em uma tentativa.

## Limites

O teste local verifica SurfaceTexture → EGL → MediaCodec, com entrada 1080×1920 e saída H.264 800×480. Não substitui Toyota PLAY+, USB/AOA real, autorização NAV ou latência/toque físicos. A imagem é escalada para ocupar a saída inteira; proporção e calibração dos toques ainda precisam de validação. Não há alegação de funcionamento completo no carro.
