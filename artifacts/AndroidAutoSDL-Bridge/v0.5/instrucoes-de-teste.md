# Bridge 0.5

O log do teste 0.4 mostrou USB SDL/Core/1.0 detectado e permission=true, seguido de SecurityException em USBTransport.registerReciever. O SDK 4.12.2 usou registerReceiver sem flags exigidas pelo Android atual; o transporte falhou antes do registro na central.

A 0.5 fornece ao SDK um ContextWrapper que adapta as duas chamadas antigas de registerReceiver para RECEIVER_NOT_EXPORTED no Android 13+. A permissão USB e os eventos de desconexão continuam no fluxo do SDK.

Instale por cima da 0.4. Com o carro estacionado, feche TomTom e SDL Probe, conecte a USB, aceite a permissão e toque em SDL bridge. Aguarde TomTom na lista da central. Se falhar, exporte o log. A correção foi compilada; a validação física está pendente.
