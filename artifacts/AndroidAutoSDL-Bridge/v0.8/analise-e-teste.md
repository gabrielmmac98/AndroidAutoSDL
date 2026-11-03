# Bridge 0.8 — falso travamento do display local

O usuário confirmou imagem íntegra e toque na Toyota PLAY+ com a 0.7. A imagem permanece esticada.

O log registra Display stall e recriação local às 19:11:29.702 (5598ms sem draw), 19:11:53.346 (6001ms) e 19:12:03.398 (8002ms, fallback GLES→SurfaceView). Antes da última ocorrência, o relay completou 300 quadros às 19:12:02.478 e chegaram ACKs SDL até a recriação. A ausência de draw no telefone era esperada porque o decoder estava apontado para a saída SDL. O falso diagnóstico encerrava NAV e era seguido de HMI LIMITED.

A 0.8 impede o watchdog do display local e a recuperação de relançamento local de operarem quando o decoder usa SDL ou a Bridge está pronta para essa saída. O comportamento de recuperação local permanece disponível fora do SDL. O nome TomTom, o App ID e a escala da 0.7 são preservados.

A correção trata as interrupções cuja sequência está demonstrada nesse log. Não afirma que toda queda USB ou de Android Auto possível foi eliminada. A confirmação de estabilidade na central ainda depende de teste físico.

Instale por cima da 0.7. Com carro estacionado, faça a mesma sequência que já exibiu imagem. Não é necessário escolher manualmente SurfaceView/GLES. Observe por um minuto e exporte um log único ao final. A imagem local ausente durante SDL não representa travamento.

Verificação local da 0.8: emulador, entrada 1080×1920, saída H.264 800×480, PASS frames=900 encodedPackets=902. O teste exercita o relay e encoder; não simula o watchdog dentro de uma sessão SDL real. Compilação e assinatura aprovadas.
