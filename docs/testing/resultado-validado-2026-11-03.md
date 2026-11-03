# Projeção SDL validada na Toyota PLAY+

Em 03/11/2026, o usuário confirmou imagem sintética e teste de toque com SDL Probe 0.5 no Yaris 2019. Duas fotografias mostram o ícone azul SDL com nome TomTom na lista e na tela inicial da central.

O diagnóstico registra:

- 07:38:55: USB SDL/Core/1.0; appId=3610601450, appName=TomTom, NAVIGATION, PT-BR.
- Registro, PutFile e SetAppIcon: SUCCESS.
- 07:39:01: HMI FULL, userSelected=true, DD_OFF.
- 07:39:07: pedido NAV sem criptografia, 800×480, configuração 30 fps e 1.500.000 bit/s; callback onServiceStarted com encrypted=false e criação de Surface.
- 691 notificações onServiceDataACK, de 07:39:07 a 07:39:42. Essas confirmações não são contagem de quadros nem medição de fps.
- 30 notificações OnTouchEvent (incluindo BEGIN e END), por exemplo (73,54), (385,59) e (86,429). Sem associação documentada entre alvo pretendido e toque, não é possível calcular correção de escala/offset confiável.
- Aproximadamente 35 segundos entre início e encerramento do vídeo; em seguida HMI LIMITED. O registro isolado não estabelece o motivo da parada nem estabilidade de longa duração.

Comparação: identidade experimental SDL Probe/8274106392 registrava e recebia ícone com SUCCESS, mas ficava oculta, tanto NAVIGATION quanto DEFAULT. Nome TomTom e ID 3610601450 foram alterados juntos; o resultado sustenta dependência da identidade/contexto associado, mas não separa efeito de nome, ID, políticas ou cache. Não comprova validação exclusiva por ID, ausência universal de autenticação ou uma vulnerabilidade geral.

O protótipo, com pacote e assinatura Android diferentes do TomTom, conseguiu nessa central obter projeção e toque sem modificar o APK original ou firmware. A etapa SDL da futura ponte foi demonstrada. Android Auto, áudio, microfone, latência e uso prolongado continuam não implementados/não validados.

Próxima etapa técnica: manter esta versão como referência e integrar uma fonte de vídeo externa à Surface SDL, depois um receptor Android Auto. A arquitetura de um único telefone versus dois dispositivos ainda precisa ser definida/verificada; o teste não resolve a recepção Android Auto no mesmo telefone que fornece SDL. Calibração de toque deve usar alvos e sequência conhecidos antes de encaminhar gestos ao receptor AA.
