# AndroidAutoSDL

Protótipos e evidências para investigar a projeção SmartDeviceLink (SDL) na Toyota PLAY+ do Yaris 2019 e avaliar uma ponte Android Auto → SDL.

## Estado

- **SDL Probe 0.5:** registrada no teste de 03/11/2026 com o app visível na central e ensaios de vídeo sintético e toque. Os registros e limites do ensaio estão em [`docs/testing/resultado-validado-2026-11-03.md`](docs/testing/resultado-validado-2026-11-03.md).
- **AndroidAutoSDL-Bridge 0.3:** no primeiro teste físico relatado, abriu no telefone mas não apareceu na lista SDL da central. Não houve confirmação de registro.
- **AndroidAutoSDL-Bridge 0.4:** build diagnóstico que reenumera o acessório USB após a permissão, confere a permissão novamente, usa o contexto da tela ativa como o Probe e registra as respostas SDL. O reteste físico ainda está pendente.
- **Bridge 0.1:** mantida para histórico; apresentava conflito de permissão e fechamento no layout retrato.
- **Bridge 0.2:** registrada em `artifacts/AndroidAutoSDL-Bridge/v0.2/README.md`; o APK e o pacote-fonte isolado não estavam disponíveis ao montar o repositório.

## Organização

- `artifacts/AndroidAutoSDL-Bridge/`: APKs, hashes, fontes compactadas e patches por versão.
- `artifacts/SDL-Probe/`: APKs, hashes, fontes e instruções do probe SDL 0.1–0.5.
- `docs/analysis/`: análise estática do TomTom, evidências SDL, bytecode selecionado e metadados.
- `docs/testing/`: resultados na Toyota e estado da ponte.

O APK mais recente da ponte é `artifacts/AndroidAutoSDL-Bridge/v0.4/AndroidAuto-SDL-Bridge-0.4.apk`. Os hashes SHA-256 acompanham cada APK.

## Fluxo de versões

Este repositório usa **somente a branch `main`**. Novas versões e correções devem ser registradas em commits diretos em `main`; não criar branches nem pull requests.

## Licença e origem

O código da ponte parte do [Open Headunit](https://github.com/andreknieriem/open-headunit), snapshot no commit `667346188bee1c50f300e245f373d13942fbd220`, sob AGPL-3.0. A licença e o aviso de copyright estão incluídos na raiz, e os pacotes-fonte preservam os avisos originais.

Versão mais recente: Bridge 0.5, correção da SecurityException no receptor USB interno do SDK SDL. Veja artifacts/AndroidAutoSDL-Bridge/v0.5. Validação na central pendente.

Teste mais recente: Bridge 0.6 — App ID 3610601450 preservado; nome AA SDL Bridge e ícone verde AA. Validação física pendente.
