# AndroidAutoSDL

Protótipos e evidências para investigar a projeção SmartDeviceLink (SDL) na Toyota PLAY+ do Yaris 2019 e avaliar uma ponte Android Auto → SDL.

## Estado

- **SDL Probe 0.5:** registrada no teste de 03/11/2026 com o app visível na central e ensaios de vídeo sintético e toque. Os registros e limites do ensaio estão em [`docs/testing/resultado-validado-2026-11-03.md`](docs/testing/resultado-validado-2026-11-03.md).
- **AndroidAutoSDL-Bridge 0.3:** versão mais recente empacotada. A inicialização foi validada no emulador Android 16; a projeção do Android Auto na central física ainda não foi validada. Detalhes em [`docs/testing/AndroidAuto-SDL-Bridge-estado-e-primeiro-teste.md`](docs/testing/AndroidAuto-SDL-Bridge-estado-e-primeiro-teste.md).
- **Bridge 0.1:** mantida para histórico. Apresentava conflito de permissão e fechamento no layout retrato.
- **Bridge 0.2:** registrada em `artifacts/AndroidAutoSDL-Bridge/v0.2/README.md`; o APK e o pacote-fonte isolado não estavam disponíveis na coleta inicial deste repositório.

## Organização

- `artifacts/AndroidAutoSDL-Bridge/`: APKs, hashes, fontes compactadas e patches por versão.
- `artifacts/SDL-Probe/`: APKs, hashes, fontes e instruções do probe SDL 0.1–0.5.
- `docs/analysis/`: análise estática do TomTom, evidências SDL, bytecode selecionado e metadados.
- `docs/testing/`: resultado do teste na Toyota e estado da ponte.

Os hashes SHA-256 acompanham cada APK. O APK mais recente da ponte é `artifacts/AndroidAutoSDL-Bridge/v0.3/AndroidAuto-SDL-Bridge-0.3.apk`.

## Fluxo de versões

Este repositório usa **somente a branch `main`**. Novas versões e correções devem ser registradas em commits diretos em `main`; não criar branches nem pull requests.

## Licença e origem

O código da ponte parte do [Open Headunit](https://github.com/andreknieriem/open-headunit), snapshot no commit `667346188bee1c50f300e245f373d13942fbd220`, sob AGPL-3.0. A licença e o aviso de copyright estão incluídos na raiz, e os pacotes-fonte preservam os avisos originais.
