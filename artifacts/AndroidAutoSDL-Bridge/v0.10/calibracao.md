# Bridge 0.10 — calibração da proporção física

O usuário confirmou funcionamento da 0.9 na Toyota PLAY+, incluindo interface, toque e áudio pelo Bluetooth. Isso valida a rota Bluetooth usada no teste; não é uma validação de áudio transportado pelo SDL.

As fotos mostram imagem íntegra com suspeita de leve deformação. A perspectiva da câmera impede determinar precisamente a proporção física. 800×480 é a resolução negociada do vídeo SDL; não comprova resolução nativa ou pixels fisicamente quadrados do painel.

A página agora separa DPI (tamanho/layout dos elementos) da proporção física (pixel aspect anunciado ao Android Auto). O padrão continua 100%, PAR E4=10000. O preset 16:9 aplica 106,7%, PAR E4=10670, para comparação na PLAY+ com vídeo 800×480. Um painel 16:9 exibindo 800×480 requer aproximadamente 6,67% de diferença na largura física por pixel.

A correção é anunciada na negociação Android Auto, mantendo vídeo e coordenadas de toque em 800×480. Ela não recorta nem desloca a imagem no relay. O controle permite 90–115%, em passos de 0,1%, e fica salvo apenas nas preferências SDL.

Para comparar, estacione, pare o vídeo, escolha Comparar proporção 16:9, mantenha o DPI atual e inicie novamente. Se necessário, selecione TomTom de novo na central. Avalie ícones que deveriam ser circulares, de frente ao painel. Se piorar, pare o vídeo e restaure proporção padrão. Não é preciso reinstalar para comparar os valores.

O trecho de log exportado termina com ACKs até 07:52:14.544, erro USB EIO às 07:52:14.583, link_lost às 07:52:17.607 e USB detached às 07:52:17.752. O trecho não registra a intenção da desconexão, portanto não permite classificá-la como falha espontânea. Não foi observado o antigo Display stall nesse trecho.

Os ACKs passam a ser resumidos a cada 150 callbacks, reduzindo a perda de registros de configuração por excesso de linhas no buffer. Não representam uma contagem exata de quadros exibidos.

A proporção correta fisicamente ainda depende da comparação na central. O teste local verifica o valor PAR anunciado e preservação de resolução, DPI e preferências comuns.
